package com.auth.jwtauth.modules.users.controller;

import com.auth.jwtauth.modules.users.dto.UserDTO;
import com.auth.jwtauth.modules.users.filter.UserFilter;
import com.auth.jwtauth.modules.users.mapper.UserMapper;
import com.auth.jwtauth.modules.users.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "users", tags = {"users"})
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get a page of user accounts..", nickname = "finAll")
    public ResponseEntity<Page<UserDTO>> list(Pageable pageable, UserFilter userFilter)
    {
        Page<UserDTO> userDTOPage = userService.findAll(userFilter, pageable)
                .map(userMapper::userToUserDTOWithoutAuthorities);

        return new ResponseEntity<>(userDTOPage, HttpStatus.OK);
    }
}
