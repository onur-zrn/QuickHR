package com.quickhr.dto.request;

import com.quickhr.enums.permissions.EPermissionType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CreateLeaveRequestDto(

		@NotNull(message = "Başlangıç tarihi boş olamaz.")
		@FutureOrPresent(message = "Başlangıç tarihi bugünden önce olamaz.")
		LocalDate beginDate,

		@NotNull(message = "Bitiş tarihi boş olamaz.")
		@FutureOrPresent(message = "Bitiş tarihi bugünden önce olamaz.")
		LocalDate endDate,

		@NotNull(message = "İzin türü boş olamaz.")
		EPermissionType permissionType, // Enum olarak

		@Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir.")
		String description

) { }
