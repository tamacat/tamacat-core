package org.tamacat.io;

/**
 * Runtime IOException.
 * @since 0.9
 */
public class RuntimeIOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeIOException() {
	}

	public RuntimeIOException(String message) {
		super(message);
	}

	public RuntimeIOException(Throwable cause) {
		super(cause);
	}

	public RuntimeIOException(String message, Throwable cause) {
		super(message, cause);
	}

}
