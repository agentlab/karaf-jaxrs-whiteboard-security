package ru.agentlab.security.oauth.commons.service;

import java.net.URI;

public interface IAuthServerProvider {

    URI getJWKSetURI() throws IdentityServerUnavailable;

    URI getTokenEndpointURI() throws IdentityServerUnavailable;

    URI getIntrospectionEndpointURI() throws IdentityServerUnavailable;

    URI getDeviceAuthorizationEndpointURI() throws IdentityServerUnavailable;

    URI getRevocationEndpointURI() throws IdentityServerUnavailable;

    URI getUserInfoEndpointURI() throws IdentityServerUnavailable;
}
