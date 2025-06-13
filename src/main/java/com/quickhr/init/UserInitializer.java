package com.quickhr.init;

import com.quickhr.entity.*;
import com.quickhr.enums.user.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

public class UserInitializer {
	
	public static List<User> userInitializer() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		User user1 = User.builder()
		                 .companyId(3L)
		                 .mail("user1@gmail.com")
		                 .password(passwordEncoder.encode("Aaa12345!"))
		                 .avatar("person1.jpg")
		                 .phone("15151515")
		                 .userState(EUserState.ACTIVE)
		                 .role(EUserRole.MANAGER)
		                 .build();
		
		User user2 = User.builder()
		                 .companyId(5L)
		                 .mail("user2@gmail.com")
		                 .password(passwordEncoder.encode("Aaa12345!"))
		                 .avatar("person2.jpg")
		                 .phone("202")
		                 .userState(EUserState.PENDING)
		                 .role(EUserRole.PERSONAL)
		                 .build();
		
		User user3 = User.builder()
		                 .companyId(5L)
		                 .mail("user3@gmail.com")
		                 .password(passwordEncoder.encode("Aaa12345!"))
		                 .avatar("person3.jpg")
		                 .phone("15151515025")
		                 .userState(EUserState.DENIED)
		                 .role(EUserRole.MANAGER)
		                 .build();
		
		User user4 = User.builder()
		                 .companyId(3L)
		                 .mail("hhayta.007@gmail.com")
		                 .password(passwordEncoder.encode("Aaa12345!"))
		                 .avatar("person1.jpg")
		                 .phone("05050002123")
		                 .userState(EUserState.ACTIVE)
		                 .role(EUserRole.MANAGER)
		                 .build();
		
		return List.of(user1, user2, user3, user4);
	}
	
}
