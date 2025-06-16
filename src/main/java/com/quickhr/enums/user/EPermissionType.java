package com.quickhr.enums.user;

public enum EPermissionType {
    MILITARY_LEAVE("Askerlik İzni"),
    MATERNITY_LEAVE("Annelik/Doğum İzni"),
    PATERNITY_LEAVE("Babalık İzni"),
    MARRIAGE_LEAVE("Evlilik İzni"),
    SICK_LEAVE("Hastalık İzni"),
    EXCUSE_LEAVE("Mazeret İzni"),
    UNPAID_LEAVE("Ücretsiz İzin"),
    DEATH_LEAVE("Cenaze İzni"),
    ANNUAL_LEAVE("Yıllık İzin"),
    ROAD_LEAVE("Yol İzni");

    private final String description;

    EPermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

