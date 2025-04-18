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

package ch.dvbern.stip.api.communication.mail.resource;

import ch.dvbern.stip.api.common.authorization.MailAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.generated.api.MailResource;
import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.SEND_EMAIL;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class MailResourceImpl implements MailResource {
    private final MailAuthorizer mailAuthorizer;
    private final MailService mailService;

    @Override
    @RolesAllowed(SEND_EMAIL)
    public void sendWelcomeEmail(WelcomeMailDto welcomeMailDto) {
        mailAuthorizer.canSend();
        mailService.sendBenutzerWelcomeEmail(welcomeMailDto);
    }
}
