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

package ch.dvbern.stip.api.bildungskategorie.entity;

import ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Getter
@Setter
@Table(
    name = "bildungskategorie",
    indexes = {
        @Index(name = "IX_person_in_ausbildung_mandant", columnList = "mandant")
    }
)
@RequiredArgsConstructor
public class Bildungskategorie extends AbstractMandantEntity {
    // Bildungskategorien from BFS code 7 adn higher are of Bildungsstufe Tertiaer, lower are sekundaer_2
    private static final int MIN_TERTIAER_BFS = 7;
    // Bildunkskategorien with BFS code 5 are considered lehren
    public static final int LEHRE_BFS = 5;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "bezeichnung_de", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungDe;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "bezeichnung_fr", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungFr;

    @Column(name = "bfs", nullable = false)
    private int bfs;

    public Bildungsstufe getBildungsstufe() {
        return bfs >= MIN_TERTIAER_BFS ? Bildungsstufe.TERTIAER : Bildungsstufe.SEKUNDAR_2;
    }
}
