package com.quickhr.service;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.enums.company.*;
import com.quickhr.enums.user.*;
import com.quickhr.exception.*;
import com.quickhr.mapper.*;
import com.quickhr.repository.*;
import lombok.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserService userService;
    private final CompanyRepository companyRepository;

    public Boolean createShift(String token, CreateShiftRequestDto dto) {
        User userValid = validateUserAndCompanyState(token);

        // Saat kontrolleri
        if (dto.beginHour().equals(dto.endHour())) {
            throw new HRAppException(ErrorType.TIME_ZONE_DOES_SAME);
        }

        if (dto.endHour().isBefore(dto.beginHour())) {
            throw new HRAppException(ErrorType.END_HOUR_BEFORE_BEGIN_HOUR);
        }

        // Kapasite kontrolleri
        if (dto.capacity() < 0) {
            throw new HRAppException(ErrorType.INVALID_CAPACITY);
        }

        Shift newShift = ShiftMapper.INSTANCE.fromCreateDto(dto);
        newShift.setCompanyId(userValid.getCompanyId());

        shiftRepository.save(newShift);
        return true;
    }

    public List<ShiftResponseDto> getAllShift(String token) {
        User userValid = validateUserAndCompanyState(token);

        // Şirket kontrolü yapılarak kendi şirketindeki vardiyalar görüntüleniyor
        return shiftRepository.findAllByCompanyId(userValid.getCompanyId())
                .stream()
                .map(ShiftMapper.INSTANCE::toDto)
                .toList();
    }

    public ShiftSortedDetailsResponseDto getShiftDetailsById(String token, Long shiftId) {
        Shift shiftValid = validateShiftState(token, shiftId);

        // Gerekli kontroller sağlandıktan sonra id'ye ait vardiya bilgisi getiriliyor
        return ShiftMapper.INSTANCE.toSortedDetailsDto(shiftValid, userService);
    }

    public ShiftDetailsResponseDto updateShift(String token, ShiftUpdateRequestDto dto, Long shiftId) {
        Shift shiftToken = validateShiftState(token, shiftId);

        // Saat kontrolleri
        if (dto.beginHour().equals(dto.endHour())) {
            throw new HRAppException(ErrorType.TIME_ZONE_DOES_SAME);
        }

        if (dto.endHour().isBefore(dto.beginHour())) {
            throw new HRAppException(ErrorType.END_HOUR_BEFORE_BEGIN_HOUR);
        }

        // Kapasite kontrolleri
        if (dto.capacity() < 0) {
            throw new HRAppException(ErrorType.INVALID_CAPACITY);
        }

        // Gerekli kontroller sağlandıktan sonra vardiya bilgisi güncelleniyor
        ShiftMapper.INSTANCE.updateShiftFromDto(dto, shiftToken);
        Shift updatedShift = shiftRepository.save(shiftToken);
        return ShiftMapper.INSTANCE.toDetailsDto(updatedShift, userService);
    }

    public Boolean deleteShift(String token, Long shiftId) {
        Shift shiftValid = validateShiftState(token, shiftId);

        // Vardiya da tanımlu user var mı?
        if (shiftValid.getAssignedUserIds() != null && !shiftValid.getAssignedUserIds().isEmpty()) {
            throw new HRAppException(ErrorType.SHIFT_ALREADY_ASSIGNED);
        }

        // Gerekli kontroller sağlandıktan sonra vardiya siliniyor
        shiftRepository.delete(shiftValid);
        return true;
    }

    public Boolean assignShift(String token, Long userId, AssignShiftRequestDto dto) {
        Shift shiftValid = validateUserAndShift(token, userId, dto.shiftId());

        // Kapasite kontrolü
        if (shiftValid.getAssignedUserIds().size() >= shiftValid.getCapacity()) {
            throw new HRAppException(ErrorType.SHIFT_CAPACITY_FULL);
        }

        // Kullanıcı o vardiyada mevcut mu?
        if (shiftValid.getAssignedUserIds().contains(userId)) {
            throw new HRAppException(ErrorType.USER_ALREADY_ASSIGNED_SHIFT);
        }

        // Kullanıcının daha önce atanmış olduğu bir vardiya varsa kaldır
        List<Shift> userAssignedShifts = shiftRepository.findAllByAssignedUserIdsContains(userId);
        for (Shift shift : userAssignedShifts) {
            if (!shift.getShiftId().equals(shiftValid.getShiftId())) {
                shift.getAssignedUserIds().remove(userId);
                shiftRepository.save(shift);
            }
        }

        // Gerekli kontroller sağlandıktan sonra vardiya ataması yapılıyor
        shiftValid.getAssignedUserIds().add(userId);
        shiftRepository.save(shiftValid);
        return true;
    }

    public Boolean assignDeleteShift(String token, Long userId, Long shiftId) {
        Shift shiftValid = validateUserAndShift(token, userId, shiftId);
        //Shift shiftValid = validateShiftState(token, shiftId);

        // Bu shift gerçekten bu kullanıcıya mı atanmış?
        if (!shiftValid.getAssignedUserIds().remove(userId)) {
            throw new HRAppException(ErrorType.SHIFT_NOT_ASSIGNED_TO_THIS_USER);
        }

        // Gerekli kontroller sağlandıktan sonra vardiya ataması (kullanıcıdan/çalışandan) siliniyor
        shiftRepository.save(shiftValid);
        return true;
    }

    public PersonalShiftDetailsResponseDto getPersonalShift(String token, Long userId) {
        User manager = validateUserAndCompanyState(token);

        // Kayıtlı çalışan var mı?
        User userById = userService.getUserById(userId)
                .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

        // Şirketleri aynı mı? (kullanıcı ile token için)
        if (!userById.getCompanyId().equals(manager.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        // Kullanıcı personal mı?
        if (!userById.getRole().equals(EUserRole.PERSONAL)) {
            throw new HRAppException(ErrorType.USER_NOT_PERSONAL);
        }

        // Kullanıcıya vardiya atanmış mı?
        List<Shift> shifts = shiftRepository.findAllByCompanyId(manager.getCompanyId());
        Shift assignedShift = shifts.stream()
                .filter(shift -> shift.getAssignedUserIds().contains(userId))
                .findFirst()
                .orElseThrow(() -> new HRAppException(ErrorType.SHIFT_DOESNT_ASSIGNED_TO_USER));

        return ShiftMapper.INSTANCE.toPersonalShiftDetailsDto(assignedShift, userById);
    }

    public MyShiftResponseDto getMyShift(String token) {
        User userValid = userService.getUserFromToken(token);

        // Token (User) aktif mi?
        if (!userValid.getUserState().equals(EUserState.ACTIVE)) {
            throw new HRAppException(ErrorType.USER_DOESNT_ACTIVE);
        }

        // Token (User) personal mı?
        if (!userValid.getRole().equals(EUserRole.PERSONAL)) {
            throw new HRAppException(ErrorType.USER_NOT_PERSONAL);
        }

        // Tüm vardiyalar içinden kullanıcıya atanmış lanı getir
        List<Shift> shifts = shiftRepository.findAllByCompanyId(userValid.getCompanyId());
        return shifts.stream()
                .filter(shift -> shift.getAssignedUserIds().contains(userValid.getId()))
                .findFirst()
                .map(ShiftMapper.INSTANCE::toMyShiftDto)
                .orElseThrow(() -> new HRAppException(ErrorType.SHIFT_NOT_FOUND));
    }

    public List<ShiftResponseDto> getAllAssignedShifts(String token) {
        User userValid = validateUserAndCompanyState(token);

        // Vardiya atanmış çalışanları gösterir
        return shiftRepository.findAllAssignedShiftsByCompanyId(userValid.getCompanyId())
                .stream()
                .map(ShiftMapper.INSTANCE::toDto)
                .toList();
    }

    // COMMAND METHODS
    private Shift validateUserAndShift(String token, Long userId, Long shiftId) {
        User userValid = validateUserAndCompanyState(token);

        // Çalışan var mı?
        User userById = userService.getUserById(userId)
                .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

        // Şirketleri aynı mı? (kullanıcı ile token için)
        if (!userById.getCompanyId().equals(userValid.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        // Kullanıcı personal mı?
        if (!userById.getRole().equals(EUserRole.PERSONAL)) {
            throw new HRAppException(ErrorType.USER_NOT_PERSONAL);
        }

        // Vardiya mevcut mu?
        Shift shiftValid = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new HRAppException(ErrorType.SHIFT_NOT_FOUND));

        // Şirketler aynı mı? (vardiya ile token için)
        if (!shiftValid.getCompanyId().equals(userValid.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }



        return shiftValid;
    }

    private Shift validateShiftState(String token, Long shiftId) {
        User userValid = validateUserAndCompanyState(token);

        // Vardiya mevcut mu?
        Shift shiftValid = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new HRAppException(ErrorType.SHIFT_NOT_FOUND));

        // Şirketler aynı mı? (vardiya ile token için)
        if (!shiftValid.getCompanyId().equals(userValid.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        return shiftValid;
    }

    private User validateUserAndCompanyState(String token) {
        User userFromToken = userService.getUserFromToken(token);
        Long userId = userFromToken.getId();
        Optional<User> userById = userService.getUserById(userId);

        Long companyId = userFromToken.getCompanyId();
        Optional<Company> companyById = companyRepository.findById(companyId);

        // Şirket aktif mi?
        companyById.filter(c -> c.getCompanyState().equals(ECompanyState.ACCEPTED))
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_ACCEPTED));

        // Kullanıcı aktif mi?
        userById.filter(u -> u.getUserState().equals(EUserState.ACTIVE))
                .orElseThrow(() ->  new HRAppException(ErrorType.USER_DOESNT_ACTIVE));

        // Kullanıcı manager mı?
        userById.filter(u -> u.getRole().equals(EUserRole.MANAGER))
                .orElseThrow(() ->  new HRAppException(ErrorType.USER_NOT_MANAGER));

        return userFromToken;
    }

}