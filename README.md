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

