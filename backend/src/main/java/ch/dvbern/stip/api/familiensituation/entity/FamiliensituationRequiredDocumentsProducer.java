package ch.dvbern.stip.api.familiensituation.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class FamiliensituationRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var famsit = formular.getFamiliensituation();
        if (famsit == null) {
            return ImmutablePair.of("", List.of());
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();
        if (Boolean.TRUE.equals(famsit.getGerichtlicheAlimentenregelung())) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_ALIMENTENREGELUNG);
        }

        if (famsit.getMutterUnbekanntGrund() == ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER);
        }

        if (famsit.getVaterUnbekanntGrund() == ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER);
        }

        if (famsit.getVaterUnbekanntGrund() == ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG) {
            requiredDocs.add(DokumentTyp.FAMILIENSITUATION_VATERSCHAFTSANERKENNUNG);
        }

        return ImmutablePair.of("familiensituation", requiredDocs);
    }
}
