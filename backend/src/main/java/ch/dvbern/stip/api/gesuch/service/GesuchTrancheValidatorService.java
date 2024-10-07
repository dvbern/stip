package ch.dvbern.stip.api.gesuch.service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.validation.GesuchEinreichenValidationGroup;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheValidatorService {
    private static final Map<GesuchTrancheStatus, Class<?>> statusToValidationGroup =
        new EnumMap<>(GesuchTrancheStatus.class);

    static {
        statusToValidationGroup.put(GesuchTrancheStatus.IN_BEARBEITUNG_GS, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(GesuchTrancheStatus.UEBERPRUEFEN, GesuchEinreichenValidationGroup.class);
        statusToValidationGroup.put(GesuchTrancheStatus.AKZEPTIERT, GesuchEinreichenValidationGroup.class);
    }

    private final Validator validator;
    private final GesuchRepository gesuchRepository;

    public void validateGesuchTrancheForStatus(final GesuchTranche toValidate, final GesuchTrancheStatus status) {
        final var validationGroup = statusToValidationGroup.getOrDefault(status, null);
        if (validationGroup != null) {
            ValidatorUtil.validate(validator, toValidate.getGesuchFormular(), validationGroup);
        }
    }

    public void validateGesuchTrancheForEinreichen(final GesuchTranche toValidate) {
        validateAdditionalEinreichenCriteria(toValidate);
        validateGesuchTrancheForStatus(toValidate, GesuchTrancheStatus.AKZEPTIERT);
    }

    public void validateAdditionalEinreichenCriteria(final GesuchTranche toValidate) {
        if (toValidate.getGesuchFormular() == null) {
            throw new ValidationsException("GesuchFormular must be set", Set.of());
        }

        if (toValidate.getGesuch() == null) {
            throw new ValidationsException("Gesuch must be set", Set.of());
        }

        final var gesuch = toValidate.getGesuch();
        // 1 Tranche and it's a TRANCHE means first einreichen
        if (gesuch.getGesuchTranchen().size() == 1 && toValidate.getTyp() == GesuchTrancheTyp.TRANCHE) {
            validateNoOtherGesuchWithSameSvNumber(toValidate.getGesuchFormular(), gesuch.getId());
        }
    }

    public void validateNoOtherGesuchWithSameSvNumber(final GesuchFormular formular, final UUID gesuchId) {
        if (formular.getPersonInAusbildung() == null) {
            return;
        }

        final var svNummer = formular.getPersonInAusbildung().getSozialversicherungsnummer();
        final var gesuche = gesuchRepository.findGesucheBySvNummer(svNummer);
        if (gesuche.anyMatch(gesuch -> gesuch.getGesuchStatus().isEingereicht() && gesuch.getId() != gesuchId)) {
            throw new CustomValidationsException(
                "Person in Ausbildung (via SV-Nummer) already has a eingereicht Gesuch",
                new CustomConstraintViolation(
                    VALIDATION_GESUCHEINREICHEN_SV_NUMMER_UNIQUE_MESSAGE,
                    "personInAusbildung"
                )
            );
        }
    }
}
