package com.quickhr.service;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.enums.EAdminRole;
import com.quickhr.enums.EState;
import com.quickhr.enums.company.ECompanyState;
import com.quickhr.exception.*;
import com.quickhr.mapper.CompanyMapper;
import com.quickhr.repository.*;
import com.quickhr.utility.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final EmployeeService employeeService;

    private final JwtManager jwtManager;
    private final RefreshTokenService refreshTokenService;
    private final CompanyMapper companyMapper;

    public Optional<Admin> findAdminById(Long authID) {
        return adminRepository.findById(authID);
    }

    public AdminDashboardResponseDto getAdminDashboard(String token) {
        getAdminFromToken(token);
        return AdminDashboardResponseDto.of(
                "Admin Dashboard",
                companyService.countCompanies(),
                companyService.countActiveCompanies(),
                employeeService.countPersonal(),
                employeeService.countActivePersonal()

        );
    }

    public AdminLoginResponseDto login(@Valid AdminLoginRequestDto dto) {
        Admin admin = adminRepository.findOptionalByUsername(dto.username())
                .orElseThrow(() -> new HRAppException(ErrorType.INVALID_USERNAME_OR_PASSWORD));

        if (!admin.getState().equals(EState.ACTIVE)) {
            throw new HRAppException(ErrorType.ACCOUNT_DOESNT_ACTIVE);
        }

        if (!passwordEncoder.matches(dto.password(), admin.getPassword())) {
            throw new HRAppException(ErrorType.INVALID_USERNAME_OR_PASSWORD);
        }

        String accessToken = jwtManager.generateAccessToken(admin.getId());
        String refreshToken = refreshTokenService.createRefreshToken(admin.getId()).getToken();

        return new AdminLoginResponseDto(accessToken, refreshToken, admin.getAdminRole());
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllPendingCompanies(String token) {
        getAdminFromToken(token);
        return companyMapper.toStateDtoList(companyService.findAllByCompanyState(ECompanyState.PENDING));
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllAcceptedCompanies(String token) {
        getAdminFromToken(token);
        return companyMapper.toStateDtoList(companyService.findAllByCompanyState(ECompanyState.ACCEPTED));
    }

    @Transactional
    public Boolean changeCompanyStatus(String token, ChangeCompanyStatusRequestDto dto) {
        getAdminFromToken(token);

        Company company = companyService.getCompanyById(dto.companyId())
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_FOUND));

        if (company.getCompanyState() == dto.newStatus()) {
            throw new HRAppException(ErrorType.COMPANY_STATE_SAME);
        }

        company.setCompanyState(dto.newStatus());
        return true;
    }

    public void deActivateAccount(String token) {
        Admin admin = getAdminFromToken(token);

        if (admin.getAdminRole().equals(EAdminRole.SUPER_ADMIN)) {
            throw new HRAppException(ErrorType.SUPER_ADMIN_NOT_DELETED);
        }

        if (admin.getState().equals(EState.PASSIVE)) {
            throw new HRAppException(ErrorType.ACCOUNT_ALREADY_PASSIVE);
        }

        admin.setState(EState.PASSIVE);
    }

    public String refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new HRAppException(ErrorType.INVALID_REFRESH_TOKEN));

        if (refreshTokenService.isExpired(token)) {
            throw new HRAppException(ErrorType.EXPIRED_REFRESH_TOKEN);
        }

        Admin admin = findAdminById(token.getAuthId())
                .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

        return jwtManager.generateAccessToken(admin.getId());
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllDeniedCompanies(String token) {
        getAdminFromToken(token);
        return companyMapper.toStateDtoList(companyService.findAllByCompanyState(ECompanyState.DENIED));
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllDeletedCompanies(String token) {
        getAdminFromToken(token);
        return companyMapper.toStateDtoList(companyService.findAllByCompanyState(ECompanyState.DELETED));
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllFindAllCompanies(String token) {
        getAdminFromToken(token);
        return companyMapper.toStateDtoList(companyService.findAllCompany());
    }

    public Boolean IsAcceptedCompany(String token, IsAcceptedCompanyRequestDto dto) {
        getAdminFromToken(token);

        Company company = companyService.getCompanyById(dto.id())
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_FOUND));

        if (!company.getCompanyState().equals(ECompanyState.PENDING)) {
            throw new HRAppException(ErrorType.COMPANY_DOESNT_PENDING);
        }

        company.setCompanyState(dto.isAccepted() ? ECompanyState.ACCEPTED : ECompanyState.DENIED);
        companyService.save(company);
        return dto.isAccepted();
    }

    public void logout(String token) {
        jwtManager.deleteRefreshToken(token);
    }

    public Admin getAdminFromToken(String token) {
        Long adminId = jwtManager.validateToken(token)
                .orElseThrow(() -> new HRAppException(ErrorType.INVALID_TOKEN));

        return adminRepository.findById(adminId)
                .orElseThrow(() -> new HRAppException(ErrorType.ADMIN_NOT_FOUND));
    }
}
