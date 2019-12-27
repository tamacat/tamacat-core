package org.tamacat.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.SecretKey;

import org.junit.Test;

public class EncryptionUtilsTest {

	String password = "password";
	String salt = "1234567890";

	@Test
	public void testGetMessageDigest() {
		assertEquals("9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08",
			EncryptionUtils.getMessageDigest("test", "SHA-256"));
		
		try {
			EncryptionUtils.getMessageDigest("test", "SHA123");
			fail();
		} catch (Exception e) {
			//java.lang.IllegalArgumentException: No such algorithm.
			//Caused by: java.security.NoSuchAlgorithmException: Algorithm SHA123 not available
			assertTrue( e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testGetMac() {
		assertEquals("D8402BD365A34B4D34729C3FA420818B2913DE85533AE9E8B22434BEF7AC7BBA",
			EncryptionUtils.getMac("test", "password", "HmacSHA256"));
		
		try {
			EncryptionUtils.getMac("test", "password", "HmacSHA123");
			fail();
		} catch (Exception e) {
			//e.printStackTrace();
			//java.lang.IllegalArgumentException: No such algorithm.
			//Caused by: java.security.NoSuchAlgorithmException: Algorithm HmacSHA123 not available
			assertTrue( e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testGetSecretKey() {
		assertEquals("ffffff8130fffffff94b686f9202fdffffffb7ffffff9ffffffff7ffffff97744f",
			StringUtils.dump(EncryptionUtils.getSecretKey(password, salt, 1024, 128).getEncoded()));
	}

	@Test
	public void testGetSecretKeyAlgorithm() {
		assertEquals("ffffff8130fffffff94b686f9202fdffffffb7ffffff9ffffffff7ffffff97744f",
			StringUtils.dump(EncryptionUtils.getSecretKey("PBKDF2WithHmacSHA1", password, salt, 1024, 128).getEncoded()));
		try {
			assertEquals("ffffff8130fffffff94b686f9202fdffffffb7ffffff9ffffffff7ffffff97744f",
				StringUtils.dump(EncryptionUtils.getSecretKey("", password, salt, 1024, 128).getEncoded()));
			fail();
		} catch (Exception e) {
			//java.lang.IllegalArgumentException: Not a valid encryption algorithm
			//Caused by: java.security.NoSuchAlgorithmException:  SecretKeyFactory not available
			assertTrue(e instanceof IllegalArgumentException);
		}
		//
	}
	
	@Test
	public void testEncryptByteArrays() {
		StringBuilder data = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			data.append("a");
			testEncryptByteArrays(data.toString());
		}
	}

	@Test
	public void testEncryptStream() throws IOException {
		StringBuilder data = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			data.append("a");
			testEncryptStream(data.toString());
		}
	}

	void testEncryptByteArrays(String data) {
		SecretKey key = EncryptionUtils.getSecretKey(password, salt, 1024, 128);
		byte[] encrypted = EncryptionUtils.encrypt(key, data.getBytes());
		assertEquals(data, new String(EncryptionUtils.decrypt(key, encrypted)));
	}

	void testEncryptStream(String data) throws IOException {
		SecretKey key = EncryptionUtils.getSecretKey(password, salt, 1024, 128);
		int len = data.getBytes().length;
		InputStream in = EncryptionUtils.encrypt(key, new ByteArrayInputStream(data.getBytes()));

		ByteArrayOutputStream out = output(in);
		assertNotEquals(data, out.toByteArray());
		assertEquals(EncryptionUtils.getEncryptedLength(len), out.toByteArray().length);

		ByteArrayOutputStream out2 = output(EncryptionUtils.decrypt(key, new ByteArrayInputStream(out.toByteArray())));
		assertEquals(data, new String(out2.toByteArray()));
	}

	ByteArrayOutputStream output(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int bytesRead = in.read(buf);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (bytesRead != -1) {
			out.write(buf, 0, bytesRead);
			bytesRead = in.read(buf);
		}
		return out;
	}
}
