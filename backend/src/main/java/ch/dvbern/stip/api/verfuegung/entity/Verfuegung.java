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

package ch.dvbern.stip.api.verfuegung.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractTenantEntity;
import ch.dvbern.stip.api.common.type.Kanton;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "verfuegung", indexes = {
        @Index(name = "IX_verfuegung_tenant", columnList = "tenant")
    }
)
@Getter
@Setter
public class Verfuegung extends AbstractTenantEntity {
    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "stip_decision")
    private StipDecision stipDecision;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_id", nullable = false)
    private Gesuch gesuch;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz_kanton")
    private Kanton kanton;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Audited(withModifiedFlag = true, modifiedColumnName = "verfuegung_status_mod")
    @Column(name = "verfuegung_status", nullable = false)
    private VerfuegungStatus verfuegungStatus = VerfuegungStatus.AUSSTEHEND;

    @NotNull
    @Column(name = "is_versendet")
    private boolean isVersendet = false;

    @OneToMany(mappedBy = "verfuegung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VerfuegungDokument> dokumente = new ArrayList<>();

    @Nullable
    @Column(columnDefinition = "text", name = "berechnung_json_data", nullable = true)
    private String berechnungJsonData;

    @Transient
    public BerechnungsresultatDto parseBerechnungData() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(berechnungJsonData, BerechnungsresultatDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error parsing stored berechnungsresultat json data", e);
        }
    }
}
