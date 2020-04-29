package ru.agentlab.security.tests;

import java.util.stream.Stream;

import javax.inject.Inject;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import com.nimbusds.oauth2.sdk.GrantType;

import ru.agentlab.security.oauth.service.IAuthService;
import ru.agentlab.security.tests.mock.wso2.Wso2TestConstants;
import ru.agentlab.security.tests.utils.FileUtil;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class AuthServiceTest extends SecurityJaxrsTestSupport {

    private static final String SUCCESS_TOKEN_RESPONSE_FILE = "success-token-response.json";
    private static final String AUTHENTICATION_FAILED_FOR_TESTUSER_FILE = "authentication-failed-for-testuser.json";

    @Configuration
    public Option[] config() {
        Option[] options = new Option[] {
                // uncomment if you need to debug (blocks test execution and waits for the
                // debugger)
                // KarafDistributionOption.debugConfiguration("5005", true)
        };
        return Stream.of(super.config(), options).flatMap(Stream::of).toArray(Option[]::new);
    }

    @Inject
    private IAuthService authService;

    @Test
    public void checkSuccessfulPasswordGrant() {

        Form form = new Form();
        form.param(Wso2TestConstants.GRANT_TYPE, GrantType.PASSWORD.getValue())
                .param(Wso2TestConstants.PASSWORD, Wso2TestConstants.TESTUSER_PASSWORD)
                .param(Wso2TestConstants.USERNAME, Wso2TestConstants.TESTUSER_USERNAME)
                .param(Wso2TestConstants.SCOPE, Wso2TestConstants.OPENID);

        Response response = authService.grantOperation(form);

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(readFile(SUCCESS_TOKEN_RESPONSE_FILE), response.getEntity());
    }

    @Test
    public void checkInvalidPasswordPasswordGrant() {

        Form form = new Form();
        form.param(Wso2TestConstants.GRANT_TYPE, GrantType.PASSWORD.getValue())
                .param(Wso2TestConstants.PASSWORD, Wso2TestConstants.TESTUSER_INVALID_PASSWORD)
                .param(Wso2TestConstants.USERNAME, Wso2TestConstants.TESTUSER_USERNAME)
                .param(Wso2TestConstants.SCOPE, Wso2TestConstants.OPENID);

        Response response = authService.grantOperation(form);

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        Assert.assertEquals(readFile(AUTHENTICATION_FAILED_FOR_TESTUSER_FILE), response.getEntity());
    }

//    @Test
    public void checkActiveRefreshTokenGrant() {

        Form form = new Form();
        form.param(Wso2TestConstants.GRANT_TYPE, GrantType.REFRESH_TOKEN.getValue())
                .param(Wso2TestConstants.REFRESH_TOKEN, Wso2TestConstants.ACTIVE_REFRESH_TOKEN);

        Response response = authService.grantOperation(form);

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        Assert.assertEquals(readFile(SUCCESS_TOKEN_RESPONSE_FILE), response.getEntity());
    }

    private String readFile(String fileName) {
        return FileUtil.readFile(this.getClass().getClassLoader().getResource(fileName));
    }

}
