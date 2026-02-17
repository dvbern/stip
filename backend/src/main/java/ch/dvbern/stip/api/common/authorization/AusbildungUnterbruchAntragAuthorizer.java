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

package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.service.AusbildungUnterbruchAntragService;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class AusbildungUnterbruchAntragAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final AusbildungUnterbruchAntragService ausbildungUnterbruchAntragService;
    private final GesuchService gesuchService;
    private final SozialdienstService sozialdienstService;

    @Transactional
    public void gsCanCreate(final UUID gesuchId) {
        final var gesuch = gesuchService.getGesuchById(gesuchId);
        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuch,
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }

        if (!ausbildungUnterbruchAntragService.canCreateAusbildungUnterbruchAntrag(gesuch.getAusbildung())) {
            forbidden();
        }
    }

    @Transactional
    public void gsCanRead(final UUID ausbildungUnterbruchAntragId) {
        final var antrag = ausbildungUnterbruchAntragService.requireById(ausbildungUnterbruchAntragId);
        if (
            !AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                antrag.getGesuch().getAusbildung().getFall(),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }

    @Transactional
    public void gsCanUpdate(final UUID ausbildungUnterbruchAntragId) {
        final var antrag = ausbildungUnterbruchAntragService.requireById(ausbildungUnterbruchAntragId);
        if (!ausbildungUnterbruchAntragService.gsCanWrite(antrag)) {
            forbidden();
        }
    }

    @Transactional
    public void gsCanDeleteDokument(final UUID dokumentId) {
        final var antrag = ausbildungUnterbruchAntragService.requireByDokumentId(dokumentId);
        if (!ausbildungUnterbruchAntragService.gsCanWrite(antrag)) {
            forbidden();
        }
    }

    @Transactional
    public void canReadDokument(final UUID dokumentId) {
        final var benutzer = benutzerService.getCurrentBenutzer();
        if (isSachbearbeiter(benutzer)) {
            return;
        }

        final var antrag = ausbildungUnterbruchAntragService.requireByDokumentId(dokumentId);

        if (
            !AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                antrag.getAusbildung().getFall(),
                benutzer,
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }

    public void sbCanRead() {
        permitAll();
    }

    public void sbCanWrite(final UUID ausbildungUnterbruchAntragId) {
        final var antrag = ausbildungUnterbruchAntragService.requireById(ausbildungUnterbruchAntragId);
        assertAusbildungUnterbruchAntragIsInState(antrag, AusbildungUnterbruchAntragStatus.EINGEGEBEN);
    }

    private void assertAusbildungUnterbruchAntragIsInState(
        final AusbildungUnterbruchAntrag antrag,
        final AusbildungUnterbruchAntragStatus status
    ) {
        if (antrag.getStatus() != status) {
            forbidden();
        }
    }
}
