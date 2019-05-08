package com.auth.jwtauth.modules.users.mapper;

import com.auth.jwtauth.modules.users.dto.UserDTO;
import com.auth.jwtauth.modules.users.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "authorities", ignore = true)
    UserDTO userToUserDTOWithoutAuthorities(User user);
}
