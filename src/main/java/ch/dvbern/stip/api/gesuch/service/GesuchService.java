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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequestScoped
@RequiredArgsConstructor
public class GesuchService {

	private final GesuchRepository gesuchRepository;

	private final GesuchMapper gesuchMapper;

	private final Validator validator;

	private final GesuchStatusService gesuchStatusService;

	private final GesuchDokumentRepository gesuchDokumentRepository;

	public Optional<GesuchDto> findGesuch(UUID id) {
		return gesuchRepository.findByIdOptional(id).map(gesuchMapper::toDto);
	}

	@Transactional
	public void updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) throws ValidationsException {
		var gesuch = gesuchRepository.requireById(gesuchId);
		final boolean isPersonInAusbildungEigenerHaushalt =
				isWohnsitzOfPersonInAusbildungEigenerHaushalt(gesuch, gesuchUpdateDto);

		final boolean mustResetPartner = hasZivilstandChangedToOnePerson(gesuch, gesuchUpdateDto);
		if (hasGeburtsdatumOfPersonInAusbildungChanged(gesuch, gesuchUpdateDto)) {
			gesuchUpdateDto.getGesuchFormularToWorkWith().setLebenslaufItems(new ArrayList<>());
		}
		gesuchMapper.partialUpdate(gesuchUpdateDto, gesuch);
		if (gesuch.getGesuchFormularToWorkWith().getEinnahmenKosten() != null && isPersonInAusbildungEigenerHaushalt) {
			gesuch.getGesuchFormularToWorkWith().getEinnahmenKosten().setAuswaertigeMittagessenProWoche(null);
		}
		if (mustResetPartner) {
			gesuch.getGesuchFormularToWorkWith().setPartner(null);
		}
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		if (!violations.isEmpty()) {
			throw new ValidationsException("Die Entität ist nicht valid", violations);
		}
	}

	private boolean hasGeburtsdatumOfPersonInAusbildungChanged(Gesuch gesuch, GesuchUpdateDto gesuchUpdate) {
		 if (gesuch.getGesuchFormularToWorkWith() == null
				 || gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung() == null
				 || gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().getGeburtsdatum() == null
				 || gesuchUpdate.getGesuchFormularToWorkWith().getPersonInAusbildung() == null) {
			 return false;
		 }

		 return !gesuch.getGesuchFormularToWorkWith()
				.getPersonInAusbildung()
				.getGeburtsdatum()
				.equals(gesuchUpdate.getGesuchFormularToWorkWith().getPersonInAusbildung().getGeburtsdatum());
	}

	private boolean isWohnsitzOfPersonInAusbildungEigenerHaushalt(Gesuch gesuch, GesuchUpdateDto gesuchUpdate) {
		if (gesuch.getGesuchFormularToWorkWith() == null
				|| gesuchUpdate.getGesuchFormularToWorkWith().getPersonInAusbildung() == null) {
			return false;
		}

		return gesuchUpdate.getGesuchFormularToWorkWith()
				.getPersonInAusbildung()
				.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT;
	}

	private boolean hasZivilstandChangedToOnePerson(Gesuch gesuch, GesuchUpdateDto gesuchUpdateDto) {
		if (gesuch.getGesuchFormularToWorkWith() == null
				|| gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung() == null
				|| gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().getZivilstand() == null
				|| gesuchUpdateDto.getGesuchFormularToWorkWith().getPersonInAusbildung() == null
				|| gesuchUpdateDto.getGesuchFormularToWorkWith().getPersonInAusbildung().getZivilstand() == null) {
			return false;
		}

		return !gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().getZivilstand().hasOnePerson() &&
				gesuchUpdateDto.getGesuchFormularToWorkWith().getPersonInAusbildung().getZivilstand().hasOnePerson();
	}

	public List<GesuchDto> findAllWithFormularToWorkWith() {
		return gesuchRepository.findAllWithFormularToWorkWith().map(gesuchMapper::toDto).toList();
	}

	@Transactional
	public GesuchDto createGesuch(GesuchCreateDto gesuchCreateDto) {
		Gesuch gesuch = gesuchMapper.toNewEntity(gesuchCreateDto);
		gesuchRepository.persist(gesuch);
		return gesuchMapper.toDto(gesuch);
	}

	public List<GesuchDto> findAllForBenutzer(UUID benutzerId) {
		return gesuchRepository.findAllForBenutzer(benutzerId).map(gesuchMapper::toDto).toList();
	}

	public List<GesuchDto> findAllForFall(UUID fallId) {
		return gesuchRepository.findAllForFall(fallId).map(gesuchMapper::toDto).toList();
	}

	@Transactional
	public void deleteGesuch(UUID gesuchId) {
		Gesuch gesuch = gesuchRepository.requireById(gesuchId);
		gesuchRepository.delete(gesuch);
	}

	@Transactional
	public void gesuchEinreichen(UUID gesuchId) {
		Gesuch gesuch = gesuchRepository.requireById(gesuchId);
		if (gesuch.getGesuchFormularToWorkWith().getFamiliensituation() == null) {
			throw new ValidationsException("Es fehlt Formular Teilen um das Gesuch einreichen zu koennen", null);
		}
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		Set<ConstraintViolation<Gesuch>> violationsEinreichen =
				validator.validate(gesuch, GesuchEinreichenValidationGroup.class);
		if (!violations.isEmpty() || !violationsEinreichen.isEmpty()) {
			Set<ConstraintViolation<Gesuch>> concatenatedViolations = new HashSet<>(violations);
			concatenatedViolations.addAll(violationsEinreichen);
			throw new ValidationsException(
					"Die Entität ist nicht valid und kann damit nicht eingereicht werden: ", concatenatedViolations);
		}
		if(gesuchDokumentRepository.findAllForGesuch(gesuchId).count() == 0){
			gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.DOKUMENT_FEHLT_EVENT);
		}
		else {
			gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.GESUCH_EINREICHEN_EVENT);
		}
	}

	@Transactional
	public void setDokumentNachfrist(UUID gesuchId) {
		Gesuch gesuch = gesuchRepository.requireById(gesuchId);
		gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.DOKUMENT_FEHLT_NACHFRIST_EVENT);
	}
}
