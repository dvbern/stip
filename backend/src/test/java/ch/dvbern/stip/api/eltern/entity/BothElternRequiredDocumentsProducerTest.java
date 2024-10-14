package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@QuarkusTest
class BothElternRequiredDocumentsProducerTest {

    @Inject
    RequiredDokumentService requiredDokumentService;

    @Test
    @Description("DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER should only appear once" +
        "in required documents")
    void vaterWohnkostenRequired() {
        GesuchTranche tranche = new GesuchTranche();
        GesuchFormular gesuchFormular = new GesuchFormular();
        gesuchFormular.setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(true));
        gesuchFormular.setElterns(Set.of(
            new Eltern().setElternTyp(ElternTyp.VATER)
                .setWohnkosten(1),
            new Eltern().setElternTyp(ElternTyp.MUTTER)
                .setWohnkosten(1)));
        tranche.setGesuchFormular(gesuchFormular);
        tranche.setGesuchDokuments(new ArrayList<>());
        gesuchFormular.setTranche(tranche);
        final var requiredDocuments = requiredDokumentService
            .getRequiredDokumentsForGesuchFormular(gesuchFormular);
        assertEquals(1, requiredDocuments.size());
    }
}
