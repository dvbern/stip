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

package ch.dvbern.stip.api.gesuchsjahr.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(
    name = "gesuchsjahr",
    indexes = @Index(name = "IX_gesuchsjahr_mandant", columnList = "mandant")
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gesuchsjahr extends AbstractMandantEntity {
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "bezeichnung_de", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungDe;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "bezeichnung_fr", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungFr;

    @Column(name = "technisches_jahr")
    private Integer technischesJahr;

    @Enumerated(EnumType.STRING)
    @Column(name = "gueltigkeit_status")
    private GueltigkeitStatus gueltigkeitStatus = GueltigkeitStatus.ENTWURF;
}
