package com.fashion.backend.constant;

import lombok.Getter;

@Getter
public enum PriceFilter {
	ALL("All"),
	BELOW199("< 199K"),
	FROM199TO299("199K - 299K"),
	FROM299TO399("199K - 299K"),
	FROM399TO499("199K - 299K"),
	FROM499TO799("199K - 299K"),
	FROM799TO999("199K - 299K"),
	ABOVE999("> 999K");

	private final String priceText;

	PriceFilter(String priceText) {
		this.priceText = priceText;
	}

	public static PriceFilter fromString(String priceName) {
		for (PriceFilter color : PriceFilter.values()) {
			if (color.name().equalsIgnoreCase(priceName)) {
				return color;
			}
		}
		throw new IllegalArgumentException("No enum constant for price: " + priceName);
	}
}

