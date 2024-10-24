package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungService {
    private final AusbildungRepository ausbildungRepository;


}
