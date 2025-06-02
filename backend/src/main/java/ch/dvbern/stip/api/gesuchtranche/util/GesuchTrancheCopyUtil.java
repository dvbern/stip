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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuchtranche.util;

import java.time.LocalDate;
import java.util.Comparator;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungCopyUtil;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.darlehen.util.DarlehenCopyUtil;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenCopyUtil;
import ch.dvbern.stip.api.steuererklaerung.util.SteuererklaerungCopyUtil;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_TRANCHE_DATERANGE_TOO_SHORT;

@UtilityClass
// TODO KSTIP-1236: Once proper test data generation is in place, test copying
public class GesuchTrancheCopyUtil {
    private static DateRange validateAndCreateDateRange(
        final LocalDate startDate,
        final LocalDate endDate,
        final Gesuch gesuch
    ) {
        var minStartDate = gesuch
            .getGesuchTranchen()
            .stream()
            .min(Comparator.comparing(gesuchTranche -> gesuchTranche.getGueltigkeit().getGueltigAb()))
            .orElseThrow(NotFoundException::new)
            .getGueltigkeit()
            .getGueltigAb();

        if (startDate.isBefore(minStartDate)) {
            throw new BadRequestException("Start date must be inside gesuch date range");
        }

        var maxEndDate = gesuch
            .getGesuchTranchen()
            .stream()
            .max(Comparator.comparing(gesuchTranche -> gesuchTranche.getGueltigkeit().getGueltigBis()))
            .orElseThrow(
                NotFoundException::new
            )
            .getGueltigkeit()
            .getGueltigBis();

        if (endDate != null && endDate.isAfter(maxEndDate)) {
            throw new BadRequestException("End date must be inside gesuch date range");
        }

        return new DateRange(minStartDate, maxEndDate);
    }

    private static DateRange validateAndCreateClampedDateRange(final DateRange gueltigkeit, final Gesuch gesuch) {
        final var maxRange =
            validateAndCreateDateRange(gueltigkeit.getGueltigAb(), gueltigkeit.getGueltigBis(), gesuch);

        final var clampedGueltigkeit = clampStartStop(
            maxRange.getGueltigAb(),
            maxRange.getGueltigBis(),
            gueltigkeit
        );

        if (DateUtil.getMonthsBetween(clampedGueltigkeit.getGueltigAb(), clampedGueltigkeit.getGueltigBis()) < 1) {
            throw new CustomValidationsException(
                "Die Zeitspanne ist zu kurz",
                new CustomConstraintViolation(VALIDATION_TRANCHE_DATERANGE_TOO_SHORT, "gueltigkeit")
            );
        }

        return clampedGueltigkeit;
    }

    /**
     * Copies an existing {@link GesuchTranche} and sets all values, so it's a complete Aenderungstranche
     */
    public GesuchTranche createAenderungstranche(
        final GesuchTranche original,
        final CreateAenderungsantragRequestDto createDto
    ) {
        var endDate = createDto.getEnd();
        final var maxDaterange = validateAndCreateClampedDateRange(
            new DateRange(createDto.getStart(), createDto.getEnd()),
            original.getGesuch()
        );

        if (endDate == null) {
            endDate = maxDaterange.getGueltigBis();
        }

        final var copy = copyTranche(
            original,
            new DateRange(createDto.getStart(), endDate),
            createDto.getComment()
        );

        copy.setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        copy.setTyp(GesuchTrancheTyp.AENDERUNG);

        return copy;
    }

    public GesuchTranche createNewTranche(
        final GesuchTranche gesuchTranche
    ) {
        return createNewTranche(
            gesuchTranche,
            gesuchTranche.getGueltigkeit(),
            gesuchTranche.getComment()
        );
    }

    /**
     * Copies an existing {@link GesuchTranche} and sets the new {@link GesuchTranche#status}
     * to {@link GesuchTrancheStatus#UEBERPRUEFEN}
     */
    public GesuchTranche createNewTranche(
        final GesuchTranche gesuchTranche,
        final DateRange gueltigkeit,
        final String comment
    ) {
        final var clamped = validateAndCreateClampedDateRange(gueltigkeit, gesuchTranche.getGesuch());
        final var newTranche = copyTranche(
            gesuchTranche,
            clamped,
            comment
        );

        newTranche.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        newTranche.setTyp(GesuchTrancheTyp.TRANCHE);

        return newTranche;
    }

    public GesuchTranche createCopy(final GesuchTranche source) {
        final var copy = new GesuchTranche();

        copy.setGueltigkeit(source.getGueltigkeit());
        copy.setGesuchFormular(source.getGesuchFormular());
        copy.setStatus(source.getStatus());
        copy.setComment(source.getComment());
        copy.setGesuchDokuments(source.getGesuchDokuments());
        copy.setTyp(source.getTyp());

        return copy;
    }

    /**
     * Copies a tranche
     */
    public GesuchTranche copyTranche(
        final GesuchTranche original,
        final DateRange createDateRange,
        final String comment
    ) {
        final var newTranche = copyTrancheExceptGesuchDokuments(original, createDateRange, comment);
        newTranche.setGesuchDokuments(
            GesuchDokumentCopyUtil.copyGesuchDokumenteWithDokumentReferences(
                newTranche,
                original.getGesuchDokuments()
            )
        );
        return newTranche;
    }

    public GesuchTranche copyTrancheExceptGesuchDokuments(
        final GesuchTranche original,
        final DateRange createDateRange,
        final String comment
    ) {
        final var newTranche = new GesuchTranche();
        newTranche.setGueltigkeit(createDateRange);
        newTranche.setComment(comment);
        newTranche.setGesuchFormular(copy(original.getGesuchFormular()));
        newTranche.getGesuchFormular().setTranche(newTranche);
        newTranche.setGesuch(original.getGesuch());
        return newTranche;
    }

    DateRange clampStartStop(
        final LocalDate startDateBoundary,
        final LocalDate endDateBoundary,
        final DateRange createDateRange
    ) {
        final var startDate = DateUtil.roundToStartOrEnd(
            DateUtil.clamp(
                createDateRange.getGueltigAb(),
                startDateBoundary,
                endDateBoundary
            ),
            15,
            false,
            true
        );

        var endDate = createDateRange.getGueltigBis();
        if (endDate == null) {
            endDate = endDateBoundary;
        }

        final var roundedEndDate = DateUtil.roundToStartOrEnd(
            DateUtil.clamp(
                endDate,
                startDateBoundary,
                endDateBoundary
            ),
            14,
            true,
            false
        );

        return new DateRange(startDate, roundedEndDate);
    }

    public GesuchFormular copy(final GesuchFormular other) {
        final var copy = new GesuchFormular();

        // PiA und PiA Adresse
        copy.setPersonInAusbildung(
            PersonInAusbildungCopyUtil.createCopyIgnoreReferences(other.getPersonInAusbildung())
        );
        final var piaAdresseCopy = AdresseCopyUtil.createCopy(other.getPersonInAusbildung().getAdresse());
        copy.getPersonInAusbildung().setAdresse(piaAdresseCopy);

        // Familiensituation
        copy.setFamiliensituation(FamiliensituationCopyUtil.createCopy(other.getFamiliensituation()));

        // Partner und Partner Adresse
        copy.setPartner(PartnerCopyUtil.createCopyIgnoreReferences(other.getPartner()));
        if (copy.getPartner() != null) {
            copy.getPartner().setAdresse(AdresseCopyUtil.createCopy(other.getPartner().getAdresse()));
        }

        // Eltern
        copy.setElterns(ElternCopyUtil.createCopyOfSetWithoutReferences(other.getElterns()));

        final var elternAdressen = new ElternAdressen();
        for (final var eltern : copy.getElterns()) {
            final var adresseCopy = AdresseCopyUtil.createCopy(eltern.getAdresse());
            elternAdressen.setForTyp(eltern.getElternTyp(), adresseCopy);

            eltern.setAdresse(adresseCopy);
        }

        // Auszahlung
        copy.setAuszahlung(AuszahlungCopyUtil.createCopyIgnoreReferences(other.getAuszahlung()));
        final var auszahlungAdresseCopy = switch (copy.getAuszahlung().getKontoinhaber()) {
            case GESUCHSTELLER -> piaAdresseCopy;
            case MUTTER -> elternAdressen.getForTyp(ElternTyp.MUTTER);
            case VATER -> elternAdressen.getForTyp(ElternTyp.VATER);
            default -> AdresseCopyUtil.createCopy(other.getAuszahlung().getAdresse());
        };
        copy.getAuszahlung().setAdresse(auszahlungAdresseCopy);

        // Einnahmen Kosten
        copy.setEinnahmenKosten(EinnahmenKostenCopyUtil.createCopy(other.getEinnahmenKosten()));

        // Lebenslauf
        copy.setLebenslaufItems(LebenslaufItemCopyUtil.createCopyOfSet(other.getLebenslaufItems()));

        // Geschwister
        copy.setGeschwisters(GeschwisterCopyUtil.createCopyOfSet(other.getGeschwisters()));

        // Kinds
        copy.setKinds(KindCopyUtil.createCopySet(other.getKinds()));

        // Steuererklaerung
        copy.setSteuererklaerung(SteuererklaerungCopyUtil.createCopySet(other.getSteuererklaerung()));

        // Steuerdaten
        copy.setSteuerdaten(SteuerdatenCopyUtil.createCopySet(other.getSteuerdaten()));

        // Darlehen
        copy.setDarlehen(DarlehenCopyUtil.createCopy(other.getDarlehen()));

        return copy;
    }

    @Getter
    public class ElternAdressen {
        private Adresse mutterAdresse;
        private Adresse vaterAdresse;

        public static ElternAdressen fromGesuchFormular(final GesuchFormular gesuchFormular) {
            final var elternAdressen = new ElternAdressen();
            for (final var eltern : gesuchFormular.getElterns()) {
                elternAdressen.setForTyp(eltern.getElternTyp(), eltern.getAdresse());
            }

            return elternAdressen;
        }

        public void setForTyp(final ElternTyp typ, final Adresse adresse) {
            if (typ == ElternTyp.VATER) {
                vaterAdresse = adresse;
            } else if (typ == ElternTyp.MUTTER) {
                mutterAdresse = adresse;
            }
        }

        public Adresse getForTyp(final ElternTyp typ) {
            return switch (typ) {
                case VATER -> vaterAdresse;
                case MUTTER -> mutterAdresse;
            };
        }
    }
}
