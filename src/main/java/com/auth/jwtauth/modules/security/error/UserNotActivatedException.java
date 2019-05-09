package com.auth.jwtauth.modules.security.error;

public class UserNotActivatedException extends RuntimeException
{

    private static final long serialVersionUID = 1L;
    private final String username;


    public UserNotActivatedException(String username)
    {
        this.username = username;
    }

    @Override
    public String getMessage()
    {
        return String.format("User %s is not activated.", username);
    }
}
