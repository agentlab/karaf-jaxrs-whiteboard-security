package ru.agentlab.oauth.commons;

public class IdentityServerUnavailable extends RuntimeException {

    public IdentityServerUnavailable(String message) {
        super(message);
    }

    public IdentityServerUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
