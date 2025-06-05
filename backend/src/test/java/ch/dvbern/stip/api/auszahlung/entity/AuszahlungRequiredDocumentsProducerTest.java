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

package ch.dvbern.stip.api.auszahlung.entity;

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuszahlungRequiredDocumentsProducerTest {
    private AuszahlungRequiredDocumentsProducer producer;

    private GesuchFormular formular;
    private GesuchTranche tranche;
    private Gesuch gesuch;
    private Ausbildung ausbildung;
    private Fall fall;

    @BeforeEach
    void setup() {
        producer = new AuszahlungRequiredDocumentsProducer();
        formular = new GesuchFormular();

        fall = new Fall();
        ausbildung = new Ausbildung();
        ausbildung.setFall(fall);
        fall.setAusbildungs(Set.of(ausbildung));

        gesuch = new Gesuch();
        gesuch.setAusbildung(ausbildung);
        ausbildung.setGesuchs(List.of(gesuch));

        tranche = new GesuchTranche();
        tranche.setGesuch(gesuch);
        gesuch.setGesuchTranchen(List.of(tranche));

        tranche.setGesuchFormular(formular);
        formular.setTranche(tranche);
    }

    @Test
    void requiresIfKontoinhaberSozialdienst() {
        formular.getTranche()
            .getGesuch()
            .getAusbildung()
            .getFall()
            .setAuszahlung(
                new Auszahlung()
                    .setAuszahlungAnSozialdienst(true)
            );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertType(requiredDocs, DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
    }

    @Test
    void requiresIfKontoinhaberAndere() {
        formular.getTranche()
            .getGesuch()
            .getAusbildung()
            .getFall()
            .setAuszahlung(
                new Auszahlung()
                    .setAuszahlungAnSozialdienst(false)
            );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertType(requiredDocs, DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
    }
}
