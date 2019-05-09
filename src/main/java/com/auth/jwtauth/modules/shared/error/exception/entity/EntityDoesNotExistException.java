package com.auth.jwtauth.modules.shared.error.exception.entity;

/***
 Exception thrown when some entity does not exist. This exception holds entity name and identificator.
 */
public class EntityDoesNotExistException extends RuntimeException
{

    private static final long serialVersionUID = 1333020306015555664L;

    private final String entityName;
    private final transient Object entityIdentificator;

    public EntityDoesNotExistException(String entityName, Object entityIdentificator)
    {
        this.entityName = entityName;
        this.entityIdentificator = entityIdentificator;
    }

    @Override
    public String getMessage()
    {
        return String.format("Entity '%s' with identificator '%s' does not exist.", entityName, entityIdentificator);
    }
}
