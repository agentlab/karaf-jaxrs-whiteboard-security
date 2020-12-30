/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.jwt.service;

/**
 * Service for working with JWT token
 */
public interface IJwtService {

    /**
     * Validates jwt token
     *
     * @param jwt - can be {@code null}
     *
     * @return {@code true} if valid, {@code false} otherwise
     * @throws JwtException if jwt invalid
     */
    boolean isValid(String jwt) throws JwtException;

    /**
     * This method does not check signature. Checks only expiration time
     *
     * @param jwt - cannot be {@code null}
     *
     * @return {@code true} expired, {@code false} otherwise
     * @throws JwtException if jwt invalid or {@code null}
     */
    boolean isExpired(String jwt) throws JwtException;

    /**
     * Gets token payload
     *
     * @param jwt - cannot be {@code null}
     *
     * @return token's payload, never {@code null}
     * @throws JwtException if jwt invalid or {@code null}
     */
    String getTokenPayload(String jwt) throws JwtException;
}
