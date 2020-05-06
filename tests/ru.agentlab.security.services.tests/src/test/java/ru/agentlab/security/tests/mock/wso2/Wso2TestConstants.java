package ru.agentlab.security.tests.mock.wso2;

public class Wso2TestConstants {

    public static final String WSO2_PORT = System.getProperty("mockserver.serverPort", "1080");
    public static final String WSO2_BASE_URL = "http://localhost:" + WSO2_PORT;
    public static final String WSO2_JKWS_ENDPOINT = WSO2_BASE_URL + "/oauth2/jwks";
    public static final String WSO2_OPENID_CONFIG_ENDPOINT = WSO2_BASE_URL
            + "/oauth2/token/.well-known/openid-configuration";

    public static final String WSO2_INTROSPECTION_ENDPOINT = WSO2_BASE_URL + "/oauth2/introspect";
    public static final String WSO2_REVOCATION_ENDPOINT = WSO2_BASE_URL + "/oauth2/revoke";
    public static final String WSO2_TOKEN_ENDPOINT = WSO2_BASE_URL + "/oauth2/token";
    public static final String WSO2_USERINFO_ENDPOINT = WSO2_BASE_URL + "/oauth2/userinfo";
    public static final String WSO2_DEVICE_ENDPOINT = WSO2_BASE_URL + "/oauth2/device_authorize";

    public static final String OPENID_CONFIGURATION = "{\"request_parameter_supported\":true,\"claims_parameter_supported\":true,\"introspection_endpoint\":\"{0}/oauth2/introspect\",\"Response_modes_supported\":[\"query\",\"fragment\",\"form_post\"],\"scopes_supported\":[\"address\",\"phone\",\"openid\",\"profile\",\"email\"],\"check_session_iframe\":\"{0}/oidc/checksession\",\"backchannel_logout_supported\":true,\"issuer\":\"{0}/oauth2/token\",\"authorization_endpoint\":\"{0}/oauth2/authorize\",\"introspection_endpoint_auth_methods_supported\":[\"client_secret_basic\",\"client_secret_post\"],\"claims_supported\":[\"phone_number\",\"country\",\"preferred_username\",\"birthdate\",\"middle_name\",\"formatted\",\"updated_at\",\"email\",\"upn\",\"sub\",\"nickname\",\"given_name\",\"locality\",\"gender\",\"region\",\"family_name\",\"email_verified\",\"profile\",\"name\",\"locale\",\"phone_number_verified\",\"zoneinfo\",\"picture\",\"postal_code\",\"street_address\",\"website\",\"groups\",\"address\",\"iss\",\"acr\"],\"userinfo_signing_alg_values_supported\":[\"RS256\"],\"token_endpoint_auth_methods_supported\":[\"client_secret_basic\",\"client_secret_post\"],\"response_modes_supported\":[\"query\",\"fragment\",\"form_post\"],\"backchannel_logout_session_supported\":true,\"token_endpoint\":\"{0}/oauth2/token\",\"response_types_supported\":[\"id_token token\",\"code\",\"id_token\",\"device\",\"token\"],\"revocation_endpoint_auth_methods_supported\":[\"client_secret_basic\",\"client_secret_post\"],\"grant_types_supported\":[\"refresh_token\",\"urn:ietf:params:oauth:grant-type:saml2-bearer\",\"password\",\"client_credentials\",\"iwa:ntlm\",\"urn:ietf:params:oauth:grant-type:device_code\",\"authorization_code\",\"urn:ietf:params:oauth:grant-type:uma-ticket\",\"account_switch\",\"urn:ietf:params:oauth:grant-type:jwt-bearer\"],\"end_session_endpoint\":\"{0}/oidc/logout\",\"revocation_endpoint\":\"{0}/oauth2/revoke\",\"userinfo_endpoint\":\"{0}/oauth2/userinfo\",\"code_challenge_methods_supported\":[\"S256\",\"plain\"],\"jwks_uri\":\"{0}/oauth2/jwks\",\"subject_types_supported\":[\"pairwise\"],\"id_token_signing_alg_values_supported\":[\"RS256\"],\"registration_endpoint\":\"{0}/api/identity/oauth2/dcr/v1.1/register\",\"request_object_signing_alg_values_supported\":[\"RS256\",\"RS384\",\"RS512\",\"PS256\",\"none\"]}"
            .replace("{0}", WSO2_BASE_URL);
    public static final String JWKS_CONFIGURATION = "{\"keys\":[{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"MzYxMmFkOGYwMWI0ZWNmNDcxNGYwYmM4ZTA3MWI2NDAzZGQzNGM0ZGRlNjJkODFkZDRiOTFkMWFhMzU2ZGVlNg_RS256\",\"alg\":\"RS256\",\"n\":\"xeqoZYbQ_Sr8DOFQ-_qbEbCp6Vzb5hzH7oa3hf2FZxRKF0H6b8COMzz8-0mvEdYVvb_31jMEL2CIQhkQRol1IruD6nBOmkjuXJSBficklMaJZORhuCrB4roHxzoG19aWmscA0gnfBKo2oGXSjJmnZxIh-2X6syHCfyMZZ00LzDyrgoXWQXyFvCA2ax54s7sKiHOM3P4A9W4QUwmoEi4HQmPgJjIM4eGVPh0GtIANN-BOQ1KkUI7OzteHCTLu3VjxM0sw8QRayZdhniPF-U9n3fa1mO4KLBsW4mDLjg8R_JuAGTX_SEEGj0B5HWQAP6myxKFz2xwDaCGvT-rdvkktOw\"}]}";

    public static final String VALID_ACCESS_JWT_TOKEN = "eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJ0ZXN0dXNlciIsImF1ZCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJuYmYiOjE1ODc4MjA0OTUsImF6cCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJzY29wZSI6Im9wZW5pZCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsIm5hbWUiOiJ0ZXN0dXNlciIsImdyb3VwcyI6IkludGVybmFsXC9ldmVyeW9uZSIsImV4cCI6Mzc1ODc4MjA0OTUsImlhdCI6MTU4NzgyMDQ5NSwianRpIjoiYmUzMjIwZjItMjM2MS00OWMxLTlmZDEtMWZkOGVhMzQwYzc2In0.T8PRblcxIci6UW9zzZ8mgMv6BYGYj9BTijnH3H-Ddo4cZcGZkpdM4tp9dY851EMcX-7nl3-TTOsU1Hk4e3X9qXcnERQrPQAAkfAIqs2NwBHBCoJlCaGFikNVe0C6hdFLIBx8sPrM6lLq_dbABRCK-BCn7kMvf3SIML8GfuMPiizL_b0y7QusfofoJCBJG6HAiMjKE3XKZEWzs3ng6CIIz6mupC_CIErjt9HIxcazTr9MPFCi9ReOlAhqspxDULYAPtW4J6rBQYHX3T4srCB1lNqEJ_Mnc6PDMQ2slWwkKV87OTcOThE-xW7FfYlVN7OyKYyIT4xYih7HO0Wh35zxGg";
    public static final String EXPIRED_ACESS_JWT_TOKEN = "eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJ0ZXN0dXNlciIsImF1ZCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJuYmYiOjE1ODc4MjA4NzcsImF6cCI6IlluaW9fRXVZVms4ajJnbl82blViSVZRYmpfQWEiLCJzY29wZSI6Im9wZW5pZCIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsIm5hbWUiOiJ0ZXN0dXNlciIsImdyb3VwcyI6IkludGVybmFsXC9ldmVyeW9uZSIsImV4cCI6MTU4NzgyMDg4MCwiaWF0IjoxNTg3ODIwODc3LCJqdGkiOiI0ZmIzZDM4MS1jYjIwLTQxNmYtOGM4OS0wYWNiNGNjYzdkYzUifQ.oT8_zWlfe9ZEJ1rQk3MBeu6qR1Ou4oC6rOzqt-lot7CG0YSMVGHRWmfDgl96jI66gXqI24C8FRQDI0hE4RZUoj654SzxOsNvVFP-S5hyMec-B2BwhDLktJGmktTA_SngAAZqXrG-4Ij1_I2tZMVIIO8gJ2Sly18jiNAYqrbKAmd9-NDvrwvRGOQmrY6OfXv38grt28Hpp6yEfpbZKwgMVQ6QMuS5tU4LVObRdSgFXL_zbIOIEyjkI1PXK6il-P2Az0jclGcKGZpsYmZXG6tHQzmSxaxCxg8HpQBKbvsBEIm7fKfDYTreFYzkmIlamRzhMXvkxaC0uPs71wbPkzJetA";
    public static final String HACKED_ACCESS_JWT_TOKEN = "eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJoYWNrZXIiLCJhdWQiOiJZbmlvX0V1WVZrOGoyZ25fNm5VYklWUWJqX0FhIiwibmJmIjoxNTg3ODIwODc3LCJhenAiOiJZbmlvX0V1WVZrOGoyZ25fNm5VYklWUWJqX0FhIiwic2NvcGUiOiJvcGVuaWQiLCJpc3MiOiJodHRwczpcL1wvbG9jYWxob3N0Ojk0NDNcL29hdXRoMlwvdG9rZW4iLCJuYW1lIjoidGVzdHVzZXIiLCJncm91cHMiOiJJbnRlcm5hbFwvZXZlcnlvbmUiLCJleHAiOjE1ODc4MjA4ODAsImlhdCI6MTU4NzgyMDg3NywianRpIjoiNGZiM2QzODEtY2IyMC00MTZmLThjODktMGFjYjRjY2M3ZGM1In0.oT8_zWlfe9ZEJ1rQk3MBeu6qR1Ou4oC6rOzqt-lot7CG0YSMVGHRWmfDgl96jI66gXqI24C8FRQDI0hE4RZUoj654SzxOsNvVFP-S5hyMec-B2BwhDLktJGmktTA_SngAAZqXrG-4Ij1_I2tZMVIIO8gJ2Sly18jiNAYqrbKAmd9-NDvrwvRGOQmrY6OfXv38grt28Hpp6yEfpbZKwgMVQ6QMuS5tU4LVObRdSgFXL_zbIOIEyjkI1PXK6il-P2Az0jclGcKGZpsYmZXG6tHQzmSxaxCxg8HpQBKbvsBEIm7fKfDYTreFYzkmIlamRzhMXvkxaC0uPs71wbPkzJetA";
    public static final String NON_JWT_ACCESS_TOKEN = "non_jwt_token";

    public static final String CLIENT_ID = "Ynio_EuYVk8j2gn_6nUbIVQbj_Aa";
    public static final String CLIENT_SECRET = "fTJGvvfJjUkWvn8R_NY8zXSyYQ0a";

    public static final String GRANT_TYPE = "grant_type";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String SCOPE = "scope";
    public static final String OPENID = "openid";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String REDIRECT_URI_VALUE = "http://example.com:3000/oauth20/callback";

    public static final String ACTIVE_CODE = "9add73e1-6281-3e35-9841-7615f35a2f5d";
    public static final String INACTIVE_CODE = "8add73e1-6281-3e35-9841-7615f35a2f5d";

    public static final String TESTUSER_PASSWORD = "testuser";
    public static final String TESTUSER_USERNAME = "testuser";
    public static final String TESTUSER_INVALID_PASSWORD = "invalid_password";

    public static final String ACTIVE_REFRESH_TOKEN = "f6351bad-ec29-3c2d-869d-7264d81d505c";
    public static final String EXPIRED_REFRESH_TOKEN = "f6351bad-ec29-3c2d-869d-7264d81d505e";

    public static final String DEVICE_GRANT_VALID_DEVICE_CODE = "a397b9fd-5460-4928-b180-4b4373ab1bde";
    public static final String DEVICE_GRANT_EXPIRED_DEVICE_CODE = "a397b9fd-5460-4928-b180-4b4373ab1bee";

    public static final String SUCCESS_TOKEN_RESPONSE_FILE = "success-token-response.json";
    public static final String AUTHENTICATION_FAILED_FOR_TESTUSER_FILE = "authentication-failed-for-testuser.json";
    public static final String REFRESH_TOKEN_EXPIRED_RESPONSE_FILE = "refresh-token-expired-response.json";
    public static final String USERINFO_ACCESS_TOKEN_VALIDATION_FAILED_FILE = "userinfo-access-token-validation-failed.json";
    public static final String USERINFO_SUCCESS_FILE = "userinfo-success.json";
    public static final String USERINFO_TOKEN_MISSING_FILE = "userinfo-token-missing.json";
    public static final String CODE_GRANT_INACTIVE_CODE_RESPONSE_FILE = "code-grant-inactive-code-response.json";
    public static final String DEVICE_GRANT_AUTH_DATA_RESPONSE_FILE = "device-grant-auth-data-response.json";
    public static final String DEVICE_GRANT_EXPIRED_CODE_RESPONSE_FILE = "device-grant-expired-code-response.json";

    public static final String TOKENS_RESPONSE_FILE = "tokens-response.json";
}
