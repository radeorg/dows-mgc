package org.dows.mgc.sql.exception;

public class EntitySqlRuntimeException extends RuntimeException {

    public EntitySqlRuntimeException(String message) {
        super(message);
    }

    public EntitySqlRuntimeException(Throwable cause) {
        super(cause);
    }
}
