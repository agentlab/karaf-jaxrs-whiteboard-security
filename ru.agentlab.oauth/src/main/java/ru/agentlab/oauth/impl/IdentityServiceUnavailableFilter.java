package ru.agentlab.oauth.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.agentlab.oauth.commons.IdentityServerUnavailable;

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
