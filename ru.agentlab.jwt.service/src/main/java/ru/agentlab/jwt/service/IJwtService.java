package ru.agentlab.jwt.service;

import java.util.Map;

/**
 * Service for working with JWT token
 */
public interface IJwtService {

    boolean isValid(String jwt) throws JwtException;

    /**
     * This method does not check signature. Checks only expiration time
     *
     * @param jwt, cannot be {@code null}
     *
     * @return {@code true} expired, {@code false} otherwise
     * @throws JwtException if jwt invalid or {@code null}
     */
    boolean isExpired(String jwt) throws JwtException;

    String getTokenPayload(String jwt) throws JwtException;

    Map<String, Object> getClaimsMap(String jwt) throws JwtException;
}
