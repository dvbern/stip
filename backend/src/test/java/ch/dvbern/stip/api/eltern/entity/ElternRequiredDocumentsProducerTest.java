package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.service.PlzOrtService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElternRequiredDocumentsProducerTest {
    private ElternRequiredDocumentsProducer producer;

    @BeforeEach
    void setup() {
        producer = new ElternRequiredDocumentsProducer(new PlzOrtService());
    }

    @Test
    void requiresIfInBern() {
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createBern(ElternTyp.VATER)),
            DokumentTyp.ELTERN_STEUERUNTERLAGEN_VATER
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createBern(ElternTyp.MUTTER)),
            DokumentTyp.ELTERN_STEUERUNTERLAGEN_MUTTER
        );
    }

    @Test
    void requiresIfSozialhilfebeitraege() {
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createSozialhilfebeitraege(ElternTyp.VATER)),
            DokumentTyp.ELTERN_SOZIALHILFEBUDGET_VATER
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createSozialhilfebeitraege(ElternTyp.MUTTER)),
            DokumentTyp.ELTERN_SOZIALHILFEBUDGET_MUTTER
        );
    }

    @Test
    void requiresIfErgaenzungsleistung() {
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createErgaenzungsleistung(ElternTyp.VATER)),
            DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER
        );
        RequiredDocsUtil.requiresOneAndType(
            producer.getForElternteil(createErgaenzungsleistung(ElternTyp.MUTTER)),
            DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER
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

    private Eltern createBern(final ElternTyp elternTyp) {
        return createWithTyp(elternTyp).setAdresse(new Adresse().setPlz("3000"));
    }

    private Eltern createSozialhilfebeitraege(final ElternTyp elternTyp) {
        return createWithTyp(elternTyp).setSozialhilfebeitraegeAusbezahlt(true);
    }

    private Eltern createErgaenzungsleistung(final ElternTyp elternTyp) {
        return createWithTyp(elternTyp).setErgaenzungsleistungAusbezahlt(true);
    }

    private Eltern createAusweisbFluechtling(final ElternTyp elternTyp) {
        return createWithTyp(elternTyp).setAusweisbFluechtling(true);
    }

    private Eltern createWithTyp(final ElternTyp typ) {
        return new Eltern().setElternTyp(typ);
    }
}
