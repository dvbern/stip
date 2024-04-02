package ch.dvbern.stip.api.einnahmen_kosten.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class EinnahmenKostenRequiredDocumentsProducer implements RequiredDocumentProducer {

    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var ek = formular.getEinnahmenKosten();
        if (ek == null) {
            return ImmutablePair.of("", List.of());
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();
        if (Boolean.TRUE.equals(ek.getVerdienstRealisiert())) {
            requiredDocs.add(DokumentTyp.EK_VERDIENST);
        }

        if (greaterThanZero(ek.getNettoerwerbseinkommen())) {
            requiredDocs.add(DokumentTyp.EK_LOHNABRECHNUNG);
        }

        // TODO KSTIP-?: Betreuungskosten eigener Kinder

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
            // TODO KSTIP-170: Check with Severin
            requiredDocs.add(DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION);
        }

        if (greaterThanZero(ek.getZulagen())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_KINDERZULAGEN);
        }

        if (greaterThanZero(ek.getAlimente())) {
            requiredDocs.add(DokumentTyp.EK_BELEG_ALIMENTE);
        }

        return ImmutablePair.of("einnahmenKosten", requiredDocs);
    }

    private boolean greaterThanZero(final BigDecimal base) {
        return base != null && base.compareTo(BigDecimal.ZERO) > 0;
    }
}
