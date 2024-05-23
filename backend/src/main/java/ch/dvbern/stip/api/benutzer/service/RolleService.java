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
     * */
    public Set<Rolle> mapOrCreateRoles(Set<String> keycloakRoles) {
        final var roles = rolleRepository.findByKeycloakIdentifier(keycloakRoles)
            .stream()
            .collect(Collectors.toMap(Rolle::getKeycloakIdentifier, x -> x));

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
