package com.parley.testing.model.roles;

public enum UserRoles {
    CN("Chief Negotiator"),
    CM("Contract Manager"),
    VIEWER("Viewer"),
    VIEWER_PLUS("Viewer Plus"),
    ADMIN("Admin"),
    REQUESTER("Requester");

    private final String role;

    UserRoles(String role){
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
