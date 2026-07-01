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

package ch.dvbern.stip.api.dokument.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.generator.entities.personinausbildung.PersonInAusbildungTestBuilder;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungRequiredDokumentsProducer;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_FIXED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
class DokumentsRequiredDokumentProducerTest {

    @Test
    void requiresIfPiaLedigWithChildren() {
        final var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var formular = gesuch.getLatestGesuchTranche().getGesuchFormular();
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
                .setZivilstand(Zivilstand.LEDIG)
        )
            .setKinds(
                Set.of(
                    new Kind()
                )
            );

        final var requiredDocs = new DokumentsRequiredDokumentProducer().getRequiredDokuments(formular, true);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION);
    }

    @ParameterizedTest
    @CsvSource(
        {
            "18,FAMILIE,TRANCHE,false,",
            "18,EIGENER_HAUSHALT,TRANCHE,true,",
            "20,EIGENER_HAUSHALT,TRANCHE,false,",
            "20,EIGENER_HAUSHALT,AENDERUNG,true,2",
            "20,EIGENER_HAUSHALT,AENDERUNG,false,0",
        }
    )
    void requiresIfPiaWohnsitzBegruendungsschreiben(
        final int piaAge,
        final Wohnsitz wohnsitz,
        final GesuchTrancheTyp trancheTyp,
        final boolean isRequired,
        final Integer aenderungAge
    ) {
        final var referenceDate = LocalDate.now().withYear(GUELTIGKEIT_PERIODE_FIXED.getGueltigAb().getYear());
        final var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var formular = gesuch.getLatestGesuchTranche().getGesuchFormular();
        final var pia = PersonInAusbildungTestBuilder.standardNestedDeps(referenceDate)
            .with(p -> {
                p.setGeburtsdatum(referenceDate.minusYears(piaAge));
                p.setWohnsitz(wohnsitz);
            })
            .build();
        final var tranche = gesuch.getLatestGesuchTranche();
        tranche.getGesuchFormular().setPersonInAusbildung(pia);
        tranche.setTyp(trancheTyp);

        if (trancheTyp == GesuchTrancheTyp.AENDERUNG) {
            tranche.setGueltigkeit(DateRange.getFruehlingOrHerbst(referenceDate.minusYears(aenderungAge)));
        }

        final List<DokumentTyp> requiredDokTypes = List.of(
            DokumentTyp.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE
        );
        tranche.setGesuchDokuments(requiredDokTypes.stream().map(dokumentTyp -> {
            final var gesuchDokument = new GesuchDokument();
            gesuchDokument.addDokument(new Dokument());
            gesuchDokument.setDokumentTyp(dokumentTyp);
            return gesuchDokument;
        }).toList());

        final var requiredDocs = new PersonInAusbildungRequiredDokumentsProducer(Mockito.mock(PlzService.class))
            .getRequiredDokuments(formular, true)
            .getRight();
        assertThat(requiredDocs.toString(), requiredDocs.contains(DokumentTyp.PERSON_EIGENER_HAUSHALT), is(isRequired));
    }
}
