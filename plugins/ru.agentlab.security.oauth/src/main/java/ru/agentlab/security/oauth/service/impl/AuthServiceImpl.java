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

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.System.getProperty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.ResourceOwnerPasswordCredentialsGrant;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.TokenRevocationRequest;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.device.DeviceCode;
import com.nimbusds.oauth2.sdk.device.DeviceCodeGrant;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.token.Token;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;

import ru.agentlab.security.oauth.commons.service.IAuthServerProvider;
import ru.agentlab.security.oauth.commons.service.IHttpClientProvider;
import ru.agentlab.security.oauth.service.IAuthService;

@Path("/oauth2")
@Component(property = { "osgi.jaxrs.resource=true" })
public class AuthServiceImpl implements IAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private static final String OAUTH_COOKIE_PATH = System.getProperty("ru.agentlab.security.oauth.cookie.path",
            getEnv("OAUTH_COOKIE_PATH", String.class, "/"));
    private static final String OAUTH_COOKIE_DOMAIN = System.getProperty("ru.agentlab.security.oauth.cookie.domain",
            getEnv("OAUTH_COOKIE_DOMAIN", String.class, null));
    private static final int OAUTH_COOKIE_EXPIRE_ACCESS_TOKEN = Integer.getInteger(
            "ru.agentlab.security.oauth.cookie.expire.access_token",
            getEnv("OAUTH_COOKIE_EXPIRE_ACCESS_TOKEN", Integer.class, 3600));
    private static final int OAUTH_COOKIE_EXPIRE_REFRESH_TOKEN = Integer.getInteger(
            "ru.agentlab.security.oauth.cookie.expire.refresh_token",
            getEnv("OAUTH_COOKIE_EXPIRE_REFRESH_TOKEN", Integer.class, 86400));

    private final ClientSecretPost clientAuthPost;

    @Reference
    private IAuthServerProvider authServerProvider;
    @Reference
    private IHttpClientProvider httpClientProvider;

    public AuthServiceImpl() {
        ClientID clientId = new ClientID(getProperty("ru.agentlab.oauth.client.id",
                getEnv("OAUTH_CLIENT_ID", String.class, "Ynio_EuYVk8j2gn_6nUbIVQbj_Aa")));
        Secret clientSecret = new Secret(getProperty("ru.agentlab.oauth.client.secret",
                getEnv("OAUTH_CLIENT_SECRET", String.class, "fTJGvvfJjUkWvn8R_NY8zXSyYQ0a")));

        clientAuthPost = new ClientSecretPost(clientId, clientSecret);

        if (!isSslVerificationEnabled())
            disableSSLVerification();
    }

    @Override
    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantOperation(Form form, @CookieParam(OAuthConstants.REFRESH_TOKEN) String refreshTokenCookie) {
        MultivaluedMap<String, String> formParams = form.asMap();

        List<String> grantTypes = formParams.get("grant_type");

        if (grantTypes == null || grantTypes.isEmpty() || grantTypes.size() > 2) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        GrantType grantType = null;

        try {
            grantType = GrantType.parse(grantTypes.get(0));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Status.BAD_REQUEST).build();
        }

        if (GrantType.PASSWORD.equals(grantType)) {
            return clientCredentialsGrantFlow(form);
        } else if (GrantType.REFRESH_TOKEN.equals(grantType)) {
            return refreshTokenGrantFlow(form, refreshTokenCookie);
        } else if (GrantType.DEVICE_CODE.equals(grantType)) {
            return deviceGrantFlow(form);
        } else if (GrantType.AUTHORIZATION_CODE.equals(grantType)) {
            return authorizationCodeGrantFlow(form);
        }

        return Response.status(Status.BAD_REQUEST).build();
    }

    @Override
    @POST
    @Path("/device_authorize")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceGrantInfo(Form form) {

        Optional<String> info = getDeviceCodeInfo(form);

        if (info.isPresent()) {
            return Response.ok().entity(info.get()).build();
        }

        return Response.status(Status.BAD_REQUEST).build();
    }

    @Override
    @POST
    @Path("/revoke")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response revokeToken(Form form, @CookieParam(OAuthConstants.ACCESS_TOKEN) String accessTokenCookie,
            @CookieParam(OAuthConstants.REFRESH_TOKEN) String refreshTokenCookie) {

        MultivaluedMap<String, String> formParams = form.asMap();

        String tokenType = formParams.getFirst(OAuthConstants.TOKEN_TYPE_HINT);

        if (Strings.isNullOrEmpty(tokenType)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        String tokenFromForm = formParams.getFirst(OAuthConstants.TOKEN);

        Token token;
        NewCookie resetCookie;

        if (OAuthConstants.ACCESS_TOKEN.equals(tokenType)) {
            String accessToken = isNullOrEmpty(accessTokenCookie) ? tokenFromForm : accessTokenCookie;
            if (isNullOrEmpty(accessToken)) {
                return Response.status(Status.BAD_REQUEST).build();
            } else {
                token = new BearerAccessToken(accessToken);
                resetCookie = createTokenCookie(OAuthConstants.ACCESS_TOKEN, "", 0);
            }
        } else if (OAuthConstants.REFRESH_TOKEN.equals(tokenType)) {
            String refreshToken = isNullOrEmpty(refreshTokenCookie) ? tokenFromForm : refreshTokenCookie;
            if (isNullOrEmpty(refreshToken)) {
                return Response.status(Status.BAD_REQUEST).build();
            } else {
                token = new RefreshToken(refreshToken);
                resetCookie = createTokenCookie(OAuthConstants.REFRESH_TOKEN, "", 0);
            }
        } else {
            return Response.status(Status.BAD_REQUEST).build();
        }

        TokenRevocationRequest revokeRequest = new TokenRevocationRequest(authServerProvider.getRevocationEndpointURI(),
                clientAuthPost, token);
        HTTPResponse response = null;
        try {
            response = revokeRequest.toHTTPRequest().send();
            if (response.indicatesSuccess()) {
                return Response.ok().cookie(resetCookie).build();
            }

            return Response.status(response.getStatusCode()).entity(response.getContent()).cookie(resetCookie).build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).cookie(resetCookie).build();
        }
    }

    @Override
    @GET
    @Path("/userinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userInfo(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @CookieParam(OAuthConstants.ACCESS_TOKEN) String accessTokenCookie) {

        Optional<BearerAccessToken> accessToken = Optional.ofNullable(accessTokenCookie)
                .map(token -> new BearerAccessToken(token))
                .or(() -> Optional.ofNullable(authorizationHeader).map(token -> {
                    try {
                        return BearerAccessToken.parse(token);
                    } catch (ParseException e) {
                        LOGGER.error(e.getMessage(), e);
                        return null;
                    }
                }));

        if (accessToken.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        try {
            HTTPResponse httpResponse = new UserInfoRequest(authServerProvider.getUserInfoEndpointURI(),
                    accessToken.get()).toHTTPRequest().send();
            UserInfoResponse userInfoResponse = UserInfoResponse.parse(httpResponse);

            if (!userInfoResponse.indicatesSuccess()) {
                ErrorObject errorObject = userInfoResponse.toErrorResponse().getErrorObject();
                return Response.status(Status.UNAUTHORIZED).entity(errorObject.getDescription()).build();
            }

            return Response.ok().entity(userInfoResponse.toSuccessResponse().getUserInfo().toJSONString()).build();

        } catch (IOException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Response clientCredentialsGrantFlow(Form form) {
        String username = form.asMap().getFirst("username");
        String password = form.asMap().getFirst("password");

        if (isBadRequest(username, password)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        AuthorizationGrant passwordGrant = new ResourceOwnerPasswordCredentialsGrant(username, new Secret(password));

        return performAuthorizationGrantOperation(passwordGrant, getRequestedScopes(form));

    }

    private Response refreshTokenGrantFlow(Form form, String refreshTokenCookie) {
        String refreshToken = Optional.ofNullable(refreshTokenCookie)
                .orElse(form.asMap().getFirst(OAuthConstants.REFRESH_TOKEN));

        if (isBadRequest(refreshToken)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        return performAuthorizationGrantOperation(new RefreshTokenGrant(new RefreshToken(refreshToken)), null);
    }

    private Response deviceGrantFlow(Form form) {
        String deviceCode = form.asMap().getFirst(GrantType.DEVICE_CODE.getValue());

        if (isBadRequest(deviceCode)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        AuthorizationGrant deviceGrant = new DeviceCodeGrant(new DeviceCode(deviceCode));

        return performAuthorizationGrantOperation(deviceGrant, getRequestedScopes(form));
    }

    private Response authorizationCodeGrantFlow(Form form) {
        String code = form.asMap().getFirst("code");
        String redirectUriRaw = form.asMap().getFirst("redirect_uri");
        String codeChallenge = form.asMap().getFirst("code_challenge");

        if (isBadRequest(code, redirectUriRaw)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        URI redirectUri;

        try {
            redirectUri = new URI(redirectUriRaw);
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Status.BAD_REQUEST).build();
        }

        CodeVerifier codeVerifier = codeChallenge != null ? new CodeVerifier(codeChallenge) : null;

        return performAuthorizationGrantOperation(
                new AuthorizationCodeGrant(new AuthorizationCode(code), redirectUri, codeVerifier), null);
    }

    private Scope getRequestedScopes(Form form) {

        List<String> scope = form.asMap().get("scope");
        Scope scopes = new Scope();

        if (scope != null) {
            scope.forEach(sc -> scopes.add(sc));
        }

        return scopes;
    }

    private Optional<String> getDeviceCodeInfo(Form form) {
        HttpPost httpPost = new HttpPost(authServerProvider.getDeviceAuthorizationEndpointURI());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Scope scopes = getRequestedScopes(form);
        if (!scopes.isEmpty())
            params.add(new BasicNameValuePair("scope", scopes.toString()));
        params.add(new BasicNameValuePair("client_id", clientAuthPost.getClientID().getValue()));
        params.add(new BasicNameValuePair("client_secret", clientAuthPost.getClientSecret().getValue()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try (CloseableHttpResponse response = httpClientProvider.getClient().execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() == Status.OK.getStatusCode()) {
                String responseString = new BasicResponseHandler().handleResponse(response);
                return Optional.ofNullable(responseString);
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private static <T> T getEnv(String key, Class<T> clazz, T def) {
        String envValue = System.getenv(key);
        if (envValue != null) {
            try {
                if (Integer.class.equals(clazz) || String.class.equals(clazz)) {
                    return clazz.getDeclaredConstructor(String.class).newInstance(envValue);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return def;
    }

    private Response performAuthorizationGrantOperation(AuthorizationGrant grant, Scope scopes) {

        TokenRequest request = new TokenRequest(authServerProvider.getTokenEndpointURI(), clientAuthPost, grant,
                scopes);

        TokenResponse response = null;
        try {
            response = TokenResponse.parse(request.toHTTPRequest().send());
        } catch (IOException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        if (!response.indicatesSuccess()) {
            ErrorObject error = response.toErrorResponse().getErrorObject();
            return Response.status(error.getHTTPStatusCode()).entity(error.toJSONObject().toString()).build();
        }

        AccessTokenResponse successResponse = response.toSuccessResponse();

        String accessToken = successResponse.getTokens().getBearerAccessToken().getValue();
        String refreshToken = successResponse.getTokens().getRefreshToken().getValue();

        List<NewCookie> cookies = convertTokensToCookies(accessToken, refreshToken);

        return Response.ok().cookie(cookies.stream().toArray(NewCookie[]::new))
                .entity(successResponse.getTokens().toString()).build();
    }

    private List<NewCookie> convertTokensToCookies(String accessToken, String refreshToken) {
        Preconditions.checkArgument(accessToken != null);
        Preconditions.checkArgument(refreshToken != null);

        NewCookie accessTokenCookie = createTokenCookie(OAuthConstants.ACCESS_TOKEN, accessToken,
                OAUTH_COOKIE_EXPIRE_ACCESS_TOKEN);
        NewCookie refreshTokenCookie = createTokenCookie(OAuthConstants.REFRESH_TOKEN, refreshToken,
                OAUTH_COOKIE_EXPIRE_REFRESH_TOKEN);

        return List.of(accessTokenCookie, refreshTokenCookie);
    }

    private NewCookie createTokenCookie(String tokenTypeHint, String token, int maxAge) {
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.SECOND, maxAge);
        return new NewCookie(tokenTypeHint, token, OAUTH_COOKIE_PATH, OAUTH_COOKIE_DOMAIN, NewCookie.DEFAULT_VERSION,
                null, maxAge, expireTime.getTime(), false, true);
    }

    private boolean isBadRequest(String... params) {
        for (String param : params) {
            if (isNullOrEmpty(param)) {
                return true;
            }
        }
        return false;
    }

    private void disableSSLVerification() {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HTTPRequest.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private static boolean isSslVerificationEnabled() {
        if (!Boolean.getBoolean("ru.agentlab.ssl.verification.enabled")) {
            return Boolean.parseBoolean(System.getProperty("SSL_VERIFICATION_ENABLED"));
        }
        return true;
    }
}
