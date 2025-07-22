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

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.util.AuthorizerUtil;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchAuthorizer extends BaseAuthorizer {
    private final BenutzerService benutzerService;
    private final GesuchRepository gesuchRepository;
    private final GesuchStatusService gesuchStatusService;
    private final FallRepository fallRepository;
    private final SozialdienstService sozialdienstService;
    private final GesuchService gesuchService;
    private final RequiredDokumentService requiredDokumentService;

    @Transactional
    public void sbCanChangeGesuchStatusToInBearbeitung(final UUID gesuchId) {
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.IN_BEARBEITUNG_SB);
    }

    @Transactional
    public void sbCanChangeGesuchStatusToNegativeVerfuegung(final UUID gesuchId) {
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG);
    }

    @Transactional
    public void sbCanChangeGesuchStatusToVersandbereit(final UUID gesuchId) {
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.VERSANDBEREIT);
    }

    @Transactional
    public void sbCanChangeGesuchStatusToVerfuegt(final UUID gesuchId) {
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.VERFUEGT);
    }

    @Transactional
    public void sbCanChangeGesuchStatusToVersendet(final UUID gesuchId) {
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.VERSENDET);
    }

    @Transactional
    public void sbCanGesuchFehlendeDokumenteUebermitteln(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE);
    }

    @Transactional
    public void sbCanChangeGesuchStatusToBereitFuerBearbeitung(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(
            gesuchId,
            Set.of(Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN, Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT)
        );
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

    @Transactional
    public void sbCanGesuchZurueckweisen(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
    }

    @Transactional
    public void gsCanFehlendeDokumenteEinreichen(final UUID gesuchId) {
        assertCanWriteAndIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
        assertGesuchIsInGesuchStatus(gesuchId, Gesuchstatus.FEHLENDE_DOKUMENTE);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN);

        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (
            !requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(gesuch, benutzerService.getCurrentBenutzer())
        ) {
            forbidden();
        }
    }

    @Transactional
    public void canGetBerechnung(final UUID gesuchId) {
        assertSBCanGetBerechnung(gesuchId);
    }

    @Transactional
    public void sbCanRead() {
        permitAll();
    }

    @Transactional
    public void sbOrJuristCanRead() {
        permitAll();
    }

    @Transactional
    public void gsCanRead(final UUID gesuchId) {
        assertIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
    }

    @Transactional
    public void gsSbOrJuristCanRead(final UUID gesuchId) {
        if (isSbOrJurist(benutzerService.getCurrentBenutzer())) {
            return;
        }
        gsCanRead(gesuchId);
    }

    @Transactional
    public void gsOrAdminCanDelete(final UUID gesuchId) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        if (isSuperUser(currentBenutzer)) {
            return;
        }

        assertGesuchIsInGesuchStatus(gesuchId, Gesuchstatus.IN_BEARBEITUNG_GS);
        assertCanWriteAndIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
    }

    @Transactional
    public void gsCanGesuchEinreichen(final UUID gesuchId) {
        assertCanWriteAndIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
        assertGesuchIsInGesuchStatus(gesuchId, Gesuchstatus.IN_BEARBEITUNG_GS);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.EINGEREICHT);
    }

    @Transactional
    public void juristCanGesuchEinreichen(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.JURIST_CAN_EDIT);
    }

    @Transactional
    public void gsCanCreate() {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        final var fall =
            fallRepository.findFallForGsOptional(currentBenutzer.getId()).orElseThrow(NotFoundException::new);
        assertCanWriteAndIsGesuchstellerOfFallOrDelegatedToSozialdienst(fall);
    }

    @Transactional
    public void sbCanCreateTranche(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
    }

    @Transactional
    public void sbCanUpdateEinreichedatum(final UUID gesuchId) {
        if (gesuchService.canUpdateEinreichedatum(gesuchRepository.requireById(gesuchId))) {
            return;
        }

        forbidden();
    }

    @Transactional
    public void gsCanCreateAenderung(final UUID gesuchId) {
        assertCanWriteAndIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN);
    }

    public void canGetGsDashboard() {
        permitAll();
    }

    public void gsCanGetGesuche() {
        permitAll();
    }

    public void sbCanGetGesuche() {
        permitAll();
    }

    @Transactional
    public void canUpdateNachfrist(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_UPDATE_NACHFRIST);
    }

    @Transactional
    public void sbCanBearbeitungAbschliessen(final UUID gesuchId) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.IN_FREIGABE);
    }

    public void assertCanPerformStatusChange(final UUID gesuchId, GesuchStatusChangeEvent gesuchStatusChangeEvent) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (!gesuchStatusService.canFire(gesuch, gesuchStatusChangeEvent)) {
            forbidden();
        }
    }

    public void assertSBCanGetBerechnung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuchStatusService.canGetBerechnung(gesuch)) {
            return;
        }
        forbidden();
    }

    public void assertGesuchIsInOneOfGesuchStatus(final UUID gesuchId, final Set<Gesuchstatus> gesuchStatusSet) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuchStatusService.gesuchIsInOneOfGesuchStatus(gesuch, gesuchStatusSet)) {
            return;
        }
        forbidden();
    }

    public void assertGesuchIsInGesuchStatus(final UUID gesuchId, final Gesuchstatus gesuchStatus) {
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Set.of(gesuchStatus));
    }

    public void assertIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(final UUID gesuchId) {
        if (
            !AuthorizerUtil.canReadAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                gesuchRepository.requireById(gesuchId).getAusbildung().getFall(),
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }

    public void assertCanWriteAndIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(
        final UUID gesuchId
    ) {
        assertCanWriteAndIsGesuchstellerOfFallOrDelegatedToSozialdienst(
            gesuchRepository.requireById(gesuchId).getAusbildung().getFall()
        );
    }

    public void assertCanWriteAndIsGesuchstellerOfFallOrDelegatedToSozialdienst(
        final Fall fall
    ) {
        if (
            !AuthorizerUtil.canWriteAndIsGesuchstellerOfOrDelegatedToSozialdienst(
                fall,
                benutzerService.getCurrentBenutzer(),
                sozialdienstService
            )
        ) {
            forbidden();
        }
    }
}
