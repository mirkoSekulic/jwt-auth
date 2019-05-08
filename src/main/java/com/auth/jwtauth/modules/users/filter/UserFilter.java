package com.auth.jwtauth.modules.users.filter;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserFilter implements Serializable {

    private String firstName;
    private String lastName;
    private String username;
}
