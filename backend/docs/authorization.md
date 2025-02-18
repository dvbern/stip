# Authorization and Authentication
Authentication is "outsourced" to a Keycloak instance and checked with annotations in our resource implementations. Each endpoint (unless explicitly disabled) must have `@RolesAllowed` that checks the incoming identity for *permissions*.  
Authorization is implemented in classes that are annotated with `@Authorizer`, each resource endpoint (unless explicitly disabled with `@AllowAll`) *must* call a method implemented inside an authorizer.

## High-Level
Authentication checks whether a user is logged in, and if a user can call an endpoint, it doesn't check if the user can call that endpoint for the specified entity.  
Authorization checks whether a user can execute an action for a given entity.

## Permissions
Note: Keycloak doesn't differentiate between permissions and roles, it's all roles. The differentiation is done by us. Permissions are "plain" roles in Keycloak, whereas what we call Roles are composite roles in Keycloak.

Permissions are fine-grained roles on the JWT, in Keycloak they are "simple" (i.e. non-composite) roles, and are statically duplicated inside our [`OidcPermissions`](../src/main/java/ch/dvbern/stip/api/common/util/OidcPermissions.java) for easy usage inside our resource implementations.  
Fundamentally there are a given set of permissions for a given resource, create, read, update and delete as well as a duplicate set of these permissions, currently at least one for Sachbearbeiter and one for Gesuchsteller.

Permissions are notably an **or**, so one endpoint annotated with e.g. `@RolesAllowed({ GESUCH_READ_GS, GESUCH_READ_SB })` means either GS **or** SB can call this endpoint. Whether the current user *can* actually read the specified entity is up to the authorizer to decide, i.e. a GS cannot read someone else's Gesuch.  

## Roles
Roles are a logical grouping of permissions and are what is eventually assigned to users, one user can have single roles, but that is mostly for development and testing, in production a single user will more than likely only have a single role with a vast majority of users having only the Gesuchsteller role. They are only rarely used in our application, almost exclusively for testing and should *never* be used to check whether a user can call an endpoint. In the application code they should only be used to check for "common" actions, i.e. if a Gesuch is in the correct Status to execute a Berechnung.

## Authorization
Authorization is there check if a given user can execute a given action on a given entity. The given action is static, an authorizer method like `GesuchAutorizer.canRead(UUID gesuchId)` checks if the current user can read the given entity. These methods may rely on the role of the user for branching implementation logic in shared methods (i.e. authorizer methods that can be called by any role). The aforementioned method for example relies on the role of the current user to check if the user can or cannot read the given Gesuch.

Authorization methods are specialized, and to reduce the amount of boilerplate, are allowed to directly access a repository to execute a query.

TODO KSTIP-1967: Rename `@AllowAll` annotation? Reduce confusion/ association with `@RolesAllowed`

Every endpoint unless explicitly disabled with `@AllowAll` must call an authorizer method.
