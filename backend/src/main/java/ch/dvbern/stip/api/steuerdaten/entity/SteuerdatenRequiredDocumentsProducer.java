package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SteuerdatenRequiredDocumentsProducer {
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

    public List<DokumentTyp> getForSteuerdaten(final Steuerdaten steuerdaten) {
        if (steuerdaten == null) {
            return List.of();
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();
        if (greaterThanZero(steuerdaten.getWohnkosten())) {
            requiredDocs.add(WOHNKOSTEN_MAP.get(steuerdaten.getSteuerdatenTyp()));
        }

        if (greaterThanZero(steuerdaten.getErgaenzungsleistungen())) {
            if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
                requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(SteuerdatenTyp.VATER));
            } else {
                requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(steuerdaten.getSteuerdatenTyp()));
            }
        }

        if (greaterThanZero(steuerdaten.getErgaenzungsleistungenPartner())) {
            requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(SteuerdatenTyp.MUTTER));
        }

        if (greaterThanZero(steuerdaten.getSozialhilfebeitraege())) {
            if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
                requiredDocs.add(SOZIALHILFEBETRAEGE_MAP.get(SteuerdatenTyp.VATER));
            } else {
                requiredDocs.add(SOZIALHILFEBETRAEGE_MAP.get(steuerdaten.getSteuerdatenTyp()));
            }
        }

        if (greaterThanZero(steuerdaten.getSozialhilfebeitraegePartner())) {
            requiredDocs.add(SOZIALHILFEBETRAEGE_MAP.get(SteuerdatenTyp.MUTTER));
        }

        return requiredDocs;
    }

    private boolean greaterThanZero(final Integer base) {
        return base != null && base > 0;
    }
}
