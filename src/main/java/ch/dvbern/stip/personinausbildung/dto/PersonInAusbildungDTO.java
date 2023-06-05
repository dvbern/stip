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

package ch.dvbern.stip.personinausbildung.dto;

import ch.dvbern.stip.adresse.dto.AdresseDTO;
import ch.dvbern.stip.adresse.model.Adresse;
import ch.dvbern.stip.personinausbildung.model.Niederlassungsstatus;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildung;
import ch.dvbern.stip.personinausbildung.model.Wohnsitz;
import ch.dvbern.stip.personinausbildung.model.Zivilstand;
import ch.dvbern.stip.shared.enums.Anrede;
import ch.dvbern.stip.shared.enums.Land;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class PersonInAusbildungDTO {
    @NotNull
    private UUID id;

    @NotNull
    private AdresseDTO adresse;

    @NotNull
    private String sozialversicherungsnummer;

    @NotNull
    private String name;

    @NotNull
    private String vorname;

    @NotNull
    private Anrede anrede;

    @NotNull
    private boolean identischerZivilrechtlicherWohnsitz;

    private String izvOrt;

    private String izvPLZ;

    @NotNull
    private String email;

    @NotNull
    private String telefonnummer;

    @NotNull
    private LocalDate geburtsdatum;

    @NotNull
    private Land nationalitaet = Land.CH;


    private String heimatort;

    private Niederlassungsstatus niederlassungsstatus;

    private Zivilstand zivilstand;

    @NotNull
    private Wohnsitz wohnsitz;

    @NotNull
    private boolean sozialhilfebeitraege;

    @NotNull
    private boolean quellenbesteuert;

    @NotNull
    private boolean kinder;

    @NotNull
    private boolean digitaleKommunikation;

    public static PersonInAusbildungDTO from(PersonInAusbildung changed) {
        return new PersonInAusbildungDTO(changed.getId(), AdresseDTO.from(changed.getAdresse()), changed.getSozialversicherungsnummer(),
                changed.getName(), changed.getVorname(), changed.getAnrede(), changed.isIdentischerZivilrechtlicherWohnsitz(),
                changed.getIzvOrt(), changed.getIzvPLZ(), changed.getEmail(), changed.getTelefonnummer(),
                changed.getGeburtsdatum(), changed.getHeimatort(), changed.getNiederlassungsstatus(),
                changed.getZivilstand(), changed.getWohnsitz(), changed.isSozialhilfebeitraege(), changed.isQuellenbesteuert(),
                changed.isKinder(), changed.isDigitaleKommunikation());
    }

    public void apply(PersonInAusbildung personInAusbildung){
        personInAusbildung.setAnrede(anrede);
        personInAusbildung.setSozialversicherungsnummer(sozialversicherungsnummer);
        personInAusbildung.setName(name);
        personInAusbildung.setVorname(vorname);
        personInAusbildung.setTelefonnummer(telefonnummer);
        personInAusbildung.setEmail(email);
        personInAusbildung.setGeburtsdatum(geburtsdatum);
        personInAusbildung.setHeimatort(heimatort);
        personInAusbildung.setDigitaleKommunikation(digitaleKommunikation);
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(identischerZivilrechtlicherWohnsitz);
        personInAusbildung.setIzvOrt(izvOrt);
        personInAusbildung.setIzvPLZ(izvPLZ);
        personInAusbildung.setKinder(kinder);
        personInAusbildung.setNiederlassungsstatus(niederlassungsstatus);
        personInAusbildung.setZivilstand(zivilstand);
        personInAusbildung.setWohnsitz(wohnsitz);
        personInAusbildung.setQuellenbesteuert(quellenbesteuert);
        personInAusbildung.setSozialhilfebeitraege(sozialhilfebeitraege);
        adresse.apply(personInAusbildung.getAdresse() != null ? personInAusbildung.getAdresse() : new Adresse());
    }
}
