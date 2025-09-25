package com.spribe.enums;

public enum UserRole {

    ADMIN("admin"),

    USER("user"),

    SUPERVISOR("supervisor");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
