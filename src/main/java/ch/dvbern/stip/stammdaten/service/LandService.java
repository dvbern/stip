package ch.dvbern.stip.stammdaten.service;

import ch.dvbern.stip.stammdaten.entity.Land;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestScoped
@RequiredArgsConstructor
public class LandService {

    public Collection<String> getAllLaender() {
        return Stream.of(Land.values())
                .map(Land::name)
                .collect(Collectors.toSet());
    }
}
