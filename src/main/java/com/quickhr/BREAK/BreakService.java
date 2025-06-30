package com.quickhr.BREAK;

import com.quickhr.entity.*;
import com.quickhr.enums.company.*;
import com.quickhr.enums.user.*;
import com.quickhr.exception.*;
import com.quickhr.service.*;
import lombok.*;
import org.springframework.stereotype.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BreakService {
	private final BreakRepository breakRepository;
	private final UserService userService;
	private final CompanyService companyService;
	
	public Boolean createBreak(String token, CreateBreakRequestDto dto) {
		User userValid = validateUserAndCompanyState(token);
		
		Break newBreak = BreakMapper.INSTANCE.fromCreateDto(dto);
		newBreak.setCompanyId(userValid.getCompanyId());
		
		breakRepository.save(newBreak);
		return true;
	}
	
	public List<BreakResponseDto> getAllBreaks(String token) {
		User userValid = validateUserAndCompanyState(token);
		
		return breakRepository.findAllByCompanyId(userValid.getCompanyId())
		                      .stream()
		                      .map(BreakMapper.INSTANCE::toDto)
		                      .toList();
	}
	
	public BreakDetailsResponseDto getBreakDetailsById(String token, Long breakId) {
		Break breakValid = validateBreakState(token, breakId);
		
		return BreakMapper.INSTANCE.toDetailsDto(breakValid, userService);
	}
	
	public BreakDetailsResponseDto updateBreak(String token, BreakUpdateRequestDto dto, Long breakId) {
		Break breakValid = validateUserAndBreak(token, dto.userId(), breakId);
		
		BreakMapper.INSTANCE.updateBreakFromDto(dto, breakValid);
		Break updatedBreak = breakRepository.save(breakValid);
		
		return BreakMapper.INSTANCE.toDetailsDto(updatedBreak, userService);
	}
	
	public Boolean deleteBreak(String token, Long breakId) {
		Break breakValid = validateBreakState(token, breakId);
		breakRepository.delete(breakValid);
		return true;
	}
	
	// Validate Methods
	private Break validateUserAndBreak(String token, Long userId, Long breakId) {
		User userValid = validateUserAndCompanyState(token);
		
		User userById = userService.getUserById(userId)
		                           .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));
		
		if (!userById.getCompanyId().equals(userValid.getCompanyId())) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}
		
		if (!userById.getRole().equals(EUserRole.PERSONAL)) {
			throw new HRAppException(ErrorType.USER_NOT_PERSONAL);
		}
		
		Break breakValid = breakRepository.findById(breakId)
		                                  .orElseThrow(() -> new HRAppException(ErrorType.BREAK_NOT_FOUND));
		
		if (!breakValid.getCompanyId().equals(userValid.getCompanyId())) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}
		
		Optional<Break> existingBreak = breakId == null
				? breakRepository.findByUserIdAndCompanyId(userId, userValid.getCompanyId())
				: breakRepository.findByUserIdAndCompanyIdAndBreakIdNot(userId, userValid.getCompanyId(), breakId);
		
		if (existingBreak.isPresent()) {
			throw new HRAppException(ErrorType.BREAK_ALREADY_ASSIGNED_TO_USER);
		}
		
		return breakValid;
	}
	
	private Break validateBreakState(String token, Long breakId) {
		User userValid = validateUserAndCompanyState(token);
		
		Break breakValid = breakRepository.findById(breakId)
		                                  .orElseThrow(() -> new HRAppException(ErrorType.BREAK_NOT_FOUND));
		
		if (!breakValid.getCompanyId().equals(userValid.getCompanyId())) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}
		
		return breakValid;
	}
	
	private User validateUserAndCompanyState(String token) {
		User userFromToken = userService.getUserFromToken(token);
		Long userId = userFromToken.getId();
		Optional<User> userById = userService.getUserById(userId);
		
		Long companyId = userFromToken.getCompanyId();
		Optional<Company> companyById = companyService.getCompanyById(companyId);
		
		companyById.filter(c -> c.getCompanyState().equals(ECompanyState.ACCEPTED))
		           .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_ACCEPTED));
		
		userById.filter(u -> u.getUserState().equals(EUserState.ACTIVE))
		        .orElseThrow(() -> new HRAppException(ErrorType.USER_DOESNT_ACTIVE));
		
		userById.filter(u -> u.getRole().equals(EUserRole.MANAGER))
		        .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_MANAGER));
		
		return userFromToken;
	}
}
