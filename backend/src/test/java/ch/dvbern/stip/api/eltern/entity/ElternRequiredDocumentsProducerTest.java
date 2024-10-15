package ch.dvbern.stip.api.eltern.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElternRequiredDocumentsProducerTest {
    private List<RequiredDocumentProducer> producers;
    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        final var elternProducer = new ElternRequiredDocumentsProducer();
        producers = List.of(
            new MutterRequiredDocumentsProducer(elternProducer),
            new VaterRequiredDocumentsProducer(elternProducer)
        );
        formular = new GesuchFormular();
    }

    @Test
    void mutterIfErgaenzungsleistungen() {
        formular.setElterns(Set.of(
            new Eltern().setElternTyp(ElternTyp.MUTTER)
                .setErgaenzungsleistungen(1)

        ));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER
        );
    }

    @Test
    void vaterIfErgaenzungsleistungen() {
        formular.setElterns(Set.of(
            new Eltern().setElternTyp(ElternTyp.VATER)
                .setErgaenzungsleistungen(1)
        ));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER
        );
    }

    @Test
    void mutterIfSozialhilfebeitraege() {
        formular.setElterns(Set.of(
            new Eltern().setElternTyp(ElternTyp.MUTTER)
                .setSozialhilfebeitraege(1)
        ));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_SOZIALHILFEBUDGET_MUTTER
        );
    }

    @Test
    void vaterIfSozialhilfebeitraege() {
        formular.setElterns(Set.of(
            new Eltern().setElternTyp(ElternTyp.VATER)
                .setSozialhilfebeitraege(1)
        ));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_SOZIALHILFEBUDGET_VATER
        );
    }

    @Test
    void wohnkostenRequired() {
        formular.setElterns(Set.of(
            new Eltern().setElternTyp(ElternTyp.VATER)
                .setWohnkosten(1)
        ));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
        );
    }

    @Test
    void familieWohnkostenRequired() {
        formular.setElterns(Set.of(
                new Eltern().setElternTyp(ElternTyp.VATER)
                    .setWohnkosten(1)
            ))
            .setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(true));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE
        );
    }

    @Test
    void vaterWohnkostenRequired() {
        formular.setElterns(Set.of(
                new Eltern().setElternTyp(ElternTyp.VATER)
                    .setWohnkosten(1)
            ))
            .setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(false));

        RequiredDocsUtil.requiresOneOfManyAndType(
            getRequiredDocuments(formular),
            DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
        );
    }

    List<Pair<String, Set<DokumentTyp>>> getRequiredDocuments(final GesuchFormular formular) {
        final var requiredTypes = new ArrayList<Pair<String, Set<DokumentTyp>>>();
        for (final var producer : producers) {
            requiredTypes.add(producer.getRequiredDocuments(formular));
        }

        return requiredTypes.stream().filter(pair -> !pair.getRight().isEmpty()).toList();
    }
}
