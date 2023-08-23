package ch.dvbern.stip.api.ausbildung.service;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangService {

	private final AusbildungsgangRepository ausbildungsgangRepository;

	private final AusbildungsgangMapper ausbildungsgangMapper;

	public AusbildungsgangDto findById(UUID ausbildungsgangId) {
		return ausbildungsgangMapper.toDto(ausbildungsgangRepository.requireById(ausbildungsgangId));
	}
}
