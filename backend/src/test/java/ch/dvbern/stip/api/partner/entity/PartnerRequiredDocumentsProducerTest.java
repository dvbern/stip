package ch.dvbern.stip.api.partner.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartnerRequiredDocumentsProducerTest {
    private PartnerRequiredDocumentsProducer producer;

    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new PartnerRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfFahrkosten() {
        formular.setPartner(
            new Partner()
                .setFahrkosten(BigDecimal.ONE)
        );

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT
        );
    }
}
