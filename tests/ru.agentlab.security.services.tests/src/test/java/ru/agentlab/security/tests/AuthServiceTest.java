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

import ru.agentlab.security.oauth.service.IAuthService;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class AuthServiceTest extends SecurityJaxrsTestSupport {

    @Configuration
    public Option[] config() {
        Option[] options = new Option[] {
                // uncomment if you need to debug (blocks test execution and waits for the
                // debugger)
//                KarafDistributionOption.debugConfiguration("5005", true) 
        };
        return Stream.of(super.config(), options).flatMap(Stream::of).toArray(Option[]::new);
    }

    @Inject
    private IAuthService authService;

    @Test
    public void checkSuccessfulPasswordAuth() {

        Form form = new Form();
        form.param("grant_type", "password").param("password", "testuser").param("username", "testuser").param("scope",
                "openid");

        Response response = authService.grantOperation(form);

        Assert.assertEquals(200, response.getStatus());

//        Assert.assertEquals(FileUtil.readFileFromResourses("success-token-response.json"), response.getEntity());
    }

}
