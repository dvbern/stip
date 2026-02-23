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

package ch.dvbern.stip.api.userconsent.service;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.userconsent.entity.UserConsent;
import ch.dvbern.stip.api.userconsent.repo.UserConsentRepository;
import ch.dvbern.stip.generated.dto.UserConsentDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class UserConsentService {

    private final BenutzerService benutzerService;
    private final UserConsentRepository userConsentRepository;
    private final UserConsentMapper userConsentMapper;

    public UserConsentDto getUserConsent() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var consent = userConsentRepository.userConsentWasGiven(benutzer.getId());
        return userConsentMapper.toDto(consent);
    }

    @Transactional
    public UserConsentDto createUserConsent(boolean consentGiven) {
        final var benutzer = benutzerService.getCurrentBenutzer();

        final var userConsent = new UserConsent();
        userConsent.setConsentGiven(consentGiven);
        userConsent.setBenutzer(benutzer);

        userConsentRepository.persistAndFlush(userConsent);
        return userConsentMapper.toDto(userConsent);
    }

}
