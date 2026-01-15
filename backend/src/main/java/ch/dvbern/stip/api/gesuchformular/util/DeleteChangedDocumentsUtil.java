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

package ch.dvbern.stip.api.gesuchformular.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DeleteChangedDocumentsUtil {
    public ArrayList<DokumentTyp> getChangedDocumentsToDelete(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular oldFormular
    ) {
        if (oldFormular.getTranche().getTyp() != GesuchTrancheTyp.AENDERUNG) {
            LOG.info("Skipping deleting documents on fields that changed for Tranche that is not AENDERUNG");
            return new ArrayList<>();
        }

        final var documentTypesToDelete = new ArrayList<DokumentTyp>();
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForPersonInAusbildung(
                newFormular.getPersonInAusbildung(),
                oldFormular.getPersonInAusbildung()
            )
        );

        for (final Eltern oldEltern : oldFormular.getElterns()) {
            final var newEltern = newFormular.getElterns()
                .stream()
                .filter(
                    elternUpdateDto -> elternUpdateDto.getElternTyp() == oldEltern.getElternTyp()
                )
                .findFirst();

            if (newEltern.isPresent()) {
                documentTypesToDelete.addAll(
                    getDocumentsToDeleteForEltern(newEltern.get(), oldEltern)
                );
            } else {
                documentTypesToDelete.add(
                    switch (oldEltern.getElternTyp()) {
                        case MUTTER -> DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER;
                        case VATER -> DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER;
                    }
                );
            }
        }

        documentTypesToDelete.addAll(
            getDocumentsToDeleteForFamiliensituation(
                newFormular.getFamiliensituation(),
                oldFormular.getFamiliensituation()
            )
        );

        documentTypesToDelete.addAll(
            getDocumentsToDeleteForPartner(newFormular.getPartner(), oldFormular.getPartner())
        );
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForEinnahmenKosten(newFormular.getEinnahmenKosten(), oldFormular.getEinnahmenKosten())
        );

        return documentTypesToDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForPersonInAusbildung(
        final PersonInAusbildungUpdateDto newPia,
        final PersonInAusbildung oldPia
    ) {
        final var toDelete = new ArrayList<DokumentTyp>();
        if (!Objects.equals(newPia.getGeburtsdatum(), oldPia.getGeburtsdatum())) {
            toDelete.add(DokumentTyp.PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN);
        }

        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForPartner(
        final PartnerUpdateDto newPartner,
        final Partner oldPartner
    ) {
        if (newPartner == null || oldPartner == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<DokumentTyp>();
        // todo kstip-2779: check parter einnahmekosten here
        // if (hasChangedAndNewIsGreaterThanZero(oldPartner.getJahreseinkommen(), newPartner.getJahreseinkommen())) {
        // toDelete.add(DokumentTyp.PARTNER_AUSBILDUNG_LOHNABRECHNUNG);
        // }
        //
        // if (hasChangedAndNewIsGreaterThanZero(oldPartner.getFahrkosten(), newPartner.getFahrkosten())) {
        // toDelete.add(DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT);
        // }

        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForEinnahmenKosten(
        final EinnahmenKostenUpdateDto newEk,
        final EinnahmenKosten oldEk
    ) {
        final var toDelete = new ArrayList<DokumentTyp>();
        if (hasChangedAndNewIsGreaterThanZero(oldEk.getNettoerwerbseinkommen(), newEk.getNettoerwerbseinkommen())) {
            toDelete.add(DokumentTyp.EK_LOHNABRECHNUNG);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getBetreuungskostenKinder(), newEk.getBetreuungskostenKinder())) {
            toDelete.add(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getWohnkosten(), newEk.getWohnkosten())) {
            toDelete.add(DokumentTyp.EK_MIETVERTRAG);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getFahrkosten(), newEk.getFahrkosten())) {
            toDelete.add(DokumentTyp.EK_BELEG_OV_ABONNEMENT);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getEoLeistungen(), newEk.getEoLeistungen())) {
            toDelete.add(DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getRenten(), newEk.getRenten())) {
            toDelete.add(DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getBeitraege(), newEk.getBeitraege())) {
            toDelete.add(DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getZulagen(), newEk.getZulagen())) {
            toDelete.add(DokumentTyp.EK_BELEG_KINDERZULAGEN);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getUnterhaltsbeitraege(), newEk.getUnterhaltsbeitraege())) {
            toDelete.add(DokumentTyp.EK_BELEG_UNTERHALTSBEITRAEGE);
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getErgaenzungsleistungen(), newEk.getErgaenzungsleistungen())) {
            toDelete.add(DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN);
        }
        if (hasChangedAndNewIsGreaterThanZero(oldEk.getVermoegen(), newEk.getVermoegen())) {
            toDelete.add(DokumentTyp.EK_VERMOEGEN);
        }

        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForDarlehen(
        final FreiwilligDarlehenDto newDarlehen,
        final FreiwilligDarlehen oldFreiwilligDarlehen
    ) {
        if (newDarlehen == null || oldFreiwilligDarlehen == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<DokumentTyp>();
        // if (
        // hasChangedAndNewIsGreaterThanZero(oldDarlehen.getAnzahlBetreibungen(), newDarlehen.getAnzahlBetreibungen())
        // ) {
        // toDelete.add(DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG);
        // }

        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForEltern(
        final ElternUpdateDto newEltern,
        final Eltern oldEltern
    ) {
        if (newEltern == null || oldEltern == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<DokumentTyp>();
        if (
            hasChangedAndNewIsGreaterThanZero(oldEltern.getWohnkosten(), newEltern.getWohnkosten())
        ) {
            final var toDeleteDoc = switch (newEltern.getElternTyp()) {
                case MUTTER -> DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER;
                case VATER -> DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER;
            };
            toDelete.add(toDeleteDoc);
            toDelete.add(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
        }
        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForFamiliensituation(
        final FamiliensituationUpdateDto newFamiliensituation,
        final Familiensituation oldFamiliensituation
    ) {
        if (newFamiliensituation == null || oldFamiliensituation == null) {
            return List.of();
        }
        final var toDelete = new ArrayList<DokumentTyp>();

        if (
            !Objects.equals(
                newFamiliensituation.getElternVerheiratetZusammen(),
                oldFamiliensituation.getElternVerheiratetZusammen()
            )
        ) {
            toDelete.add(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER);
            toDelete.add(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER);
            toDelete.add(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
        }
        return toDelete;
    }

    private boolean hasChangedAndNewIsGreaterThanZero(final Integer oldVal, final Integer newVal) {
        return !Objects.equals(oldVal, newVal) && newVal != null && newVal > 0;
    }
}
