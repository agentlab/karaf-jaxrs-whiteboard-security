package ru.agentlab.security.tests;

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

import ru.agentlab.security.jwt.service.IJwtService;

//@RunWith(PaxExam.class)
//@ExamReactorStrategy(PerClass.class)
public class JwtServiceTest extends SecurityJaxrsTestSupport {

    private static final String VALID_JWT_TOKEN = "eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJ0ZXN0dXNlciIsImF1ZCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJuYmYiOjE1ODc4MjA0OTUsImF6cCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJzY29wZSI6Im9wZW5pZCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsIm5hbWUiOiJ0ZXN0dXNlciIsImdyb3VwcyI6IkludGVybmFsXC9ldmVyeW9uZSIsImV4cCI6Mzc1ODc4MjA0OTUsImlhdCI6MTU4NzgyMDQ5NSwianRpIjoiYmUzMjIwZjItMjM2MS00OWMxLTlmZDEtMWZkOGVhMzQwYzc2In0.T8PRblcxIci6UW9zzZ8mgMv6BYGYj9BTijnH3H-Ddo4cZcGZkpdM4tp9dY851EMcX-7nl3-TTOsU1Hk4e3X9qXcnERQrPQAAkfAIqs2NwBHBCoJlCaGFikNVe0C6hdFLIBx8sPrM6lLq_dbABRCK-BCn7kMvf3SIML8GfuMPiizL_b0y7QusfofoJCBJG6HAiMjKE3XKZEWzs3ng6CIIz6mupC_CIErjt9HIxcazTr9MPFCi9ReOlAhqspxDULYAPtW4J6rBQYHX3T4srCB1lNqEJ_Mnc6PDMQ2slWwkKV87OTcOThE-xW7FfYlVN7OyKYyIT4xYih7HO0Wh35zxGg";

    private static final String EXPIRED_JWT_TOKEN = "eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJ0ZXN0dXNlciIsImF1ZCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJuYmYiOjE1ODc4MjA4NzcsImF6cCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJzY29wZSI6Im9wZW5pZCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsIm5hbWUiOiJ0ZXN0dXNlciIsImdyb3VwcyI6IkludGVybmFsXC9ldmVyeW9uZSIsImV4cCI6MTU4NzgyMDg4MCwiaWF0IjoxNTg3ODIwODc3LCJqdGkiOiI0ZmIzZDM4MS1jYjIwLTQxNmYtOGM4OS0wYWNiNGNjYzdkYzUifQ.oT8_zWlfe9ZEJ1rQk3MBeu6qR1Ou4oC6rOzqt-lot7CG0YSMVGHRWmfDgl96jI66gXqI24C8FRQDI0hE4RZUoj654SzxOsNvVFP-S5hyMec-B2BwhDLktJGmktTA_SngAAZqXrG-4Ij1_I2tZMVIIO8gJ2Sly18jiNAYqrbKAmd9-NDvrwvRGOQmrY6OfXv38grt28Hpp6yEfpbZKwgMVQ6QMuS5tU4LVObRdSgFXL_zbIOIEyjkI1PXK6il-P2Az0jclGcKGZpsYmZXG6tHQzmSxaxCxg8HpQBKbvsBEIm7fKfDYTreFYzkmIlamRzhMXvkxaC0uPs71wbPkzJetA";

//    @Configuration
    public Option[] config() {
        Option[] options = new Option[] {
                // uncomment if you need to debug (blocks test execution and waits for the
                // debugger)
                // KarafDistributionOption.debugConfiguration("5005", true),
        };
        return Stream.of(super.config(), options).flatMap(Stream::of).toArray(Option[]::new);
    }

//    @Inject
    private IJwtService jwtService;

//    @Test
    public void checkClientNotNull() {
        assertNotNull(jwtService);
    }

}
