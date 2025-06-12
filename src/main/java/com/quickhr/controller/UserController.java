package com.quickhr.controller;


import com.quickhr.dto.request.ChangeMailRequestDto;
import com.quickhr.dto.request.ChangePasswordRequestDto;
import com.quickhr.dto.request.MailVerifyCodeRequestDto;
import com.quickhr.dto.request.UpdateProfileRequestDto;
import com.quickhr.dto.response.BaseResponse;
import com.quickhr.dto.response.UserProfileResponseDTO;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping(USER_PROFILE)
    public ResponseEntity<BaseResponse<UserProfileResponseDTO>> getProfile(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<UserProfileResponseDTO>builder()
                .code(200)
                .data(userService.getProfile(token))
                .success(true)
                .message("Profil bilgileri listelendi.").build());
    }

    @PutMapping(USER_PROFILE)
    public ResponseEntity<BaseResponse<UserProfileResponseDTO>> updateProfile(String token,
                                                            @RequestBody @Valid UpdateProfileRequestDto dto) {
        UserProfileResponseDTO updated = userService.updateProfile(token, dto);
        return ResponseEntity.ok(BaseResponse.<UserProfileResponseDTO>builder()
                .code(200)
                .success(true)
                .message("Profil güncellendi.")
                .data(updated)
                .build());
    }

    @PutMapping(USER_CHANGE_EMAIL) //change email valid
    public ResponseEntity<BaseResponse<Boolean>> changeEmail(String token,
                                                             @RequestBody @Valid ChangeMailRequestDto dto) {

        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Email değiştime kodu e mail adresinize gönderildi.")
                .data( userService.changeMail(token, dto))
                .build());
    }

    @PutMapping(USER_VERIFY_EMAIL)
    public ResponseEntity<BaseResponse<String>> confirmEmailChange(
            @RequestParam String token,
            @RequestBody MailVerifyCodeRequestDto dto) {
        userService.confirmEmailChange(token, dto);

        return ResponseEntity.ok(BaseResponse.<String>builder()
                .code(200)
                .success(true)
                .message("Email başarıyla değiştirildi.")
                .data("OK")
                .build());
    }

    @PutMapping(USER_CHANGE_PASSWORD)
    public ResponseEntity<BaseResponse<Boolean>> changePassword(@RequestParam String token,
                                                                @RequestBody @Valid ChangePasswordRequestDto dto) {

        if (!dto.newPassword().equals((dto.reNewPassword()))) throw new HRAppException(ErrorType.PASSWORD_MISMATCH);
        Boolean isChanged = userService.changePassword(token, dto);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(isChanged)
                .message("Şifre başarıyla değiştirildi.")
                .data(true)
                .build());
    }

    @PutMapping(USER_DEACTIVATE)
    public ResponseEntity<BaseResponse<Boolean>> deactivateAccount(@RequestParam String token, @RequestParam Long userId) {
        userService.deActivateAccount(token, userId);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Kullanıcı askıya alındı.")
                .data(true)
                .build());
    }
}