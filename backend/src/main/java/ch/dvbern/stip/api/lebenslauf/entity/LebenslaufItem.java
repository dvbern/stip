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
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@LebenslaufItemArtRequiredFieldsConstraint
@LebenslaufItemAusbildungBerufsbezeichnungConstraint
@LebenslaufItemAusbildungFachrichtungConstraint
@LebenslaufItemAusbildungTitelDesAbschlussesConstraint
@Entity
@Table(
    name = "lebenslauf_item",
    indexes = @Index(name = "IX_lebenslauf_item_mandant", columnList = "mandant")
)
@Getter
@Setter
public class LebenslaufItem extends AbstractMandantEntity {
    @Column(name = "bildungsart")
    @Enumerated(EnumType.STRING)
    private LebenslaufAusbildungsArt bildungsart;

    @NotNull
    @Column(name = "von", nullable = false)
    private LocalDate von;

    @NotNull
    @Column(name = "bis", nullable = false)
    private LocalDate bis;

    @Column(name = "taetigkeitsart")
    @Enumerated(EnumType.STRING)
    private Taetigkeitsart taetigkeitsart;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "taetigkeits_beschreibung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String taetigkeitsBeschreibung;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "berufsbezeichnung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String berufsbezeichnung;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "fachrichtung", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String fachrichtung;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "titel_des_abschlusses", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String titelDesAbschlusses;

    @Column(name = "ausbildung_abgeschlossen", nullable = false)
    private boolean ausbildungAbgeschlossen = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz", nullable = false)
    private WohnsitzKanton wohnsitz;

    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;
}
