package com.quickhr.enums.permissions;

public enum EPermissionType {
    MANDATORY("Zorunlu İzin"),
    ANNUAL_LEAVE("Yıllık İzin"),
    UNPAID_LEAVE("Ücretsiz İzin");
    //EMPLOYED("Çalışıyor");

    private final String description;

    EPermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

