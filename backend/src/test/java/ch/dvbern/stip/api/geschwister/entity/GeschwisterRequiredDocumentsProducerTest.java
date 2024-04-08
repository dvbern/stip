package ch.dvbern.stip.api.geschwister.entity;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import org.junit.jupiter.api.BeforeEach;

class GeschwisterRequiredDocumentsProducerTest {
    private GeschwisterRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new GeschwisterRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    // TODO KSTIP-856: implement test
}
