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

package ch.dvbern.stip.berechnung.service;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BerechnungTestcase {
    @JsonProperty("ausbildung")
    AusbildungUpdateDto ausbildung;
    @JsonProperty("bildungskategorie")
    Bildungskategorie bildungskategorie;
    @JsonProperty("bildungsrichtung")
    Bildungsrichtung bildungsrichtung;
    @JsonProperty("gesuch")
    GesuchUpdateDto gesuch;
    @JsonProperty("steuerdaten")
    List<SteuerdatenDto> steuerdaten;
    @JsonProperty("gesuchperiode")
    GesuchsperiodeCreateDto gesuchperiode;
}
