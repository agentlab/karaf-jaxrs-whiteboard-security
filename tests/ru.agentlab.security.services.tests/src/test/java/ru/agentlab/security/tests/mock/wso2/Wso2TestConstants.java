package ru.agentlab.security.tests.mock.wso2;

public class Wso2TestConstants {

    private static final String WSO2_PORT = System.getProperty("mockserver.serverPort", "1080");
    public static final String WSO2_BASE_URL = "http://localhost:" + WSO2_PORT;
    public static final String WSO2_JKWS_ENDPOINT = WSO2_BASE_URL + "/oauth2/jwks";
    public static final String WSO2_OPENID_CONFIG_ENDPOINT = WSO2_BASE_URL
            + "/oauth2/token/.well-known/openid-configuration";

    public static final String WSO2_INTROSPECTION_ENDPOINT = WSO2_BASE_URL + "/oauth2/introspect";
    public static final String WSO2_REVOCATION_ENDPOINT = WSO2_BASE_URL + "/oauth2/revoke";
    public static final String WSO2_TOKEN_ENDPOINT = WSO2_BASE_URL + "/oauth2/token";
    public static final String WSO2_USERINFO_ENDPOINT = WSO2_BASE_URL + "/oauth2/userinfo";

    public static final String OPENID_CONFIGURATION = "{\"request_parameter_supported\":true,\"claims_parameter_supported\":true,\"introspection_endpoint\":\"{0}/oauth2/introspect\",\"Response_modes_supported\":[\"query\",\"fragment\",\"form_post\"],\"scopes_supported\":[\"address\",\"phone\",\"openid\",\"profile\",\"email\"],\"check_session_iframe\":\"{0}/oidc/checksession\",\"backchannel_logout_supported\":true,\"issuer\":\"{0}/oauth2/token\",\"authorization_endpoint\":\"{0}/oauth2/authorize\",\"introspection_endpoint_auth_methods_supported\":[\"client_secret_basic\",\"client_secret_post\"],\"claims_supported\":[\"phone_number\",\"country\",\"preferred_username\",\"birthdate\",\"middle_name\",\"formatted\",\"updated_at\",\"email\",\"upn\",\"sub\",\"nickname\",\"given_name\",\"locality\",\"gender\",\"region\",\"family_name\",\"email_verified\",\"profile\",\"name\",\"locale\",\"phone_number_verified\",\"zoneinfo\",\"picture\",\"postal_code\",\"street_address\",\"website\",\"groups\",\"address\",\"iss\",\"acr\"],\"userinfo_signing_alg_values_supported\":[\"RS256\"],\"token_endpoint_auth_methods_supported\":[\"client_secret_basic\",\"client_secret_post\"],\"response_modes_supported\":[\"query\",\"fragment\",\"form_post\"],\"backchannel_logout_session_supported\":true,\"token_endpoint\":\"{0}/oauth2/token\",\"response_types_supported\":[\"id_token token\",\"code\",\"id_token\",\"device\",\"token\"],\"revocation_endpoint_auth_methods_supported\":[\"client_secret_basic\",\"client_secret_post\"],\"grant_types_supported\":[\"refresh_token\",\"urn:ietf:params:oauth:grant-type:saml2-bearer\",\"password\",\"client_credentials\",\"iwa:ntlm\",\"urn:ietf:params:oauth:grant-type:device_code\",\"authorization_code\",\"urn:ietf:params:oauth:grant-type:uma-ticket\",\"account_switch\",\"urn:ietf:params:oauth:grant-type:jwt-bearer\"],\"end_session_endpoint\":\"{0}/oidc/logout\",\"revocation_endpoint\":\"{0}/oauth2/revoke\",\"userinfo_endpoint\":\"{0}/oauth2/userinfo\",\"code_challenge_methods_supported\":[\"S256\",\"plain\"],\"jwks_uri\":\"{0}/oauth2/jwks\",\"subject_types_supported\":[\"pairwise\"],\"id_token_signing_alg_values_supported\":[\"RS256\"],\"registration_endpoint\":\"{0}/api/identity/oauth2/dcr/v1.1/register\",\"request_object_signing_alg_values_supported\":[\"RS256\",\"RS384\",\"RS512\",\"PS256\",\"none\"]}"
            .replace("{0}", WSO2_BASE_URL);
    public static final String JWKS_CONFIGURATION = "{\"keys\":[{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"MzYxMmFkOGYwMWI0ZWNmNDcxNGYwYmM4ZTA3MWI2NDAzZGQzNGM0ZGRlNjJkODFkZDRiOTFkMWFhMzU2ZGVlNg_RS256\",\"alg\":\"RS256\",\"n\":\"xeqoZYbQ_Sr8DOFQ-_qbEbCp6Vzb5hzH7oa3hf2FZxRKF0H6b8COMzz8-0mvEdYVvb_31jMEL2CIQhkQRol1IruD6nBOmkjuXJSBficklMaJZORhuCrB4roHxzoG19aWmscA0gnfBKo2oGXSjJmnZxIh-2X6syHCfyMZZ00LzDyrgoXWQXyFvCA2ax54s7sKiHOM3P4A9W4QUwmoEi4HQmPgJjIM4eGVPh0GtIANN-BOQ1KkUI7OzteHCTLu3VjxM0sw8QRayZdhniPF-U9n3fa1mO4KLBsW4mDLjg8R_JuAGTX_SEEGj0B5HWQAP6myxKFz2xwDaCGvT-rdvkktOw\"}]}";

}
