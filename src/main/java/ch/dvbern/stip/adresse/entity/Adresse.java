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

package ch.dvbern.stip.adresse.entity;

import ch.dvbern.stip.persistence.AbstractEntity;
import ch.dvbern.stip.stammdaten.entity.Land;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.shared.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.shared.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Entity
@Audited
public class Adresse extends AbstractEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Land land = Land.CH;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String coAdresse;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String strasse;

    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(nullable = true)
    private String hausnummer;

    @NotNull
    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(nullable = false)
    private String plz;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String ort;
    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public String getCoAdresse() {
        return coAdresse;
    }

    public void setCoAdresse(String coAdresse) {
        this.coAdresse = coAdresse;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }
}
