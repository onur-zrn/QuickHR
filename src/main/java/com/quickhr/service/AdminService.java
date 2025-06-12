package com.quickhr.service;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.enums.EAdminRole;
import com.quickhr.enums.EState;
import com.quickhr.enums.company.ECompanyState;
import com.quickhr.enums.user.EUserState;
import com.quickhr.exception.*;
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
    private final JwtManager jwtManager;

    public AdminDashboardResponseDto getAdminDashboard(String token) {
        getAdminFromToken(token);
        return new AdminDashboardResponseDto(
                "Admin Dashboard",
                150,    // Total companies
                4500,               // Total employees
                85,                 // Active sessions
                List.of(
                        "New company registration: TechCorp",
                        "User john doe updated profile",
                        "System backup completed"
                )
        );
    }

    public AdminLoginResponseDto login(@Valid AdminLoginRequestDto dto) {
        Optional<Admin> optionalUsername = adminRepository.findOptionalByUsername(dto.username());
        if (optionalUsername.isEmpty()) {
            throw new HRAppException(ErrorType.INVALID_USERNAME_OR_PASSWORD);
        }

        Admin admin = optionalUsername.get();

        if (!admin.getState().equals(EState.ACTIVE)) {
            throw new HRAppException(ErrorType.ACCOUNT_DOESNT_ACTIVE);
        }

        if(!passwordEncoder.matches(dto.password(), admin.getPassword())){
            throw new HRAppException(ErrorType.INVALID_USERNAME_OR_PASSWORD);
        }

        String token = jwtManager.generateToken(optionalUsername.get().getId());
        return new AdminLoginResponseDto(token, admin.getAdminRole());
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllPendingCompanies(String token) {
        getAdminFromToken(token);
        return companyService.findAllByCompanyState(ECompanyState.PENDING).stream()
                .map(company -> new CompanyStateResponseDto(
                        company.getId(),
                        company.getName(),
                        company.getCompanyState()
                ))
                .toList();
    }

    @Transactional
    public List<CompanyStateResponseDto> listAllAcceptedCompanies(String token) {
        getAdminFromToken(token);
        return companyService.findAllByCompanyState(ECompanyState.ACCEPTED).stream()
                .map(company -> new CompanyStateResponseDto(
                        company.getId(),
                        company.getName(),
                        company.getCompanyState()
                ))
                .toList();
    }

    @Transactional
    public Boolean changeCompanyStatus(String token, ChangeCompanyStatusRequestDto dto) {
        getAdminFromToken(token);
        Long companyId = dto.companyId();
        ECompanyState newCompanyState = dto.newStatus();

        Company company = companyService.getCompanyById(companyId)
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_FOUND));

        if (company.getCompanyState() == newCompanyState) {
            throw new HRAppException(ErrorType.COMPANY_STATE_SAME);
        }

        company.setCompanyState(newCompanyState);
        return true;
    }

    public void deActivateAccount(String token) {
        Admin admin = getAdminFromToken(token);
        if(admin.getAdminRole().equals(EAdminRole.SUPER_ADMIN)){
            throw new HRAppException(ErrorType.SUPER_ADMIN_NOT_DELETED);
        }
        if (admin.getState().equals(EState.PASSIVE)) {
            throw new HRAppException(ErrorType.ACCOUNT_ALREADY_PASSIVE);
        }

        admin.setState(EState.PASSIVE);
    }

    public Admin getAdminFromToken(String token) {
        Optional<Long> adminId = jwtManager.validateToken(token);
        if (adminId.isEmpty()) {
            throw new HRAppException(ErrorType.INVALID_TOKEN);
        }
        return adminRepository.findById(adminId.get())
                .orElseThrow(() -> new HRAppException(ErrorType.ADMIN_NOT_FOUND));
    }
}
