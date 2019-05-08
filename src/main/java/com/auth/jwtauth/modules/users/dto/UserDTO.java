package com.auth.jwtauth.modules.users.dto;

import com.auth.jwtauth.modules.users.domain.Authority;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A user dto used for representing user.
 */
@ApiModel(value="User")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserDTO implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Min(1)
    private Long id;

    @Size(max = 50)
    @NotEmpty
    private String firstName;

    @Size(max = 50)
    @NotEmpty
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^[_'.@A-Za-z0-9-]*$")
    @Size(min = 1, max = 50)
    private String username;

    @Email
    @Size(min = 5, max = 100)
    @NotEmpty
    private String email;

    private Set<Authority> authorities = new HashSet<>();
}
