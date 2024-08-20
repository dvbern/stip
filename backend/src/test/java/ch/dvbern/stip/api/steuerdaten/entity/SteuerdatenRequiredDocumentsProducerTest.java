package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenTypRequiredDocumentsProducer.FamilieRequiredDocumentsProducer;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenTypRequiredDocumentsProducer.MutterRequiredDocumentsProducer;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenTypRequiredDocumentsProducer.VaterRequiredDocumentsProducer;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SteuerdatenRequiredDocumentsProducerTest {
    private List<RequiredDocumentProducer> producers;
    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        final var producer = new SteuerdatenRequiredDocumentsProducer();
        producers = List.of(
            new FamilieRequiredDocumentsProducer(producer),
            new MutterRequiredDocumentsProducer(producer),
            new VaterRequiredDocumentsProducer(producer)
        );
        formular = new GesuchFormular();
    }

    @Test
    void requiresIfWohnkostenFamilie() {
        formular.setSteuerdaten(Set.of(new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.FAMILIE).setWohnkosten(100)));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        final var required = getRequiredDocuments(formular);
        final Function<String, Pair<String, List<DokumentTyp>>> getWhereProperty = (prop) -> required.stream()
            .filter(req -> req.getLeft().equals(prop))
            .findFirst()
            .orElse(null);

        final var mutter = getWhereProperty.apply("steuerdatenMutter");
        final var vater = getWhereProperty.apply("steuerdatenVater");

        RequiredDocsUtil.requiresOneAndType(mutter, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER);
        RequiredDocsUtil.requiresOneAndType(vater, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER);
    }

    @Test
    void familieRequiresIfErgaenzungsleistungen() {
        formular.setSteuerdaten(Set.of(
            new Steuerdaten()
                .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                .setErgaenzungsleistungen(100)
        ));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresFirstCountAndTypes(
            2,
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
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

        RequiredDocsUtil.requiresFirstCountAndTypes(
            2,
            getRequiredDocuments(formular),
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_VATER,
            DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_MUTTER
        );
    }

     List<Pair<String, List<DokumentTyp>>> getRequiredDocuments(final GesuchFormular formular) {
        final var requiredTypes = new ArrayList<Pair<String, List<DokumentTyp>>>();
        for (final var producer : producers) {
            requiredTypes.add(producer.getRequiredDocuments(formular));
        }

        return requiredTypes.stream().filter(pair -> !pair.getRight().isEmpty()).toList();
    }
}
