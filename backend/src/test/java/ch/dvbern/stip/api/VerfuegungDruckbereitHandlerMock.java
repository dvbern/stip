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

package ch.dvbern.stip.api;

import java.util.UUID;

import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungDruckbereitHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@Mock
@RequestScoped
public class VerfuegungDruckbereitHandlerMock extends VerfuegungDruckbereitHandler {

    public VerfuegungDruckbereitHandlerMock() {
        super(null, null, null, null, null, null);
    }

    @Transactional
    @Override
    public void handle(Gesuch gesuch) {
        // KSTIP-2845: due to a requirement in NotificationService,
        // a dokument of type VERSENDETE_VERFUEGUNG must be created in order to execute the tests
        var versendeteVerfuegung = new VerfuegungDokument();
        versendeteVerfuegung.setTyp(VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG);
        versendeteVerfuegung.setObjectId(UUID.randomUUID().toString());

        var currentVerfuegung = gesuch.getVerfuegungs().getFirst();
        versendeteVerfuegung.setVerfuegung(currentVerfuegung);
        currentVerfuegung.getDokumente().add(versendeteVerfuegung);

    }
}
