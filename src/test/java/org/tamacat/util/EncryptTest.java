package org.tamacat.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptTest {

	@Test
	public void testEncryptAES128_CBC() {
		assertEquals("n4Km7P4NkPTpRXvpc94qIQ==", Encrypt.aes128cbc().iv("1234567890abcdef").secret("password").build().encrypt("ABCDEFG"));
	}
	
//	@Test
//	public void testEncryptAES256_CBC() {
//		assertEquals("1ZY7qV4IyNg4aycloNxw1Q==", Encrypt.aes256cbc().iv("1234567890abcdef").secret("password","salt").build().encrypt("ABCDEFG"));
//		assertEquals("1ZY7qV4IyNg4aycloNxw1Q==", Encrypt.aes256cbc().iv("1234567890abcdef").secret("password","salt").build().encrypt("ABCDEFG"));
//	}
	
	@Test
	public void testEncryptAES128_GCM() {
		assertEquals("ImitIXmNQ/A5GhmpZeRVZbkK7B5VPPU=", Encrypt.aes128gcm().iv("1234567890abcdef").secret("password","salt").build().encrypt("ABCDEFG"));
		assertEquals("O1taV38NMUZBPoRICv1WAfEov2zYapM=", Encrypt.aes128gcm().iv("1234").secret("password","salt").build().encrypt("ABCDEFG"));
	}
	

	@Test
	public void testEncryptGCM() {
		Encrypt encrypt = Encrypt.getDefault().mode("AES/GCM/NoPadding").iv("1234567890abcdef").secret("password","salt").build();
		String encrypted = encrypt.encrypt("ABCDEFG");
		
		assertEquals("ImitIXmNQ/A5GhmpZeRVZbkK7B5VPPU=", encrypted);
		assertEquals("ABCDEFG", encrypt.decrypt(encrypted));
	}
	
//	@Test
//	public void testEncryptAES256_GCM() {
//		Encrypt encrypt1 = Encrypt.aes256gcm().iv("1234567890abcdef").secret("password","salt").build();
//		Encrypt encrypt2 = Encrypt.aes256gcm().iv("1234567890abcdef").secret("password","salt").build();
//
//		String encrypted = encrypt1.encrypt("ABCDEFG");
//
//		assertEquals("WkKWPTAzxxSATEGRDdDXJdvqz1GdU7I=", encrypted);
//		assertEquals("ABCDEFG", encrypt1.decrypt(encrypted));
//		
//		assertEquals("ABCDEFG", encrypt2.decrypt(encrypted));
//	}
	
	@Test
	public void testEncrypt() {
		Encrypt encrypt = Encrypt.aes128cbc().iv("1234567890abcdef").secret("password").build();
		String encrypted = encrypt.encrypt("ABCDEFG");
		
		assertEquals("n4Km7P4NkPTpRXvpc94qIQ==", encrypted);
		assertEquals("ABCDEFG",  encrypt.decrypt(encrypted));
	}

	@Test
	public void testDecrypt() {
		Encrypt encrypt = Encrypt.aes128cbc().iv("1234567890abcdef").secret("password").build();
		assertEquals("ABCDEFG", encrypt.decrypt("n4Km7P4NkPTpRXvpc94qIQ=="));
	}
	
	@Test
	public void testHash() {
		assertEquals("5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8", Encrypt.sha256hash("password"));
		assertEquals("5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8", Encrypt.sha1hash("password"));
		assertEquals("5F4DCC3B5AA765D61D8327DEB882CF99", Encrypt.md5hash("password"));
	}
}
