package com.magicsweet.chestloot.Exception;

public class InvalidTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTypeException(String msg) {
		super("\n" + msg);
	}
}
