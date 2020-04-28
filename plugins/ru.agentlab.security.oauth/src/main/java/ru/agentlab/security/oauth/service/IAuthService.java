package ru.agentlab.security.oauth.service;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

public interface IAuthService {

    Response grantOperation(Form form);

    Response getDeviceGrantInfo(Form form);

    Response revokeToken(Form form, String accessTokenCookie, String refreshTokenCookie);

    Response userInfo(String authorizationHeader, String accessTokenCookie);
}
