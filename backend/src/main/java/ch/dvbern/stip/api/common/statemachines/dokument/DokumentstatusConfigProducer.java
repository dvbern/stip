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

package ch.dvbern.stip.api.common.statemachines.dokument;

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class DokumentstatusConfigProducer {
    public StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> createStateMachineConfig() {
        final StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> config = new StateMachineConfig<>();
        config.configure(Dokumentstatus.AUSSTEHEND)
            .permit(DokumentstatusChangeEvent.ABGELEHNT, Dokumentstatus.ABGELEHNT)
            .permit(DokumentstatusChangeEvent.AKZEPTIERT, Dokumentstatus.AKZEPTIERT);

        config.configure(Dokumentstatus.ABGELEHNT)
            .permit(DokumentstatusChangeEvent.AUSSTEHEND, Dokumentstatus.AUSSTEHEND);
        config.configure(Dokumentstatus.AKZEPTIERT);

        return config;
    }
}
