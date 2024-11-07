/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.benutzer.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.repo.RolleRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class RolleService {
    private final RolleRepository rolleRepository;

    /**
     * Maps all roles in the given set to roles from the database or creates them if they don't exist
     */
    public Set<Rolle> mapOrCreateRoles(Set<String> keycloakRoles) {
        final var roles = rolleRepository.findByKeycloakIdentifier(keycloakRoles)
            .stream()
            .collect(Collectors.toMap(Rolle::getKeycloakIdentifier, rolle -> rolle));

        final var toCreate = new HashSet<Rolle>();
        keycloakRoles.stream()
            .filter(x -> !roles.containsKey(x))
            .forEach(keycloakId -> toCreate.add(new Rolle().setKeycloakIdentifier(keycloakId)));

        if (!toCreate.isEmpty()) {
            rolleRepository.persist(toCreate);
            roles.putAll(
                toCreate.stream().collect(Collectors.toMap(Rolle::getKeycloakIdentifier, x -> x))
            );
        }

        return new HashSet<>(roles.values());
    }
}
