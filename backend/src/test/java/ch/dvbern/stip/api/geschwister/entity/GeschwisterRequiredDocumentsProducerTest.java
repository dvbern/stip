package ch.dvbern.stip.api.geschwister.entity;

import java.util.HashSet;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeschwisterRequiredDocumentsProducerTest {
    private GeschwisterRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new GeschwisterRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfGeschwisterInAusbildung() {
        formular.setGeschwisters(new HashSet<>() {{
            add(new Geschwister().setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG));
        }});

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
    }
}
