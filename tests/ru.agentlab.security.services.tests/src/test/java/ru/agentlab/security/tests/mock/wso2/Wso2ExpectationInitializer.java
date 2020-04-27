package ru.agentlab.security.tests.mock.wso2;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;

import java.io.UncheckedIOException;

import org.mockserver.client.MockServerClient;
import org.mockserver.model.JsonBody;
import org.mockserver.model.MediaType;

import ru.agentlab.security.tests.utils.FileUtil;

public class Wso2ExpectationInitializer implements org.mockserver.client.initialize.PluginExpectationInitializer {

    @Override
    public void initializeExpectations(MockServerClient mockServerClient) {

        mockOpenIdConfig(mockServerClient);
        mockJwksConfig(mockServerClient);
        mockPasswordAuth(mockServerClient);
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
//                    .withContentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .withPath("/oauth2/token")
                    .withBody(
                            params(
                                    param("grant_type", "password"),
                                    param("password", "testuser"),
                                    param("username", "testuser"),
                                    param("scope", "openid"),
                                    param("client_secret", Wso2TestConstants.CLIENT_SECRET),
                                    param("client_id", Wso2TestConstants.CLIENT_ID)
                                    )
                            )
                    )
            .respond(response()
                    .withStatusCode(OK_200.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(new JsonBody(getSuccessTokenResponse())));
        // @formatter:on

        return mockServerClient;
    }

    private String getSuccessTokenResponse() throws UncheckedIOException {
        return FileUtil.readFileFromResourses("success-token-response.json");
    }

}
