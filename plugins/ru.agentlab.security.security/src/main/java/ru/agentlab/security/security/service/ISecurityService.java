/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
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
