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

package ch.dvbern.stip.api.dokument.entity;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentProducer;
import ch.dvbern.stip.api.dokument.service.CustomDokumentTypService;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomDocumentsRequiredDocumentProducer implements RequiredCustomDocumentProducer {
    private final CustomDokumentTypService customDokumentTypService;

    @Override
    public Pair<String, Set<CustomDokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var allCustomDokumentTyps = customDokumentTypService.getAllCustomDokumentTyps();
        if (allCustomDokumentTyps.isEmpty()) {
            return ImmutablePair.of("", Set.of());
        }
        Set<CustomDokumentTyp> requiredDocuments = new HashSet<>();
        allCustomDokumentTyps.forEach(requiredDocuments::add);
        return ImmutablePair.of("custom-documents", requiredDocuments);
    }
}
