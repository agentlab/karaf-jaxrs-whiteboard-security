package ru.agentlab.security.security.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;

public interface ISecurityService {

    /**
     * Performs jaas's login
     *
     * @param accessToken JWT access token, cannot be {@code null}
     *
     * @throws AuthenticationException
     * @throws IllegalArgumentException when token is {@code null} or empty
     */
    void setSubject(String accessToken) throws AuthenticationException, IllegalArgumentException;

    /**
     * This method does not check signature. Checks only expiration time
     *
     * @param accessToken JWT access token, cannot be {@code null}
     *
     * @throws IncorrectCredentialsException when token is not JWT
     * @throws IllegalArgumentException      when token is {@code null} or empty
     */
    boolean isTokenExpired(String accessToken);
}
