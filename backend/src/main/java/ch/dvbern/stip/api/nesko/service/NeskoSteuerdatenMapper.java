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

import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.SteuerdatenType;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class NeskoSteuerdatenMapper {
    public Steuerdaten updateFromNeskoSteuerdaten(
        Steuerdaten steuerdaten,
        GetSteuerdatenResponse getSteuerdatenResponse
    ) {
        var steuerdatenNesko = getSteuerdatenResponse.getSteuerdaten();

        steuerdaten.setTotalEinkuenfte(
            Objects.requireNonNullElse(steuerdatenNesko.getTotalEinkuenfte().getEffektiv().intValue(), 0)
        );
        steuerdaten.setEigenmietwert(
            Objects.requireNonNullElse(steuerdatenNesko.getMietwertKanton(), BigDecimal.ZERO).intValue()
        );
        boolean isArbeitsverhaeltnisSelbstaendig = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), false)
            || Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), false);
            case MUTTER -> Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), false);
            case VATER -> Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), false);
        };
        steuerdaten.setIsArbeitsverhaeltnisSelbstaendig(isArbeitsverhaeltnisSelbstaendig);
        int saeule3a = 0;
        int saeule2 = 0;
        if (isArbeitsverhaeltnisSelbstaendig) {
            if (Objects.nonNull(steuerdatenNesko.getBeitraegeSaeule3A())) {
                if (Objects.nonNull(steuerdatenNesko.getBeitraegeSaeule3A().getMann())) {
                    saeule3a += Objects
                        .requireNonNullElse(
                            steuerdatenNesko.getBeitraegeSaeule3A().getMann().getEffektiv(),
                            BigDecimal.ZERO
                        )
                        .intValue();
                }
                if (Objects.nonNull(steuerdatenNesko.getBeitraegeSaeule3A().getFrau())) {
                    saeule3a += Objects
                        .requireNonNullElse(
                            steuerdatenNesko.getBeitraegeSaeule3A().getFrau().getEffektiv(),
                            BigDecimal.ZERO
                        )
                        .intValue();
                }
            }
            if (Objects.nonNull(steuerdatenNesko.getAufwaendeSelbstErwerbAngefragtePerson())) {
                saeule2 += Objects.requireNonNullElse(
                    steuerdatenNesko.getAufwaendeSelbstErwerbAngefragtePerson()
                        .getPersoenlicheBeitraegeSaeule2()
                        .getEffektiv(),
                    BigDecimal.ZERO
                )
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn())) {
                saeule2 += Objects.requireNonNullElse(
                    steuerdatenNesko.getAufwaendeSelbstErwerbEhepartnerIn()
                        .getPersoenlicheBeitraegeSaeule2()
                        .getEffektiv(),
                    BigDecimal.ZERO
                )
                    .intValue();
            }
        }
        steuerdaten.setSaeule3a(saeule3a);
        steuerdaten.setSaeule2(saeule2);
        steuerdaten.setKinderalimente(0); // TODO: Fix
        steuerdaten.setVermoegen(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbaresVermoegenKanton().getEffektiv(), BigDecimal.ZERO)
                .intValue()
        );
        steuerdaten.setSteuernKantonGemeinde(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragKanton(), BigDecimal.ZERO).intValue()
        );
        steuerdaten.setSteuernBund(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragBund(), BigDecimal.ZERO).intValue()
        );
        int[] steuerdatenKosten = getSteuerdatenKostenBySteuerdatenTyp(steuerdaten, steuerdatenNesko);
        int fahrkosten = steuerdatenKosten[0];
        int fahrkostenPartner = steuerdatenKosten[1];
        int verpflegung = steuerdatenKosten[2];
        int verpflegungPartner = steuerdatenKosten[3];

        steuerdaten.setFahrkosten(fahrkosten);
        steuerdaten.setFahrkostenPartner(fahrkostenPartner);

        steuerdaten.setVerpflegung(verpflegung);
        steuerdaten.setVerpflegungPartner(verpflegungPartner);
        steuerdaten.setSteuerjahr(getSteuerdatenResponse.getSteuerjahr());
        steuerdaten.setVeranlagungsCode(0);
        return steuerdaten;
    }

    private int[] getSteuerdatenKostenBySteuerdatenTyp(
        final Steuerdaten steuerdaten,
        final SteuerdatenType steuerdatenNesko
    ) {
        int fahrkosten = 0;
        int fahrkostenPartner = 0;
        int verpflegung = 0;
        int verpflegungPartner = 0;
        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getMann())) {
                fahrkosten += Objects
                    .requireNonNullElse(steuerdatenNesko.getFahrkosten().getMann().getEffektiv(), BigDecimal.ZERO)
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getFrau())) {
                fahrkosten += Objects
                    .requireNonNullElse(steuerdatenNesko.getFahrkosten().getFrau().getEffektiv(), BigDecimal.ZERO)
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann())) {
                verpflegung += Objects
                    .requireNonNullElse(
                        steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann().getEffektiv(),
                        BigDecimal.ZERO
                    )
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau())) {
                verpflegung += Objects
                    .requireNonNullElse(
                        steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau().getEffektiv(),
                        BigDecimal.ZERO
                    )
                    .intValue();
            }
        } else if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.VATER) {
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getMann())) {
                fahrkosten += Objects
                    .requireNonNullElse(steuerdatenNesko.getFahrkosten().getMann().getEffektiv(), BigDecimal.ZERO)
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getFrau())) {
                fahrkostenPartner += Objects
                    .requireNonNullElse(steuerdatenNesko.getFahrkosten().getFrau().getEffektiv(), BigDecimal.ZERO)
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann())) {
                verpflegung += Objects
                    .requireNonNullElse(
                        steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann().getEffektiv(),
                        BigDecimal.ZERO
                    )
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau())) {
                verpflegungPartner += Objects
                    .requireNonNullElse(
                        steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau().getEffektiv(),
                        BigDecimal.ZERO
                    )
                    .intValue();
            }
        } else if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.MUTTER) {
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getFrau())) {
                fahrkosten += Objects
                    .requireNonNullElse(steuerdatenNesko.getFahrkosten().getFrau().getEffektiv(), BigDecimal.ZERO)
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getMann())) {
                fahrkostenPartner += Objects
                    .requireNonNullElse(steuerdatenNesko.getFahrkosten().getMann().getEffektiv(), BigDecimal.ZERO)
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau())) {
                verpflegung += Objects
                    .requireNonNullElse(
                        steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau().getEffektiv(),
                        BigDecimal.ZERO
                    )
                    .intValue();
            }
            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann())) {
                verpflegungPartner += Objects
                    .requireNonNullElse(
                        steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann().getEffektiv(),
                        BigDecimal.ZERO
                    )
                    .intValue();
            }
        }
        return new int[] { fahrkosten, fahrkostenPartner, verpflegung, verpflegungPartner };
    }
}
