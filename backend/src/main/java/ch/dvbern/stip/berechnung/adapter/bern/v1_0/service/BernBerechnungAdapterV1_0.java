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
import java.util.Comparator;
import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.berechnung.domain.model.BerechnungAdapterType;
import ch.dvbern.stip.berechnung.domain.port.BerechnungPort;
import ch.dvbern.stip.berechnung.domain.qualifier.BerechnungQualifier;
import ch.dvbern.stip.berechnung.domain.service.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.domain.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
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
        if (Objects.isNull(gesuch.getEinreichedatum())) {
            throw new IllegalStateException("Berechnen of a Gesuch which has no Einreichedatum is not allowed");
        }

        final DateRange gesuchsDateRange = DateUtil.getGesuchDateRange(gesuch);
        final Gesuchsperiode gesuchsperiode = gesuch.getGesuchsperiode();
        final int gesuchsjahr = gesuch.getGesuchGueltigkeitAb().getYear();

        final var gesuchTranchen = gesuch.getTranchenTranchen().toList();

        final var berechnungsresultate = gesuchTranchen.stream()
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

        int berechnungVorKuerzungUndTeilung =
            -berechnungsresultate.stream().mapToInt(TranchenBerechnungsresultatDto::getTotal).sum();
        if (berechnungVorKuerzungUndTeilung < gesuch.getGesuchsperiode().getStipLimiteMinimalstipendium()) {
            berechnungVorKuerzungUndTeilung = 0;
        }
        final var monateMitDarlehen = getMonateMitDarlehen(gesuch);
        final Integer ungekuerztDarlehen = getDarlehen(berechnungVorKuerzungUndTeilung, monateMitDarlehen);
        final int ungekuerztStipendien =
            BerechnungUtil.subtractGesezlichesDarlehen(berechnungVorKuerzungUndTeilung, monateMitDarlehen);

        final var monateUebrigNachEinreichefrist = DateUtil.wasEingereichtAfterDueDate(gesuch)
            ? DateUtil.getStipendiumDurationRoundDown(gesuch)
            : 12;

        final var totalNachKuerzungNachEinreichefrist =
            monateUebrigNachEinreichefrist < 12
                ? BigDecimal.valueOf(berechnungVorKuerzungUndTeilung)
                    .multiply(BigDecimal.valueOf(monateUebrigNachEinreichefrist))
                    .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
                    .intValue()
                : null;

        final var totalVorKuerzungUnterbruch =
            Objects.requireNonNullElse(totalNachKuerzungNachEinreichefrist, berechnungVorKuerzungUndTeilung);

        final var anzahlMonateUnterbruch = gesuch.getAusbildung()
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

        final var totalNachKuerzungUnterbruch =
            anzahlMonateUnterbruch > 0
                ? BigDecimal.valueOf(totalVorKuerzungUnterbruch)
                    .multiply(BigDecimal.valueOf(12 - anzahlMonateUnterbruch))
                    .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
                    .intValue()
                : null;

        final int berechnungVorTeilungDarlehen =
            Objects.requireNonNullElse(totalNachKuerzungUnterbruch, totalVorKuerzungUnterbruch);

        final var berechnungDarlehen = getDarlehen(berechnungVorTeilungDarlehen, monateMitDarlehen);
        final var berechnungStipendium =
            BerechnungUtil.subtractGesezlichesDarlehen(berechnungVorTeilungDarlehen, monateMitDarlehen);

        return new BerechnungsresultatDto(
            gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr(),
            berechnungVorKuerzungUndTeilung,
            berechnungVorTeilungDarlehen,
            berechnungStipendium,
            berechnungsresultate,
            monateMitDarlehen,
            ungekuerztStipendien,
            ungekuerztDarlehen,
            totalNachKuerzungNachEinreichefrist,
            12 - monateUebrigNachEinreichefrist,
            totalNachKuerzungUnterbruch,
            anzahlMonateUnterbruch,
            berechnungDarlehen
        );
    }

    private static Integer getDarlehen(final int stipendium, final int monateMitDarlehen) {
        if (monateMitDarlehen == 0) {
            return null;
        }

        final var darlehenFuer12MonateMitDarlehen = BerechnungUtil.calculateGesetzlichesDarlehen(stipendium);

        if (monateMitDarlehen == 12) {
            return darlehenFuer12MonateMitDarlehen;
        }
        return BerechnungUtil.roundGesetzlichesDarlehen(darlehenFuer12MonateMitDarlehen * monateMitDarlehen / 12);
    }

    public static int getMonateMitDarlehen(Gesuch gesuch) {
        final var ausbildung = gesuch.getAusbildung();

        if (!ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie().isTertiaerstufe()) {
            return 0;
        }

        int monateTertiaerstufeLebenslauf = 0;

        for (var item : gesuch.getLatestGesuchTranche().getGesuchFormular().getLebenslaufItems()) {
            if (
                item.isAusbildung()
                && item.getAbschluss().getBildungskategorie().isTertiaerstufe()
            ) {
                monateTertiaerstufeLebenslauf += DateUtil.getMonthsBetween(item.getVon(), item.getBis());
            }
        }

        final var gesuchStartDate = gesuch.getEarliestGesuchTranche().getGueltigkeit().getGueltigAb();

        final var monateTertiaerstufeBisGesuchStart = monateTertiaerstufeLebenslauf + DateUtil.getMonthsBetween(
            ausbildung.getAusbildungBegin(),
            gesuchStartDate.atStartOfDay().toLocalDate()
        );

        final var monateTertiaerStufeBisGesuchEnde = monateTertiaerstufeBisGesuchStart + 12;

        if (monateTertiaerStufeBisGesuchEnde < BerechnungUtil.MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE) {
            return 0;
        }

        if (monateTertiaerstufeBisGesuchStart < BerechnungUtil.MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE) {
            final var monateMitDarlehen =
                monateTertiaerStufeBisGesuchEnde - BerechnungUtil.MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE;
            return monateMitDarlehen;
        }
        return 12;
    }
}
