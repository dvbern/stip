# DV Stip Workspace

Welcome to DV Stip Workspace!

<!-- toc -->

- [Glossary](#glossary)
- [Getting Started](#getting-started)
  - [Authorization](#authorization)
  - [TLDR local development:](#tldr-local-development)
- [Architecture](#architecture)
- [Adding Code (Features, Data-Access, etc)](#adding-code-features-data-access-etc)
- [Linting](#linting)
- [Testing](#testing)
  - [Unit testing](#unit-testing)
  - [E2E](#e2e)
- [Troubleshooting](#troubleshooting)

<!-- tocstop -->

## Glossary

The following abbreviations are being used in this documentation:

- GS: Gesuchsteller, also sometimes known as Antragssteller.
- SB: Sachbearbeiter

## Getting Started

Install global `nx` CLI with `npm i -g nx` as it will make running of some commands easier.
Install the dependencies with `npm ci` (append `--legacy-peer-deps` or `--force` if necessary)

> The `--legacy-peer-deps` flag might need to used in case the dependencies available at the time of last workspace update did not fulfil their peerDependencies ranges perfectly. This might change again in the future as newer versions of the libraries are released and the `--legacy-peer-deps` flag might not be needed anymore.

There are two different Apps that can be built, served, tested, etc: `gesuch-app` and `sachbearbeitung-app`. `npm run start` for example starts the GS and SB App.

| Task     | Gesuch-App         | Sachbearbeitung-App | All                |
| -------- | ------------------ | ------------------- | ------------------ |
| Serve    | `npm run start:gs` | `npm run start:sb`  | `npm run start`    |
| Build    | `npm run build:gs` | `npm run build:sb`  | `npm run build`    |
| Lint     | `npm run lint:gs`  | `npm run lint:sb`   | `npm run lint`     |
| Test     | `npm run test:gs`  | `npm run test:sb`   | `npm run test`     |
| e2e      | `npm run e2e:gs`   | `npm run e2e:sb`    | `npm run e2e`      |
| Validate | -                  | -                   | `npm run validate` |

### Authorization

During local development the dev Auth instance is being used. The Authorization is being handled by a dedicated Keycloak instance.

To obtain a test user it is possible to log in the administration panel on https://auth.stip.dev.apps.test.kibon.ch/admin/master/console/#/bern/users using the credentials found at **LastPass**, search for `stip` and use the `Keycload Admin (DEV)` credentials. After that follow these steps to create a new user or modify existing ones:

1. `Add User`
2. Set `Username`, `Email` (optional), `First name` and `Last name`
3. `Create`
4. Select newly created user
5. Go to `Credentials` tab -> `Set password` -> fill new password -> Disable `Temporary`
6. Set Roles for the user `Role mapping` -> `Assign role` -> `Admin` or GS/SB related roles.

### TLDR local development:

1. `npm ci`
2. Ensure that the API and everything else is running:
   - https://gitlab.dvbern.ch/kibon/stip-api
3. Ensure the [Authorization](#authorization) is configured
4. `npm run start` or `npm run start:gs` / `npm run start:sb`

## Architecture

Stip uses a very strict but also robust software architecture, i.e. the arrangement of the files and their affiliations are predefined and compliance with this structure is also verified with validators.

Both apps have a big common part, the Gesuch Formular views. These Features and Pages are shared between both apps using a sub router outlet approach:

- `GesuchApp (App)` -> _`<router-outlet>`_

  - `Cockpit (Feature)` / `<dv-gesuch-app-pattern-main-layout>`
  - `Gesuch Form (Feature)` -> `<dv-gesuch-app-pattern-gesuch-step-layout>`._`<router-outlet>`_
    - `shared/Gesuch Form Person (Feature)`
    - `shared/Gesuch Form Ausbildung (Feature)`
    - `shared/Gesuch Form Eltern (Feature)`
    - ...

- `SachbearbeitungApp (App)` -> _`<router-outlet>`_
  - `Cockpit (Feature)` / `<dv-sachbearbeitung-app-pattern-overview-layout>`
  - `Gesuch Form (Feature)` -> `<dv-sachbearbeitung-app-pattern-gesuch-step-layout>`._`<router-outlet>`_
    - `shared/Gesuch Form Person (Feature)`
    - `shared/Gesuch Form Ausbildung (Feature)`
    - `shared/Gesuch Form Eltern (Feature)`
    - ...

More details about this structure can be found [here](docs/architecture.md).

## Adding Code (Features, Data-Access, etc)

To ensure that the architecture is being upheld correctly, a multitude of generators and other costumization tools can be used.

Use the tools that are mentioned in the [Workspace](docs/workspace.md) documentation.

## Linting

```
npm run lint
```

A general overview on how the linting works in this project can be found [here](docs/linting.md).

## SonarQube

SonarQube is being used to ensure code quality and clean code, the Analysis is being triggered on each code check-in in the build pipeline.  
It is possible to run the SonarQube analysis locally by adding the [SonarLint](https://marketplace.visualstudio.com/items?itemName=SonarSource.sonarlint-vscode) VsCode Extension and following these steps:

1. Install https://docs.sonarsource.com/sonarlint/vs-code/getting-started/installation/
2. Setup the connection to the SonarQube hosted instance: https://docs.sonarsource.com/sonarlint/vs-code/team-features/connected-mode/#connection-setup
3. Verify if it works by commenting out some code, after a few seconds a warning should be visible.

## Testing

### Unit testing

```
npm run test
```

In general, we strive to write components with little or even NO logic at all which means
they don't really have to be tested. This is great because components are the most
complicated part of Angular application to test and those tests execute run the slowest.

Most logic is then extracted into `data-access` or `util` type libraries which are
headless and hence much easier to test.

The main product critical flows should be covered by the `e2e` tests which provide
the best tradeoff between effective / useful coverage and effort required to write them.

### E2E tesing with Playwright

For best developper experience, use vs code with the recommended playwright extension
It provides many handy features, like running individual tests.

#### commands to run e2e via command line

Before running any e2e test, make sure the dev servers for the apps are running.

to rerun a test, it might be necessary to add '--skip-nx-cache' so nx will run a test again

run all tests:

```bash
npm run e2e
```

run for specific app by adding :gs or :sb

```bash
npm run e2e:gs
```

run a test in playwrights ui mode:

```bash
npm run e2e:gs -- --ui
```

To see the test in a browser, run:

```bash
npm run e2e:gs -- --headed
```

#### Preparation:

1. Copy `.env.template` to `.env`
2. Fill the values DEV Keycloak Credentials from LastPass (`LastPass` -> `Stip E2E (DEV)`)

You can run e2e tests in headless mode for all (`npm run e2e`) or for a specific app
(`npm run e2e:<app-name>`, these have to be added to the `package.json` file `scripts` when new app is added to the workspace).

Besides that it is also possible to serve desired app in the dev mode (eg `npm run serve:gs`)
and then start e2e tests in GUI mode with `npm run e2e:gs:open` which will start Cypress GUI.
(again, these scripts have to be added to the `package.json` file `scripts` when new app is added to the workspace).

> When writing E2E tests, always make sure to use `data-testid="some-id"` attributes instead of
> component selectors, classes or other attributes. This way we can make sure that tests are not
> brittle and will not break when we change the component structure or styling.

### Component tests

Component testing is performed with Vitest and [testing-library](https://testing-library.com/docs/angular-testing-library/intro/). To create a new test, just add a file ending with .test.spec (whereas unit tests end with .spec.ts) to the component you want to test.

#### Testing Library

Testing Library is a very lightweight solution for testing without all the implementation details. The main utilities it provides involve querying for nodes similarly to how users would find them. By working with real DOM nodes, Testing Library encourages you to write tests that closely resemble how your code is used.

```typescript
describe('should display warning if not all of personInAusbildung, familiensituation, ausbildung are defined', () => {
  it('should display warning if personInAusbildung is undefined', async () => {
    const { queryByTestId } = await setup({
      personInAusbildung: undefined,
      ausbildung: createEmptyAusbildung(),
      familiensituation: { elternVerheiratetZusammen: true },
    });

    expect(queryByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning')).toBeInTheDocument();
  });
});
```

an extensive list of examples can be found [here](https://github.com/testing-library/angular-testing-library/tree/main/apps/example-app/src/app/examples)

## Troubleshooting

NX monorepo is a great piece of technology, but it is not perfect. Even though caching leads to great performance
it can also lead to inconsistent state, especially when removing, moving or renaming projects and files.
If you run into any issues try to run `nx reset` (or `npm run reset) and then try to run original command again.
If the problem still persists then it's most likely a real problem which than has to be solved.
