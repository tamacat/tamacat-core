package org.tamacat.util;

public class Encrypt_test {

	static Encrypt encrypt = Encrypt.aes128cbc().iv("1234567890abcdef").secret("password").build();
	
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
			String encrypted = encrypt.encrypt(text);
			
			//try {
			//	Thread.sleep((long)(Math.random() * 100));	
			//} catch (Exception e) {
			//}
			
			String decrypted = encrypt.decrypt(encrypted);
			if (! text.equals(decrypted)) {
				System.err.println(text);
			}
		}
	}
}
