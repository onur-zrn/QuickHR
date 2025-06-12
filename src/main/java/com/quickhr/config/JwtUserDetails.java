package com.quickhr.config;

import com.quickhr.entity.*;
import com.quickhr.service.*;
import com.quickhr.enums.*;
import com.quickhr.enums.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {
    private final UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    
    public  UserDetails getUserById(Long userId){
        Optional<User> userOptional = userService.findUserById(userId);
        if(userOptional.isEmpty()){
            return null;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<User> users = userService.findAllByUserId(userId);
        users.forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole.getRole().toString())));
        authorities.add(new SimpleGrantedAuthority(EAdminRole.ADMIN.toString()));
        authorities.add(new SimpleGrantedAuthority(EAdminRole.SUPER_ADMIN.toString()));
        authorities.add(new SimpleGrantedAuthority(EUserRole.MANAGER.toString()));
        authorities.add(new SimpleGrantedAuthority(EUserRole.PERSONAL.toString()));
        
        User user = userOptional.get();
        
        return org.springframework.security.core.userdetails.User.builder()
                                                                 .username(user.getMail())
                                                                 .password(user.getPassword())
                                                                 .accountExpired(false)
                                                                 .accountLocked(false)
                                                                 .authorities(authorities)
                                                                 .build();
    }
    
}
