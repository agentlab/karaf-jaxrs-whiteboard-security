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

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.agentlab.security.oauth.commons.service.IdentityServerUnavailable;

@Component(property = { "osgi.jaxrs.extension=true" })
@Provider
public class IdentityServiceUnavailableFilter implements ExceptionMapper<IdentityServerUnavailable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServiceUnavailableFilter.class);

    @Override
    public Response toResponse(IdentityServerUnavailable exception) {
        LOGGER.error(exception.getMessage(), exception);
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
