package com.fashion.backend.constant;

import lombok.Getter;

@Getter
public enum Color {
	BLACK("#000000"),
	BLUE("#0000FF"),
	BROWN("#8B4513"),
	GREEN("#008000"),
	GREY("#808080"),
	MULTI_COLOR("MULTI_COLOR"),
	ORANGE("#FFA500"),
	PINK("#FFC0CB"),
	PURPLE("#800080"),
	RED("#FF0000"),
	WHITE("#FFFFFF"),
	YELLOW("#FFFF00");

	private final String hexValue;

	Color(String hexValue) {
		this.hexValue = hexValue;
	}

	public static Color fromString(String colorName) {
		for (Color color : Color.values()) {
			if (color.name().equalsIgnoreCase(colorName)) {
				return color;
			}
		}
		throw new IllegalArgumentException("No enum constant for color: " + colorName);
	}
}

