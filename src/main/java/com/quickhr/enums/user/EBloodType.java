package com.quickhr.enums.user;

public enum EBloodType  {
	A_POSITIVE("A Rh+"),
	A_NEGATIVE("A Rh-"),
	B_POSITIVE("B Rh+"),
	B_NEGATIVE("B Rh-"),
	AB_POSITIVE("AB Rh+"),
	AB_NEGATIVE("AB Rh-"),
	O_POSITIVE("O Rh+"),
	O_NEGATIVE("O Rh-");
	
	private final String description;
	
	EBloodType (String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
