package ru.agentlab.security.jwt.service.impl;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Resource;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import ru.agentlab.security.jwt.service.IJwtService;
import ru.agentlab.security.jwt.service.JwtException;
import ru.agentlab.security.oauth.commons.service.IAuthServerProvider;
import ru.agentlab.security.oauth.commons.service.IHttpClientProvider;

@Component
public class TokenServiceImpl implements IJwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private volatile ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

    private Object lock = new Object();

    @Reference
    private IAuthServerProvider authServerProvider;
    @Reference
    private IHttpClientProvider httpClientProvider;

    @Override
    public boolean isValid(String jwt) throws JwtException {

        if (Strings.isNullOrEmpty(jwt))
            return false;

        try {
            getJwtProcessor().process(preProcessToken(jwt), null);
        } catch (ParseException | BadJOSEException | JOSEException e) {
            throw new JwtException(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public boolean isExpired(String jwt) throws JwtException {

        if (Strings.isNullOrEmpty(jwt)) {
            throw new JwtException("Empty token");
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(preProcessToken(jwt));
            if (new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())) {
                return false;
            }
        } catch (ParseException e) {
            throw new JwtException(e.getMessage(), e);
        }

        return true;
    }

    @Override
    public String getTokenPayload(String jwt) throws JwtException {

        if (Strings.isNullOrEmpty(jwt))
            return null;

        try {
            return getJwtProcessor().process(preProcessToken(jwt), null).toJSONObject().toString();
        } catch (ParseException | BadJOSEException | JOSEException e) {

            throw new JwtException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getClaimsMap(String jwt) throws JwtException {

        if (Strings.isNullOrEmpty(jwt))
            return null;

        try {
            Map<String, Object> claimsMap = getJwtProcessor().process(preProcessToken(jwt), null).getClaims();

            return claimsMap;
        } catch (ParseException | BadJOSEException | JOSEException e) {
            throw new JwtException(e.getMessage(), e);
        }
    }

    private ConfigurableJWTProcessor<SecurityContext> getJwtProcessor() {
        if (jwtProcessor == null) {
            synchronized (lock) {
                if (jwtProcessor != null)
                    return jwtProcessor;
                try {
                    ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
                    JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(authServerProvider.getJWKSetURI().toURL(),
                            new JwkResourceRetriever());
                    jwtProcessor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource));
                    this.jwtProcessor = jwtProcessor;
                } catch (MalformedURLException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        return jwtProcessor;
    }

    private String preProcessToken(String token) {
        return StringUtils.removeStart(token, "Bearer ");
    }

    private class JwkResourceRetriever implements ResourceRetriever {

        @Override
        public Resource retrieveResource(URL url) throws IOException {

            HttpGet httpGet = new HttpGet(url.toString());

            try (CloseableHttpResponse response = httpClientProvider.getClient().execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String keys = EntityUtils.toString(response.getEntity());
                    String contentType = response.getEntity().getContentType() == null ? null
                            : response.getEntity().getContentType().getValue();
                    return new Resource(keys, contentType);
                }
                throw new IOException(format("Resource {0} could not be retrieved", url.toString()));
            }
        }
    }

}
