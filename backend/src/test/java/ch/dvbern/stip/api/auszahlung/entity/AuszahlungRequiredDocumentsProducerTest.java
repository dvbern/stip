package ch.dvbern.stip.api.auszahlung.entity;

import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuszahlungRequiredDocumentsProducerTest {
    private AuszahlungRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new AuszahlungRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfKontoinhaberSozialdienst() {
        formular.setAuszahlung(
            new Auszahlung()
                .setKontoinhaber(Kontoinhaber.SOZIALDIENST_INSTITUTION)
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertType(requiredDocs, DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
    }

    @Test
    void requiresIfKontoinhaberAndere() {
        formular.setAuszahlung(
            new Auszahlung()
                .setKontoinhaber(Kontoinhaber.ANDERE)
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.assertCount(requiredDocs, 1);
        RequiredDocsUtil.assertType(requiredDocs, DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
    }
}
