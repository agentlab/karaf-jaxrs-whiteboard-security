/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.oauth.service;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

public interface IAuthService {

    /**
     * Performs grant operation
     *
     * @param form - form parameters, cannot {@code null}
     * @param refreshTokenCookie - refreshToken, can be {@code null}
     *
     * @return response, never {@code null}
     */
    Response grantOperation(Form form, String refreshTokenCookie);

    /**
     * Performs device_flow grant operation
     *
     * @param form - form parameters, cannot {@code null}
     *
     * @return response, never {@code null}
     */
    Response getDeviceGrantInfo(Form form);

    /**
     * Revokes token
     *
     * @param form - form parameters, cannot {@code null}
     * @param accessTokenCookie - accessToken, can be {@code null}
     * @param refreshTokenCookie - refreshToken, can be {@code null}
     *
     * @return response, never {@code null}
     */
    Response revokeToken(Form form, String accessTokenCookie, String refreshTokenCookie);

    /**
     * Gets userInfo
     *
     * @param form - form parameters, cannot {@code null}
     * @param accessTokenCookie - accessToken, can be {@code null}
     * @param refreshTokenCookie - refreshToken, can be {@code null}
     *
     * @return response, never {@code null}
     */
    Response userInfo(String authorizationHeader, String accessTokenCookie);
}
