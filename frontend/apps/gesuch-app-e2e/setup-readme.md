# Playwright E2E Setup Functions

### Overview of how Tests are initialized

Authentication for multi-user flows happens once in the `setup` project
(`src/auth.setup.ts`), before the parallel workers start. It writes one
Gesuchsteller storage state per worker plus two shared Sachbearbeiter storage
states to `playwright/.auth/`. The test projects declare `dependencies: ['setup']`
and the fixtures only read those pre-authenticated storage states — no login
happens inside the tests, which removes the previous per-worker race.

The Sachbearbeiter page is created lazily via `createSbPage()` so its browser
window only opens once a test actually switches to the SB app. Use
`createSbPage(1)` when a flow needs the second Sachbearbeiter.

## Multiple User Test Initialization

### `initializeMultiUserTest`

This function initializes a test with multiple user roles. It sets up the necessary contexts and provides a way to authenticate users.

## Single User Test Initialization

### `initializeTest`

This function initializes a single user test. It creates the test context and provides a way to authenticate the user.

## Info

**When should you do page.close() and when not?**

In general, you should call `page.close()` when you are done with a page and want to free up resources. However, if you are running multiple tests in parallel and need to keep the browser context alive for faster test execution, you may choose not to close the page immediately. Instead, you can reuse the page across tests.

When creating the page to be used in for example `initializeTest` and `initializeMultiUserTest`, you should should not close the page.

When using the cockpit for authentication, you should close the page after the authentication is done, as it is not needed anymore.
