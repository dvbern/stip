package ch.dvbern.stip.api.familiensituation.entity;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamiliensituationRequiredDocumentsProducerTest {
    private FamiliensituationRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new FamiliensituationRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfAlimentenregelung() {
        formular.setFamiliensituation(
            new Familiensituation()
                .setGerichtlicheAlimentenregelung(true)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_ALIMENTENREGELUNG
        );
    }

    @Test
    void requiresIfElternteilUnbekannterAufenthaltsort() {
        formular.setFamiliensituation(
            new Familiensituation()
                .setVaterUnbekanntGrund(ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER
        );

        formular.setFamiliensituation(
            new Familiensituation()
                .setMutterUnbekanntGrund(ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER
        );
    }

    @Test
    void requiresIfVaterschaftsanerkennung() {
        formular.setFamiliensituation(
            new Familiensituation()
                .setVaterUnbekanntGrund(ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.FAMILIENSITUATION_VATERSCHAFTSANERKENNUNG
        );
    }
}
