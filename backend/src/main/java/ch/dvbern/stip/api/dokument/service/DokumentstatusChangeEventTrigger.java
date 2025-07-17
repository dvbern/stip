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

package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

public class DokumentstatusChangeEventTrigger
extends TriggerWithParameters1<GesuchDokument, GesuchDokumentStatusChangeEvent> {
    private DokumentstatusChangeEventTrigger(GesuchDokumentStatusChangeEvent trigger) {
        super(trigger, GesuchDokument.class);
    }

    public static DokumentstatusChangeEventTrigger createTrigger(GesuchDokumentStatusChangeEvent trigger) {
        return new DokumentstatusChangeEventTrigger(trigger);
    }
}
