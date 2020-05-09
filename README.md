# karaf-jaxrs-whiteboard-security
Karaf OSGi JAX-RS Whiteboard Security

## Building from sources

Build `mvn clean install`

## Deployment in Karaf

### Run Karaf

`./bin/karaf`

Run from root Karaf floder, not from ./bin folder! See details in https://karaf.apache.org/get-started.html

### Deploy OSGi JAX-RS Whiteboard Security server to Karaf

Before installation you should build server from sources! (Its due to Karaf installs all from local maven repository by default.)

#### Add feature repository

* `feature:repo-add mvn:ru.agentlab.security/ru.agentlab.security.feature/LATEST/xml`

#### Install karaf features and activate OSGi bundles

Install main feature (installs all sub-features except cors plugin):

* `feature:install ru.agentlab.security.deploy`

Install main feature (installs all sub-features with cors plugin):

* `feature:install ru.agentlab.security.cors.deploy`

Or you colud install sub-features one by one:

* `feature:install agentlab-aries-jax-rs-whiteboard-jackson`
* `feature:install nimbus-oauth-sdk`
* `feature:install ru.agentlab.security.deps`
* `feature:install ru.agentlab.security.deploy`
* `feature:install ru.agentlab.security.cors.deploy` - optional

## Development

* `bundle:watch *` -- Karaf should monitor local maven repository and redeploy rebuilded bundles automatically

* `bundle:list` и `la` -- list all plugins
* `feature:list` -- list all features

* `display` -- show logs
* `log:set DEBUG` -- set logger filter into detailed mode

* `./bin/karaf debug` -- allows to attach with debugger on 5005 port

## Settings

|                     System property                    |                ENV                |          Default value         |
|:------------------------------------------------------:|:---------------------------------:|:------------------------------:|
| ru.agentlab.security.oauth.cookie.path                 |         OAUTH_COOKIE_PATH         |               "/"              |
| ru.agentlab.security.oauth.cookie.domain               |        OAUTH_COOKIE_DOMAIN        |              null              |
| ru.agentlab.security.oauth.cookie.expire.access_token  |  OAUTH_COOKIE_EXPIRE_ACCESS_TOKEN |              3600              |
| ru.agentlab.security.oauth.cookie.expire.refresh_token | OAUTH_COOKIE_EXPIRE_REFRESH_TOKEN |              86400             |
| ru.agentlab.oauth.client.id                            |          OAUTH_CLIENT_ID          | "Ynio_EuYVk8j2gn_6nUbIVQbj_Aa" |
| ru.agentlab.oauth.client.secret                        |        OAUTH_CLIENT_SECRET        | "fTJGvvfJjUkWvn8R_NY8zXSyYQ0a" |
|                                                        |                                   |                                |
| ru.agentlab.wso2.protocol                              |           WSO2_PROTOCOL           |             "https"            |
| ru.agentlab.wso2.host                                  |             WSO2_HOST             |           "localhost"          |
| ru.agentlab.wso2.port                                  |             WSO2_PORT             |              9443              |
| ru.agentlab.wso2.prefix                                |            WSO2_PREFIX            |               ""               |
| ru.agentlab.ssl.verification.enabled                   |      SSL_VERIFICATION_ENABLED     |              false             |
