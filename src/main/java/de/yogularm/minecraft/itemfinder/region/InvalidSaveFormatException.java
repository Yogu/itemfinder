package de.yogularm.minecraft.itemfinder.region;

import java.io.IOException;

public class InvalidSaveFormatException extends IOException {
	public InvalidSaveFormatException() {
		super("The minecraft save is of invalid format");
	}
	
	public InvalidSaveFormatException(String message) {
		super(message);
	}
}
