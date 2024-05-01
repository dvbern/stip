package ch.dvbern.stip.api.partner.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class PartnerRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var partner = formular.getPartner();
        if (partner == null) {
            return ImmutablePair.of("", List.of());
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();

        // if fahrkosten > 0
        if (partner.getFahrkosten() != null && partner.getFahrkosten() > 0) {
            requiredDocs.add(DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT);
        }

        if (partner.getJahreseinkommen() != null && partner.getJahreseinkommen() > 0) {
            requiredDocs.add(DokumentTyp.PARTNER_DOK);
        }

        return ImmutablePair.of("partner", requiredDocs);
    }
}
