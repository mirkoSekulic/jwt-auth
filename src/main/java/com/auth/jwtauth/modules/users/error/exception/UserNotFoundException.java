package com.auth.jwtauth.modules.users.error.exception;


import com.auth.jwtauth.modules.shared.error.exception.entity.EntityDoesNotExistException;

public class UserNotFoundException extends EntityDoesNotExistException
{
    private static final long serialVersionUID = -363437578101642142L;

    public UserNotFoundException(Object entityIdentificator)
    {
        super("user", entityIdentificator);
    }
}
