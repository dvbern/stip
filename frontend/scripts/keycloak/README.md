# Usage

This tool is used to sync Roles and Permissions with Keycloak using the definitions in [`roles-map.ts`](./roles-map.ts)

**Ensure Auth with Keycloak works**

In [`.env`](../../.env) set the following values to be able to authenticate with Keycloak: (See [`.env-template`](../../.env.template))

```
KEYCLOAK_ADMIN_URL_DEV
KEYCLOAK_ADMIN_USERNAME_DEV
KEYCLOAK_ADMIN_PASSWORD_DEV
KEYCLOAK_ADMIN_URL_UAT
KEYCLOAK_ADMIN_USERNAME_UAT
KEYCLOAK_ADMIN_PASSWORD_UAT
```

## Sync the Roles with Keycloak

**Dev**: `npm run sync-roles -- -e DEV`  
**Uat**: `npm run sync-roles -- -e UAT`

## Add users

1. Ensure you _add_ the following _ENV_ variable to [`.env`](../../.env): _`NEW_USER_PASSWORD=` (this will be the password for your new user)_
2. Use the following command structure: `npm run add-users -- -e {DEV|UAT} -u "{username},{firstname},{lastname}" -r {role 1} {role 2} {...}`

Here are some _examples_:

```
npm run add-users -- -e DEV -u "stip-scph-gs-1,Philipp,GS 1" -r V0_Gesuchsteller
npm run add-users -- -e DEV -u "stip-scph-gs-2,Philipp,GS 2,philipp.schaerer+gs-2@dvbern.ch" -r V0_Gesuchsteller
...
npm run add-users -- -e DEV -u "stip-scph-sb-1,Philipp,SB 1"         -r V0_Sachbearbeiter
npm run add-users -- -e DEV -u "stip-scph-frei-1,Philipp,Freigabe 1" -r V0_Freigabestelle
npm run add-users -- -e DEV -u "stip-scph-admin-1,Philipp,Admin 1"   -r V0_Sachbearbeiter-Admin V0_Sachbearbeiter
npm run add-users -- -e DEV -u "stip-scph-jurist-1,Philipp,Jurist 1" -r V0_Jurist
```
