/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.partner.entity;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentsProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class PartnerRequiredDocumentsProducer implements RequiredDocumentsProducer {
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
