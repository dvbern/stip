package ch.dvbern.stip.api.eltern.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.plz.service.PlzService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ElternRequiredDocumentsProducer {
    private final PlzService plzService;

    private static final Map<ElternTyp, DokumentTyp> STEUERUNTERLAGEN_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_STEUERUNTERLAGEN_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_STEUERUNTERLAGEN_VATER
    );

    private static final Map<ElternTyp, DokumentTyp> SOZIALHILFEBEITRAEGE_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_SOZIALHILFEBUDGET_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_SOZIALHILFEBUDGET_VATER
    );

    private static final Map<ElternTyp, DokumentTyp> ERGAENZUNGSLEISTUNGEN_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER
    );

    private static final Map<ElternTyp, DokumentTyp> AUSWEIS_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_LOHNABRECHNUNG_VERMOEGEN_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_LOHNABRECHNUNG_VERMOEGEN_VATER
    );

    public static final Map<ElternTyp, DokumentTyp> WOHNKOSTEN_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
    );

    public List<DokumentTyp> getForElternteil(final Eltern elternteil) {
        if (elternteil == null) {
            return List.of();
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();

        if (!plzService.isInBern(elternteil.getAdresse())) {
            requiredDocs.add(STEUERUNTERLAGEN_MAP.get(elternteil.getElternTyp()));
        }

        if (Boolean.TRUE.equals(elternteil.getSozialhilfebeitraegeAusbezahlt())) {
            requiredDocs.add(SOZIALHILFEBEITRAEGE_MAP.get(elternteil.getElternTyp()));
        }

        if (Boolean.TRUE.equals(elternteil.getErgaenzungsleistungAusbezahlt())) {
            requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(elternteil.getElternTyp()));
        }

        if (Boolean.TRUE.equals(elternteil.getAusweisbFluechtling())) {
            requiredDocs.add(AUSWEIS_MAP.get(elternteil.getElternTyp()));
        }

        if (elternteil.getWohnkosten() != null) {
            requiredDocs.add(WOHNKOSTEN_MAP.get(elternteil.getElternTyp()));
        }

        return requiredDocs;
    }
}
