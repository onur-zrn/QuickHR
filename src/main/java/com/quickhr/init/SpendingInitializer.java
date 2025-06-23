package com.quickhr.init;

import com.quickhr.entity.PersonalSpending;
import com.quickhr.enums.spendings.ESpendingState;
import com.quickhr.enums.spendings.ESpendingType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpendingInitializer {

    public static List<PersonalSpending> spendingInitializer() {
        List<PersonalSpending> spendings = new ArrayList<>();

        spendings.add(PersonalSpending.builder()
                .userId(11L)
                .description("Otelde konaklama")
                .billDocumentUrl(null)
                .billAmount(1500.00)
                .spendingDate(LocalDate.of(2025, 6, 15))
                .spendingState(ESpendingState.APPROVED)
                .spendingType(ESpendingType.ACCOMMODATION)
                .build());

        spendings.add(PersonalSpending.builder()
                .userId(11L)
                .description("İş yemeği")
                .billDocumentUrl(null)
                .billAmount(250.00)
                .spendingDate(LocalDate.of(2025, 6, 16))
                .spendingState(ESpendingState.PENDING)
                .spendingType(ESpendingType.MEAL)
                .build());

        spendings.add(PersonalSpending.builder()
                .userId(21L)
                .description("Ofis kırtasiye alımı")
                .billDocumentUrl(null)
                .billAmount(75.50)
                .spendingDate(LocalDate.of(2025, 5, 10))
                .spendingState(ESpendingState.REJECTED)
                .spendingType(ESpendingType.OFFICE_SUPPLIES)
                .build());

        spendings.add(PersonalSpending.builder()
                .userId(31L)
                .description("Şehir içi yolculuk")
                .billDocumentUrl(null)
                .billAmount(120.00)
                .spendingDate(LocalDate.of(2025, 7, 1))
                .spendingState(ESpendingState.APPROVED)
                .spendingType(ESpendingType.SOFTWARE)
                .build());

        spendings.add(PersonalSpending.builder()
                .userId(33L)
                .description("Akşam yemeği")
                .billDocumentUrl(null)
                .billAmount(300.00)
                .spendingDate(LocalDate.of(2025, 6, 30))
                .spendingState(ESpendingState.PENDING)
                .spendingType(ESpendingType.MEAL)
                .build());

        return spendings;
    }
}
