package com.example.login.service;

import com.example.login.model.Admin;
import com.example.login.model.User;
import com.example.login.repository.AdminRepository;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// 🆕 ADDED MISSING IMPORT:
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {

        // 1. Try finding a regular User by email
        Optional<User> userOpt = userRepository.findByEmail(emailOrUsername);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );
        }

        // 2. Try finding an Admin by email
        Optional<Admin> adminOpt = adminRepository.findByEmail(emailOrUsername);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()));
            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    authorities
            );
        }

        // 3. Fallback: User not found
        throw new UsernameNotFoundException("User not found with email or username: " + emailOrUsername);
    }
}