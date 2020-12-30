/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.tests;

import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.EXPIRED_ACESS_JWT_TOKEN;
import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.HACKED_ACCESS_JWT_TOKEN;
import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.NON_JWT_ACCESS_TOKEN;
import static ru.agentlab.security.tests.mock.wso2.Wso2TestConstants.VALID_ACCESS_JWT_TOKEN;

import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import ru.agentlab.security.jwt.service.IJwtService;
import ru.agentlab.security.jwt.service.JwtException;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JwtServiceTest extends SecurityJaxrsTestSupport {

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
    private IJwtService jwtService;

    @Test
    public void checkIsValidWithValidToken() {
        Assert.assertTrue(jwtService.isValid(VALID_ACCESS_JWT_TOKEN));
    }

    @Test(expected = JwtException.class)
    public void checkIsValidWithExpiredToken() {
        jwtService.isValid(EXPIRED_ACESS_JWT_TOKEN);
    }

    @Test
    public void checkIsValidWithEmptyToken() {
        Assert.assertFalse(jwtService.isValid(null));
        Assert.assertFalse(jwtService.isValid(""));
    }

    @Test(expected = JwtException.class)
    public void checkIsValidWithHackedToken() {
        jwtService.isValid(HACKED_ACCESS_JWT_TOKEN);
    }

    @Test(expected = JwtException.class)
    public void checkIsValidWithNonJwtToken() {
        jwtService.isValid(NON_JWT_ACCESS_TOKEN);
    }

    @Test
    public void checkIsExpiredJwtToken() {
        Assert.assertTrue(jwtService.isExpired(EXPIRED_ACESS_JWT_TOKEN));
        Assert.assertFalse(jwtService.isExpired(VALID_ACCESS_JWT_TOKEN));
    }

    @Test(expected = JwtException.class)
    public void checkIsExpiredNullToken() {
        jwtService.isExpired(null);
    }

    @Test(expected = JwtException.class)
    public void checkIsExpiredEmptyToken() {
        jwtService.isExpired("");
    }

    @Test(expected = JwtException.class)
    public void checkIsExpiredWithNonJwtTiken() {
        jwtService.isExpired(NON_JWT_ACCESS_TOKEN);
    }

    @Test
    public void checkGetPayloadFromValidToken() {
        Assert.assertNotNull(jwtService.getTokenPayload(VALID_ACCESS_JWT_TOKEN));
    }

    @Test(expected = JwtException.class)
    public void checkGetPayloadFromHackedToken() {
        jwtService.getTokenPayload(null);
    }

    @Test(expected = JwtException.class)
    public void checkGetPayloadFromExpiredToken() {
        jwtService.getTokenPayload(EXPIRED_ACESS_JWT_TOKEN);
    }

    @Test(expected = JwtException.class)
    public void checkGetPayloadFromEmptyToken() {
        jwtService.getTokenPayload("");
    }

    @Test(expected = JwtException.class)
    public void getPayloadFromNullToken() {
        jwtService.getTokenPayload(null);
    }

    @Test(expected = JwtException.class)
    public void getPayloadFromNonJwtToken() {
        jwtService.getTokenPayload(NON_JWT_ACCESS_TOKEN);
    }
}
