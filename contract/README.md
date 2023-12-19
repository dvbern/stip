# Stip Contract

The Contract-First approach with OpenAPI is a software development methodology that emphasizes defining the API contract before writing any implementation code. This methodology centers around using the OpenAPI Specification (OAS), formerly known as Swagger, to create a clear and detailed description of your API's structure, endpoints, request/response formats, and other relevant information.

[Read more](./docs/about.md)

## Usage

1. Create a feature branch
2. Modify the contract [openapi.yaml](openapi.yaml)
   1. Manually
   2. With [Apicuritio](https://www.apicur.io/apicurito/pwa/)
3. Commit your changes with a [conventional commit](https://www.conventionalcommits.org/en/v1.0.0/) message
4. Create a merge request
5. CI will automatically create a new contract release