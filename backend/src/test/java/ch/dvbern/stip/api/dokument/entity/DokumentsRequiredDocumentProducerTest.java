package ch.dvbern.stip.api.dokument.entity;

import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DokumentsRequiredDocumentProducerTest {
    private DokumentsRequiredDocumentProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new DokumentsRequiredDocumentProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfPiaLedigWithChildren() {
        formular.setPersonInAusbildung(
            new PersonInAusbildung()
                .setZivilstand(Zivilstand.LEDIG)
        ).setKinds(
            Set.of(
                new Kind()
            )
        );

        final var requiredDocs = producer.getRequiredDocuments(formular);
        RequiredDocsUtil.requiresOneAndType(requiredDocs, DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION);
    }
}
