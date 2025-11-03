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

package ch.dvbern.stip.api.nesko.service;

import java.math.BigDecimal;
import java.util.Objects;

import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.EffSatzType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GeschlechtType;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.MannFrauEffSatzType;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@UtilityClass
public class NeskoSteuerdatenMapper {
    public Steuerdaten updateFromNeskoSteuerdaten(
        Steuerdaten steuerdaten,
        GetSteuerdatenResponse getSteuerdatenResponse
    ) {
        var steuerdatenNesko = getSteuerdatenResponse.getSteuerdaten();

        steuerdaten.setTotalEinkuenfte(
            getMaxOrZeroFromEffSatzType(steuerdatenNesko.getTotalEinkuenfte())
        );
        steuerdaten.setEigenmietwert(
            Objects.requireNonNullElse(steuerdatenNesko.getMietwertKanton(), BigDecimal.ZERO).intValue()
        );

        // Default ist: steuerdatenNesko.isFrauErwerbstaetigkeitSUS == true
        // --> isArbeitsverhaeltnisSelbstaendig == false
        // Gemäss Spec isFrauErwerbstaetigkeitSUS:
        // - true=Unselbstaendige Erwerbstaetigkeit Frau
        // - false=Selbstaendige Erwerbstaetigkeit Frau
        boolean isArbeitsverhaeltnisSelbstaendig = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> !Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), true)
            || !Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), true);
            case MUTTER -> !Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), true);
            case VATER -> !Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), true);
        };
        steuerdaten.setIsArbeitsverhaeltnisSelbstaendig(isArbeitsverhaeltnisSelbstaendig);
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
            }
            if (Objects.nonNull(steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn())) {
                saeule2 += getMaxOrZeroFromEffSatzType(
                    steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn().getPersoenlicheBeitraegeSaeule2()
                );
            }
        }
        steuerdaten.setSaeule3a(saeule3a);
        steuerdaten.setSaeule2(saeule2);

        steuerdaten.setVermoegen(
            getZeroOrPositiveValue(getValueOrZeroFromEffSatzType(steuerdatenNesko.getSteuerbaresVermoegenKanton()))
        );

        steuerdaten.setSteuernKantonGemeinde(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragKanton(), BigDecimal.ZERO).intValue()
        );
        steuerdaten.setSteuernBund(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragBund(), BigDecimal.ZERO).intValue()
        );
        steuerdaten.setSteuerjahr(getSteuerdatenResponse.getSteuerjahr());
        steuerdaten.setVeranlagungsStatus(getSteuerdatenResponse.getSteuerdaten().getStatusVeranlagung().value());

        // If the requested NESKO Steuerdatentyp is MUTTER, then we reverse the Partner and main value assignment
        var geschlechter =
            steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.MUTTER ? Pair.of(GeschlechtType.FRAU, GeschlechtType.MANN)
                : Pair.of(GeschlechtType.MANN, GeschlechtType.FRAU);

        int fahrkosten = valueFromSatzType(steuerdatenNesko.getFahrkosten(), geschlechter.getLeft());
        steuerdaten.setFahrkosten(fahrkosten);

        int fahrkostenPartner = valueFromSatzType(steuerdatenNesko.getFahrkosten(), geschlechter.getRight());
        steuerdaten.setFahrkostenPartner(fahrkostenPartner);

        int verpflegung = valueFromSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung(), geschlechter.getLeft());
        steuerdaten.setVerpflegung(verpflegung);

        int verpflegungPartner =
            valueFromSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung(), geschlechter.getRight());
        steuerdaten.setVerpflegungPartner(verpflegungPartner);

        return steuerdaten;
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

    private int valueFromSatzType(MannFrauEffSatzType data, GeschlechtType geschlechtType) {
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
