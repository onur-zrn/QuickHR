package com.quickhr.enums.permissions;

public enum EPermissionState {
    PENDING("Bekliyor (onay bekleyen durum)"),
    APPROVED("Onaylandı"),
    REJECTED("Reddedildi"),
    CANCELLED("İptal edildi"),
    IN_PROGRESS("İzinde"),
    COMPLETED("İzin bitti)");

    private final String description;

    EPermissionState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
