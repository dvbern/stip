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

package ch.dvbern.stip.api.einnahmen_kosten.entity;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmeKostenRequiredDocumentsProducerUtil {

    public Set<DokumentTyp> getRequiredDocumentsForPIA(
        GesuchFormular formular
    ) {
        final var ek = formular.getEinnahmenKosten();
        if (ek == null) {
            return Set.of();
        }

        final var requiredDocs = new HashSet<DokumentTyp>();

        if (greaterThanZero(ek.getNettoerwerbseinkommen())) {
            requiredDocs.add(DokumentTyp.EK_LOHNABRECHNUNG);
        }

        if (greaterThanZero(ek.getBetreuungskostenKinder())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER);
        }

        if (greaterThanZero(ek.getWohnkosten())) {
            requiredDocs.add(DokumentTyp.EK_MIETVERTRAG);
        }

        if (greaterThanZero(ek.getFahrkosten())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_OV_ABONNEMENT);
        }

        if (greaterThanZero(ek.getEoLeistungen())) {
            requiredDocs.add(DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO);
        }

        if (greaterThanZero(ek.getRenten())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN);
        }

        if (greaterThanZero(ek.getBeitraege())) {
            requiredDocs.add(DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION);
        }

        if (greaterThanZero(ek.getZulagen())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_KINDERZULAGEN);
        }

        if (greaterThanZero(ek.getUnterhaltsbeitraege())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_UNTERHALTSBEITRAEGE);
        }

        if (greaterThanZero(ek.getErgaenzungsleistungen())) {
            requiredDocs.add(DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN);
        }
        if (greaterThanZero(ek.getVermoegen())) {
            requiredDocs.add(DokumentTyp.EK_VERMOEGEN);
        }
        if (greaterThanZero(ek.getEinnahmenBGSA())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_EINNAHMEN_BGSA);
        }
        if (greaterThanZero(ek.getTaggelderAHVIV())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_TAGGELDER_AHV_IV);
        }
        if (greaterThanZero(ek.getAndereEinnahmen())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_ANDERE_EINNAHMEN);
        }

        return requiredDocs;

    }

    public Set<DokumentTyp> getRequiredDocumentsForPartner(
        GesuchFormular formular
    ) {
        final var partner = formular.getPartner();
        final var ekPartner = formular.getEinnahmenKostenPartner();
        if (partner == null || ekPartner == null) {
            return Set.of();
        }

        final var requiredDocs = new HashSet<DokumentTyp>();

        if (greaterThanZero(ekPartner.getNettoerwerbseinkommen())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_LOHNABRECHNUNG);
        }

        if (greaterThanZero(ekPartner.getBetreuungskostenKinder())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_BETREUUNGSKOSTEN_KINDER);
        }

        if (greaterThanZero(ekPartner.getFahrkosten())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_OV_ABONNEMENT);
        }

        if (greaterThanZero(ekPartner.getEoLeistungen())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO);
        }

        if (greaterThanZero(ekPartner.getRenten())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_BEZAHLTE_RENTEN);
        }

        if (greaterThanZero(ekPartner.getBeitraege())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION);
        }

        if (greaterThanZero(ekPartner.getZulagen())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_KINDERZULAGEN);
        }

        if (greaterThanZero(ekPartner.getUnterhaltsbeitraege())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_UNTERHALTSBEITRAEGE);
        }

        if (greaterThanZero(ekPartner.getErgaenzungsleistungen())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN);
        }
        if (greaterThanZero(ekPartner.getVermoegen())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_VERMOEGEN);
        }
        if (greaterThanZero(ekPartner.getEinnahmenBGSA())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_EINNAHMEN_BGSA);
        }
        if (greaterThanZero(ekPartner.getTaggelderAHVIV())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_TAGGELDER_AHV_IV);
        }
        if (greaterThanZero(ekPartner.getAndereEinnahmen())) {
            requiredDocs.add(DokumentTyp.EK_PARTNER_BELEG_ANDERE_EINNAHMEN);
        }

        return requiredDocs;

    }

    // todo kstip-2571: refactor & remove duplication
    private boolean greaterThanZero(final Integer base) {
        return base != null && base > 0;
    }
}
