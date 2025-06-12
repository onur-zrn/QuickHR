package com.quickhr.service;

import com.quickhr.entity.*;
import com.quickhr.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeInfoService {
	private final EmployeeInfoRepository employeeInfoRepository;
	
	public void createEmployeeInfo(Long userId, String firstName, String lastName, String mail, String employeeRole) {
		EmployeeInfo employeeInfo = EmployeeInfo.builder()
		                                        .userId(userId)
		                                        .firstName(firstName)
		                                        .lastName(lastName)
		                                        .mail(mail)
		                                        .position(employeeRole)
		                                        .build();
		employeeInfoRepository.save(employeeInfo);
	}
	
}
