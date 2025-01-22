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

package ch.dvbern.stip.api.communication.mail.service;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MailServiceUtils {
    public void sendStandardNotificationEmailForGesuch(final MailService mailService, final Gesuch gesuch) {
        final var pia = gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung();
        mailService.sendStandardNotificationEmails(
            pia.getNachname(),
            pia.getVorname(),
            AppLanguages.fromLocale(pia.getKorrespondenzSprache().getLocale()),
            gatherRecipients(gesuch)
        );
    }

    List<String> gatherRecipients(final Gesuch gesuch) {
        final var result = new ArrayList<String>();

        final var delegierung = gesuch.getAusbildung().getFall().getDelegierung();
        if (delegierung != null) {
            result.add(delegierung.getSozialdienst().getSozialdienstAdmin().getEmail());
        }

        result.add(gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung().getEmail());

        return result;
    }
}
