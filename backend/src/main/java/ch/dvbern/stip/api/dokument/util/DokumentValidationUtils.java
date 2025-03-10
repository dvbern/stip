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

package ch.dvbern.stip.api.dokument.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.validation.RequiredCustomDocumentsProducer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DokumentValidationUtils {
    public List<String> getMissingCustomDocumentTypsByTranche(
        Instance<RequiredCustomDocumentsProducer> customProducers,
        GesuchTranche tranche
    ) {
        List<String> result = new ArrayList<>();
        final var required = DokumentValidationUtils.getRequiredCustomDokumentTypes(customProducers, tranche);
        final var existingByCustomDokumentTypId =
            DokumentValidationUtils.getExistingGesuchDokumentsOfCustomDokumentType(tranche.getGesuchFormular())
                .stream()
                .map(AbstractEntity::getId)
                .toList();

        required.forEach(req -> {
            if (!existingByCustomDokumentTypId.contains(req.getId())) {
                result.add(req.getType());
            }
        });
        return result;
    }

    public List<CustomDokumentTyp> getRequiredCustomDokumentTypes(
        Instance<RequiredCustomDocumentsProducer> customProducers,
        GesuchTranche tranche
    ) {

        ArrayList<CustomDokumentTyp> customDokumentTypes = new ArrayList<>();
        customProducers.stream()
            .map(producer -> producer.getRequiredDocuments(tranche))
            .forEach(pair -> {
                if (pair != null) {
                    customDokumentTypes.addAll(pair.getValue());
                }
            });

        return customDokumentTypes;
    }

    public List<CustomDokumentTyp> getExistingGesuchDokumentsOfCustomDokumentType(GesuchFormular formular) {
        return formular.getTranche()
            .getGesuchDokuments()
            .stream()
            .filter(
                gesuchDokument -> Objects.nonNull(gesuchDokument.getCustomDokumentTyp())
                && !gesuchDokument.getDokumente().isEmpty()
            )
            .map(GesuchDokument::getCustomDokumentTyp)
            .toList();
    }
}
