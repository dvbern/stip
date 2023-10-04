package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangService {

	private final AusbildungsgangRepository ausbildungsgangRepository;

	private final AusbildungsstaetteRepository ausbildungsstaetteRepository;

	private final AusbildungsgangMapper ausbildungsgangMapper;

	public AusbildungsgangDto findById(UUID ausbildungsgangId) {
		return ausbildungsgangMapper.toDto(ausbildungsgangRepository.requireById(ausbildungsgangId));
	}

	@Transactional
	public AusbildungsgangDto createAusbildungsgang(AusbildungsgangUpdateDto ausbildungsgangDto) {
		return persistsAusbildungsgang(ausbildungsgangDto, new Ausbildungsgang());
	}

	@Transactional
	public AusbildungsgangDto updateAusbildungsgang(UUID ausbildungsgangId, AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
		var ausbildungsgangToUpdate = ausbildungsgangRepository.requireById(ausbildungsgangId);
		return persistsAusbildungsgang(ausbildungsgangUpdateDto, ausbildungsgangToUpdate);
	}

	@Transactional
	public void deleteAusbildungsgang(UUID ausbildungsgangId) {
		var ausbildungsgang = ausbildungsgangRepository.requireById(ausbildungsgangId);
		ausbildungsgangRepository.delete(ausbildungsgang);
	}

	private AusbildungsgangDto persistsAusbildungsgang(AusbildungsgangUpdateDto ausbildungsgangUpdate, Ausbildungsgang ausbildungsgangToUpdate) {
		ausbildungsgangToUpdate.setAusbildungsstaette(loadAusbildungsstaetteIfExists(ausbildungsgangUpdate.getAusbildungsstaette()));
		ausbildungsgangMapper.partialUpdate(ausbildungsgangUpdate, ausbildungsgangToUpdate);
		ausbildungsgangRepository.persist(ausbildungsgangToUpdate);
		return ausbildungsgangMapper.toDto(ausbildungsgangToUpdate);
	}

	private Ausbildungsstaette loadAusbildungsstaetteIfExists(AusbildungsstaetteUpdateDto ausbildungsstaette) {
		return ausbildungsstaette.getId() != null ?
				ausbildungsstaetteRepository.requireById(ausbildungsstaette.getId()) :
				new Ausbildungsstaette();
	}
}
