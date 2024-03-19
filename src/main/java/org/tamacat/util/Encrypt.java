package org.tamacat.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Encrypt {

	public static Encryptor getDefault() {
		return new Encryptor();
	}
	
	public static Encryptor aes128gcm() {
		return new Encryptor().mode("AES/GCM/NoPadding");
	}
	
	public static Encryptor aes128cbc() {
		return new Encryptor().mode("AES/CBC/PKCS5Padding");
	}
	
	public static Encryptor aes256gcm() {
		return new Encryptor().strength(256).mode("AES/GCM/NoPadding");
	}
	
	public static Encryptor aes256cbc() {
		return new Encryptor().strength(256).mode("AES/CBC/PKCS5Padding");
	}
	
	Cipher encrypter;
	Cipher decrypter;
	
    public synchronized String encrypt(String plainText) {
		try {
			byte[] crypto = encrypter.doFinal(plainText.getBytes());
	        byte[] str64 = Base64.getEncoder().encode(crypto);
	        return new String(str64);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
    }
    
    public synchronized String decrypt(String encrypted) {
    	try {
	        byte[] str = Base64.getDecoder().decode(encrypted);
	        byte[] text = decrypter.doFinal(str);
	        return new String(text);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
    }
    
	private Encrypt() {}
	
	static class Encryptor {
		String algorithm = "AES";
		int strength = 128;
		Key secretKey;
		AlgorithmParameterSpec iv;
		String mode = "AES/CBC/PKCS5Padding";
		int iteration = 1;
		
		public Encryptor algorithm(String algorithm) {
			this.algorithm = algorithm;
			return this;
		}
		
		public Encryptor mode(String mode) {
			this.mode = mode;
			return this;
		}
		
		public Encryptor strength(int strength) {
			this.strength = strength;
			return this;
		}
		
		public Encryptor secret(byte[] key) {
			secretKey = new SecretKeySpec(key, algorithm);
			return this;
		}
		
		public Encryptor secret(String key) {
			secretKey = new SecretKeySpec(sha256(key), algorithm);			
			return this;
		}
		
	    public Encryptor secret(String password, String salt) {
	        secretKey = getSecretKey(PBKDF2_HMAC_SHA256_ALGORITHM, password, salt, iteration, strength);
	        return this;
	    }
		
		public Encryptor iv(byte[] ivs) {
			this.iv = new IvParameterSpec(ivs);
			return this;
		}
		
		public Encryptor iv(String ivs) {
			String ivs_ = ivs;
			if (mode.contains("/GCM/")) {
				iv = new GCMParameterSpec(128, ivs_.getBytes(StandardCharsets.UTF_8));
			} else if (mode.contains("/CBC/")) {
				if (ivs.length() >= 16) {
					ivs_ = ivs.substring(0, 16);
				}
				iv = new IvParameterSpec(ivs_.getBytes(StandardCharsets.UTF_8));
			}
			return this;
		}
		
		public Encrypt build() {
			Encrypt encrypt = new Encrypt();
			try {
				encrypt.encrypter = Cipher.getInstance(mode);
				encrypt.decrypter = Cipher.getInstance(mode);
				encrypt.encrypter.init(Cipher.ENCRYPT_MODE, secretKey, iv);
				encrypt.decrypter.init(Cipher.DECRYPT_MODE, secretKey, iv);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
			return encrypt;
		}
	}
	
	static final String PBKDF2_HMAC_SHA256_ALGORITHM = "PBKDF2WithHmacSHA256";

	public static SecretKey getSecretKey(String algorithm, String password, String salt, int iterationCount, int keyLength) {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
		SecretKey secretKey1 = generateSecret(algorithm, keySpec);
		return new SecretKeySpec(secretKey1.getEncoded(), "AES");
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
    
    public static byte[] sha256(String value) {
    	return md(value, "SHA-256");
    }
    
    public static String sha256hash(String value) {
    	return hash(sha256(value));
    }
    
    public static byte[] sha1(String value) {
    	return md(value, "SHA");
    }
    
    public static String sha1hash(String value) {
    	return hash(sha1(value));
    }
    
    public static byte[] md5(String value) {
    	return md(value, "MD5");
    }
    
    public static String md5hash(String value) {
    	return hash(md5(value));
    }
    
	public static byte[] md(String value, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			return md.digest(value.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm.", e);
		}
	}
	
	public static String hash(byte[] digest) {
		return DatatypeConverter.printHexBinary(digest);
	}
}
