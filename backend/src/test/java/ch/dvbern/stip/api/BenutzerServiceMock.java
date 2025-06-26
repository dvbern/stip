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

import java.util.HashSet;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterRepository;
import ch.dvbern.stip.api.benutzer.repo.SachbearbeiterZuordnungStammdatenRepository;
import ch.dvbern.stip.api.benutzer.service.BenutzerMapper;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.service.RolleService;
import ch.dvbern.stip.api.benutzer.service.SachbearbeiterZuordnungStammdatenMapper;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.zuordnung.repo.ZuordnungRepository;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Mock
@RequestScoped
public class BenutzerServiceMock extends BenutzerService {
    private final JsonWebToken jsonWebToken;
    private static final HashSet<String> SEEN_BENUTZERS = new HashSet<>();

    public BenutzerServiceMock() {
        super(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        jsonWebToken = null;
    }

    @Inject
    public BenutzerServiceMock(
    JsonWebToken jsonWebToken,
    BenutzerMapper benutzerMapper,
    SachbearbeiterZuordnungStammdatenMapper sachbearbeiterZuordnungStammdatenMapper,
    BenutzerRepository benutzerRepository,
    SachbearbeiterRepository sachbearbeiterRepository,
    SozialdienstBenutzerRepository sozialdienstBenutzerRepository,
    RolleService rolleService,
    SachbearbeiterZuordnungStammdatenRepository sachbearbeiterZuordnungStammdatenRepository,
    SecurityIdentity identity,
    ZuordnungRepository zuordnungRepository
    ) {
        super(
            jsonWebToken,
            benutzerMapper,
            sachbearbeiterZuordnungStammdatenMapper,
            benutzerRepository,
            sachbearbeiterRepository,
            sozialdienstBenutzerRepository,
            rolleService,
            sachbearbeiterZuordnungStammdatenRepository,
            identity,
            zuordnungRepository
        );

        this.jsonWebToken = jsonWebToken;
    }

    @Override
    @Transactional
    public Benutzer getCurrentBenutzer() {
        if (SEEN_BENUTZERS.contains(jsonWebToken.getSubject())) {
            return super.getCurrentBenutzer();
        } else {
            SEEN_BENUTZERS.add(jsonWebToken.getSubject());
            super.getOrCreateAndUpdateCurrentBenutzer();
            return super.getCurrentBenutzer();
        }
    }
}
