package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
@QuarkusTest
class ElternRequiredDocumentsProducerTest {
    private ElternRequiredDocumentsProducer producer;

    @Inject
    PlzService plzService;

    @BeforeEach
    void setup() {
        producer = new ElternRequiredDocumentsProducer(plzService);
    }

    @Test
    void requiresIfInBern() {
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createNotBern(ElternTyp.VATER)),
            DokumentTyp.ELTERN_STEUERUNTERLAGEN_VATER
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createNotBern(ElternTyp.MUTTER)),
            DokumentTyp.ELTERN_STEUERUNTERLAGEN_MUTTER
        );
    }

    @Test
    void requiresIfAusweisbFluechtling() {
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createAusweisbFluechtling(ElternTyp.VATER)),
            DokumentTyp.ELTERN_LOHNABRECHNUNG_VERMOEGEN_VATER
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createAusweisbFluechtling(ElternTyp.MUTTER)),
            DokumentTyp.ELTERN_LOHNABRECHNUNG_VERMOEGEN_MUTTER
        );
    }

    private Eltern createNotBern(final ElternTyp elternTyp) {
        return createWithTyp(elternTyp).setAdresse(new Adresse().setPlz("7000"));
    }

    private Eltern createAusweisbFluechtling(final ElternTyp elternTyp) {
        return setBernAdresse(createWithTyp(elternTyp).setAusweisbFluechtling(true));
    }

    private Eltern createWithTyp(final ElternTyp typ) {
        return new Eltern().setElternTyp(typ);
    }

    private Eltern setBernAdresse(final Eltern eltern) {
        return eltern.setAdresse(new Adresse().setPlz("3011"));
    }
}
