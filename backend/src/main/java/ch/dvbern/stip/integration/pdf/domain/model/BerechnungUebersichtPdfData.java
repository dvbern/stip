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

package ch.dvbern.stip.integration.pdf.domain.model;

import java.util.ArrayList;

import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;

/**
 * Helper for building a stripped-down {@link BerechnungsresultatDto} used by the
 * "Übersicht" PDF template: same top-level values as the source, but each
 * tranche is emitted without {@code persoenlichesBudgetresultat} and with an
 * empty {@code familienBudgetresultate} list (those are rendered in separate PDFs).
 */
public final class BerechnungUebersichtPdfData {

    private BerechnungUebersichtPdfData() {}

    public static BerechnungsresultatDto from(final BerechnungsresultatDto source) {
        final var tranchen = source.getTranchenBerechnungsresultate()
            .stream()
            .map(BerechnungUebersichtPdfData::stripBudgets)
            .toList();

        return new BerechnungsresultatDto()
            .year(source.getYear())
            .berechnungVorKuerzungUndTeilung(source.getBerechnungVorKuerzungUndTeilung())
            .berechnungVorTeilungDarlehen(source.getBerechnungVorTeilungDarlehen())
            .berechnungStipendium(source.getBerechnungStipendium())
            .tranchenBerechnungsresultate(tranchen)
            .monateMitDarlehen(source.getMonateMitDarlehen())
            .ungekuerztStipendien(source.getUngekuerztStipendien())
            .ungekuerztDarlehen(source.getUngekuerztDarlehen())
            .totalNachKuerzungNachEinreichefrist(source.getTotalNachKuerzungNachEinreichefrist())
            .anzahlMonateEinreichefrist(source.getAnzahlMonateEinreichefrist())
            .totalNachKuerzungUnterbruch(source.getTotalNachKuerzungUnterbruch())
            .anzahlMonateUnterbruch(source.getAnzahlMonateUnterbruch())
            .berechnungDarlehen(source.getBerechnungDarlehen());
    }

    private static TranchenBerechnungsresultatDto stripBudgets(final TranchenBerechnungsresultatDto t) {
        return new TranchenBerechnungsresultatDto()
            .total(t.getTotal())
            .ungekuerztTotal(t.getUngekuerztTotal())
            .gueltigAb(t.getGueltigAb())
            .gueltigBis(t.getGueltigBis())
            .ausbildungAb(t.getAusbildungAb())
            .ausbildungBis(t.getAusbildungBis())
            .yearRange(t.getYearRange())
            .gesuchTrancheId(t.getGesuchTrancheId())
            .teilzeitKinderBeiPiaAnrechnen(t.getTeilzeitKinderBeiPiaAnrechnen())
            .berechnungsStammdaten(null)
            .persoenlichesBudgetresultat(null)
            .familienBudgetresultate(new ArrayList<>())
            .personenHaushaltGroups(new ArrayList<>())
            .berechnungsanteilKinder(t.getBerechnungsanteilKinder())
            .berechnungsanteilKinderPia(t.getBerechnungsanteilKinderPia());
    }
}
