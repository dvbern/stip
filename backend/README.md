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
