package ch.dvbern.stip.api.stammdaten.service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class LandService {

    public Collection<String> getAllLaender() {
        return Stream.of(Land.values())
            .map(Land::name)
            .collect(Collectors.toSet());
    }
}
