package com.auth.jwtauth.modules.users.service.implementation;

import com.auth.jwtauth.modules.users.domain.User;
import com.auth.jwtauth.modules.users.filter.UserFilter;
import com.auth.jwtauth.modules.users.repository.UserRepository;
import com.auth.jwtauth.modules.users.service.UserService;
import com.auth.jwtauth.modules.users.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAll(@NotNull UserFilter userFilter, Pageable pageable) {
        return userRepository.findAll(UserSpecification.of(userFilter), pageable);
    }
}
