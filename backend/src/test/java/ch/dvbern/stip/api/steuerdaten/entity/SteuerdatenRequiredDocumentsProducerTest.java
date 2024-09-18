package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.entity.ElternRequiredDocumentsProducer;
import ch.dvbern.stip.api.eltern.entity.MutterRequiredDocumentsProducer;
import ch.dvbern.stip.api.eltern.entity.VaterRequiredDocumentsProducer;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.entity.SteuerdatenTypRequiredDocumentsProducer.FamilieRequiredDocumentsProducer;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.RequiredDocsUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class SteuerdatenRequiredDocumentsProducerTest {
    private List<RequiredDocumentProducer> producers;
    private GesuchFormular formular;

    @BeforeEach
    void setup() {
        final var producer = new SteuerdatenRequiredDocumentsProducer();
        final var elternProducer = new ElternRequiredDocumentsProducer();
        producers = List.of(
            new FamilieRequiredDocumentsProducer(producer),
            new MutterRequiredDocumentsProducer(elternProducer),
            new VaterRequiredDocumentsProducer(elternProducer)
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

        assertTrue(required.isEmpty());


        // todo kstip 1409
        //RequiredDocsUtil.requiresOneAndType(mutter, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER);
        //RequiredDocsUtil.requiresOneAndType(vater, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER);
    }



     List<Pair<String, List<DokumentTyp>>> getRequiredDocuments(final GesuchFormular formular) {
        final var requiredTypes = new ArrayList<Pair<String, List<DokumentTyp>>>();
        for (final var producer : producers) {
            requiredTypes.add(producer.getRequiredDocuments(formular));
        }

        return requiredTypes.stream().filter(pair -> !pair.getRight().isEmpty()).toList();
    }
}
