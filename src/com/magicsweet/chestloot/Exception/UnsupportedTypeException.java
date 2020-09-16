package com.magicsweet.chestloot.Exception;

public class UnsupportedTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnsupportedTypeException(String type) {
		super("\nUnsupported type: " + type);
	}

}
