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

package ch.dvbern.stip.api.ausbildung.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.annotation.Nullable;
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
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;

@AusbildungUnterbruchAntragRequiredFieldsConstraint
@AusbildungUnterbruchAntragGueltigkeitConstraint
@Audited
@Entity
@Table(
    name = "ausbildung_unterbruch_antrag",
    indexes = @Index(name = "IX_ausbildung_unterbruch_antrag_mandant", columnList = "mandant")
)
@Getter
@Setter
public class AusbildungUnterbruchAntrag extends AbstractMandantEntity {
    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AusbildungUnterbruchAntragStatus status = AusbildungUnterbruchAntragStatus.IN_BEARBEITUNG_GS;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "kommentar_gs", length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String kommentarGS;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "kommentar_sb", length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String kommentarSB;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(
        name = "ausbildung_unterbruch_antrag_id",
        foreignKey = @ForeignKey(name = "FK_ausbildung_unterbruch_antrag_dokument_id")
    )
    private Set<Dokument> dokuments = new LinkedHashSet<>();

    @Embedded
    private @Valid DateRange gueltigkeit = new DateRange();

    @Nullable
    @Column(name = "monate_ohne_anspruch")
    @Min(0)
    @Max(12)
    private Integer monateOhneAnspruch;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "gesuch_id",
        foreignKey = @ForeignKey(name = "FK_ausbildung_unterbruch_antrag_gesuch_id"),
        nullable = false
    )
    private Gesuch gesuch;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(
        name = "ausbildung_id", foreignKey = @ForeignKey(name = "FK_ausbildung_unterbruch_antrag_ausbildung_id")
    )
    private Ausbildung ausbildung;
}
