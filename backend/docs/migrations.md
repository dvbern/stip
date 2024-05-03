# Migrations
We use Liquibase to generate and ultimately apply our migrations. The Liquibase base changelog is in the `db/migration` directory of the resources, the baseline migration is also in that directory.

## Generating a new Migration
We use the Liquibase Maven plugin to automatically generate changelogs based on the DB and the JPA entities, where the JPA entities are our source of truth. To do this, execute

    mvnw liquibase:diff
    
This will generate a `XXXXX_diffchange.xml` changelog with only your changes in the `src/resources/db/migration` directory. You should always check the generated changelog for any potential errors. For example, Liquibase will generate `NOT NULL` constraints on properties with the `@NotNull` annotation, which is nice, but it will also generate those constraints on properties that have `@NotNull(groups = ...)` i.e. conditionally not null properties, which will break things.

## Naming guidelines
Although all Liquibase changelogs are always applied, it only does the things it needs to, this means there technically isn't an order in which changelogs are applied, but in practice (at least on UAT or higher environments) there exists an order. As such, we want to name our changelogs so that the order in IntelliJ (or your favourite IDE) is roughly equivalent to the order applied to the database. As such we follow the following naming guideline:

    {version}-{ticket}.xml
    
So, in Sprint 18, for the Ticket KSTIP-972 the changelog would be

    0.18.0-KSTIP-972.xml
    
This will keep the changelogs in a nice order grouped by the version as well as minimize potential conflicts as each ticket gets their own changelog.

## Known pain points
Liquibase will always generate a not null constraint, even if the column may be null in some cases, i.e. when using `groups` on a property like `@NotNull(groups = ...)`. And as we have some nullable references, particularly in the `GesuchFormular` entity, every newly generated migration will try and make those columns not nullable, meaning we have to manually delete those `<changeSet/>` entries every time when generating.
