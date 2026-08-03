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

package ch.dvbern.stip.berechnung.adapter.bern.v1_0.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.berechnung.domain.model.BerechnungAdapterType;
import ch.dvbern.stip.berechnung.domain.port.BerechnungPort;
import ch.dvbern.stip.berechnung.domain.qualifier.BerechnungQualifier;
import ch.dvbern.stip.berechnung.domain.service.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.domain.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDtoBuilder;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
@BerechnungQualifier(
    type = BerechnungAdapterType.BERN,
    majorVersion = 1,
    minorVersion = 0
)
public class BernBerechnungAdapterV1_0 implements BerechnungPort {
    private final BerechnungsStammdatenMapper berechnungsStammdatenMapper;

    @Override
    public BerechnungsresultatDto getBerechnungsresultat(Gesuch gesuch) {
        final DateRange gesuchsDateRange = DateUtil.getGesuchDateRange(gesuch);
        final Gesuchsperiode gesuchsperiode = gesuch.getGesuchsperiode();
        final int gesuchsjahr = gesuch.getGesuchGueltigkeitAb().getYear();

        final List<GesuchTranche> gesuchTranchen = gesuch.getTranchenTranchen().toList();

        final List<TranchenBerechnungsresultatDto> berechnungsresultate = gesuchTranchen.stream()
            .flatMap(
                gesuchTranche -> TranchenBerechnungsresultatCalculator.getTranchenBerechnungsresultat(
                    gesuchTranche,
                    gesuchsDateRange,
                    gesuchsperiode,
                    gesuchsjahr,
                    berechnungsStammdatenMapper
                ).stream()
            )
            .toList();

        final int berechnungVorKuerzungUndTeilung =
            -berechnungsresultate.stream().mapToInt(TranchenBerechnungsresultatDto::getTotal).sum();

        final int monateMitDarlehen = getMonateMitDarlehen(gesuch);
        final Integer ungekuerztDarlehen = getDarlehen(berechnungVorKuerzungUndTeilung, monateMitDarlehen);
        final int ungekuerztStipendien =
            BerechnungUtil.subtractGesezlichesDarlehen(berechnungVorKuerzungUndTeilung, monateMitDarlehen);

        final int monateUebrigNachEinreichefrist = DateUtil.wasEingereichtAfterDueDate(gesuch)
            ? DateUtil.getStipendiumDurationRoundDown(gesuch)
            : 12;

        final Integer totalNachKuerzungNachEinreichefrist =
            monateUebrigNachEinreichefrist < 12
                ? BigDecimal.valueOf(berechnungVorKuerzungUndTeilung)
                    .multiply(BigDecimal.valueOf(monateUebrigNachEinreichefrist))
                    .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
                    .intValue()
                : null;

        final Integer totalVorKuerzungUnterbruch =
            Objects.requireNonNullElse(totalNachKuerzungNachEinreichefrist, berechnungVorKuerzungUndTeilung);

        final Integer anzahlMonateUnterbruch = gesuch.getAusbildung()
            .getAusbildungUnterbruchAntrags()
            .stream()
            .sorted(Comparator.comparing(AusbildungUnterbruchAntrag::getTimestampErstellt))
            .filter(ausbildungUnterbruchAntrag -> ausbildungUnterbruchAntrag.getGesuch().getId().equals(gesuch.getId()))
            .filter(
                ausbildungUnterbruchAntrag -> ausbildungUnterbruchAntrag
                    .getStatus() == AusbildungUnterbruchAntragStatus.AKZEPTIERT
            )
            .map(
                ausbildungUnterbruchAntrag -> Objects
                    .requireNonNullElse(ausbildungUnterbruchAntrag.getMonateOhneAnspruch(), 0)
            )
            .findFirst()
            .orElse(0);

        final Integer totalNachKuerzungUnterbruch =
            anzahlMonateUnterbruch > 0
                ? BigDecimal.valueOf(totalVorKuerzungUnterbruch)
                    .multiply(BigDecimal.valueOf(12 - anzahlMonateUnterbruch))
                    .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
                    .intValue()
                : null;

        int berechnungVorTeilungDarlehen =
            Objects.requireNonNullElse(totalNachKuerzungUnterbruch, totalVorKuerzungUnterbruch);

        if (berechnungVorTeilungDarlehen < gesuch.getGesuchsperiode().getStipLimiteMinimalstipendium()) {
            berechnungVorTeilungDarlehen = 0;
        }

        final Integer berechnungDarlehen = getDarlehen(berechnungVorTeilungDarlehen, monateMitDarlehen);
        final int berechnungStipendium =
            BerechnungUtil.subtractGesezlichesDarlehen(berechnungVorTeilungDarlehen, monateMitDarlehen);

        return BerechnungsresultatDtoBuilder.berechnungsresultatDto()
            .year(gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr())
            .berechnungVorKuerzungUndTeilung(berechnungVorKuerzungUndTeilung)
            .berechnungVorTeilungDarlehen(berechnungVorTeilungDarlehen)
            .berechnungStipendium(berechnungStipendium)
            .tranchenBerechnungsresultate(berechnungsresultate)
            .monateMitDarlehen(monateMitDarlehen)
            .ungekuerztStipendien(ungekuerztStipendien)
            .ungekuerztDarlehen(ungekuerztDarlehen)
            .totalNachKuerzungNachEinreichefrist(totalNachKuerzungNachEinreichefrist)
            .anzahlMonateEinreichefrist(12 - monateUebrigNachEinreichefrist)
            .totalNachKuerzungUnterbruch(totalNachKuerzungUnterbruch)
            .anzahlMonateUnterbruch(anzahlMonateUnterbruch)
            .berechnungDarlehen(berechnungDarlehen)
            .build();
    }

    private static Integer getDarlehen(final int stipendium, final int monateMitDarlehen) {
        if (monateMitDarlehen == 0) {
            return null;
        }

        final int darlehenFuer12MonateMitDarlehen = BerechnungUtil.calculateGesetzlichesDarlehen(stipendium);

        if (monateMitDarlehen == 12) {
            return darlehenFuer12MonateMitDarlehen;
        }
        return BerechnungUtil.roundGesetzlichesDarlehen(darlehenFuer12MonateMitDarlehen * monateMitDarlehen / 12);
    }

    public static int getMonateMitDarlehen(Gesuch gesuch) {
        final Ausbildung ausbildung = gesuch.getAusbildung();

        if (!ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie().isTertiaerstufe()) {
            return 0;
        }

        final int monateTertiaerstufeLebenslauf = gesuch.getLatestGesuchTranche()
            .getGesuchFormular()
            .getLebenslaufItems()
            .stream()
            .filter(
                item -> item.isAusbildung()
                && item.getAbschluss().getBildungskategorie().isTertiaerstufe()
            )
            .mapToInt(item -> DateUtil.getMonthsBetween(item.getVon(), item.getBis()))
            .sum();

        final LocalDate gesuchStartDate = gesuch.getEarliestGesuchTranche().getGueltigkeit().getGueltigAb();

        final int monateTertiaerstufeBisGesuchStart = monateTertiaerstufeLebenslauf + DateUtil.getMonthsBetween(
            ausbildung.getAusbildungBegin(),
            gesuchStartDate.atStartOfDay().toLocalDate()
        );

        final int monateTertiaerStufeBisGesuchEnde = monateTertiaerstufeBisGesuchStart + 12;

        if (monateTertiaerStufeBisGesuchEnde < BerechnungUtil.MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE) {
            return 0;
        }

        if (monateTertiaerstufeBisGesuchStart < BerechnungUtil.MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE) {
            final int monateMitDarlehen =
                monateTertiaerStufeBisGesuchEnde - BerechnungUtil.MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE;
            return monateMitDarlehen;
        }
        return 12;
    }
}
