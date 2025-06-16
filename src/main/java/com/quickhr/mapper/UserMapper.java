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
	void updateUserFromDto(UpdateProfileRequestDto dto, @MappingTarget User user);

	// User entity'sini UserProfileResponseDTO'ya dönüştürür
	UserProfileResponseDTO toUserProfileResponseDTO(User user);
	// Personel ekleme için: EmployeeRequestDto → User
	User fromEmployeeRequestDto(EmployeeRequestDto dto);

	// @AfterMapping ile null olanları set etmiyoruz, sadece gelen verileri güncelliyoruz.
	@AfterMapping
	default void updateNullFields(@MappingTarget User user, UpdateProfileRequestDto dto) {
		if (dto.firstName() != null) {
			user.setFirstName(dto.firstName());
		}
		if (dto.lastName() != null) {
			user.setLastName(dto.lastName());
		}
		if (dto.phone() != null) {
			user.setPhone(dto.phone());
		}
		if (dto.avatar() != null) {
			user.setAvatar(dto.avatar());
		}
	}

}
