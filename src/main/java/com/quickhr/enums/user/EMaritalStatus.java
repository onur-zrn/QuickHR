package com.quickhr.enums.user;

public enum EMaritalStatus {
	MARRIED("Evli"),
	SINGLE("Bekar");
	
	private final String description;
	
	EMaritalStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
