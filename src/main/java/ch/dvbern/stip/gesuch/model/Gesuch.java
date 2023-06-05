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

package ch.dvbern.stip.gesuch.model;

import ch.dvbern.stip.fall.model.Fall;
import ch.dvbern.stip.gesuchsperiode.model.Gesuchsperiode;
import ch.dvbern.stip.persistence.AbstractEntity;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildungContainer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Gesuch extends AbstractEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_fall_id"))
    private Fall fall;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_gesuchsperiode_id"))
    private Gesuchsperiode gesuchsperiode;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_container_id"), nullable = true)
    private PersonInAusbildungContainer personInAusbildungContainer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gesuchstatus gesuchStatus;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private int gesuchNummer = 0;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime gesuchStatusAenderungDatum = LocalDateTime.now();

    public Gesuchsperiode getGesuchsperiode() {
        return gesuchsperiode;
    }

    public void setGesuchsperiode(Gesuchsperiode gesuchsperiode) {
        this.gesuchsperiode = gesuchsperiode;
    }

    public PersonInAusbildungContainer getPersonInAusbildungContainer() {
        return personInAusbildungContainer;
    }

    public void setPersonInAusbildungContainer(PersonInAusbildungContainer personInAusbildungContainer) {
        this.personInAusbildungContainer = personInAusbildungContainer;
    }

    public Gesuchstatus getGesuchStatus() {
        return gesuchStatus;
    }

    public void setGesuchStatus(Gesuchstatus gesuchStatus) {
        this.gesuchStatus = gesuchStatus;
    }

    public int getGesuchNummer() {
        return gesuchNummer;
    }

    public void setGesuchNummer(int gesuchNummer) {
        this.gesuchNummer = gesuchNummer;
    }

    public LocalDateTime getGesuchStatusAenderungDatum() {
        return gesuchStatusAenderungDatum;
    }

    public void setGesuchStatusAenderungDatum(LocalDateTime gesuchStatusAenderungDatum) {
        this.gesuchStatusAenderungDatum = gesuchStatusAenderungDatum;
    }

    public Fall getFall() {
        return fall;
    }

    public void setFall(Fall fall) {
        this.fall = fall;
    }
}
