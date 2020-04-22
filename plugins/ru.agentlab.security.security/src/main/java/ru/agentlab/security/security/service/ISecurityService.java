package ru.agentlab.security.security.service;

import org.apache.shiro.authc.AuthenticationException;

public interface ISecurityService {

    /**
     * Performs jaas's login
     *
     * @param accessToken JWT access token, cannot be {@code null}
     */
    void setSubject(String accessToken) throws AuthenticationException;

    boolean isTokenExpired(String accessToken);
}
