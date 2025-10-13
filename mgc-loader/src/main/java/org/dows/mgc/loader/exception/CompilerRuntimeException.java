package org.dows.mgc.loader.exception;

public class CompilerRuntimeException extends RuntimeException {
    public CompilerRuntimeException(String message) {
        super(message);
    }

    public CompilerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
