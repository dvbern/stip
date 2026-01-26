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

package ch.dvbern.stip.api.gesuchtranche.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;

@Audited
@Entity
@Table(
    name = "gesuch_tranche",
    indexes = {
        @Index(name = "IX_gesuch_tranche_gesuch_id", columnList = "gesuch_id"),
        @Index(name = "IX_gesuch_tranche_gesuch_formular_id", columnList = "gesuch_formular_id"),
        @Index(name = "IX_gesuch_tranche_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class GesuchTranche extends AbstractMandantEntity {
    @Embedded
    private @Valid DateRange gueltigkeit = new DateRange();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(
        name = "gesuch_formular_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_formular_id"),
        nullable = false
    )
    private @Valid GesuchFormular gesuchFormular;

    @ManyToOne(
        optional = false,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH,
        },
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_id"), nullable = false)
    private Gesuch gesuch;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Audited(withModifiedFlag = true, modifiedColumnName = "status_mod")
    private GesuchTrancheStatus status = GesuchTrancheStatus.IN_BEARBEITUNG_GS;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @Column(name = "comment", length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String comment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuchTranche")
    private @Valid List<GesuchDokument> gesuchDokuments = new ArrayList<>();

    @NotNull
    @Column(name = "typ", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private GesuchTrancheTyp typ;
}
