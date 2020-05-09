package ru.agentlab.security.tests;

import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.EXPIRED_ACESS_JWT_TOKEN;
import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.HACKED_ACCESS_JWT_TOKEN;
import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.NON_JWT_ACCESS_TOKEN;
import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.VALID_ACCESS_JWT_TOKEN;

import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import ru.agentlab.security.jwt.service.IJwtService;
import ru.agentlab.security.security.service.ISecurityService;
import ru.agentlab.security.security.service.TokenPayload;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class SecurityServiceTest extends SecurityJaxrsTestSupport {

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
    private ISecurityService securityService;
    @Inject
    private IJwtService jwtService;

    @Test
    public void checkIsTokenExpired() {
        Assert.assertFalse(securityService.isTokenExpired(VALID_ACCESS_JWT_TOKEN));
        Assert.assertTrue(securityService.isTokenExpired(EXPIRED_ACESS_JWT_TOKEN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIsTokenExpiredWithNullToken() {
        securityService.isTokenExpired(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIsTokenExpiredWithEmptyToken() {
        securityService.isTokenExpired("");
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void checkIsTokenExpiredWithNonJwtToken() {
        securityService.isTokenExpired(NON_JWT_ACCESS_TOKEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkSetSubjectWithNullToken() {
        securityService.setSubject(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkSetSubjectWithEmptyToken() {
        securityService.setSubject("");
    }

    @Test(expected = AuthenticationException.class)
    public void checkSetSubjectWithHackedToken() {
        securityService.setSubject(HACKED_ACCESS_JWT_TOKEN);
    }

    @Test(expected = AuthenticationException.class)
    public void checkSetSubjectWithExpiredToken() {
        securityService.setSubject(EXPIRED_ACESS_JWT_TOKEN);
    }

    @Test
    public void checkSetSubjectWithValidToken() throws JsonMappingException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        TokenPayload tokenPayload = mapper.readValue(jwtService.getTokenPayload(VALID_ACCESS_JWT_TOKEN), TokenPayload.class);

        securityService.setSubject(VALID_ACCESS_JWT_TOKEN);

        Assert.assertEquals(tokenPayload, SecurityUtils.getSubject().getPrincipal());
        Assert.assertTrue(SecurityUtils.getSubject().hasAllRoles(tokenPayload.getGroups()));
    }

}
