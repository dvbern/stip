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

package ch.dvbern.stip.adresse.dto;

import ch.dvbern.stip.adresse.model.Adresse;
import ch.dvbern.stip.shared.enums.Land;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class AdresseDTO {

    @NotNull
    private UUID id;
    @NotNull
    private Land land;

    private String coAdresse;

    @NotNull
    private String strasse;

    private String hausnummer;

    @NotNull
    private String plz;

    @NotNull
    private String ort;

    public static AdresseDTO from(Adresse changed) {
        return new AdresseDTO(changed.getId(), changed.getLand(), changed.getCoAdresse(), changed.getStrasse(), changed.getHausnummer(), changed.getPlz(), changed.getOrt());
    }

    public void apply(Adresse adresse) {
        adresse.setCoAdresse(coAdresse);
        adresse.setLand(land);
        adresse.setHausnummer(hausnummer);
        adresse.setStrasse(strasse);
        adresse.setPlz(plz);
        adresse.setOrt(ort);
    }
}
