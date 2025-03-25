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

package ch.dvbern.stip.api.personinausbildung.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import static ch.dvbern.stip.api.common.util.Constants.MAX_AGE_AUSBILDUNGSBEGIN;

@ApplicationScoped
@RequiredArgsConstructor
public class PersonInAusbildungRequiredDocumentsProducer implements RequiredDocumentsProducer {
    private final PlzService plzService;

    private final Map<Niederlassungsstatus, DokumentTyp> niederlassungsstatusMap = Map.of(
        Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B,
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
        Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C,
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
        Niederlassungsstatus.FLUECHTLING,
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE
    );

    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var pia = formular.getPersonInAusbildung();
        if (pia == null) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<DokumentTyp>();
        final var niederlassungsstatus = pia.getNiederlassungsstatus();
        if (niederlassungsstatus != null && niederlassungsstatusMap.containsKey(niederlassungsstatus)) {
            requiredDocs.add(niederlassungsstatusMap.get(niederlassungsstatus));
        }

        if (pia.isVormundschaft()) {
            requiredDocs.add(DokumentTyp.PERSON_KESB_ERNENNUNG);
        }

        if (pia.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
            requiredDocs.add(DokumentTyp.PERSON_MIETVERTRAG);
        }

        if (pia.isSozialhilfebeitraege()) {
            requiredDocs.add(DokumentTyp.PERSON_SOZIALHILFEBUDGET);
        }

        if (
            DateUtil.getAgeInYearsAtDate(
                pia.getGeburtsdatum(),
                formular.getAusbildung().getAusbildungBegin().minusDays(1)
            ) >= MAX_AGE_AUSBILDUNGSBEGIN
        ) {
            requiredDocs.add(DokumentTyp.PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN);
        }

        final var zivilstand = pia.getZivilstand();
        if (
            zivilstand == Zivilstand.GESCHIEDEN_GERICHTLICH ||
            zivilstand == Zivilstand.AUFGELOESTE_PARTNERSCHAFT
        ) {
            requiredDocs.add(DokumentTyp.PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG);
        }

        if (plzService.isInBern(pia.getAdresse()) && parentsLiveAbroad(formular)) {
            requiredDocs.add(DokumentTyp.PERSON_AUSWEIS);
        }

        return ImmutablePair.of("personInAusbildung", requiredDocs);
    }

    // Returns whether both parents live abroad or not
    private boolean parentsLiveAbroad(final GesuchFormular formular) {
        if (formular.getElterns().isEmpty()) {
            return false;
        }

        return formular.getElterns().stream().allMatch(x -> {
            final var adresse = x.getAdresse();
            if (adresse == null) {
                return false;
            }

            return adresse.getLand() != Land.CH;
        });
    }
}
