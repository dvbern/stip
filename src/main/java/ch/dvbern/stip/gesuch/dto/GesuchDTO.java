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

package ch.dvbern.stip.gesuch.dto;

import ch.dvbern.stip.ausbildung.dto.AusbildungContainerDTO;
import ch.dvbern.stip.ausbildung.entity.AusbildungContainer;
import ch.dvbern.stip.familiensituation.dto.FamiliensituationContainerDTO;
import ch.dvbern.stip.familiensituation.model.FamiliensituationContainer;
import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.model.Gesuchstatus;
import ch.dvbern.stip.gesuchsperiode.dto.GesuchsperiodeDTO;
import ch.dvbern.stip.personinausbildung.dto.PersonInAusbildungContainerDTO;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildungContainer;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class GesuchDTO {

    private UUID id;

    @NotNull
    //private FallDto fall;

    @NotNull
    private GesuchsperiodeDTO gesuchsperiode;

    private PersonInAusbildungContainerDTO personInAusbildungContainer;

    private AusbildungContainerDTO ausbildungContainer;

    private FamiliensituationContainerDTO familiensituationContainer;

    @NotNull
    private Gesuchstatus gesuchStatus;

    @NotNull
    private int gesuchNummer;

    public static GesuchDTO from(Gesuch changed) {
        return new GesuchDTO(changed.getId(),
                //FallDTO.from(changed.getFall()),
                GesuchsperiodeDTO.from(changed.getGesuchsperiode()),
                PersonInAusbildungContainerDTO.from(changed.getPersonInAusbildungContainer()),
                AusbildungContainerDTO.from(changed.getAusbildungContainer()),
                FamiliensituationContainerDTO.from(changed.getFamiliensituationContainer()),
                changed.getGesuchStatus(),
                changed.getGesuchNummer());
    }

    public void apply(Gesuch gesuch) {
        if (this.personInAusbildungContainer != null) {
            PersonInAusbildungContainer personInAusbildungContainerFromGesuch = gesuch.getPersonInAusbildungContainer() != null ? gesuch.getPersonInAusbildungContainer() : new PersonInAusbildungContainer();
            personInAusbildungContainer.apply(personInAusbildungContainerFromGesuch);
            gesuch.setPersonInAusbildungContainer(personInAusbildungContainerFromGesuch);
        }
        if (this.ausbildungContainer != null) {
            AusbildungContainer ausbildungContainerFromGesuch = gesuch.getAusbildungContainer() != null ? gesuch.getAusbildungContainer() : new AusbildungContainer();
            ausbildungContainer.apply(ausbildungContainerFromGesuch);
            gesuch.setAusbildungContainer(ausbildungContainerFromGesuch);
        }
        if(this.familiensituationContainer != null) {
            FamiliensituationContainer familiensituationContainerFromGesuch = gesuch.getFamiliensituationContainer() != null ? gesuch.getFamiliensituationContainer() : new FamiliensituationContainer();
            familiensituationContainer.apply(familiensituationContainerFromGesuch);
            gesuch.setFamiliensituationContainer(familiensituationContainerFromGesuch);
        }
    }
}
