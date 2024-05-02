package ch.dvbern.stip.api.personinausbildung.entity;

import java.util.HashSet;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.service.PlzOrtService;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonInAusbildungRequiredDocumentsProducerTest {
    private PersonInAusbildungRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new PersonInAusbildungRequiredDocumentsProducer(new PlzOrtService());
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfNiederlassungsstatusB() {
        formular.setPersonInAusbildung(createWithNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B));
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B);
    }

    @Test
    void requiresIfNiederlassungsstatusC() {
        formular.setPersonInAusbildung(createWithNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C));
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C);
    }

    @Test
    void requiresIfNiederlassungsstatusFluechtling() {
        formular.setPersonInAusbildung(createWithNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING));
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE);
    }

    @Test
    void requiresIfVormundschaft() {
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
                .setSozialhilfebeitraege(false)
                .setVormundschaft(true)
        );
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.PERSON_KESB_ERNENNUNG);
    }

    @Test
    void requiresIfWohnsitz() {
        formular.setPersonInAusbildung(
            (PersonInAusbildung) new PersonInAusbildung()
                .setSozialhilfebeitraege(false)
                .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
        );
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.PERSON_MIETVERTRAG);
    }

    @Test
    void requiresIfQuellenbesteuert() {
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
                .setSozialhilfebeitraege(false)
                .setNationalitaet(Land.DE)
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
            new PersonInAusbildung()
                .setSozialhilfebeitraege(false)
                .setSozialhilfebeitraege(true)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_SOZIALHILFEBUDGET
        );
    }

    @Test
    void requiresIfGeschieden() {
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
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
            new PersonInAusbildung()
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
                new PersonInAusbildung()
                    .setSozialhilfebeitraege(false)
                    .setAdresse(
                        new Adresse()
                            .setPlz("3000")
                    )
            )
            .setElterns(new HashSet<>() {{
                add(new Eltern().setAdresse(new Adresse().setLand(Land.DE)));
            }});
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.PERSON_AUSWEIS);
    }

    @Test
    void requiresIfVermoegenVorjahr() {
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
                .setSozialhilfebeitraege(false)
                .setVermoegenVorjahr(1)
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PERSON_VERMOEGENSNACHWEIS_VORJAHR
        );
    }

    private PersonInAusbildung createWithNiederlassungsstatus(Niederlassungsstatus status) {
        return new PersonInAusbildung()
            .setSozialhilfebeitraege(false).setNiederlassungsstatus(status);
    }
}
