/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.oauth.service.impl;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authc.AuthenticationException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import ru.agentlab.security.security.service.ISecurityService;

@Component(property = { "osgi.jaxrs.extension=true" })
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter, ExceptionMapper<AuthenticationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Reference
    private ISecurityService securityService;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (uriInfo.getPath().contains("oauth2/")) {
            return;
        }

        String accessToken = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        Cookie accessTokenCookie = requestContext.getCookies().get(OAuthConstants.ACCESS_TOKEN);

        if (!Strings.isNullOrEmpty(accessToken) && !securityService.isTokenExpired(accessToken)) {
            securityService.setSubject(accessToken);
            return;
        } else if (accessTokenCookie != null) {
            String accessTokenFromCookie = accessTokenCookie.getValue();
            if (!Strings.isNullOrEmpty(accessTokenFromCookie)
                    && !securityService.isTokenExpired(accessTokenFromCookie)) {
                securityService.setSubject(accessTokenFromCookie);
                return;
            }
        }

        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

    }

    @Override
    public Response toResponse(AuthenticationException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
