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

package ch.dvbern.stip.api.lebenslauf.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.service.NullableUnlessGenerated;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@LebenslaufItemArtRequiredFieldsConstraint
@LebenslaufItemAusbildungFachrichtungBerufsbezeichnungConstraint
@Entity
@Table(
    name = "lebenslauf_item",
    indexes = @Index(name = "IX_lebenslauf_item_mandant", columnList = "mandant")
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class LebenslaufItem extends AbstractMandantEntity {
    @NullableUnlessGenerated
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "abschluss_id", nullable = true, foreignKey = @ForeignKey(name = "FK_lebenslauf_item_abschluss_id")
    )
    private Abschluss abschluss;

    @NotNull
    @Column(name = "von", nullable = false)
    private LocalDate von;

    @NotNull
    @Column(name = "bis", nullable = false)
    private LocalDate bis;

    @Column(name = "taetigkeitsart")
    @Enumerated(EnumType.STRING)
    private Taetigkeitsart taetigkeitsart;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "taetigkeits_beschreibung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String taetigkeitsBeschreibung;

    @NullableUnlessGenerated
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "fachrichtung_berufsbezeichnung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String fachrichtungBerufsbezeichnung;

    @Column(name = "ausbildung_abgeschlossen", nullable = false)
    private boolean ausbildungAbgeschlossen = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz", nullable = false)
    private WohnsitzKanton wohnsitz;

    @Deprecated(forRemoval = true) // Not used anymore
    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;

    @Transient
    public boolean isAusbildung() {
        return Objects.nonNull(abschluss);
    }
}
