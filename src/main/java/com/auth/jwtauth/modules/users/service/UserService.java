package com.auth.jwtauth.modules.users.service;

import com.auth.jwtauth.modules.users.domain.User;
import com.auth.jwtauth.modules.users.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> findAll(UserFilter userFilter, Pageable pageable);
}
