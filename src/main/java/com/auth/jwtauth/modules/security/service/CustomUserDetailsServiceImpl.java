package com.auth.jwtauth.modules.security.service;

import com.auth.jwtauth.modules.security.error.UserNotActivatedException;
import com.auth.jwtauth.modules.users.domain.User;
import com.auth.jwtauth.modules.users.error.exception.UserNotFoundException;
import com.auth.jwtauth.modules.users.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        String lowercaseLogin = login.toLowerCase();

        Optional<User> userFromDatabaseByUsername = userRepository.findOneWithAuthoritiesByUsername(login);

        return userFromDatabaseByUsername.map(user -> createSpringSecurityUser(lowercaseLogin, user)).orElseGet(() -> {
            Optional<User> userFromDatabaseByEmail = userRepository.findOneWithAuthoritiesByEmail(login);
            return userFromDatabaseByEmail.map(user -> createSpringSecurityUser(lowercaseLogin, user))
                    .orElseThrow(() -> new UserNotFoundException(login));
        });
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException(lowercaseLogin);
        }

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getCode()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}

