package ru.agentlab.security.tests.mock.wso2;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.BAD_REQUEST_400;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.mockserver.model.HttpStatusCode.UNAUTHORIZED_401;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;

import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.mockserver.client.MockServerClient;
import org.mockserver.client.initialize.PluginExpectationInitializer;
import org.mockserver.model.JsonBody;
import org.mockserver.model.MediaType;
import org.mockserver.model.Parameter;

import com.nimbusds.oauth2.sdk.GrantType;

import ru.agentlab.security.tests.utils.FileUtil;

public class Wso2ExpectationInitializer implements PluginExpectationInitializer {

    @Override
    public void initializeExpectations(MockServerClient mockServerClient) {

        mockOpenIdConfig(mockServerClient);
        mockJwksConfig(mockServerClient);
        mockPasswordAuth(mockServerClient);
        mockRefreshToken(mockServerClient);
    }

    private MockServerClient mockOpenIdConfig(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request() 
                    .withMethod("GET")
                    .withPath("/oauth2/token/.well-known/openid-configuration"))
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(Wso2TestConstants.OPENID_CONFIGURATION)));
        // @formatter:on

        return mockServerClient;
    }

    private MockServerClient mockJwksConfig(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request() 
                    .withMethod("GET")
                    .withPath("/oauth2/jwks"))
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(Wso2TestConstants.JWKS_CONFIGURATION)));
        // @formatter:on

        return mockServerClient;
    }

    private MockServerClient mockPasswordAuth(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.PASSWORD.getValue()),
                                    param(Wso2TestConstants.PASSWORD, Wso2TestConstants.TESTUSER_PASSWORD),
                                    param(Wso2TestConstants.USERNAME, Wso2TestConstants.TESTUSER_USERNAME),
                                    getOpenIdParam(),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getSuccessTokenResponse())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.PASSWORD.getValue()),
                                    param(Wso2TestConstants.PASSWORD, Wso2TestConstants.TESTUSER_INVALID_PASSWORD),
                                    param(Wso2TestConstants.USERNAME, Wso2TestConstants.TESTUSER_USERNAME),
                                    getOpenIdParam(),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(UNAUTHORIZED_401.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getAuthenticationFailedForTestuserResponse())));
        // @formatter:on

        return mockServerClient;
    }

    private MockServerClient mockRefreshToken(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.REFRESH_TOKEN.getValue()),
                                    param(Wso2TestConstants.REFRESH_TOKEN, Wso2TestConstants.ACTIVE_REFRESH_TOKEN),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getSuccessTokenResponse())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.REFRESH_TOKEN.getValue()),
                                    param(Wso2TestConstants.REFRESH_TOKEN, Wso2TestConstants.EXPIRED_REFRESH_TOKEN),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(UNAUTHORIZED_401.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getRefreshTokenExpiredResponse())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.REFRESH_TOKEN.getValue()),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(BAD_REQUEST_400.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getRefreshTokenExpiredResponse())));
        // @formatter:on

        return mockServerClient;
    }

    private Parameter getClientIdParam() {
        return param("client_id", Wso2TestConstants.CLIENT_ID);
    }

    private Parameter getClientSecretParam() {
        return param("client_secret", Wso2TestConstants.CLIENT_SECRET);
    }

    private Parameter getOpenIdParam() {
        return param(Wso2TestConstants.SCOPE, Wso2TestConstants.OPENID);
    }

    private String getSuccessTokenResponse() throws UncheckedIOException {
        return readFileFromResourses("success-token-response.json");
    }

    private String getAuthenticationFailedForTestuserResponse() {
        return readFileFromResourses("authentication-failed-for-testuser.json");
    }

    private String getRefreshTokenExpiredResponse() {
        return readFileFromResourses("refresh-token-expired-response.json");
    }

    private String readFileFromResourses(String fileName) throws UncheckedIOException {
        return FileUtil.readFile(this.getClass().getClassLoader().getResource(fileName));
    }

}
