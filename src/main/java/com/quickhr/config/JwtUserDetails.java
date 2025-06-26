package com.quickhr.config;

import com.quickhr.entity.*;
import com.quickhr.enums.EAdminRole;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.service.*;
import com.quickhr.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUserDetails {

    private final UserService userService;
    private final AdminService adminService;
    private final JwtManager jwtManager;

    // Admin rollerini enum’dan dinamik olarak Set’e alıyoruz
    private static final Set<String> ADMIN_ROLES = Arrays.stream(EAdminRole.values())
            .map(Enum::toString)
            .collect(Collectors.toSet());

    // User rollerini enum’dan dinamik olarak Set’e alıyoruz
    private static final Set<String> USER_ROLES = Arrays.stream(EUserRole.values())
            .map(Enum::toString)
            .collect(Collectors.toSet());

    public UserDetails loadUserByToken(String token) {
        Optional<Long> authIdOpt = jwtManager.validateToken(token);
        Optional<String> roleOpt = jwtManager.getRoleFromToken(token);

        if (authIdOpt.isEmpty() || roleOpt.isEmpty()) {
            return null; // veya hata fırlat
        }

        Long authId = authIdOpt.get();
        String role = roleOpt.get();

        if (ADMIN_ROLES.contains(role)) {
            Optional<Admin> adminOpt = adminService.findAdminById(authId);
            if (adminOpt.isEmpty()) {
                log.warn("Admin bulunamadı: id = {}", authId);
                return null;
            }
            Admin admin = adminOpt.get();
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(admin.getAdminRole().toString()));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getId().toString())
                    .password(admin.getPassword())
                    .authorities(authorities)
                    .accountExpired(false)
                    .accountLocked(false)
                    .build();

        } else if (USER_ROLES.contains(role)) {
            Optional<User> userOpt = userService.findUserById(authId);
            if (userOpt.isEmpty()) {
                log.warn("User bulunamadı: id = {}", authId);
                return null;
            }
            User user = userOpt.get();
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getId().toString())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .accountExpired(false)
                    .accountLocked(false)
                    .build();

        } else {
            log.warn("Token'dan alınan rol ne admin ne de user rollerinden biri: {}", role);
            return null;
        }
    }
}