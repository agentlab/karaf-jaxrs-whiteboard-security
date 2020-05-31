package ru.agentlab.security.security.service.impl;

import java.io.IOException;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import ru.agentlab.security.jwt.service.IJwtService;
import ru.agentlab.security.jwt.service.JwtException;
import ru.agentlab.security.security.service.ISecurityService;
import ru.agentlab.security.security.service.TokenPayload;

@Component
public class SecurityServiceImpl implements ISecurityService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final AuthorizingRealm realm;
    private final SecurityManager securityManager;

    static {
        MAPPER.registerModule(new Jdk8Module());
        MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Reference
    private IJwtService jwtService;

    public SecurityServiceImpl() {
        realm = new BearerRealm();
        realm.setCachingEnabled(false);

        securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Override
    public void setSubject(String accessToken) throws AuthenticationException, IllegalArgumentException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(accessToken));

        BearerToken token = new BearerToken(accessToken);
        SecurityUtils.getSubject().login(token);
    }

    public boolean isTokenExpired(String accessToken) throws IllegalArgumentException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(accessToken));
        try {
            return jwtService.isExpired(accessToken);
        } catch (JwtException e) {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        }
    }

    private class BearerRealm extends AuthorizingRealm {

        BearerRealm() {
            this.setAuthenticationTokenClass(BearerToken.class);
        }

        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

            Collection<TokenPayload> tokenPayload = principals.byType(TokenPayload.class);

            TokenPayload payload = null;

            if (!tokenPayload.isEmpty()) {
                payload = tokenPayload.iterator().next();
            }

            SimpleAuthorizationInfo simpleAuth = new SimpleAuthorizationInfo();

            if ("testuser".equals(payload.getName())) {
                simpleAuth.addObjectPermission(new WildcardPermission("repository:read:123"));
            } else if ("admin".equals(payload.getName())) {
                simpleAuth.addObjectPermission(new WildcardPermission("repository:*"));
            }

            if (payload != null) {
                simpleAuth.addRoles(payload.getGroups());
            }

            return simpleAuth;
        }

        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

            if (token == null) {
                throw new IncorrectCredentialsException("Token can not be null");
            }

            if (token instanceof BearerToken) {

                BearerToken bearerToken = (BearerToken) token;

                String tokenPayload = null;

                try {
                    tokenPayload = jwtService.getTokenPayload(bearerToken.getToken());
                } catch (JwtException e) {
                    throw new IncorrectCredentialsException(e.getMessage(), e);
                }

                TokenPayload payload = getTokenPayload(tokenPayload);

                SimplePrincipalCollection principals = new SimplePrincipalCollection(payload, getName());

                return new SimpleAuthenticationInfo(principals, bearerToken.getCredentials());

            } else {
                throw new UnsupportedTokenException(token.getClass().toString());
            }
        }

        private TokenPayload getTokenPayload(String tokenPayload) throws IncorrectCredentialsException {
            try {
                return MAPPER.readValue(tokenPayload, TokenPayload.class);
            } catch (IOException e) {
                throw new IncorrectCredentialsException(e);
            }
        }
    }

}
