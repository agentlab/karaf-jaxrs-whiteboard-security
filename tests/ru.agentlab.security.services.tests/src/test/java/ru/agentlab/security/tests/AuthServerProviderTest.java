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

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import ru.agentlab.security.oauth.commons.service.IAuthServerProvider;
import ru.agentlab.security.tests.mock.wso2.Wso2TestConstants;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class AuthServerProviderTest extends SecurityJaxrsTestSupport {

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
    private IAuthServerProvider authServerProvider;

    @Test
    public void checkService() {
        // @formatter:off
        assertEquals(Wso2TestConstants.WSO2_DEVICE_ENDPOINT,authServerProvider.getDeviceAuthorizationEndpointURI().toString());
        assertEquals(Wso2TestConstants.WSO2_INTROSPECTION_ENDPOINT,authServerProvider.getIntrospectionEndpointURI().toString());
        assertEquals(Wso2TestConstants.WSO2_JKWS_ENDPOINT, authServerProvider.getJWKSetURI().toString());
        assertEquals(Wso2TestConstants.WSO2_REVOCATION_ENDPOINT,authServerProvider.getRevocationEndpointURI().toString());
        assertEquals(Wso2TestConstants.WSO2_TOKEN_ENDPOINT, authServerProvider.getTokenEndpointURI().toString());
        assertEquals(Wso2TestConstants.WSO2_USERINFO_ENDPOINT, authServerProvider.getUserInfoEndpointURI().toString());
        // @formatter:on
    }
}
