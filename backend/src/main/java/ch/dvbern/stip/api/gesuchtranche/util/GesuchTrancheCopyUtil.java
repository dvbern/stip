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

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungCopyUtil;
import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenCopyUtil;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import jakarta.ws.rs.NotFoundException;
import lombok.experimental.UtilityClass;

@UtilityClass
// TODO KSTIP-1236: Once proper test data generation is in place, test copying
public class GesuchTrancheCopyUtil {
    /**
     * Copies an existing {@link GesuchTranche} and sets all values, so it's a complete Aenderungstranche
     */
    public GesuchTranche createAenderungstranche(
        final GesuchTranche original,
        final CreateAenderungsantragRequestDto createDto
    ) {
        var endDate = createDto.getEnd();
        if (endDate == null) {
            endDate = original.getGueltigkeit().getGueltigBis();
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

    public GesuchTranche createNewTranche(final GesuchTranche gesuchTranche) {
        return createNewTranche(gesuchTranche, gesuchTranche.getGueltigkeit(), gesuchTranche.getComment());
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
        final var gesuchTranchen = gesuchTranche.getGesuch()
            .getGesuchTranchen();
        final var clampDateStart = gesuchTranchen
            .stream()
            .filter(gesuchTranche1 -> gesuchTranche1.getTyp() == GesuchTrancheTyp.TRANCHE)
            .min(
                (gesuchTranche1, gesuchTranche2) -> gesuchTranche1.getGueltigkeit()
                    .getGueltigAb()
                    .isAfter(gesuchTranche2.getGueltigkeit().getGueltigAb()) ? 1 : -1
            )
            .orElseThrow(NotFoundException::new)
            .getGueltigkeit()
            .getGueltigAb();

        final var clampDateStop = gesuchTranchen
            .stream()
            .filter(gesuchTranche1 -> gesuchTranche1.getTyp() == GesuchTrancheTyp.TRANCHE)
            .min(
                (gesuchTranche1, gesuchTranche2) -> gesuchTranche1.getGueltigkeit()
                    .getGueltigBis()
                    .isBefore(gesuchTranche2.getGueltigkeit().getGueltigBis()) ? 1 : -1
            )
            .orElseThrow(NotFoundException::new)
            .getGueltigkeit()
            .getGueltigBis();

        final var clamped = clampStartStop(
            clampDateStart,
            clampDateStop,
            gueltigkeit
        );

        final var newTranche = copyTranche(
            gesuchTranche,
            clamped,
            comment
        );

        newTranche.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        newTranche.setTyp(GesuchTrancheTyp.TRANCHE);

        return newTranche;
    }

    /**
     * Copies a tranche
     */
    public GesuchTranche copyTranche(
        final GesuchTranche original,
        final DateRange createDateRange,
        final String comment
    ) {
        final var newTranche = new GesuchTranche();
        newTranche.setGueltigkeit(createDateRange);
        newTranche.setComment(comment);
        newTranche.setGesuchFormular(copy(original.getGesuchFormular()));
        newTranche.setGesuchDokuments(
            GesuchDokumentCopyUtil.copyGesuchDokumenteWithDokumentReferences(
                newTranche,
                original.getGesuchDokuments()
            )
        );
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

        if (startDate.isAfter(roundedEndDate)) {
            throw new AppErrorException("Start date for new GesuchTranche must be after end date");
        }

        return new DateRange(startDate, roundedEndDate);
    }

    GesuchFormular copy(final GesuchFormular other) {
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
        Adresse mutterAdresseCopy = null;
        Adresse vaterAdresseCopy = null;
        for (final var eltern : copy.getElterns()) {
            final var adresseCopy = AdresseCopyUtil.createCopy(eltern.getAdresse());
            if (eltern.getElternTyp() == ElternTyp.MUTTER) {
                mutterAdresseCopy = adresseCopy;
            } else if (eltern.getElternTyp() == ElternTyp.VATER) {
                vaterAdresseCopy = adresseCopy;
            }

            eltern.setAdresse(adresseCopy);
        }

        // Auszahlung
        copy.setAuszahlung(AuszahlungCopyUtil.createCopyIgnoreReferences(other.getAuszahlung()));
        final var auszahlungAdresseCopy = switch (copy.getAuszahlung().getKontoinhaber()) {
            case GESUCHSTELLER -> piaAdresseCopy;
            case MUTTER -> mutterAdresseCopy;
            case VATER -> vaterAdresseCopy;
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

        // Steuerdaten
        copy.setSteuerdaten(SteuerdatenCopyUtil.createCopySet(other.getSteuerdaten()));

        return copy;
    }
}