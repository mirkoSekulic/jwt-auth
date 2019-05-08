package com.auth.jwtauth.modules.users.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "authority")
@Data
public class Authority implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Min(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull
    @Size(max = 50)
    private String code;

    @Size(max = 50)
    @Column(length = 50)
    private String name;
}
