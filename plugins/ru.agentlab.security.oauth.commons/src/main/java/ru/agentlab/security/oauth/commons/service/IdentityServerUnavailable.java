package ru.agentlab.security.oauth.commons.service;

public class IdentityServerUnavailable extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IdentityServerUnavailable(String message) {
        super(message);
    }

    public IdentityServerUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
