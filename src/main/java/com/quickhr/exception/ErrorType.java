package com.quickhr.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
	// COMPANY
	ALREADY_EXIST_COMPANY(9001, "A company with the same name already exists! Please choose a different name!", HttpStatus.CONFLICT),
	INVALID_COMPANY_MAIL_FORMAT(9002, "The email address format is invalid or does not match the expected company domain (ex. → yourname@yourcompany.com)", HttpStatus.UNPROCESSABLE_ENTITY),
	MAIL_COMPANY_MISMATCH(9003, "The email domain name does not match the registered company name! Please use a company-affiliated email address", HttpStatus.UNPROCESSABLE_ENTITY),
	COMPANY_NOT_FOUND(9004, "Company not found! Please check the information you entered!", HttpStatus.NOT_FOUND),
	COMPANY_ALREADY_ACCEPTED(9005, "An account with this company already exists! Please try a different company!", HttpStatus.CONFLICT),
	COMPANY_STATE_SAME(9006, "A company with this name already exists! Please choose a different name!", HttpStatus.CONFLICT),
	COMPANY_NOT_ACCEPTED(9007, "A!", HttpStatus.CONFLICT),
	COMPANY_DOESNT_PENDING(9008, "The company is not in a 'PENDING' state! This action can only be performed on companies that are pending approval!", HttpStatus.BAD_REQUEST),
	EMPLOYEE_IN_COMPANY_NOT_FOUND(9009, "No employees were found in the specified company!", HttpStatus.NOT_FOUND),
	COMPANY_OR_EMPLOYEE_NOT_FOUND(9010, "The specified company or its employees could not be found!", HttpStatus.NOT_FOUND),
	EMPLOYEE_NOT_FOUND(9011, "Employee not found! Please check the information you entered!", HttpStatus.NOT_FOUND),
	EMPLOYEE_ALREADY_EXIST_ACTIVE(9012, "Kullanıcı zaten 'ACTIVE' durumdadır!", HttpStatus.CONFLICT),
	EMPLOYEE_ALREADY_EXIST_INACTIVE(9013, "Kullanıcı zaten 'INACTIVE' durumdadır!", HttpStatus.CONFLICT),
	USER_STATE_DOESNT_ACTIVE(9014, "Token sahibi kullanıcı aktif durumda değildir!", HttpStatus.BAD_REQUEST),
	USER_COMPANY_STATE_DOESNT_ACCEPTED(9015, "Kullanıcının bağlı olduğu şirket aktif durumda değildir!", HttpStatus.BAD_REQUEST),
	USER_NOT_MANAGER(9016, "Erişim yetkiniz yok! Bu işlemi yalnızca yöneticiler gerçekleştirebilir!", HttpStatus.UNPROCESSABLE_ENTITY),
	USER_STATE_SAME(9017, "Kullanıcının mevcut durumu ile aynı değer girildi! Durumu değiştirmek istiyorsanız farklı bir değer girin!", HttpStatus.CONFLICT),
	EMPLOYEE_DOESNT_PENDING(9018, "Kullanıcının durumu 'PENDING' değildir!", HttpStatus.BAD_REQUEST),
	EMPLOYEE_ALREADY_EXIST_DELETED(9019, "Kullanıcı zaten 'DELETED' durumdadır!", HttpStatus.CONFLICT),

	// USER
	ALREADY_EXIST_USER_MAIL(8001, "An account with this email address already exists! Please try a different email!", HttpStatus.CONFLICT),
	ALREADY_EXIST_USER_PHONE(8002, "An account with this phone number already exists! Please try a different phone number", HttpStatus.CONFLICT),
	USER_NOT_FOUND(8003, "User not found! Please check the information you entered!", HttpStatus.NOT_FOUND),

	// ADMIN
	ALREADY_EXIST_ADMIN_USERNAME(4001, "Admin username already exists!", HttpStatus.CONFLICT),
	ALREADY_EXIST_ADMIN_MAIL(4002, "This email address is already in use by another admin!", HttpStatus.CONFLICT),
	ADMIN_NOT_FOUND(4003, "Admin not found!", HttpStatus.NOT_FOUND),
	INVALID_USERNAME_OR_PASSWORD(4004, "Invalid username or password!", HttpStatus.UNAUTHORIZED),
	SUPER_ADMIN_NOT_DELETED(4005, "Super admin cannot be deleted. Founder or Head Admin!", HttpStatus.UNAUTHORIZED),

	// ACCOUNT
	ACCOUNT_ALREADY_ACTIVE(7001, "This account is already active!", HttpStatus.BAD_REQUEST),
	ACCOUNT_DOESNT_ACTIVE(7002, "This account has not been activated yet! Please activate your account to continue!", HttpStatus.FORBIDDEN),
	ACCOUNT_PENDING(7003, "Your account is pending approval! Please wait for confirmation!", HttpStatus.FORBIDDEN),
	ACCOUNT_DENIED(7004, "Your account request has been denied! Please contact support for further assistance!", HttpStatus.FORBIDDEN),
	ACCOUNT_INACTIVE(7005, "Your account is currently inactive! Please contact support to resolve this issue!", HttpStatus.FORBIDDEN),
	ACCOUNT_BANNED(7006, "Your account has been banned! Access is restricted!", HttpStatus.FORBIDDEN),
	ACCOUNT_ALREADY_PASSIVE(7007, "This account is already passive!", HttpStatus.BAD_REQUEST),

	// AUTHENTICATION (REGISTER-LOGIN-ACTIVATION)
	INVALID_MAIL_OR_PASSWORD(6001, "Invalid e-mail or password! Please check your credentials and try again!", HttpStatus.UNAUTHORIZED),
	ACTIVATION_CODE_MISMATCH(6002, "The activation code you entered is incorrect! Please check the code and try again!", HttpStatus.BAD_REQUEST),
	ACTIVATION_CODE_EXPIRED(6003, "The activation code has expired! Please request a new code to proceed!", HttpStatus.GONE),
	PASSWORD_RESET_CODE_MISMATCH(6004, "The password reset code is incorrect or invalid!", HttpStatus.BAD_REQUEST),
	PASSWORD_RESET_CODE_EXPIRED(6005, "The password reset code has expired!", HttpStatus.BAD_REQUEST),
	INVALID_TOKEN(6006, "The token information is invalid!", HttpStatus.BAD_REQUEST),
	NEW_PASSWORD_RENEW_PASSWORD_MISMATCH(6007, "The new password and the new repeated password do not match!", HttpStatus.BAD_REQUEST),

	// HOLIDAY
	IN_LAST_THERE_YEARS(5001, "Year must be within the last three years!", HttpStatus.BAD_REQUEST),
	FUTURE_YEARS(5002, "The year must not be in the future!", HttpStatus.BAD_REQUEST),

	// COMMON
	VALIDATION_EXCEPTION(500, "One or more fields are invalid! Please check your input and try again!", HttpStatus.UNPROCESSABLE_ENTITY),
	AUTHENTICATION_EXCEPTION(501, "Authentication failed! Please check your login credentials!", HttpStatus.UNAUTHORIZED),
	AUTHORIZATION_EXCEPTION(502, "You do not have permission to access this resource!", HttpStatus.FORBIDDEN),
	RESOURCE_NOT_FOUND_EXCEPTION(503, "Resource not found!", HttpStatus.NOT_FOUND),
	BUSINESS_EXCEPTION(504, "The request could not be processed due to a business rule violation!", HttpStatus.UNPROCESSABLE_ENTITY),
	INTERNAL_SERVER_ERROR(505, "An unexpected error occurred on the server! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR),
	JSON_CONVERT_ERROR(506, "Invalid input parameters! Failed to parse JSON data!", HttpStatus.BAD_REQUEST),
	BAD_REQUEST_ERROR(507, "Invalid or malformed request parameters! Please check your input!", HttpStatus.BAD_REQUEST),
	DUPLICATE_KEY(508, "A record with the same unique field already exists! Please use different values!", HttpStatus.CONFLICT),
	DATA_INTEGRITY_ERROR(509, "Data integrity violation! The operation could not be completed due to inconsistent or conflicting data!", HttpStatus.CONFLICT),
	PASSWORD_MISMATCH(510, "The password does not match!", HttpStatus.UNPROCESSABLE_ENTITY),
	PASSWORD_SAME(511, "Password already same", HttpStatus.UNPROCESSABLE_ENTITY),
	DELETED_ERROR_NOT_AUTH(511, "No delete permission", HttpStatus.UNPROCESSABLE_ENTITY),
	UNAUTHORIZED_OPERATION(512, "Unauthorized operation! You do not have permission to perform this action!", HttpStatus.FORBIDDEN),


	CHANGE_MAIL_CODE_MISMATCH(6002, "Verification code mismatch. Please check the code and try again.", HttpStatus.BAD_REQUEST),
	CHANGE_MAIL_CODE_EXPIRED(6003, "Verification code has expired. Please request a new code to proceed.", HttpStatus.GONE),

	MAIL_ALREADY_TAKEN(511, "This email address is already in use.", HttpStatus.BAD_REQUEST),
	MAIL_SAME(512, "The new email address is the same as your current one. Please use a different email address.", HttpStatus.BAD_REQUEST),

	INVALID_REFRESH_TOKEN(513, "Invalid refresh token.", HttpStatus.BAD_REQUEST),
	EXPIRED_REFRESH_TOKEN(514, "Refresh token has expired.", HttpStatus.BAD_REQUEST),

	//PERMISSSIONS
	PERMISSION_NOT_FOUND(4001, "PERMISSION not found!", HttpStatus.NOT_FOUND),
	INSUFFICIENT_LEAVE_BALANCE(4002, "Yıllık izin bakiyesi yetersiz!", HttpStatus.BAD_REQUEST),
	ALREADY_HAS_PENDING_LEAVE_REQUEST(4003, "Çalışanın cevaplanmamış bir izin talebi bulunmaktadır.", HttpStatus.BAD_REQUEST),
	PERMISSION_STATE_DOESNT_PENDING(4004, "PERMISSION_STATE_DOESNT_PENDING!", HttpStatus.BAD_REQUEST),

	;


	// END

	int code;
	String message;
	HttpStatus httpStatus;

}
