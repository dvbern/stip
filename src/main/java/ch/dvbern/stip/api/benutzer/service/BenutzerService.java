package ch.dvbern.stip.api.benutzer.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.generated.dto.BenutzerDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BenutzerService {

	private final BenutzerMapper benutzerMapper;
	private final BenutzerRepository benutzerRepository;

	public Optional<BenutzerDto> getBenutzer(UUID id) {
		var optionalFall = benutzerRepository.findByIdOptional(id);
		return optionalFall.map(benutzerMapper::toDto);
	}

	public List<BenutzerDto> getAllBenutzer() {
		return benutzerRepository.findAll().stream().map(benutzerMapper::toDto).toList();
	}
}
