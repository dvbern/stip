FROM docker-registry.dvbern.ch/devops/docker/caddy:2.7

ARG APP="gesuch-app"

COPY Caddyfile /etc/caddy/Caddyfile
COPY dist/apps/${APP} /srv/app
