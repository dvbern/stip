package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class SteuerdatenRequiredDocumentsProducer implements RequiredDocumentProducer {
    private static final Map<SteuerdatenTyp, DokumentTyp> SOZIALHILFEBETRAEGE_MAP = Map.of(
        SteuerdatenTyp.MUTTER, DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_MUTTER,
        SteuerdatenTyp.VATER, DokumentTyp.STEUERDATEN_SOZIALHILFEBUDGET_VATER
    );

    private static final Map<SteuerdatenTyp, DokumentTyp> ERGAENZUNGSLEISTUNGEN_MAP = Map.of(
        SteuerdatenTyp.MUTTER, DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_MUTTER,
        SteuerdatenTyp.VATER, DokumentTyp.STEUERDATEN_ERGAENZUNGSLEISTUNGEN_VATER
    );

    private static final Map<SteuerdatenTyp, DokumentTyp> WOHNKOSTEN_MAP = Map.of(
        SteuerdatenTyp.MUTTER, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
        SteuerdatenTyp.VATER, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER,
        SteuerdatenTyp.FAMILIE, DokumentTyp.STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE
    );

    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var steuerdaten = formular.getSteuerdaten();
        if (steuerdaten.isEmpty()) {
            return ImmutablePair.of("", List.of());
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();
        for (final var steuerTab : steuerdaten) {
            if (greaterThanZero(steuerTab.getWohnkosten())) {
                requiredDocs.add(WOHNKOSTEN_MAP.get(steuerTab.getSteuerdatenTyp()));
            }

            if (greaterThanZero(steuerTab.getErgaenzungsleistungen())) {
                if (steuerTab.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
                    requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(SteuerdatenTyp.VATER));
                } else {
                    requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(steuerTab.getSteuerdatenTyp()));
                }
            }

            if (greaterThanZero(steuerTab.getErgaenzungsleistungenPartner())) {
                requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(SteuerdatenTyp.MUTTER));
            }

            if (greaterThanZero(steuerTab.getSozialhilfebeitraege())) {
                if (steuerTab.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
                    requiredDocs.add(SOZIALHILFEBETRAEGE_MAP.get(SteuerdatenTyp.VATER));
                } else {
                    requiredDocs.add(SOZIALHILFEBETRAEGE_MAP.get(steuerTab.getSteuerdatenTyp()));
                }
            }

            if (greaterThanZero(steuerTab.getSozialhilfebeitraegePartner())) {
                requiredDocs.add(SOZIALHILFEBETRAEGE_MAP.get(SteuerdatenTyp.MUTTER));
            }
        }

        return ImmutablePair.of("steuerdaten", requiredDocs);
    }

    private boolean greaterThanZero(final Integer base) {
        return base != null && base > 0;
    }
}
