package ch.dvbern.stip.api.partner.entity;

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
public class PartnerRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var partner = formular.getPartner();
        if (partner == null) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<DokumentTyp>();

        // if fahrkosten > 0
        if (partner.getFahrkosten() != null && partner.getFahrkosten() > 0) {
            requiredDocs.add(DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT);
        }

        if (partner.getJahreseinkommen() != null && partner.getJahreseinkommen() > 0) {
            requiredDocs.add(DokumentTyp.PARTNER_AUSBILDUNG_LOHNABRECHNUNG);
        }

        return ImmutablePair.of("partner", requiredDocs);
    }
}
