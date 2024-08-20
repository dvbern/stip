package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class SteuerdatenTypRequiredDocumentsProducer {
    private static @Nullable Steuerdaten getForTyp(final Set<Steuerdaten> steuerdatenSet, final SteuerdatenTyp typ) {
        return steuerdatenSet.stream()
            .filter(steuerdaten -> steuerdaten.getSteuerdatenTyp() == typ)
            .findFirst()
            .orElse(null);
    }

    private static boolean nullOrEmpty(final Set<Steuerdaten> steuerdatenSet) {
        return steuerdatenSet == null || steuerdatenSet.isEmpty();
    }

    @ApplicationScoped
    @RequiredArgsConstructor
    public static class MutterRequiredDocumentsProducer implements RequiredDocumentProducer {
        private final SteuerdatenRequiredDocumentsProducer producer;

        @Override
        public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            final var steuerdatenSet = formular.getSteuerdaten();
            if (nullOrEmpty(steuerdatenSet)) {
                return ImmutablePair.of("", List.of());
            }

            final var mutterSteuerdaten = getForTyp(steuerdatenSet, SteuerdatenTyp.MUTTER);
            return ImmutablePair.of("steuerdatenMutter", producer.getForSteuerdaten(mutterSteuerdaten));
        }
    }

    @ApplicationScoped
    @RequiredArgsConstructor
    public static class VaterRequiredDocumentsProducer implements RequiredDocumentProducer {
        private final SteuerdatenRequiredDocumentsProducer producer;

        @Override
        public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            final var steuerdatenSet = formular.getSteuerdaten();
            if (nullOrEmpty(steuerdatenSet)) {
                return ImmutablePair.of("", List.of());
            }

            final var mutterSteuerdaten = getForTyp(steuerdatenSet, SteuerdatenTyp.VATER);
            return ImmutablePair.of("steuerdatenVater", producer.getForSteuerdaten(mutterSteuerdaten));
        }
    }

    @ApplicationScoped
    @RequiredArgsConstructor
    public static class FamilieRequiredDocumentsProducer implements RequiredDocumentProducer {
        private final SteuerdatenRequiredDocumentsProducer producer;

        @Override
        public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
            final var steuerdatenSet = formular.getSteuerdaten();
            if (nullOrEmpty(steuerdatenSet)) {
                return ImmutablePair.of("", List.of());
            }

            final var mutterSteuerdaten = getForTyp(steuerdatenSet, SteuerdatenTyp.FAMILIE);
            return ImmutablePair.of("steuerdaten", producer.getForSteuerdaten(mutterSteuerdaten));
        }
    }


}
