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

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.stipdecision.type.Kanton;
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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "verfuegung", indexes = {
        @Index(name = "IX_verfuegung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Verfuegung extends AbstractMandantEntity {
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

    @Column(name = "is_negative_verfuegung", nullable = false)
    private boolean isNegativeVerfuegung = false;

    @NotNull
    @Column(name = "is_versendet")
    private boolean isVersendet = false;

    @OneToMany(mappedBy = "verfuegung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VerfuegungDokument> dokumente = new ArrayList<>();
}
