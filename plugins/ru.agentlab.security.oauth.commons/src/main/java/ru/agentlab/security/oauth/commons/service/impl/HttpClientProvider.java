package ru.agentlab.security.oauth.commons.service.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.agentlab.security.oauth.commons.service.IHttpClientProvider;

@Component
public class HttpClientProvider implements IHttpClientProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientProvider.class);

    private CloseableHttpClient httpClient;

    public HttpClientProvider() {

        httpClient = HttpClients.createDefault();

        if (!isSslVerificationEnabled()) {
            try {
                TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                        NoopHostnameVerifier.INSTANCE);

                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                        .<ConnectionSocketFactory>create().register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory()).build();

                BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
                        socketFactoryRegistry);
                httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(connectionManager)
                        .build();

            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                LOGGER.error(e.getMessage(), e);
                httpClient = HttpClients.createDefault();
            }
        }
    }

    @Override
    public CloseableHttpClient getClient() {
        return httpClient;
    }

    private static boolean isSslVerificationEnabled() {
        if (!Boolean.getBoolean("ru.agentlab.ssl.verification.enabled")) {
            return Boolean.parseBoolean(System.getProperty("SSL_VERIFICATION_ENABLED"));
        }
        return true;
    }

}
