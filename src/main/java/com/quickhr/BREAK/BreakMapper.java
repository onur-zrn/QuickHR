package com.quickhr.BREAK;

import com.quickhr.service.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BreakMapper {
	BreakMapper INSTANCE = Mappers.getMapper(BreakMapper.class);
	
	BreakResponseDto toDto(Break aBreak);
	
	@Mapping(target = "fullName", expression = "java(getFullName(aBreak.getUserId(), userService))")
	BreakDetailsResponseDto toDetailsDto(Break aBreak, @Context UserService userService);
	
	default String getFullName(Long userId, UserService userService) {
		if (userId == null) return null;
		
		return userService.findUserById(userId)
		                  .map(user -> user.getFirstName() + " " + user.getLastName())
		                  .orElse("Unknown");
	}
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateBreakFromDto(BreakUpdateRequestDto dto, @MappingTarget Break aBreak);
	
	@Mapping(target = "breakId", ignore = true)
	@Mapping(target = "companyId", ignore = true)
	Break fromCreateDto(CreateBreakRequestDto dto);
}
