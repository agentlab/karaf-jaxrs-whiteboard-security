package ru.agentlab.oauth.impl;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
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

import ru.agentlab.oauth.commons.AuthServerUnavailable;
import ru.agentlab.security.ISecurityService;

@Component(property = { "osgi.jaxrs.extension=true" })
@Provider
public class HttpFilter implements ContainerRequestFilter, ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpFilter.class);

    @Reference
    private ISecurityService securityService;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (!uriInfo.getPath().contains("oauth2/")) {

            String accessToken = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

            Cookie accessTokenCookie = requestContext.getCookies().get(OAuthConstants.ACCESS_TOKEN);

            if (!Strings.isNullOrEmpty(accessToken)) {
                securityService.setSubject(accessToken);
                return;
            } else if (accessTokenCookie != null) {
                String accessTokenFromCookie = accessTokenCookie.getValue();
                if (!Strings.isNullOrEmpty(accessTokenFromCookie)) {
                    securityService.setSubject(accessTokenFromCookie);
                    return;
                }
            }

            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    @Override
    public Response toResponse(RuntimeException exception) {

        LOGGER.error(exception.getMessage(), exception);

        if (exception instanceof AuthenticationException) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else if (exception instanceof AuthServerUnavailable) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
