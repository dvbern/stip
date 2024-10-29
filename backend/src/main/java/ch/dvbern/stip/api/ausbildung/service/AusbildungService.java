package ch.dvbern.stip.api.ausbildung.service;

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungService {
    private final AusbildungRepository ausbildungRepository;
    private final AusbildungMapper ausbildungMapper;
    private final Validator validator;

    @Transactional
    public AusbildungDto createAusbildung(final AusbildungUpdateDto ausbildungUpdateDto) {
        final var ausbildung = ausbildungMapper.toNewEntity(ausbildungUpdateDto);
        Set<ConstraintViolation<Ausbildung>> violations = validator.validate(ausbildung);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }
        ausbildungRepository.persistAndFlush(ausbildung);
        return ausbildungMapper.toDto(ausbildung);
    }

    @Transactional
    public AusbildungDto getAusbildungById(final UUID ausbildungId) {
        return ausbildungMapper.toDto(ausbildungRepository.requireById(ausbildungId));
    }
}
