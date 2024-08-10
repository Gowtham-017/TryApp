package com.emsportal.model;
public enum UserType {
	ADMIN("ADMIN"), ORGANISER("ORGANISER"), USER("USER");
	
	private final String type;
	
	UserType(String string) {
		type = string;
	}
	@Override
	public String toString() {
		return type;
	}
}
