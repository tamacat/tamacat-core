/*
 * Copyright (c) 2008 tamacat.org
 * All rights reserved.
 */
package org.tamacat.util;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;

public class EncryptionUtils {

	static final Log LOG = LogFactory.getLog(EncryptionUtils.class);
	
	static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
	static final String PBKDF2_HMAC_SHA1_ALGORITHM = "PBKDF2WithHmacSHA1";
	static final String PBKDF2_HMAC_SHA256_ALGORITHM = "PBKDF2WithHmacSHA256";

	/**
	 * Get a message digest.
	 * @param value
	 * @param algorithm "SHA-256"
	 * @return String value of MessageDigest
	 */
	public static String getMessageDigest(String value, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] digest = md.digest(value.getBytes());
			return DatatypeConverter.printHexBinary(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm.", e);
		}
	}
	
	/**
	 * Message Authentication Code (MAC) algorithm.  
	 * @param algorithm AES, ARCFOUR, Blowfish, DES, DESede, HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
	 */
	public static String getMac(String value, String signatureKey, String algorithm) {
		try {
			SecretKey secretKey = new SecretKeySpec(signatureKey.getBytes(), algorithm);
			Mac mac = Mac.getInstance(algorithm);
			mac.init(secretKey);
		    mac.update(value.getBytes());
		    byte[] encrypted = mac.doFinal();
			return DatatypeConverter.printHexBinary(encrypted);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm.", e);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("Invalid key.", e);
		}
	}
	
	public static byte[] encrypt(SecretKey secretKey, byte[] bytes) {
		byte[] iv = EMPTY_IV_GENERATOR.generateKey();
		Cipher encryptor = getCipher(AES_ALGORITHM);
		initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] encrypted = doFinal(encryptor, bytes);
		return concatenate(iv, encrypted);
	}

	public static byte[] decrypt(SecretKey secretKey, byte[] encryptedBytes) {
		byte[] iv = iv(encryptedBytes);
		Cipher decryptor = getCipher(AES_ALGORITHM);
		initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return doFinal(decryptor, encrypted(encryptedBytes, iv.length));
	}

	public static CipherInputStream encrypt(SecretKey secretKey, InputStream in) {
		byte[] iv = EMPTY_IV_GENERATOR.generateKey();
		Cipher encryptor = getCipher(AES_ALGORITHM);
		initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return new CipherInputStream(in, encryptor);
	}

	public static CipherInputStream decrypt(SecretKey secretKey, InputStream in) {
		byte[] iv = EMPTY_IV_GENERATOR.generateKey();
		Cipher decryptor = getCipher(AES_ALGORITHM);
		initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return new CipherInputStream(in, decryptor);
	}

	/**
	 * PBKDF2WithHmacSHA1/AES
	 * @param password
	 * @param salt
	 * @param iterationCount
	 * @param keyLength
	 * @return SecretKey
	 */
	public static SecretKey getSecretKey(String password, String salt, int iterationCount, int keyLength) {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
		SecretKey secretKey1 = generateSecret(PBKDF2_HMAC_SHA1_ALGORITHM, keySpec);
		return new SecretKeySpec(secretKey1.getEncoded(), "AES");
	}

	/**
	 * 
	 * @param algorithm
	 * @param password
	 * @param salt
	 * @param iterationCount
	 * @param keyLength
	 * @return
	 */
	public static SecretKey getSecretKey(String algorithm, String password, String salt, int iterationCount, int keyLength) {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
		SecretKey secretKey1 = generateSecret(algorithm, keySpec);
		return new SecretKeySpec(secretKey1.getEncoded(), "AES");
	}
	
	public static long getEncryptedLength(long len) {
		return len + (16 - (len % 16));
	}

	public static SecretKey generateSecret(String algorithm, PBEKeySpec keySpec) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
			return factory.generateSecret(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Not a valid encryption algorithm", e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException("Not a valid secret key", e);
		}
	}

	public static Cipher getCipher(String algorithm) {
		try {
			return Cipher.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Not a valid encryption algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException("Not a valid padding", e);
		}
	}
	
	public static byte[] doFinal(Cipher cipher, byte[] input) {
		try {
			return cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException("Unable to invoke Cipher due to illegal block size", e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException("Unable to invoke Cipher due to bad padding", e);
		}
	}
	
	public static void initCipher(Cipher cipher, int mode, SecretKey secretKey, AlgorithmParameterSpec paramSpec) {
		try {
			if (paramSpec != null) {
				cipher.init(mode, secretKey, paramSpec);
			} else {
				cipher.init(mode, secretKey);
			}
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("Unable to initialize due to invalid secret key", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new IllegalStateException("Unable to initialize due to invalid decryption parameter spec", e);
		}
	}

	static byte[] iv(byte[] encrypted) {
		return subArray(encrypted, 0, EMPTY_IV_GENERATOR.getKeyLength());
	}

	static byte[] encrypted(byte[] encryptedBytes, int ivLength) {
		return subArray(encryptedBytes, ivLength, encryptedBytes.length);
	}

	public static byte[] concatenate(byte[]... arrays) {
		int length = 0;
		for (byte[] array : arrays) {
			length += array.length;
		}
		byte[] newArray = new byte[length];
		int destPos = 0;
		for (byte[] array : arrays) {
			System.arraycopy(array, 0, newArray, destPos, array.length);
			destPos += array.length;
		}
		return newArray;
	}

	public static byte[] subArray(byte[] array, int beginIndex, int endIndex) {
		int length = endIndex - beginIndex;
		byte[] subarray = new byte[length];
		System.arraycopy(array, beginIndex, subarray, 0, length);
		return subarray;
	}

	interface BytesKeyGenerator {
		int getKeyLength();
		byte[] generateKey();
	}
	
	static final BytesKeyGenerator EMPTY_IV_GENERATOR = new BytesKeyGenerator() {
		private final byte[] VALUE = new byte[16];

		public int getKeyLength() {
			return VALUE.length;
		}

		public byte[] generateKey() {
			return VALUE;
		}
	};
}
