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

import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.darlehen.repo.DarlehenRepository;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Authorizer
@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenAuthorizer extends BaseAuthorizer {
    private final FallRepository fallRepository;
    private final DarlehenRepository darlehenRepository;
    private final BenutzerService benutzerService;
    private final SozialdienstService sozialdienstService;

    @Transactional
    public void canGetDarlehenGs(UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);

        canGetDarlehenByFallId(darlehen.getFall().getId());
    }

    @Transactional
    public void canGetDarlehenByFallId(UUID fallId) {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);

        if (
            !AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(fall, benutzer, sozialdienstService)
        ) {
            forbidden();
        }
    }

    public void canGetDarlehenSb() {
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (!isSachbearbeiterOrFreigabestelle(benutzer)) {
            forbidden();
        }
    }

    @Transactional
    public void canCreateDarlehen(UUID fallId) {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.requireById(fallId);
        final var ausbildungs = fall.getAusbildungs();

        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(fall, benutzer, sozialdienstService)
        ) {
            forbidden();
        }

        final var hasNoOpenDarlehen =
            fall.getDarlehens().stream().map(Darlehen::getStatus).allMatch(DarlehenStatus::isCompleted);
        if (!hasNoOpenDarlehen) {
            forbidden();
        }

        final var atLeastOneGesuchEingereicht = ausbildungs
            .stream()
            .anyMatch(
                // has more than one Gesuch
                ausbildung -> ausbildung.getGesuchs().size() > 1
                // or has at least one eingereicht gesuch
                || ausbildung.getGesuchs().stream().anyMatch(g -> g.getGesuchStatus().isEingereicht())
            );
        if (!atLeastOneGesuchEingereicht) {
            forbidden();
        }
    }

    @Transactional
    public void canDarlehenAblehenenAkzeptieren(UUID darlehenId) {
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (!isFreigabestelle(benutzer)) {
            forbidden();
        }

        assertStatus(darlehenId, DarlehenStatus.IN_FREIGABE);
    }

    @Transactional
    public void canDarlehenEingeben(UUID darlehenId) {
        assertStatus(darlehenId, DarlehenStatus.IN_BEARBEITUNG_GS);
    }

    @Transactional
    public void canDarlehenFreigeben(UUID darlehenId) {
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (!isSachbearbeiter(benutzer)) {
            forbidden();
        }

        assertStatus(darlehenId, DarlehenStatus.EINGEGEBEN);
    }

    @Transactional
    public void canDarlehenZurueckweisen(UUID darlehenId) {
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (!isSachbearbeiterOrFreigabestelle(benutzer)) {
            forbidden();
        }

        assertStatus(darlehenId, DarlehenStatus.EINGEGEBEN);
    }

    @Transactional
    public void canDarlehenUpdateGs(UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                darlehen.getFall(),
                benutzer,
                sozialdienstService
            )
        ) {
            forbidden();
        }

        assertStatus(darlehenId, DarlehenStatus.IN_BEARBEITUNG_GS);
    }

    @Transactional
    public void canDarlehenUpdateSb(UUID darlehenId) {
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (!isSachbearbeiterOrFreigabestelle(benutzer)) {
            forbidden();
        }

        assertStatus(darlehenId, DarlehenStatus.EINGEGEBEN, DarlehenStatus.IN_FREIGABE);
    }

    @Transactional
    public void canCreateDarlehenDokument(UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                darlehen.getFall(),
                benutzer,
                sozialdienstService
            )
        ) {
            forbidden();
        }

        assertStatus(darlehenId, DarlehenStatus.IN_BEARBEITUNG_GS);
    }

    private void canGetDarlehenDokument(Darlehen darlehen) {
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                darlehen.getFall(),
                benutzer,
                sozialdienstService
            ) && !isSachbearbeiterOrFreigabestelle(benutzer)
        ) {
            forbidden();
        }
    }

    @Transactional
    public void canGetDarlehenDokument(UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        canGetDarlehenDokument(darlehen);
    }

    @Transactional
    public void canGetDarlehenDokumentByDokumentId(UUID dokumentId) {
        final var darlehen = darlehenRepository.requireByDokumentOrDarlehensVerfuegungId(dokumentId);
        canGetDarlehenDokument(darlehen);
    }

    public void canDeleteDarlehenDokument(UUID dokumentId) {
        final var darlehen = darlehenRepository.requireByDokumentId(dokumentId);
        final var benutzer = benutzerService.getCurrentBenutzer();

        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                darlehen.getFall(),
                benutzer,
                sozialdienstService
            )
        ) {
            forbidden();
        }

        assertStatus(darlehen.getId(), DarlehenStatus.IN_BEARBEITUNG_GS);
    }

    public void assertStatus(UUID darlehenId, DarlehenStatus... darlehenStatus) {
        final var darlehen = darlehenRepository.requireById(darlehenId);

        if (Arrays.stream(darlehenStatus).anyMatch(status -> darlehen.getStatus().equals(status))) {
            return;
        }

        forbidden();
    }
}
