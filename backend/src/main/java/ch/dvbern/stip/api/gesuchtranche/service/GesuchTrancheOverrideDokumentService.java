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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheOverrideDokumentService {
    private final GesuchDokumentService gesuchDokumentService;

    private static final Set<DokumentTyp> DOKUMENTE_ON_JAHRESWERTE = Set.of(
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_FAMILIE,
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_VATER,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_FAMILIE,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_VATER,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_FAMILIE,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER,
        DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER,
        DokumentTyp.EK_LOHNABRECHNUNG,
        DokumentTyp.EK_BELEG_KINDERZULAGEN,
        DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
        DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
        DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION,
        DokumentTyp.EK_BELEG_EINNAHMEN_BGSA,
        DokumentTyp.EK_BELEG_TAGGELDER_AHV_IV,
        DokumentTyp.EK_BELEG_ANDERE_EINNAHMEN,
        DokumentTyp.EK_VERMOEGEN,
        DokumentTyp.EK_PARTNER_LOHNABRECHNUNG,
        DokumentTyp.EK_PARTNER_BELEG_KINDERZULAGEN,
        DokumentTyp.EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO,
        DokumentTyp.EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN,
        DokumentTyp.EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION,
        DokumentTyp.EK_PARTNER_BELEG_EINNAHMEN_BGSA,
        DokumentTyp.EK_PARTNER_BELEG_TAGGELDER_AHV_IV,
        DokumentTyp.EK_PARTNER_BELEG_ANDERE_EINNAHMEN,
        DokumentTyp.EK_PARTNER_VERMOEGEN
    );

    void overrideJahreswertDokumente(final Gesuch gesuch, final GesuchTranche newTranche) {
        final var targetTranchen = gesuch.getTranchenTranchen()
            .filter(tranche -> !tranche.getId().equals(newTranche.getId()))
            .toList();

        if (targetTranchen.isEmpty()) {
            return;
        }

        final var targetTranchenLUT = targetTranchen.stream()
            .collect(
                Collectors.toMap(
                    AbstractEntity::getId,
                    tranche -> tranche.getGesuchDokuments()
                        .stream()
                        .collect(Collectors.toMap(GesuchDokument::getDokumentTyp, gesuchDokument -> gesuchDokument))
                )
            );

        final BiConsumer<DokumentTyp, GesuchDokument> newDokumentFound = (jahreswertDokument, newDokument) -> {
            for (final var targetTranche : targetTranchen) {
                var oldDokument = targetTranchenLUT.get(targetTranche.getId()).get(jahreswertDokument);
                if (oldDokument == null) {
                    oldDokument = gesuchDokumentService.copyGesuchDokument(newDokument, targetTranche);
                } else {
                    oldDokument.getDokumente().clear();
                }

                gesuchDokumentService.copyDokumente(oldDokument, newDokument.getDokumente());
            }
        };

        final Consumer<DokumentTyp> noNewDokument = (jahreswertDokument) -> {
            for (final var targetTranche : targetTranchen) {
                // TODO construct a map of this;
                final var oldDokumente = targetTranche.getGesuchDokuments()
                    .stream()
                    .filter(gesuchDokument -> gesuchDokument.getDokumentTyp() == jahreswertDokument)
                    .toList();

                for (final var oldDokument : oldDokumente) {
                    oldDokument.getDokumente().clear();
                }
            }
        };

        final var newDokumente = newTranche.getGesuchDokuments()
            .stream()
            .filter(gesuchDokument -> gesuchDokument.getDokumentTyp() != null)
            .filter(gesuchDokument -> DOKUMENTE_ON_JAHRESWERTE.contains(gesuchDokument.getDokumentTyp()))
            .collect(Collectors.toMap(GesuchDokument::getDokumentTyp, gesuchDokument -> gesuchDokument));

        for (final var jahreswertDokument : DOKUMENTE_ON_JAHRESWERTE) {
            if (newDokumente.containsKey(jahreswertDokument)) {
                newDokumentFound.accept(jahreswertDokument, newDokumente.get(jahreswertDokument));
            } else {
                noNewDokument.accept(jahreswertDokument);
            }
        }
    }
}
