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

import java.time.LocalDate;
import java.util.HashSet;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.generator.entities.service.LandGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.common.util.Constants.MAX_AGE_AUSBILDUNGSBEGIN;

@QuarkusTest
class PersonInAusbildungRequiredDocumentsProducerTest {
    private PersonInAusbildungRequiredDocumentsProducer producer;

    private GesuchFormular formular;
    @Inject
    PlzService plzService;

    @BeforeEach
    void setup() {
        producer = new PersonInAusbildungRequiredDocumentsProducer(plzService);
        formular = new GesuchFormular();

        final Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().withDayOfMonth(1));
        formular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
    }

    @Test
    void requiresIfNiederlassungsstatusB() {
        formular.setPersonInAusbildung(createWithNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B));
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B
        );
    }

    @Test
    void requiresIfNiederlassungsstatusC() {
        formular
            .setPersonInAusbildung(createWithNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C));
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C
        );
    }

    @Test
    void requiresIfNiederlassungsstatusVorlaeufigAufgenommen_AndererKatnon() {
        formular.setPersonInAusbildung(
            createWithNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON
        );
    }

    @Test
    void requiresIfNiederlassungsstatusVorlaeufigAufgenommen_Mandant() {
        formular.setPersonInAusbildung(
            createWithNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT
        );
    }

    @Test
    void requiresIfNiederlassungsstatusVorlaeufigAufgenommen_OhneFluechtlingsstatus() {
        formular.setPersonInAusbildung(
            createWithNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS
        );
    }

    @Test
    void requiresIfVormundschaft() {
        formular.setPersonInAusbildung(
            createNewPia()
                .setSozialhilfebeitraege(false)
                .setVormundschaft(true)
        );
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.PERSON_KESB_ERNENNUNG);
    }

    @Test
    void requiresIfWohnsitz() {
        formular.setPersonInAusbildung(
            (PersonInAusbildung) createNewPia()
                .setSozialhilfebeitraege(false)
                .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
        );
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.PERSON_MIETVERTRAG);
    }

    @Test
    void requiresIfQuellenbesteuert() {
        formular.setPersonInAusbildung(
            createNewPia()
                .setSozialhilfebeitraege(false)
                .setNationalitaet(LandGenerator.initGermany())
                .setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B)
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertTypes(
            requiredDocs,
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B
        );
    }

    @Test
    void requiresIfSozialhilfebeitraege() {
        formular.setPersonInAusbildung(
            createNewPia()
                .setSozialhilfebeitraege(false)
                .setSozialhilfebeitraege(true)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_SOZIALHILFEBUDGET
        );
    }

    @Test
    void requiresIfAgeOverAusbildungsbeginMax() {
        final PersonInAusbildung person = createNewPia();
        person.setGeburtsdatum(
            LocalDate
                .now()
                .withDayOfMonth(1)
                .minusYears(MAX_AGE_AUSBILDUNGSBEGIN)
                .minusDays(1)
        );
        formular.setPersonInAusbildung(person);
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN
        );
    }

    @Test
    void requiresIfGeschieden() {
        formular.setPersonInAusbildung(
            createNewPia()
                .setSozialhilfebeitraege(false)
                .setZivilstand(Zivilstand.GESCHIEDEN_GERICHTLICH)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG
        );
    }

    @Test
    void requiresIfAufgeloestePartnerschaft() {
        formular.setPersonInAusbildung(
            createNewPia()
                .setSozialhilfebeitraege(false)
                .setZivilstand(Zivilstand.AUFGELOESTE_PARTNERSCHAFT)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG
        );
    }

    @Test
    void requiresIfInBernAndParentsAbroad() {
        formular.setPersonInAusbildung(
            createNewPia()
                .setSozialhilfebeitraege(false)
                .setAdresse(
                    new Adresse()
                        .setPlz("3011")
                )
        )
            .setElterns(new HashSet<>() {
                {
                    add(new Eltern().setAdresse(new Adresse().setLand(LandGenerator.initGermany())));
                }
            });
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.PERSON_AUSWEIS);
    }

    private PersonInAusbildung createNewPia() {
        final PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setGeburtsdatum(LocalDate.now());
        return personInAusbildung;
    }

    private PersonInAusbildung createWithNiederlassungsstatus(Niederlassungsstatus status) {
        return createNewPia()
            .setSozialhilfebeitraege(false)
            .setNiederlassungsstatus(status);
    }
}
