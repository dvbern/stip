# Seeding
Data Seeding is an important of every application, and we're no different. Historically (in Stip) data was seeded all over the place, some was seeded in migrations or dev migrations, other data was seeded in SQL load scripts for certain environments (e.g. test). We're currently moving away from that approach and to seeding data using Java code.

## Seeding with Java
There aren't any out of the box solutions for seeding data in Quarkus, Hibernate or Liquibase that allow for (arbitrary) Java code to seed the data, instead we just use the Quarkus runtime `@Startup` annotation and the `ConfigUtils.getProfiles` to conditionally run some code to seed some data. Currently only a `Gesuchsjahr` and two `Gesuchsperiode` are seeded this way, but the idea is that everything that's needs to be seeded is seeded that way, see the [`GesuchsperiodeSeeding`](../src/main/java/ch/dvbern/stip/api/common/service/seeding/GesuchsperiodeSeeding.java) class for an example.
