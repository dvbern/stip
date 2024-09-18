package ch.dvbern.stip.api.steuerdaten.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.enterprise.context.ApplicationScoped;
import ch.dvbern.stip.api.steuerdaten.util.RequiredDocumentsProducerUtils;

@ApplicationScoped
public class SteuerdatenRequiredDocumentsProducer {

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
        if (RequiredDocumentsProducerUtils.greaterThanZero(steuerdaten.getWohnkosten())) {
            requiredDocs.add(WOHNKOSTEN_MAP.get(steuerdaten.getSteuerdatenTyp()));
        }

        return requiredDocs;
    }

}
