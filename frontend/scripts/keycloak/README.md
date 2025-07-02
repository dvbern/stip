# Usage

This tool is used to sync Roles and Permissions with Keycloak using the definitions in [`roles-map.ts`](./roles-map.ts)

**Ensure Auth with Keycloak works**

In [`.env`](../../.env) set the following values to be able to authenticate with Keycloak:  (See [`.env-template`](../../.env.template))

```
KEYCLOAK_ADMIN_URL_DEV
KEYCLOAK_ADMIN_USERNAME_DEV
KEYCLOAK_ADMIN_PASSWORD_DEV
KEYCLOAK_ADMIN_URL_UAT
KEYCLOAK_ADMIN_USERNAME_UAT
KEYCLOAK_ADMIN_PASSWORD_UAT
```

**Sync the Roles with Keycloak:**

**Dev**: npm run sync-roles -- -e DEV  
**Uat**: npm run sync-roles -- -e UAT
