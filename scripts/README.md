# Modifying a partial dump from Keycloak

## Generate new roles
To generate the new roles/ composites from a partial edit the filter.json and call the `generateRealm.js` script with the correct input. It takes an input `-r` or `--realm` which is the path to a partial dump from Keycloak which must contain the "base" roles for the script (i.e. `GESUCH_READ` and `AUSBILDUNG_CREATE` etc.), it also takes an input `-f` or `--filter` which is the path to a "filter" file, that file specifies different "types" of users (i.e. `GS`, `SB` etc.) with a given array of roles. The script then takes that information and generates a new json (in `out/generated_realm.json`) which contains all the roles and composites in a format Keycloak can partial import to modify a given realm.

## Generate OidcPermissions.java
To generate the `OidcPermissions.java` call the script `generateOidcPermissions.js`, it takes an input `-f` or `--filter` and generates a new java file (in `out/OidcPermissions.java`) which contains a list of `public static final String`s which represent the permissions a given user type has.

## Filter
The current filter is in `filter.json`, its main purpose is to specify which "types" of users exist and what permissions they have. It also defines the name for that user "type" composite and the current version prefix.
