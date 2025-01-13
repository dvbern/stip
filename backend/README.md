# stip-api

## Getting Started

### Installation (quick-start)

1. Copy `.env-template` to `.env` and adjust secrets/config
2. Copy `docker-compose.override-template.yml` to `docker-compose.override.yml` and configure the local stack
3. Create a Docker volume for the database by running `docker volume create stip-database-data`
4. Start the local infra stack using `docker compose up -d`
5. Generate sources and build the application `./mvnw clean package`
6. You can now start the application `./mvnw compile quarkus:dev`

> **_NOTE:_**  Quarkus ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Formatting/ Linting

The backend uses [Spotless](https://github.com/diffplug/spotless) in conjunction with a [custom eclipse style.xml](./stip-codestyle.xml) to check and apply formatting. This is however only applied to files that have changed compared to the `origin/main` branch. To manually run the check, simply `./mvnw spotless:check`, to apply them run `./mvnw spotless:apply`.

There is also a git pre-commit hook ([here](../frontend/.husky/pre-commit)), installed automatically by the frontend, that runs `./mvnw spotless:check` and aborts the commit if any violations were found.

When using IntelliJ for development you can install the [Spotless Applier](https://plugins.jetbrains.com/plugin/22455-spotless-applier) plugin. Enabling format on save is also simple, under `Settings` > `Tools` > `Actions on Save` activate `Run spotless`, it is recommended to only run it for Java files (using the dropdown in the same row). It is also recommended to enable `Optimize imports before applying` and `Prohibit imports with asterisk '*'` under `Settings` > `Tools` > `Spotless Applier` to automatically remove wildcard imports as Spotless currently cannot do it by itself. 

### Generate stubs from contract

Whenever there is a changes to the contract you need to regenerate the JaxRS stubs for Quarkus. The regeneration is
triggered by enabling the `generate-stubs-from-contract` Maven profile. One might run the following command to regenerate the stubs:

```shell
./mvnw clean package -Pgenerate-stubs-from-contract
```

### How to debug

* Per default Quarkus open in development mode a debug port (5005) that you can attach in a remote JVM debug configuration
* When you're building the project with ./mvnw clean install the Quarkus tests are executed in a shared environment
* You can debug a maven build like this:
  * ./mvnw clean install -Dmaven.surefire.debug
  * maven will wait by the test phase that you connect a remote jvm debugger
  * Once the remote jvm debugger is connected the tests are executed and breakpoints can be accessed

## Debugging in vscode
  * Debugging the backend under vscode is relatively simple. All you need is the quarkus extensions (and all of its dependencies) and the following launch.json file in your .vscode directory:  
  ```
  {
      "version": "0.2.0",
      "configurations": [
          {
              "preLaunchTask": "quarkus:dev",
              "type": "java",
              "request": "attach",
              "hostName": "localhost",
              "projectName": "stip-api",
              "name": "Debug Quarkus application",
              "port": 5005
          }
      ]
  }
  ```
  * If you simply open the backend in vscode with the quarkus extension installed and launch a debug session this launch.json file is automatically created. The only thing that needs to be manually added is the "projectName" config parameter (in this case it is 'stip-api').  

## Batch creation of Users

Loginto Openshift and navigate to the pod running the keycloak instance.  
Open the terminal for the pod and execute the following shell script (change credentials first):

```
server="http://localhost:8080"
admRealm="master"
admUser="admin"
admPw=""

userRealm="bern"

createAndConfigUsr () {
    userName=$1
    userPw=$2
    bin/kcadm.sh create users -r $userRealm -s username=$userName -s enabled=true -s firstName=$userName -s lastName=$userName --no-config --server $server --realm $admRealm --user $admUser --password $admPw
    bin/kcadm.sh set-password -r $userRealm --username $userName --new-password $userPw --no-config --server $server --realm $admRealm --user $admUser --password $admPw
    bin/kcadm.sh add-roles --uusername $userName --rolename Admin -r $userRealm --no-config --server $server --realm $admRealm --user $admUser --password $admPw
    bin/kcadm.sh add-roles --uusername $userName --rolename Sachbearbeiter -r $userRealm --no-config --server $server --realm $admRealm --user $admUser --password $admPw
}

userNamePrefix=""
userPassword=""

for i in `seq 1 50`; do createAndConfigUsr ${userNamePrefix}${i} $userPassword; done
```

## ClamAV fails on non-x86 platforms

The ClamAV image (clamav/clamav) used by Quarkus for its dev service in our backend only has a x86_64 image, and Testcontainers refuses to pull an image that is not native to the platform. To "fix" the problem you have to manually pull the image, like

    docker pull --platform linux/arm64 clamav/clamav:latest

## Contributing Guidelines

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for the process for submitting pull requests to us.

## Code of Conduct

One healthy social atmospehere is very important to us, wherefore we rate our Code of Conduct high.
For details check the file [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)

## Authors

* **DV Bern AG** - *Initial work* - [dvbern](https://github.com/dvbern)

See also the list of [contributors](https://github.com/dvbern/(RepoName)/contributors)
who participated in this project.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details.

## Related guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- Flyway ([guide](https://quarkus.io/guides/flyway)): Handle your database schema migrations
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Logging GELF ([guide](https://quarkus.io/guides/centralized-log-management)): Log using the Graylog Extended Log Format and centralize your logs in ELK or EFK
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
