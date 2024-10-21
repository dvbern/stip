package ch.dvbern.stip.api.einnahmen_kosten.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class EinnahmenKostenRequiredDocumentsProducer implements RequiredDocumentProducer {

    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var ek = formular.getEinnahmenKosten();
        if (ek == null) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<DokumentTyp>();
        if (Boolean.TRUE.equals(ek.getVerdienstRealisiert())) {
            requiredDocs.add(DokumentTyp.EK_VERDIENST);
        }

        if (greaterThanZero(ek.getNettoerwerbseinkommen())) {
            requiredDocs.add(DokumentTyp.EK_LOHNABRECHNUNG);
        }

        if (greaterThanZero(ek.getBetreuungskostenKinder())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER);
        }

        if (greaterThanZero(ek.getWohnkosten())) {
            requiredDocs.add(DokumentTyp.EK_MIETVERTRAG);
        }

        if (greaterThanZero(ek.getFahrkosten())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_OV_ABONNEMENT);
        }

        if (greaterThanZero(ek.getEoLeistungen())) {
            requiredDocs.add(DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO);
        }

        if (greaterThanZero(ek.getRenten())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN);
        }

        if (greaterThanZero(ek.getBeitraege())) {
            requiredDocs.add(DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION);
        }

        if (greaterThanZero(ek.getZulagen())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_KINDERZULAGEN);
        }

        if (greaterThanZero(ek.getAlimente())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_ALIMENTE);
        }

        if (greaterThanZero(ek.getErgaenzungsleistungen())) {
            requiredDocs.add(DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN);
        }
        if (greaterThanZero(ek.getVermoegen())) {
            requiredDocs.add(DokumentTyp.EK_VERMOEGEN);
        }

        return ImmutablePair.of("einnahmenKosten", requiredDocs);
    }

    private boolean greaterThanZero(final Integer base) {
        return base != null && base > 0;
    }
}
