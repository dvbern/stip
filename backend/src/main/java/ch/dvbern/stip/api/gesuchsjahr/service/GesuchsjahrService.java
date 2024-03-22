package ch.dvbern.stip.api.gesuchsjahr.service;

import java.util.Collection;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.AllArgsConstructor;

@RequestScoped
@AllArgsConstructor
public class GesuchsjahrService {

	private final GesuchsjahrMapper gesuchsjahrMapper;
	private final GesuchsjahrRepository gesuchsjahrRepository;

	public GesuchsjahrDto getGesuchsjahr(UUID gesuchsjahrId) {
		var gesuchsperiode = gesuchsjahrRepository.requireById(gesuchsjahrId);
		return gesuchsjahrMapper.toDto(gesuchsperiode);
	}

	public Collection<GesuchsjahrDto> getGesuchsjahre() {
		return gesuchsjahrRepository.findAll()
				.stream()
				.map(gesuchsjahrMapper::toDto)
				.toList();
	}

	public GesuchsjahrDto createGesuchsjahr(GesuchsjahrCreateDto gesuchsjahrCreateDto) {
		Gesuchsjahr gesuchsjahr = gesuchsjahrMapper.toEntity(gesuchsjahrCreateDto);
		gesuchsjahrRepository.persist(gesuchsjahr);
		return gesuchsjahrMapper.toDto(gesuchsjahr);
	}

	public GesuchsjahrDto updateGesuchsjahr(UUID gesuchsjahrId, GesuchsjahrUpdateDto gesuchsjahrUpdateDto) {
		Gesuchsjahr gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
		gesuchsjahr = gesuchsjahrMapper.partialUpdate(gesuchsjahrUpdateDto, gesuchsjahr);
		gesuchsjahrRepository.persist(gesuchsjahr);
		return gesuchsjahrMapper.toDto(gesuchsjahr);
	}

	public GesuchsjahrDto publishGesuchsjahr(UUID gesuchsjahrId) {
		Gesuchsjahr gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
		gesuchsjahr.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
    	gesuchsjahrRepository.persist(gesuchsjahr);
    	return gesuchsjahrMapper.toDto(gesuchsjahr);
	}

	public void deleteGesuchsjahr(UUID gesuchsjahrId) {
		Gesuchsjahr gesuchsjahr = gesuchsjahrRepository.requireById(gesuchsjahrId);
		gesuchsjahrRepository.delete(gesuchsjahr);
	}
}
