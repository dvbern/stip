package ch.dvbern.stip.api.einnahmen_kosten.entity;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.EinnahmenKostenRequiredDocumentsProducer;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EinnahmenKostenRequiredDocumentsProducerTest {
    private EinnahmenKostenRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new EinnahmenKostenRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfVerdienstRealisiert() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setVerdienstRealisiert(true)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_VERDIENST);
    }

    @Test
    void requiresIfNettoerwerbseinkommen() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(1)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_LOHNABRECHNUNG);
    }

    @Test
    void requiresIfWohnkosten() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setWohnkosten(1)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_MIETVERTRAG);
    }

    @Test
    void requiresIfFahrkosten() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setFahrkosten(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_BELEG_OV_ABONNEMENT
        );
    }

    @Test
    void requiresIfEoLeistungen() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setEoLeistungen(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
        );
    }

    @Test
    void requiresIfRenten() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setRenten(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN
        );
    }

    @Test
    void requiresIfBeitraege() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setBeitraege(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION
        );
    }

    @Test
    void requiresIfZulagen() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setZulagen(1)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.EK_BELEG_KINDERZULAGEN
        );
    }

    @Test
    void requiresIfAlimente() {
        formular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setAlimente(1)
        );

        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_BELEG_ALIMENTE);
    }

    @Test
    void requiresIfVermoegen(){
        formular.setEinnahmenKosten(new EinnahmenKosten().setVermoegen(1000));
        RequiredDocsUtil.requiresOneAndType(producer.getRequiredDocuments(formular), DokumentTyp.EK_VERMOEGEN);

    }
}
