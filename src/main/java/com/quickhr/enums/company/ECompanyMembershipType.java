package com.quickhr.enums.company;

public enum ECompanyMembershipType {
	NONE("Üyelik YAPILMADI -TEST", 0.0, 0),
	BASIC("Temel Üyelik", 9.99, 2),
	STANDARD("Standart Üyelik", 19.99, 3),
	PREMIUM("Premium Üyelik", 29.99, 4),
	ENTERPRISE("Kurumsal Üyelik", 39.99, 5);
	
	private final String description;
	private final double monthlyFee;  // yearlyFee
	private final int maxUsers;
	
	ECompanyMembershipType(String description, double monthlyFee, int maxUsers) {
		this.description = description;
		this.monthlyFee = monthlyFee;
		this.maxUsers = maxUsers;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getMonthlyFee() {
		return monthlyFee;
	}
	
	public int getMaxUsers() {
		return maxUsers;
	}
	
}
