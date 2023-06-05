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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.personinausbildung.model;

import ch.dvbern.stip.persistence.AbstractEntity;

import jakarta.persistence.*;

@Entity
public class PersonInAusbildungContainer extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_container_person_in_ausbildunggs_id"), nullable = true)
    private PersonInAusbildung personInAusbildungGS;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_container_person_in_ausbildungsb_id"), nullable = true)
    private PersonInAusbildung personInAusbildungSB;
    public PersonInAusbildung getPersonInAusbildungGS() {
        return personInAusbildungGS;
    }

    public void setPersonInAusbildungGS(PersonInAusbildung personInAusbildungGS) {
        this.personInAusbildungGS = personInAusbildungGS;
    }

    public PersonInAusbildung getPersonInAusbildungSB() {
        return personInAusbildungSB;
    }

    public void setPersonInAusbildungSB(PersonInAusbildung personInAusbildungSB) {
        this.personInAusbildungSB = personInAusbildungSB;
    }
}
