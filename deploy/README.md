# Deployment and Operation Notes

> NOTE: Deploying applications using GitOps methods is expermintal and subject to change. You may consult the internal [documentation](https://intra.dvbern.ch/display/DEV/OpenShift%3A+GitOps+with+Flux) to understand key concepts. 

## Secrets management

Secrets are encrypted using [Mozilla SOPS](https://github.com/mozilla/sops) and [AGE](https://github.com/FiloSottile/age) as encryption provider.
The encrypted YAML is decrypted in the cluster through the Flux Kustomize controller.

### Installation

1. Install [SOPS](https://github.com/mozilla/sops)
2. Install [AGE](https://github.com/FiloSottile/age#installation)
3. Retrive the age.agekey from the project LastPass and store it under `deploy/age.agekey`
4. When using direnv run `cd deploy && direnv allow` or manually export the key file variable by running `export SOPS_AGE_KEY_FILE=age.agekey`


### Ecrypt a file

1. `cd deploy`
2. `sops -e -i path/to-my-unencrypted-file.yml`

### Edit a secret

1. `cd deploy`
2. `sops dev/cluster-app-user-secret.yml`

