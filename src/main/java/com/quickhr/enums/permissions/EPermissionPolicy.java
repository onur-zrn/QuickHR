package com.quickhr.enums.permissions;

import java.time.LocalDate;
import java.time.Period;

public enum EPermissionPolicy {
    LESS_THAN_ONE_YEAR(10, "0-1 yıl arası çalışma süresi için 10 gün izin hakkı."),
    BETWEEN_ONE_AND_TEN_YEARS(20, "1-10 yıl arası çalışma süresi için 20 gün izin hakkı."),
    MORE_THAN_TEN_YEARS(30, "10 yıldan fazla çalışma süresi için 30 gün izin hakkı.");

    private final int days;
    private final String description;

    EPermissionPolicy(int days, String description) {
        this.days = days;
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public String getDescription() {
        return description;
    }

    public static String getAnnualDetail(LocalDate dateOfEmployment) {
        if (dateOfEmployment == null) {
            return "İşe başlama tarihi belirtilmemiş.";
        }

        LocalDate now = LocalDate.now();
        Period period = Period.between(dateOfEmployment, now);
        long years = period.getYears();

        int izinGunu;
        if (years < 1) {
            izinGunu = LESS_THAN_ONE_YEAR.getDays();
        } else if (years <= 10) {
            izinGunu = BETWEEN_ONE_AND_TEN_YEARS.getDays();
        } else {
            izinGunu = MORE_THAN_TEN_YEARS.getDays();
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Yıllık izin haklarınız çalışma sürenize göre aşağıdaki gibidir:\n\n");
        sb.append(" - 0 ile 1 yıl arası çalışma süresi: ").append(LESS_THAN_ONE_YEAR.days).append(" gün izin hakkı.\n");
        sb.append(" - 1 ile 10 yıl arası çalışma süresi: ").append(BETWEEN_ONE_AND_TEN_YEARS.days).append(" gün izin hakkı.\n");
        sb.append(" - 10 yıldan fazla çalışma süresi: ").append(MORE_THAN_TEN_YEARS.days).append(" gün izin hakkı.\n\n");

        sb.append("Mevcut çalışma süreniz: ")
                .append(period.getYears()).append(" yıl, ")
                .append(period.getMonths()).append(" ay, ")
                .append(period.getDays()).append(" gün.\n");

        sb.append("Bu nedenle yıllık izin hakkınız toplam ").append(izinGunu).append(" gündür.");

        return sb.toString();
    }
}
