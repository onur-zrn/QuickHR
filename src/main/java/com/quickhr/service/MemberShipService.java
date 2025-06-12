package com.quickhr.service;

import com.quickhr.entity.*;
import com.quickhr.enums.company.*;
import com.quickhr.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberShipService {
	private final MemberShipRepository memberShipRepository;
	
	public void createMemberShip(Long companyId) {
		MemberShip memberShip = MemberShip.builder()
		                                  .companyId(companyId)
		                                  .companyMembershipType(ECompanyMembershipType.NONE)
		                                  .isMembershipActive(false)
		                                  .build();
		memberShipRepository.save(memberShip);
	}
	
	public void createOrFindMemberShip(Long companyId) {
		Optional<MemberShip> memberShipOptional = memberShipRepository.findByCompanyId(companyId);
		if (memberShipOptional.isEmpty()) {
			createMemberShip(companyId);
		}
	}
	
}
