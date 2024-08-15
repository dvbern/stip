package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SteuerdatenRequiredDocumentsProducerTest {
    private SteuerdatenRequiredDocumentsProducer producer;
    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        producer = new SteuerdatenRequiredDocumentsProducer();
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfWohnkostenFamilie() {
        formular.setSteuerdaten(Set.of(new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.FAMILIE).setWohnkosten(100)));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE
        );
    }

    @Test
    void requiresIfWohnkostenMutterVater() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                .setWohnkosten(100),
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                .setWohnkosten(100)
        ));

        RequiredDocsUtil.requiresCountAndTypes(
            2,
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
            DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
        );
    }

    @Test
    void familieRequiresIfErgaenzungsleistungen() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setErgaenzungsleistungen(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_VATER
        );
    }

    @Test
    void mutterIfErgaenzungsleistungen() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                .setErgaenzungsleistungen(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_MUTTER
        );
    }

    @Test
    void vaterIfErgaenzungsleistungen() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                .setErgaenzungsleistungen(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_VATER
        );
    }

    @Test
    void familieRequiresIfErgaenzungsleistungenPartner() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setErgaenzungsleistungenPartner(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_MUTTER
        );
    }

    @Test
    void familieRequiresIfBothErgaenzungsleistungen() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setErgaenzungsleistungen(100)
                .setErgaenzungsleistungenPartner(100)
        ));

        RequiredDocsUtil.requiresCountAndTypes(
            2,
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_VATER,
            DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_MUTTER
        );
    }

    @Test
    void familieRequiresIfSozialhilfebeitraege() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setSozialhilfebeitraege(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_VATER
        );
    }

    @Test
    void mutterIfSozialhilfebeitraege() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                .setSozialhilfebeitraege(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_MUTTER
        );
    }

    @Test
    void vaterIfSozialhilfebeitraege() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                .setSozialhilfebeitraege(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_VATER
        );
    }

    @Test
    void familieRequiresIfSozialhilfebeitraegePartner() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setSozialhilfebeitraegePartner(100)
        ));

        RequiredDocsUtil.requiresOneAndType(
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_MUTTER
        );
    }

    @Test
    void familieRequiresIfBothSozialhilfebeitraege() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setSozialhilfebeitraege(100)
                .setSozialhilfebeitraegePartner(100)
        ));

        RequiredDocsUtil.requiresCountAndTypes(
            2,
            producer.getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_VATER,
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_MUTTER
        );
    }
}
