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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BerechnungBlattPdfData<T> implements Serializable {

    @Valid
    @NotNull
    @JsonProperty("budget")
    private T budget;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("ungekuerztTotal")
    private Integer ungekuerztTotal;

    @JsonProperty("gueltigAb")
    private LocalDate gueltigAb;

    @JsonProperty("gueltigBis")
    private LocalDate gueltigBis;

    @JsonProperty("ausbildungAb")
    private String ausbildungAb;

    @JsonProperty("ausbildungBis")
    private String ausbildungBis;

    @JsonProperty("yearRange")
    private String yearRange;

    @JsonProperty("gesuchTrancheId")
    private UUID gesuchTrancheId;

    @JsonProperty("teilzeitKinderBeiPiaAnrechnen")
    private Boolean teilzeitKinderBeiPiaAnrechnen;

    @JsonProperty("berechnungsStammdaten")
    private BerechnungsStammdatenDto berechnungsStammdaten;

    @JsonProperty("berechnungsanteilKinder")
    private BigDecimal berechnungsanteilKinder;

    @JsonProperty("berechnungsanteilKinderPia")
    private BigDecimal berechnungsanteilKinderPia;

    public static <T> BerechnungBlattPdfData<T> of(
        final T budget,
        final TranchenBerechnungsresultatDto tranche
    ) {
        return BerechnungBlattPdfData.<T>builder()
            .budget(budget)
            .total(tranche.getTotal())
            .ungekuerztTotal(tranche.getUngekuerztTotal())
            .gueltigAb(tranche.getGueltigAb())
            .gueltigBis(tranche.getGueltigBis())
            .ausbildungAb(tranche.getAusbildungAb())
            .ausbildungBis(tranche.getAusbildungBis())
            .yearRange(tranche.getYearRange())
            .gesuchTrancheId(tranche.getGesuchTrancheId())
            .teilzeitKinderBeiPiaAnrechnen(tranche.getTeilzeitKinderBeiPiaAnrechnen())
            .berechnungsStammdaten(tranche.getBerechnungsStammdaten())
            .berechnungsanteilKinder(tranche.getBerechnungsanteilKinderDerEltern())
            .berechnungsanteilKinderPia(tranche.getBerechnungsanteilKinderPia())
            .build();
    }
}
