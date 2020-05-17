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
import org.mockserver.model.Header;
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
        mockUserInfo(mockServerClient);
        mockCodeFlow(mockServerClient);
        mockDeviceFlow(mockServerClient);
        mockRevokeRequest(mockServerClient);
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
                    .withStatusCode(BAD_REQUEST_400.code())
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

    private MockServerClient mockUserInfo(MockServerClient mockServerClient) {

        // @formatter:off
        mockServerClient
            .when(request()
                    .withHeader(new Header("Authorization", "Bearer " + Wso2TestConstants.VALID_ACCESS_JWT_TOKEN))
                    .withMethod("GET")
                    .withPath("/oauth2/userinfo")
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getUserinfoSuccess())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withHeader("Authorization", "Bearer " + Wso2TestConstants.EXPIRED_ACESS_JWT_TOKEN)
                    .withMethod("GET")
                    .withPath("/oauth2/userinfo")
                    )
            .respond(response()
                    .withStatusCode(UNAUTHORIZED_401.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getUserinfoAccessTokenValidationFailed())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withHeader("Authorization", "Bearer " + Wso2TestConstants.HACKED_ACCESS_JWT_TOKEN)
                    .withMethod("GET")
                    .withPath("/oauth2/userinfo")
                    )
            .respond(response()
                    .withStatusCode(UNAUTHORIZED_401.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getUserinfoAccessTokenValidationFailed())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withHeader("Authorization", "Bearer " + Wso2TestConstants.NON_JWT_ACCESS_TOKEN)
                    .withMethod("GET")
                    .withPath("/oauth2/userinfo")
                    )
            .respond(response()
                    .withStatusCode(UNAUTHORIZED_401.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getUserinfoAccessTokenValidationFailed())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("GET")
                    .withPath("/oauth2/userinfo")
                    )
            .respond(response()
                    .withStatusCode(BAD_REQUEST_400.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getUserinfoTokenMissing())));
        // @formatter:on

        return mockServerClient;
    }

    private MockServerClient mockCodeFlow(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getValue()),
                                    param(Wso2TestConstants.CODE, Wso2TestConstants.ACTIVE_CODE),
                                    param(Wso2TestConstants.REDIRECT_URI, Wso2TestConstants.REDIRECT_URI_VALUE),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getCodeFlowActiveCodeResponse())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getValue()),
                                    param(Wso2TestConstants.CODE, Wso2TestConstants.INACTIVE_CODE),
                                    param(Wso2TestConstants.REDIRECT_URI, Wso2TestConstants.REDIRECT_URI_VALUE),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(BAD_REQUEST_400.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getCodeFlowInactiveCodeResponse())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getValue()),
                                    param(Wso2TestConstants.REDIRECT_URI, Wso2TestConstants.REDIRECT_URI_VALUE),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response().withStatusCode(BAD_REQUEST_400.code())); // TODO: add body
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getValue()),
                                    param(Wso2TestConstants.CODE, Wso2TestConstants.INACTIVE_CODE),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response().withStatusCode(BAD_REQUEST_400.code())); // TODO: add body
        // @formatter:on

        return mockServerClient;
    }

    private MockServerClient mockDeviceFlow(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .withPath("/oauth2/device_authorize")
                    .withBody(
                            params(
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getDeviceGrantAuthDataFileResponse())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.DEVICE_CODE.getValue()),
                                    getValidDeviceCodeParam(Wso2TestConstants.DEVICE_GRANT_VALID_DEVICE_CODE),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getDeviceGrantActiveCodeResponseFile())));
        // @formatter:on

        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8))
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param(Wso2TestConstants.GRANT_TYPE, GrantType.DEVICE_CODE.getValue()),
                                    getValidDeviceCodeParam(Wso2TestConstants.DEVICE_GRANT_EXPIRED_DEVICE_CODE),
                                    getClientIdParam(),
                                    getClientSecretParam()
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(BAD_REQUEST_400.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getDeviceGrantExpiredCodeResponseFile())));
        // @formatter:on

        return mockServerClient;
    }

    private MockServerClient mockRevokeRequest(MockServerClient mockServerClient) {
        // @formatter:off
        mockServerClient
            .when(request()
                    .withMethod("POST")
                    .withPath("/oauth2/revoke"))
            .respond(response().withStatusCode(OK_200.code()));
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

    private Parameter getValidDeviceCodeParam(String deviceCode) {
        return param("device_code", deviceCode);
    }

    private String getSuccessTokenResponse() throws UncheckedIOException {
        return readFileFromResourses(Wso2TestConstants.SUCCESS_TOKEN_RESPONSE_FILE);
    }

    private String getAuthenticationFailedForTestuserResponse() {
        return readFileFromResourses(Wso2TestConstants.AUTHENTICATION_FAILED_FOR_TESTUSER_FILE);
    }

    private String getRefreshTokenExpiredResponse() {
        return readFileFromResourses(Wso2TestConstants.REFRESH_TOKEN_EXPIRED_RESPONSE_FILE);
    }

    private String getUserinfoAccessTokenValidationFailed() {
        return readFileFromResourses(Wso2TestConstants.USERINFO_ACCESS_TOKEN_VALIDATION_FAILED_FILE);
    }

    private String getUserinfoSuccess() {
        return readFileFromResourses(Wso2TestConstants.USERINFO_SUCCESS_FILE);
    }

    private String getUserinfoTokenMissing() {
        return readFileFromResourses(Wso2TestConstants.USERINFO_TOKEN_MISSING_FILE);
    }

    private String getCodeFlowActiveCodeResponse() {
        return readFileFromResourses(Wso2TestConstants.SUCCESS_TOKEN_RESPONSE_FILE);
    }

    private String getCodeFlowInactiveCodeResponse() {
        return readFileFromResourses(Wso2TestConstants.CODE_GRANT_INACTIVE_CODE_RESPONSE_FILE);
    }

    private String getDeviceGrantAuthDataFileResponse() {
        return readFileFromResourses(Wso2TestConstants.DEVICE_GRANT_AUTH_DATA_RESPONSE_FILE);
    }

    private String getDeviceGrantExpiredCodeResponseFile() {
        return readFileFromResourses(Wso2TestConstants.DEVICE_GRANT_EXPIRED_CODE_RESPONSE_FILE);
    }

    private String getDeviceGrantActiveCodeResponseFile() {
        return readFileFromResourses(Wso2TestConstants.SUCCESS_TOKEN_RESPONSE_FILE);
    }

    private String readFileFromResourses(String fileName) throws UncheckedIOException {
        return FileUtil.readFile(this.getClass().getClassLoader().getResource(fileName));
    }

}
