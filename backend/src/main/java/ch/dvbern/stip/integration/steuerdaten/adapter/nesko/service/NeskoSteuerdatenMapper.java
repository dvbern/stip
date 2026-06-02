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

package ch.dvbern.stip.integration.steuerdaten.adapter.nesko.service;

import java.math.BigDecimal;
import java.util.Objects;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GeschlechtType;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.MannFrauEffSatzType;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenPortData;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
public class NeskoSteuerdatenMapper {
    public SteuerdatenPortData toSteuerdatenPortData(
        GetSteuerdatenResponse getSteuerdatenResponse,
        SteuerdatenTyp steuerdatenTyp
    ) {
        var steuerdatenNesko = getSteuerdatenResponse.getSteuerdaten();
        var portData = SteuerdatenPortData.builder();

        portData.totalEinkuenfte(
            getMaxOrZeroFromEffSatzType(steuerdatenNesko.getTotalEinkuenfte())
        );
        portData.eigenmietwert(
            Objects.requireNonNullElse(steuerdatenNesko.getMietwertKanton(), BigDecimal.ZERO).intValue()
        );

        // Default ist: steuerdatenNesko.isFrauErwerbstaetigkeitSUS == true
        // --> isArbeitsverhaeltnisSelbstaendig == false
        // Gemäss Spec isFrauErwerbstaetigkeitSUS:
        // - true=Unselbstaendige Erwerbstaetigkeit Frau
        // - false=Selbstaendige Erwerbstaetigkeit Frau
        boolean isArbeitsverhaeltnisSelbstaendig = switch (steuerdatenTyp) {
            case FAMILIE -> !Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), true)
            || !Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), true);
            case MUTTER -> !Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), true);
            case VATER -> !Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), true);
        };
        portData.isArbeitsverhaeltnisSelbstaendig(isArbeitsverhaeltnisSelbstaendig);

        int saeule3a = 0;
        int saeule2 = 0;
        if (isArbeitsverhaeltnisSelbstaendig) {
            if (Objects.nonNull(steuerdatenNesko.getBeitraegeSaeule3A())) {
                if (Objects.nonNull(steuerdatenNesko.getBeitraegeSaeule3A().getMann())) {
                    saeule3a += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getBeitraegeSaeule3A().getMann());
                }
                if (Objects.nonNull(steuerdatenNesko.getBeitraegeSaeule3A().getFrau())) {
                    saeule3a += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getBeitraegeSaeule3A().getFrau());
                }
            }
            if (Objects.nonNull(steuerdatenNesko.getAufwaendeSelbstErwerbAngefragtePerson())) {
                saeule2 += getMaxOrZeroFromEffSatzType(
                    steuerdatenNesko.getAufwaendeSelbstErwerbAngefragtePerson().getPersoenlicheBeitraegeSaeule2()
                );
                saeule2 -= getMaxOrZeroFromEffSatzType(
                    steuerdatenNesko.getAufwaendeSelbstErwerbAngefragtePerson().getERBelasteteAnteileSaeule2()
                );
            }
            if (Objects.nonNull(steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn())) {
                saeule2 += getMaxOrZeroFromEffSatzType(
                    steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn().getPersoenlicheBeitraegeSaeule2()
                );
                saeule2 -= getMaxOrZeroFromEffSatzType(
                    steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn().getERBelasteteAnteileSaeule2()
                );
            }
        }
        portData.saeule3a(saeule3a);
        portData.saeule2(saeule2);

        portData.vermoegen(
            getZeroOrPositiveValue(getValueOrZeroFromEffSatzType(steuerdatenNesko.getSteuerbaresVermoegenKanton()))
        );

        portData.steuernKantonGemeinde(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragKanton(), BigDecimal.ZERO).intValue()
        );
        portData.steuernBund(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragBund(), BigDecimal.ZERO).intValue()
        );
        portData.steuerJahr(getSteuerdatenResponse.getSteuerjahr());
        portData.veranlagungsStatus(getSteuerdatenResponse.getSteuerdaten().getStatusVeranlagung().value());

        var geschlechter =
            steuerdatenTyp == SteuerdatenTyp.MUTTER ? Pair.of(GeschlechtType.FRAU, GeschlechtType.MANN)
                : Pair.of(GeschlechtType.MANN, GeschlechtType.FRAU);

        int fahrkosten = valueFromSatzType(steuerdatenNesko.getFahrkosten(), geschlechter.getLeft());
        portData.fahrkosten(fahrkosten);

        int fahrkostenPartner = valueFromSatzType(steuerdatenNesko.getFahrkosten(), geschlechter.getRight());
        portData.fahrkostenPartner(fahrkostenPartner);

        int verpflegung = valueFromSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung(), geschlechter.getLeft());
        portData.verpflegung(verpflegung);

        int verpflegungPartner =
            valueFromSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung(), geschlechter.getRight());
        portData.verpflegungPartner(verpflegungPartner);

        return portData.build();
    }

    private Integer getZeroOrPositiveValue(final Integer value) {
        if (value > 0) {
            return value;
        }
        return 0;
    }

    private Integer getValueOrZeroFromEffSatzType(final EffSatzType effSatzType) {
        var effektiv = Objects.requireNonNullElse(effSatzType.getEffektiv(), BigDecimal.ZERO);
        var satzbestimmend = Objects.requireNonNullElse(effSatzType.getSatzbestimmend(), BigDecimal.ZERO);
        return effektiv.max(satzbestimmend).intValue();
    }

    private Integer getMaxOrZeroFromEffSatzType(EffSatzType effSatzType) {
        var effektiv = Objects.requireNonNullElse(effSatzType.getEffektiv(), BigDecimal.ZERO).abs();
        var satzbestimmend = Objects.requireNonNullElse(effSatzType.getSatzbestimmend(), BigDecimal.ZERO).abs();
        return effektiv.max(satzbestimmend).intValue();
    }

    private Integer valueFromSatzType(MannFrauEffSatzType data, GeschlechtType geschlechtType) {
        if (Objects.isNull(data)) {
            return 0;
        }
        final var value = switch (geschlechtType) {
            case MANN, NEUTRAL -> data.getMann();
            case FRAU -> data.getFrau();
        };
        if (Objects.isNull(value)) {
            return 0;
        }

        return getMaxOrZeroFromEffSatzType(value);
    }
}
