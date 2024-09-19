package ch.dvbern.stip.api.eltern.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
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

    public List<DokumentTyp> getForElternteil(final Eltern elternteil) {
        if (elternteil == null) {
            return List.of();
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();

        if(RequiredDocumentsProducerUtils.greaterThanZero(elternteil.getErgaenzungsleistungen())){
            requiredDocs.add(ERGAENZUNGSLEISTUNGEN_MAP.get(elternteil.getElternTyp()));
        }

        if(RequiredDocumentsProducerUtils.greaterThanZero(elternteil.getSozialhilfebeitraege())){
            requiredDocs.add(SOZIALHILFEBUDGET_MAP.get(elternteil.getElternTyp()));
        }

        return requiredDocs;
    }
}

