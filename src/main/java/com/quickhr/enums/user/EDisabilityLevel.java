package com.quickhr.enums.user;

public enum EDisabilityLevel {
	NONE("Herhangi Bir Engeli Yoktur"),
	MILD("Hafif Derecede Engelli"),
	MODERATE("Orta Derecede Engelli"),
	SEVERE("Ağır Derecede Engelli"),
	VERY_SEVERE("Çok Ağır Derecede Engelli");
	
	private final String description;
	
	EDisabilityLevel(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
