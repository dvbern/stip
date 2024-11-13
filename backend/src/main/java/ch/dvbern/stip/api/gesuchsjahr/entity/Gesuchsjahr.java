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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

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
    @Column(name = "bezeichnung_de")
    private String bezeichnungDe;

    @Column(name = "bezeichnung_fr")
    private String bezeichnungFr;

    @Column(name = "technisches_jahr")
    private Integer technischesJahr;

    @Enumerated(EnumType.STRING)
    @Column(name = "gueltigkeit_status")
    private GueltigkeitStatus gueltigkeitStatus = GueltigkeitStatus.ENTWURF;
}
