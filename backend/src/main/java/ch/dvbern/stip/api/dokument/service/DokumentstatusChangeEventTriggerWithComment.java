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
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;

public class DokumentstatusChangeEventTriggerWithComment
extends TriggerWithParameters2<GesuchDokument, String, DokumentstatusChangeEvent> {
    private DokumentstatusChangeEventTriggerWithComment(DokumentstatusChangeEvent trigger) {
        super(trigger, GesuchDokument.class, String.class);
    }

    public static DokumentstatusChangeEventTriggerWithComment createTrigger(DokumentstatusChangeEvent trigger) {
        return new DokumentstatusChangeEventTriggerWithComment(trigger);
    }
}
