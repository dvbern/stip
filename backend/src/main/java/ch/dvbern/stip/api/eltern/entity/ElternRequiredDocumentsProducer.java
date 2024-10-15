package ch.dvbern.stip.api.eltern.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.steuerdaten.util.RequiredDocumentsProducerUtils;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ElternRequiredDocumentsProducer {

    private static final Map<ElternTyp, DokumentTyp> ERGAENZUNGSLEISTUNGEN_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_ERGAENZUNGSLEISTUNGEN_VATER
    );

    private static final Map<ElternTyp, DokumentTyp> SOZIALHILFEBUDGET_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_SOZIALHILFEBUDGET_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_SOZIALHILFEBUDGET_VATER
    );

    private static final Map<ElternTyp, DokumentTyp> WOHNKOSTEN_MAP = Map.of(
        ElternTyp.MUTTER, DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
        ElternTyp.VATER, DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
    );

    public Set<DokumentTyp> getForElternteil(final Eltern elternteil, final Familiensituation familiensituation) {
        if (elternteil == null) {
            return Set.of();
        }

        final var requiredDocs = new HashSet<DokumentTyp>();

        if(RequiredDocumentsProducerUtils.greaterThanZero(elternteil.getErgaenzungsleistungen())){
            requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(elternteil.getElternTyp()));
        }

        if(RequiredDocumentsProducerUtils.greaterThanZero(elternteil.getSozialhilfebeitraege())){
            requiredDocs.add(SOZIALHILFEBUDGET_MAP.get(elternteil.getElternTyp()));
        }

        if (RequiredDocumentsProducerUtils.greaterThanZero(elternteil.getWohnkosten())) {
            if (familiensituation != null && familiensituation.getElternVerheiratetZusammen()) {
                requiredDocs.add(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
            } else {
                requiredDocs.add(WOHNKOSTEN_MAP.get(elternteil.getElternTyp()));
            }
        }


        return requiredDocs;
    }
}

