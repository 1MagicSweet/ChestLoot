package com.magicsweet.chestloot.Exception;

public class TypeFormatException extends Exception  {
	private static final long serialVersionUID = 1L;
	
	public TypeFormatException(String string) {
		super("\n Wrong type format: " + string);
	}
}
