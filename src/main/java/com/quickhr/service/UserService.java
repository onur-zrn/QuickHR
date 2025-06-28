package com.quickhr.service;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.UserProfileResponseDTO;
import com.quickhr.entity.ChangeMailCode;
import com.quickhr.entity.Employee;
import com.quickhr.entity.User;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.enums.user.EUserState;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.EmployeeMapper;
import com.quickhr.mapper.UserMapper;
import com.quickhr.repository.EmployeeRepository;
import com.quickhr.repository.UserRepository;
import com.quickhr.utility.CodeGenerator;
import com.quickhr.utility.JwtManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final EmployeeRepository employeeRepository;

	private final MailSenderService mailSenderService;
	private final JwtManager jwtManager;
	private final CodeGenerator codeGenerator;
	private final PasswordEncoder passwordEncoder;


	public boolean existsByMail(String mail) {
		return userRepository.existsByMail(mail);
	}
	public List<Long> getUserIdsByCompanyId(Long companyId) {
		return userRepository.findUserIdByCompanyId(companyId);
	}

	public User save(User user) {
		return userRepository.save(user);
	}
	public Optional<User> findUserById(Long userId) {
		return userRepository.findById(userId);
	}

	public List<User> findAllByUserId(Long userId) {
		return userRepository.findAllById(userId);
	}

	public User findById(Long aLong) {
		return userRepository.findById(aLong).orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));
	}

	@Transactional
	public UserProfileResponseDTO getProfile(String token) {
		User user = getUserFromToken(token);
		return UserMapper.INSTANCE.toUserProfileResponseDTO(user);
	}

	@Transactional
	public UserProfileResponseDTO updateProfile(String token, UpdateProfileRequestDto dto) {
		// Validate the token and get the user ID
		Optional<Long> userId = jwtManager.validateToken(token);
		if (userId.isEmpty()) {
			throw new HRAppException(ErrorType.INVALID_TOKEN);
		}

		// Find the user by ID
		User user = userRepository.findById(userId.get())
				.orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

		// Check if the user account is active
		if (user.getUserState() != EUserState.ACTIVE) {
			throw new HRAppException(ErrorType.ACCOUNT_DOESNT_ACTIVE);
		}

		// Update the user entity with the new data from the DTO
		UserMapper.INSTANCE.updateUserFromDto(dto, user);

		// Save the updated user
		userRepository.save(user);

		// User ile ilişkili Employee'yi bul (örneğin userId ile)
		Optional<Employee> employeeOpt = employeeRepository.findByUserId(user.getId());
		if (employeeOpt.isPresent()) {
			Employee employee = employeeOpt.get();
			// User'daki ortak alanları Employee'ye kopyala
			EmployeeMapper.INSTANCE.updateEmployeeFromUser(user, employee);
			employeeRepository.save(employee);
		}

		// Return the response DTO
		return UserMapper.INSTANCE.toUserProfileResponseDTO(user);
	}

	@Transactional
	public Boolean changeMail(String token, ChangeMailRequestDto dto) {
		// Token'dan kullanıcıyı al
		User user = getUserFromToken(token);

		// Yeni e-posta mevcut e-posta ile aynıysa hata fırlat
		if (user.getMail().equals(dto.newMail())) {
			throw new HRAppException(ErrorType.MAIL_SAME);
		}

		// Yeni e-posta adresi zaten alınmışsa hata fırlat
		if (userRepository.existsByMail(dto.newMail())) {
			throw new HRAppException(ErrorType.MAIL_ALREADY_TAKEN);
		}

		// @ten sonrasını alarak domain karşılaştırması
		String currentEmailDomain = user.getMail().split("@")[1]; // Mevcut e-posta domaini
		String newEmailDomain = dto.newMail().split("@")[1]; // Yeni e-posta domaini

		// Domainler eşleşmiyorsa hata fırlat
		if (!currentEmailDomain.equals(newEmailDomain)) {
			throw new HRAppException(ErrorType.MAIL_COMPANY_MISMATCH);  // Domainler farklıysa hata fırlat
		}

		// Yeni e-posta adresi geçerli ise, e-posta değişikliği için kod üret
		// Kullanıcıyı güncelle
		String generatedCode = codeGenerator.generateCode();
		ChangeMailCode changeMailCode = new ChangeMailCode();
		changeMailCode.setUserId(user.getId());
		changeMailCode.setCode(generatedCode);
		changeMailCode.setExpirationTime(LocalDateTime.now().plusMinutes(15));

		user.setChangeMailCode(changeMailCode);
		user.setPendingMail(dto.newMail());
		// Send Activation E-mail
		mailSenderService.sendMail(new MailSenderRequestDto(user.getMail(), generatedCode));

		// Kullanıcıyı kaydet
		userRepository.save(user);

		// Aktivasyon kodu içeren e-posta gönder
		mailSenderService.sendMail(new MailSenderRequestDto(dto.newMail(), generatedCode));

		return true;
	}

	@Transactional
	public void confirmEmailChange(String token, MailVerifyCodeRequestDto dto) {
		User user = getUserFromToken(token);


		ChangeMailCode userCode = user.getChangeMailCode();
		if (userCode == null) { //Ayrı hata verebiliriz. Henüz code gönderilmedi
			throw new HRAppException(ErrorType.CHANGE_MAIL_CODE_MISMATCH);
		}

		// Check ExpirationTime
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(userCode.getExpirationTime())) {
			throw new HRAppException(ErrorType.CHANGE_MAIL_CODE_EXPIRED);
		}

		// Comparing Activation Code (Does code match)
		if (!userCode.getCode().equals(dto.verificationCode())) {
			throw new HRAppException(ErrorType.CHANGE_MAIL_CODE_MISMATCH);
		}


		user.setMail(user.getPendingMail());
		user.setPendingMail(null);
		user.setChangeMailCode(null);
		userRepository.save(user);
	}
	@Transactional
	public Boolean changePassword(String token, ChangePasswordRequestDto dto) {

		User user = getUserFromToken(token);

		// Old password check
		if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
			throw new HRAppException(ErrorType.PASSWORD_MISMATCH);
		}

		// Yeni şifre eski şifre kontrol
		if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
			throw new HRAppException(ErrorType.PASSWORD_SAME);
		}

		user.setPassword(passwordEncoder.encode(dto.newPassword()));
		userRepository.save(user);
		return true;
	}

	//deactive abonelik süresi bitince olacak.
	//user deactivede role manager ise kendini silemesin ise diğer o şirkette çalışan userlar da deactive olsun role bazlı kontrol
	//manager userı silsin
	@Transactional
	public void deActivateAccount(String token, Long userId) {
		User user = getUserFromToken(token);
		if(user.getRole().equals(EUserRole.PERSONAL)){
			throw new HRAppException(ErrorType.DELETED_ERROR_NOT_AUTH);
		}
		if(user.getRole().equals(EUserRole.MANAGER)){
			Optional<User> personalOptional = findUserById(userId);
			if(personalOptional.isEmpty()){
				throw new HRAppException(ErrorType.USER_NOT_FOUND);
			}
			User personal = personalOptional.get();
			if (!Objects.equals(user.getCompanyId(), personal.getCompanyId())) {
				throw new HRAppException(ErrorType.DELETED_ERROR_NOT_AUTH);
			}
			personal.setUserState(EUserState.INACTIVE);
			userRepository.save(personal);
		}
	}

	public User getUserFromToken(String token) {
		Optional<Long> userId = jwtManager.validateToken(token);
		if (userId.isEmpty()) {
			throw new HRAppException(ErrorType.INVALID_TOKEN);
		}

		User user = userRepository.findById(userId.get())
				.orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

		if (user.getUserState() != EUserState.ACTIVE) {
			throw new HRAppException(ErrorType.ACCOUNT_DOESNT_ACTIVE);
		}
		return user;
	}

}
