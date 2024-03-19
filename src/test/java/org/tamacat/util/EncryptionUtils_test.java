package org.tamacat.util;

import javax.crypto.SecretKey;

public class EncryptionUtils_test {

	static SecretKey key = EncryptionUtils.getSecretKey("password", "salt", 1024, 128);
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			DecryptThread t = new DecryptThread();
			t.start();
		}
		System.out.println("time: " + (System.currentTimeMillis() - start));
	}
	
	static class DecryptThread extends Thread {
		
		
		public void run() {
			String text = "ABCDEFGAAAAAAAAAAAAAAAAAAAAAAA-"+System.currentTimeMillis();
			byte[] encrypted = EncryptionUtils.encrypt(key, text.getBytes());
			
			String decrypted = new String(EncryptionUtils.decrypt(key, encrypted));
			if (! text.equals(decrypted)) {
				System.err.println(text);
			}
		}
	}
}
