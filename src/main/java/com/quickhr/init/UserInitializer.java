package com.quickhr.init;

import com.quickhr.entity.*;
import com.quickhr.enums.user.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

public class UserInitializer {

	public static List<User> userInitializer() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		List<User> users = new ArrayList<>();

		// Şirket domain listesi
		Map<Long, String> companyDomains = Map.of(
				1L, "technova.com",
				2L, "greensolutions.com",
				3L, "skynet.com",
				4L, "futuresoft.io",
				5L, "urbantech.co",
				6L, "ecoware.org",
				7L, "neonedge.ai",
				8L, "solarcore.com",
				9L, "codenation.dev",
				10L, "pixelpeak.com"
		);
		//manager1 manager1@technova.com  Aaa12345!
		// user1  user11@technova.com  Aaa12345!

		int userCounter = 1;

		// Her şirkete kesin bir MANAGER ekle (1-10 arası)
		for (long companyId = 1; companyId <= 10; companyId++) {
			String domain = companyDomains.get(companyId);
			users.add(User.builder()
					.companyId(companyId)
					.firstName("ManagerFirstName" + companyId)
					.lastName("ManagerLastName" + companyId)
					.mail("manager" + companyId + "@" + domain)
					.password(passwordEncoder.encode("Aaa12345!"))
					.avatar("manager" + companyId + ".jpg")
					.phone("0500111" + String.format("%04d", companyId))
					.userState(EUserState.ACTIVE)
					.role(EUserRole.MANAGER)
					.build());
			userCounter++;
		}

		// 40 tane daha kullanıcı oluştur (rastgele ama mantıklı şekilde)
		while (userCounter <= 50) {
			long companyId = ((userCounter - 1) % 10) + 1; // 1-10 arası döngüsel companyId

			String domain = companyDomains.get(companyId);

			// Roller: 6'ya tam bölünenler manager, diğerleri personal
			EUserRole role = (userCounter % 6 == 0) ? EUserRole.MANAGER : EUserRole.PERSONAL;

			// Durumlar: 5'e tam bölünen denied, 3'e tam bölünen pending, diğerleri active
			EUserState state;
			if (userCounter % 5 == 0) {
				state = EUserState.DENIED;
			} else if (userCounter % 3 == 0) {
				state = EUserState.PENDING;
			} else {
				state = EUserState.ACTIVE;
			}

			users.add(User.builder()
					.companyId(companyId)
					.firstName("FirstName" + userCounter)
					.lastName("LastName" + userCounter)
					.mail("user" + userCounter + "@" + domain)
					.password(passwordEncoder.encode("Aaa12345!"))
					.avatar("person" + ((userCounter % 5) + 1) + ".jpg")
					.phone("0500" + String.format("%07d", userCounter * 222))
					.userState(state)
					.role(role)
					.build());

			userCounter++;
		}

		return users;
	}
}
