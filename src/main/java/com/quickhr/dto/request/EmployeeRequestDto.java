package com.quickhr.dto.request;

import com.quickhr.enums.user.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.quickhr.constant.RegexConstants.PASSWORD_REGEX;
import static com.quickhr.constant.RegexConstants.PHONE_REGEX_E164;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {

    @NotBlank(message = "Ad alanı boş bırakılamaz.")
    private String firstName;

    @NotBlank(message = "Soyad alanı boş bırakılamaz.")
    private String lastName;

    @NotBlank(message = "E-posta adresi zorunludur.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String mail;

    @NotBlank(message = "Şifre zorunludur.")
    @Size(min = 8, max = 64, message = "Şifre en az 8, en fazla 64 karakter olmalıdır.")
    @Pattern(
            message = "Şifre en az bir büyük harf, bir küçük harf, bir rakam ve bir özel karakter (@#$%^&+=*!.,?/ vb.) içermelidir.",
            regexp = PASSWORD_REGEX
    )
    private String password;

    @NotBlank(message = "TC kimlik numarası zorunludur.")
    private String identityNumber;

    @NotNull(message = "Doğum tarihi zorunludur.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Telefon numarası boş bırakılamaz.")
    @Pattern(
            regexp = PHONE_REGEX_E164,
            message = "Telefon numarası uluslararası formatta olmalıdır. Örn: +905xxxxxxxxx"
    )
    private String phone;

    @NotBlank(message = "Adres bilgisi zorunludur.")
    private String address;

    @NotNull(message = "Cinsiyet seçilmelidir.")
    private EGender gender;

    @NotNull(message = "Medeni durum seçilmelidir.")
    private EMaritalStatus maritalStatus;

    @NotNull(message = "Eğitim durumu seçilmelidir.")
    private EEducationLevel educationLevel;

    @NotBlank(message = "Pozisyon bilgisi girilmelidir.")
    private String position;

    @NotNull(message = "Çalışma tipi seçilmelidir.")
    private EWorkType workType;

    @NotNull(message = "İşe başlama tarihi zorunludur.")
    private LocalDate dateOfEmployment;

    private LocalDate dateOfTermination;

    @NotNull(message = "Maaş bilgisi girilmelidir.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maaş pozitif bir değer olmalıdır.")
    private Double salary;

    @NotNull(message = "Çalışma durumu seçilmelidir.")
    EUserState userState;

//    @NotNull(message = "Çalışma durumu seçilmelidir.")
//    private EEmploymentStatus employmentStatus;
}