package com.magicsweet.chestloot.Exception;

public class IllegalLocationException extends Exception {
	private static final long serialVersionUID = 1L;

	public IllegalLocationException(String message) {
		super("\n" + message);
	}
}
