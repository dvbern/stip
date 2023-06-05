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

import ch.dvbern.stip.adresse.model.Adresse;
import ch.dvbern.stip.persistence.AbstractEntity;
import ch.dvbern.stip.shared.enums.Anrede;
import ch.dvbern.stip.shared.enums.Land;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static ch.dvbern.stip.shared.util.Constants.*;

@Entity
public class PersonInAusbildung extends AbstractEntity {

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_adresse_id"), nullable = false)
    private Adresse adresse;
    @NotNull
    @Size(max = DB_DEFAULT_SHORT_LENGTH)
    @Column(nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String vorname;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Anrede anrede;

    @NotNull
    @Column(nullable = false)
    private boolean identischerZivilrechtlicherWohnsitz = true;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String izvOrt;

    @Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
    @Column(nullable = true)
    private String izvPLZ;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String email;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String telefonnummer;

    @NotNull
    @Column(nullable = false)
    private LocalDate geburtsdatum;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Land nationalitaet = Land.CH;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = true)
    private String heimatort;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Niederlassungsstatus niederlassungsstatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Zivilstand zivilstand;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wohnsitz wohnsitz;

    @NotNull
    @Column(nullable = false)
    private boolean sozialhilfebeitraege = true;

    @NotNull
    @Column(nullable = false)
    private boolean quellenbesteuert = true;

    @NotNull
    @Column(nullable = false)
    private boolean kinder = true;

    @NotNull
    @Column(nullable = false)
    private boolean digitaleKommunikation = true;

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getSozialversicherungsnummer() {
        return sozialversicherungsnummer;
    }

    public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
        this.sozialversicherungsnummer = sozialversicherungsnummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public Anrede getAnrede() {
        return anrede;
    }

    public void setAnrede(Anrede anrede) {
        this.anrede = anrede;
    }

    public boolean isIdentischerZivilrechtlicherWohnsitz() {
        return identischerZivilrechtlicherWohnsitz;
    }

    public void setIdentischerZivilrechtlicherWohnsitz(boolean identischerZivilrechtlicherWohnsitz) {
        this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
    }

    public String getIzvOrt() {
        return izvOrt;
    }

    public void setIzvOrt(String izvOrt) {
        this.izvOrt = izvOrt;
    }

    public String getIzvPLZ() {
        return izvPLZ;
    }

    public void setIzvPLZ(String izvPLZ) {
        this.izvPLZ = izvPLZ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Land getNationalitaet() {
        return nationalitaet;
    }

    public void setNationalitaet(Land nationalitaet) {
        this.nationalitaet = nationalitaet;
    }

    public String getHeimatort() {
        return heimatort;
    }

    public void setHeimatort(String heimatort) {
        this.heimatort = heimatort;
    }

    public Niederlassungsstatus getNiederlassungsstatus() {
        return niederlassungsstatus;
    }

    public void setNiederlassungsstatus(Niederlassungsstatus niederlassungsstatus) {
        this.niederlassungsstatus = niederlassungsstatus;
    }

    public Zivilstand getZivilstand() {
        return zivilstand;
    }

    public void setZivilstand(Zivilstand zivilstand) {
        this.zivilstand = zivilstand;
    }

    public Wohnsitz getWohnsitz() {
        return wohnsitz;
    }

    public void setWohnsitz(Wohnsitz wohnsitz) {
        this.wohnsitz = wohnsitz;
    }

    public boolean isSozialhilfebeitraege() {
        return sozialhilfebeitraege;
    }

    public void setSozialhilfebeitraege(boolean sozialhilfebeitraege) {
        this.sozialhilfebeitraege = sozialhilfebeitraege;
    }

    public boolean isQuellenbesteuert() {
        return quellenbesteuert;
    }

    public void setQuellenbesteuert(boolean quellenbesteuert) {
        this.quellenbesteuert = quellenbesteuert;
    }

    public boolean isKinder() {
        return kinder;
    }

    public void setKinder(boolean kinder) {
        this.kinder = kinder;
    }

    public boolean isDigitaleKommunikation() {
        return digitaleKommunikation;
    }

    public void setDigitaleKommunikation(boolean digitaleKommunikation) {
        this.digitaleKommunikation = digitaleKommunikation;
    }

    public void apply(PersonInAusbildung personInAusbildungGS) {
    }
}
