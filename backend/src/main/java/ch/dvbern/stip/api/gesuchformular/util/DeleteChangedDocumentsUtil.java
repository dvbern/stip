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

import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DeleteChangedDocumentsUtil {
    public void deleteChangedDocuments(
        final GesuchDokumentService gesuchDokumentService,
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular oldFormular
    ) {
        if (oldFormular.getTranche().getTyp() != GesuchTrancheTyp.AENDERUNG) {
            LOG.info("Skipping deleting documents on fields that changed for Tranche that is not AENDERUNG");
            return;
        }

        final var documentTypesToDelete = new ArrayList<DokumentTyp>();
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForPersonInAusbildung(
                newFormular.getPersonInAusbildung(),
                oldFormular.getPersonInAusbildung()
            )
        );
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForPartner(newFormular.getPartner(), oldFormular.getPartner())
        );
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForEinnahmenKosten(newFormular.getEinnahmenKosten(), oldFormular.getEinnahmenKosten())
        );
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForDarlehen(newFormular.getDarlehen(), oldFormular.getDarlehen())
        );

        gesuchDokumentService.deleteDokumenteForTranche(oldFormular.getTranche().getId(), documentTypesToDelete);
    }

    List<DokumentTyp> getDocumentsToDeleteForPersonInAusbildung(
        final PersonInAusbildungUpdateDto newPia,
        final PersonInAusbildung oldPia
    ) {
        final var toDelete = new ArrayList<DokumentTyp>();
        if (hasChanged(newPia.getGeburtsdatum(), oldPia.getGeburtsdatum())) {
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
        if (hasChanged(newPartner.getJahreseinkommen(), oldPartner.getJahreseinkommen())) {
            toDelete.add(DokumentTyp.PARTNER_AUSBILDUNG_LOHNABRECHNUNG);
        }

        if (hasChanged(newPartner.getFahrkosten(), oldPartner.getFahrkosten())) {
            toDelete.add(DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT);
        }

        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForEinnahmenKosten(
        final EinnahmenKostenUpdateDto newEk,
        final EinnahmenKosten oldEk
    ) {
        final var toDelete = new ArrayList<DokumentTyp>();
        if (hasChanged(oldEk.getNettoerwerbseinkommen(), newEk.getNettoerwerbseinkommen())) {
            toDelete.add(DokumentTyp.EK_LOHNABRECHNUNG);
        }

        if (hasChanged(oldEk.getBetreuungskostenKinder(), newEk.getBetreuungskostenKinder())) {
            toDelete.add(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER);
        }

        if (hasChanged(oldEk.getWohnkosten(), newEk.getWohnkosten())) {
            toDelete.add(DokumentTyp.EK_MIETVERTRAG);
        }

        if (hasChanged(oldEk.getFahrkosten(), newEk.getFahrkosten())) {
            toDelete.add(DokumentTyp.EK_BELEG_OV_ABONNEMENT);
        }

        if (hasChanged(oldEk.getEoLeistungen(), newEk.getEoLeistungen())) {
            toDelete.add(DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO);
        }

        if (hasChanged(oldEk.getRenten(), newEk.getRenten())) {
            toDelete.add(DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN);
        }

        if (hasChanged(oldEk.getBeitraege(), newEk.getBeitraege())) {
            toDelete.add(DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION);
        }

        if (hasChanged(oldEk.getZulagen(), newEk.getZulagen())) {
            toDelete.add(DokumentTyp.EK_BELEG_KINDERZULAGEN);
        }

        if (hasChanged(oldEk.getAlimente(), newEk.getAlimente())) {
            toDelete.add(DokumentTyp.EK_BELEG_ALIMENTE);
        }

        if (hasChanged(oldEk.getErgaenzungsleistungen(), newEk.getErgaenzungsleistungen())) {
            toDelete.add(DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN);
        }
        if (hasChanged(oldEk.getVermoegen(), newEk.getVermoegen())) {
            toDelete.add(DokumentTyp.EK_VERMOEGEN);
        }

        return toDelete;
    }

    List<DokumentTyp> getDocumentsToDeleteForDarlehen(
        final DarlehenDto newDarlehen,
        final Darlehen oldDarlehen
    ) {
        if (newDarlehen == null || oldDarlehen == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<DokumentTyp>();
        if (hasChanged(newDarlehen.getAnzahlBetreibungen(), oldDarlehen.getAnzahlBetreibungen())) {
            toDelete.add(DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG);
        }

        return toDelete;
    }

    private <T> boolean hasChanged(final T left, final T right) {
        return !Objects.equals(left, right);
    }
}
