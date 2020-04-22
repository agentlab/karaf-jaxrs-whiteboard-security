package ru.agentlab.security.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import ru.agentlab.security.oauth.commons.service.IHttpClientProvider;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class HttpClientProviderTest extends Rdf4jJaxrsTestSupport {

    @Configuration
    public Option[] config() {
        Option[] options = new Option[] {
                // uncomment if you need to debug (blocks test execution and waits for the
                // debugger)
                // KarafDistributionOption.debugConfiguration("5005", true),
        };
        return Stream.of(super.config(), options).flatMap(Stream::of).toArray(Option[]::new);
    }

    @Inject
    private IHttpClientProvider httpClientProvider;

    @Test
    public void checkClientNotNull() {
        System.out.println("\n\n\n\n\n\n");
        
        assertEquals(3, 5);
        
        assertNotNull(httpClientProvider.getClient());
        
        
        
        System.out.println("\n\n\n\n\n\n");
    }

}
