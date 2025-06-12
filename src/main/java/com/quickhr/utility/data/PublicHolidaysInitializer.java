package com.quickhr.utility.data;

import com.quickhr.entity.*;
import com.quickhr.enums.publicHoliday.*;
import com.quickhr.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import java.time.*;
import java.util.*;

@Configuration
public class PublicHolidaysInitializer {
	@Bean
	public CommandLineRunner loadHolidayData(PublicHolidaysRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				List<PublicHolidays> holidays = List.of(
						PublicHolidays.builder()
						              .holidaysName("January 1 New Year's Holiday")
						              .startDate(LocalDate.of(2100, 1, 1))
						              .endDate(LocalDate.of(2100, 1, 1))
						              .publicHolidays(EPublicHolidays.OFFICIAL)
						              .description("1 Ocak Yılbaşı Tatili")
						              .build(),
						
						PublicHolidays.builder()
						              .holidaysName("January 1 New Year's Holiday")
						              .startDate(LocalDate.of(2000, 1, 1))
						              .endDate(LocalDate.of(2000, 1, 1))
						              .publicHolidays(EPublicHolidays.OFFICIAL)
						              .description("1 Ocak Yılbaşı Tatili")
						              .build(),
						
						PublicHolidays.builder()
						              .holidaysName("January 1 New Year's Holiday")
						              .startDate(LocalDate.of(2022, 1, 1))
						              .endDate(LocalDate.of(2022, 1, 1))
						              .publicHolidays(EPublicHolidays.OFFICIAL)
						              .description("1 Ocak Yılbaşı Tatili")
						              .build(),
						
						PublicHolidays.builder()
						              .holidaysName("January 1 New Year's Holiday")
						              .startDate(LocalDate.of(2023, 1, 1))
						              .endDate(LocalDate.of(2023, 1, 1))
						              .publicHolidays(EPublicHolidays.OFFICIAL)
						              .description("1 Ocak Yılbaşı Tatili")
						              .build(),
						
						PublicHolidays.builder()
						              .holidaysName("January 1 New Year's Holiday")
						              .startDate(LocalDate.of(2024, 1, 1))
						              .endDate(LocalDate.of(2024, 1, 1))
						              .publicHolidays(EPublicHolidays.OFFICIAL)
						              .description("1 Ocak Yılbaşı Tatili")
						              .build(),
						
						PublicHolidays.builder()
						             .holidaysName("January 1 New Year's Holiday")
						             .startDate(LocalDate.of(LocalDate.now().getYear(), 1, 1))
						             .endDate(LocalDate.of(LocalDate.now().getYear(), 1, 1))
						             .publicHolidays(EPublicHolidays.OFFICIAL)
						             .description("1 Ocak Yılbaşı Tatili")
						             .build(),
						
						PublicHolidays.builder()
						             .holidaysName("April 23 National Sovereignty and Children's Day")
						             .startDate(LocalDate.of(LocalDate.now().getYear(), 4, 23))
						             .endDate(LocalDate.of(LocalDate.now().getYear(), 4, 23))
						             .publicHolidays(EPublicHolidays.NATIONAL)
						             .description("23 Nisan Ulusal Egemenlik ve Çocuk Bayramı")
						             .build(),
						
						PublicHolidays.builder()
						             .holidaysName("May 1 Labor Day")
						             .startDate(LocalDate.of(LocalDate.now().getYear(), 5, 1))
						             .endDate(LocalDate.of(LocalDate.now().getYear(), 5, 1))
						             .publicHolidays(EPublicHolidays.OFFICIAL)
						             .description("1 Mayıs İşçi Bayramı")
						             .build(),
						
						PublicHolidays.builder()
						             .holidaysName("May 19 Commemoration of Atatürk, Youth and Sports Day")
						             .startDate(LocalDate.of(LocalDate.now().getYear(), 5, 19))
						             .endDate(LocalDate.of(LocalDate.now().getYear(), 5, 19))
						             .publicHolidays(EPublicHolidays.NATIONAL)
								     .description("19 Mayıs Atatürk’ü Anma, Gençlik ve Spor Bayramı")
						             .build(),
						
						PublicHolidays.builder()
						              .holidaysName("July 15 Democracy and National Unity Day")
						              .startDate(LocalDate.of(LocalDate.now().getYear(), 7, 15))
						              .endDate(LocalDate.of(LocalDate.now().getYear(), 7, 15))
						              .publicHolidays(EPublicHolidays.NATIONAL)
						              .description("15 Temmuz Demokrasi ve Milli Birlik Günü")
						              .build(),
						
						PublicHolidays.builder()
						             .holidaysName("August 30 Victory Day")
						             .startDate(LocalDate.of(LocalDate.now().getYear(), 8, 30))
						             .endDate(LocalDate.of(LocalDate.now().getYear(), 8, 30))
						             .publicHolidays(EPublicHolidays.NATIONAL)
						             .description("30 Ağustos Zafer Bayramı")
						             .build(),
						
						PublicHolidays.builder()
						             .holidaysName("October 29 Republic Day")
						             .startDate(LocalDate.of(LocalDate.now().getYear(), 10, 29))
						             .endDate(LocalDate.of(LocalDate.now().getYear(), 10, 29))
						             .publicHolidays(EPublicHolidays.NATIONAL)
									 .description("29 Ekim Cumhuriyet Bayramı")
						             .build(),
						//
						PublicHolidays.builder()
						              .holidaysName("October 29 Republic Day")
						              .startDate(LocalDate.of(2025, 3, 30))
						              .endDate(LocalDate.of(2025, 4, 1))
						              .publicHolidays(EPublicHolidays.RELIGIOUS)
						              .description("Ramazan Bayramı")
						              .build(),
						
						PublicHolidays.builder()
						              .holidaysName("October 29 Republic Day")
						              .startDate(LocalDate.of(2025, 6, 6))
						              .endDate(LocalDate.of(2025, 6, 9))
						              .publicHolidays(EPublicHolidays.RELIGIOUS)
						              .description("Kurban Bayramı")
						              .build()
				);
				repository.saveAll(holidays);
				System.out.println("📅 Public holiday data has been loaded successfully!");
			}
		};
	}
}
