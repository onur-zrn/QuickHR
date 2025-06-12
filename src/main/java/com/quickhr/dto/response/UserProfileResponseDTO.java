package com.quickhr.dto.response;

import com.quickhr.enums.user.EUserRole;
import com.quickhr.enums.user.EUserState;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Long companyId;
    private String mail;
    private String avatar;
    private String phone;
    private EUserState userState;
    private EUserRole role;
}