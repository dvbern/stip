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
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class NeskoSteuerdatenMapper {
    private Integer getMaxOrZeroFromEffSatzType(EffSatzType effSatzType) {
        var effektiv = Objects.requireNonNullElse(effSatzType.getEffektiv(), BigDecimal.ZERO);
        var satzbestimmend = Objects.requireNonNullElse(effSatzType.getSatzbestimmend(), BigDecimal.ZERO);
        return effektiv.max(satzbestimmend).intValue();
    }

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
        boolean isArbeitsverhaeltnisSelbstaendig = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> !Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), false)
            || !Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), false);
            case MUTTER -> !Objects.requireNonNullElse(steuerdatenNesko.isFrauErwerbstaetigkeitSUS(), false);
            case VATER -> !Objects.requireNonNullElse(steuerdatenNesko.isMannErwerbstaetigkeitSUS(), false);
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
        steuerdaten.setKinderalimente(0); // TODO: Fix
        steuerdaten.setVermoegen(getMaxOrZeroFromEffSatzType(steuerdatenNesko.getSteuerbaresVermoegenKanton()));
        steuerdaten.setSteuernKantonGemeinde(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragKanton(), BigDecimal.ZERO).intValue()
        );
        steuerdaten.setSteuernBund(
            Objects.requireNonNullElse(steuerdatenNesko.getSteuerbetragBund(), BigDecimal.ZERO).intValue()
        );
        int fahrkosten = 0;
        int fahrkostenPartner = 0;
        int verpflegung = 0;
        int verpflegungPartner = 0;
        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten())) {
                if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getMann())) {
                    fahrkosten += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getFahrkosten().getMann());
                }
                if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getFrau())) {
                    fahrkosten += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getFahrkosten().getFrau());
                }
            }

            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung())) {
                if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann())) {
                    verpflegung +=
                        getMaxOrZeroFromEffSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann());
                }
                if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau())) {
                    verpflegung +=
                        getMaxOrZeroFromEffSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau());
                }
            }

        } else if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.VATER) {
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten())) {
                if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getMann())) {
                    fahrkosten += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getFahrkosten().getMann());
                }
                if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getFrau())) {
                    fahrkostenPartner += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getFahrkosten().getFrau());
                }
            }

            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung())) {
                if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann())) {
                    verpflegung +=
                        getMaxOrZeroFromEffSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann());
                }
                if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau())) {
                    verpflegungPartner +=
                        getMaxOrZeroFromEffSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau());
                }
            }

        } else if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.MUTTER) {
            if (Objects.nonNull(steuerdatenNesko.getFahrkosten())) {
                if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getFrau())) {
                    fahrkosten += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getFahrkosten().getFrau());
                }
                if (Objects.nonNull(steuerdatenNesko.getFahrkosten().getMann())) {
                    fahrkostenPartner += getMaxOrZeroFromEffSatzType(steuerdatenNesko.getFahrkosten().getMann());
                }
            }

            if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung())) {
                if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau())) {
                    verpflegung +=
                        getMaxOrZeroFromEffSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau());
                }
                if (Objects.nonNull(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann())) {
                    verpflegungPartner +=
                        getMaxOrZeroFromEffSatzType(steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann());
                }
            }
        }

        steuerdaten.setFahrkosten(fahrkosten);
        steuerdaten.setFahrkostenPartner(fahrkostenPartner);

        steuerdaten.setVerpflegung(verpflegung);
        steuerdaten.setVerpflegungPartner(verpflegungPartner);
        steuerdaten.setSteuerjahr(getSteuerdatenResponse.getSteuerjahr());
        steuerdaten.setVeranlagungsCode(0);
        return steuerdaten;
    }
}
