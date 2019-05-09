package com.auth.jwtauth.modules.users.error.exception;

public class UnauthorizedException extends RuntimeException
{

    private static final long serialVersionUID = -4161655198636671529L;

    @Override
    public String getMessage()
    {
        return "You are not authorized. Please log in.";
    }
}
