package ru.agentlab.security.oauth.commons.service;

import org.apache.http.impl.client.CloseableHttpClient;

public interface IHttpClientProvider {
    CloseableHttpClient getClient();
}
