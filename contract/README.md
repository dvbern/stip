# Stip Contract

The Contract-First approach with OpenAPI is a software development methodology that emphasizes defining the API contract before writing any implementation code. This methodology centers around using the OpenAPI Specification (OAS), formerly known as Swagger, to create a clear and detailed description of your API's structure, endpoints, request/response formats, and other relevant information.

[Read more](./docs/about.md)

> [!WARNING]  
> Do not edit the [openapi.yaml](./openapi.yaml) file directly, it is generated using YAML files located at `[openapi/*.yaml]`.

## Install

Run `npm ci` in the `./contract` root.

## Usage

- Add or change the contract values at the YAML files located at `[openapi/*.yaml]` and generate the joined file using `npm run build`.
- If a schema or parameter is being used by multiple API definitions, share them using the `components` folder, by adding new files or using the existing ones.
- For values that are only relevant to the given API, use the `components: -> (schemas/parameters):`.
- `GesuchFormular` relevant data declarations are located at `[gesuch-formular.yaml](./openapi/components/gesuch-formular.yaml)`

## Example

**Adding a new Feature:**

1. Add a the **`{feature-name}.yaml`** file to `openapi/...`
2. Use already existing **`[Schemas](./openapi/components/schemas.yaml)`**
3. Add new DTO definitions to the own `components:` part of the file if they are only relevant to this feature
4. `npm run build`

**Updating a Gesuch relevant Feature**

1. Update the `[gesuch.yaml](./openapi/gesuch.yaml)` file accordingly
2. Use or update already existing **`[Schemas](./openapi/components/schemas.yaml)`**
3. Use or update already existing **`[GesuchFormular Schemas](./openapi/components/gesuch-formular.yaml)`**
4. Add new DTO definitions to the own `components:` part of the file if they are only relevant to this feature
5. `npm run build`

### `npm run build`

Join all the files into the [`openapi.yaml`](./openapi.yaml)

### `npm run debug`

Generates a openapi file for each file at `./openapi/generated`, useful for debugging.

### `npm lint`

Validates the definition.

### `npm start`

Starts the reference docs preview server.
