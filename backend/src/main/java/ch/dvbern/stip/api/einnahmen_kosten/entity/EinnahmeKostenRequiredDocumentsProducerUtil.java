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
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmeKostenRequiredDocumentsProducerUtil {
    public enum EinnahmenKostenType {
        PERSON_IN_AUSBILDUNG,
        PARTNER
    }

    private static final Map<EinnahmenKostenType, DokumentTyp> NETTOERWERBSEINKOMMEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_LOHNABRECHNUNG,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_LOHNABRECHNUNG
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> BETREUUNGSKOSTENKINDER = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_BETREUUNGSKOSTEN_KINDER
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> FAHRKOSTEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_OV_ABONNEMENT,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_OV_ABONNEMENT
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> EOLEISTUNGEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> RENTEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_BEZAHLTE_RENTEN
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> BEITRAEGE = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> ZULAGEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_KINDERZULAGEN,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_KINDERZULAGEN
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> UNTERHALTSBEITRAEGE = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_UNTERHALTSBEITRAEGE,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_UNTERHALTSBEITRAEGE
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> ERGAENZUNGSLEISTUNGEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> VERMOEGEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_VERMOEGEN,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_VERMOEGEN
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> EINNAHMENBGSA = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_EINNAHMEN_BGSA,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_EINNAHMEN_BGSA
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> TAGGELDERAHVIV = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_TAGGELDER_AHV_IV,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_TAGGELDER_AHV_IV
    );

    private static final Map<EinnahmenKostenType, DokumentTyp> ANDEREEINNAHMEN = Map.of(
        EinnahmenKostenType.PERSON_IN_AUSBILDUNG,
        DokumentTyp.EK_BELEG_ANDERE_EINNAHMEN,
        EinnahmenKostenType.PARTNER,
        DokumentTyp.EK_PARTNER_BELEG_ANDERE_EINNAHMEN
    );

    public Set<DokumentTyp> getRequiredDocuments(
        final GesuchFormular formular,
        final EinnahmenKostenType einnahmenKostenType
    ) {
        final var ek = switch (einnahmenKostenType) {
            case PERSON_IN_AUSBILDUNG -> formular.getEinnahmenKosten();
            case PARTNER -> formular.getEinnahmenKostenPartner();
        };

        if (ek == null) {
            return Set.of();
        }

        final var requiredDocs = new HashSet<DokumentTyp>();

        if (greaterThanZero(ek.getNettoerwerbseinkommen())) {
            requiredDocs.add(NETTOERWERBSEINKOMMEN.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getBetreuungskostenKinder())) {
            requiredDocs.add(BETREUUNGSKOSTENKINDER.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getWohnkosten())) {
            if (einnahmenKostenType == EinnahmenKostenType.PARTNER) {
                throw new IllegalStateException("Einnahmen Kosten of Partner cannot have Wohnkosten set");
            }

            requiredDocs.add(DokumentTyp.EK_MIETVERTRAG);
        }

        if (greaterThanZero(ek.getFahrkosten())) {
            requiredDocs.add(FAHRKOSTEN.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getEoLeistungen())) {
            requiredDocs.add(EOLEISTUNGEN.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getRenten())) {
            requiredDocs.add(RENTEN.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getBeitraege())) {
            requiredDocs.add(BEITRAEGE.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getZulagen())) {
            requiredDocs.add(ZULAGEN.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getUnterhaltsbeitraege())) {
            requiredDocs.add(UNTERHALTSBEITRAEGE.get(einnahmenKostenType));
        }

        if (greaterThanZero(ek.getErgaenzungsleistungen())) {
            requiredDocs.add(ERGAENZUNGSLEISTUNGEN.get(einnahmenKostenType));
        }
        if (greaterThanZero(ek.getVermoegen())) {
            requiredDocs.add(VERMOEGEN.get(einnahmenKostenType));
        }
        if (greaterThanZero(ek.getEinnahmenBGSA())) {
            requiredDocs.add(EINNAHMENBGSA.get(einnahmenKostenType));
        }
        if (greaterThanZero(ek.getTaggelderAHVIV())) {
            requiredDocs.add(TAGGELDERAHVIV.get(einnahmenKostenType));
        }
        if (greaterThanZero(ek.getAndereEinnahmen())) {
            requiredDocs.add(ANDEREEINNAHMEN.get(einnahmenKostenType));
        }

        return requiredDocs;

    }

    private boolean greaterThanZero(final Integer base) {
        return base != null && base > 0;
    }
}
