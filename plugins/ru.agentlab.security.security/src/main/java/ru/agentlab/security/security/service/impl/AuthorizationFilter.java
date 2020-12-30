/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.security.service.impl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.agentlab.security.security.service.ISecurityService;

@Component(property = { "osgi.jaxrs.extension=true" })
@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter, ExceptionMapper<AuthenticationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);

    private static final Pattern FILTER = Pattern.compile("(?<=repositories/)(.*)((?=[/]))|(?<=repositories/)(.*)");

    @Reference
    private ISecurityService securityService;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Matcher matcher = FILTER.matcher(uriInfo.getPath());

        if (matcher.find()) {

            String repositoryId = uriInfo.getPath().substring(matcher.start(), matcher.end());

            if (HttpMethod.GET.equals(requestContext.getMethod())) {
                if (SecurityUtils.getSubject().isPermitted(Permissions.readRepositoryPermission(repositoryId)))
                    return;
            } else {
                if (SecurityUtils.getSubject().isPermitted(Permissions.writeRepositoryPermission(repositoryId)))
                    return;
            }
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    @Override
    public Response toResponse(AuthenticationException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
