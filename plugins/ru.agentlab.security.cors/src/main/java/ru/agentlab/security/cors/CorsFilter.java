/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.cors;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

@Component(property = { "osgi.jaxrs.extension=true" })
@Provider
public class CorsFilter implements ContainerResponseFilter {

    private static final String ALLOW_ORIGIN = getEnv("ALLOW_ORIGIN", "http://example.com:3000");

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Credentials", true);
        headers.add("Access-Control-Allow-Origin", ALLOW_ORIGIN);
        headers.addAll("Access-Control-Allow-Methods", HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                HttpMethod.DELETE, HttpMethod.OPTIONS, HttpMethod.HEAD);
        headers.addAll("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, user-agent, cookie");
        headers.addAll("Access-Control-Expose-Headers",
                "Set-Cookie, Cache-Control, Content-Language, Content-Length, Content-Type, Expires, Last-Modified, Pragma");
        headers.add("Access-Control-Max-Age", 86400);
    }

    private static String getEnv(String key, String def) {
        String value = System.getenv(key);
        return value != null ? value : def;
    }
}
