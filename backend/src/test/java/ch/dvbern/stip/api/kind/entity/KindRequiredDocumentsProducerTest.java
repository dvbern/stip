package ch.dvbern.stip.api.kind.entity;

import java.math.BigDecimal;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KindRequiredDocumentsProducerTest {
    private KindRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new KindRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfAlimentebeitraege() {
        formular.setKinds(Set.of(
            new Kind()
                .setErhalteneAlimentebeitraege(BigDecimal.ONE)
        ));

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_ALIMENTENVERORDUNG);
    }
}
