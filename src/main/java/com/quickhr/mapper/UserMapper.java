package com.quickhr.mapper;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.UserProfileResponseDTO;
import com.quickhr.entity.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	User fromRegisterDto(RegisterRequestDto dto);

	// to map UpdateProfileRequestDto to User
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateUserFromDto(UpdateProfileRequestDto dto, @MappingTarget User user);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateUserFromEmployee(Employee employee, @MappingTarget User user);

	// User entity'sini UserProfileResponseDTO'ya dönüştürür
	UserProfileResponseDTO toUserProfileResponseDTO(User user);
	// Personel ekleme için: EmployeeRequestDto → User
	User fromEmployeeRequestDto(EmployeeRequestDto dto);


}
