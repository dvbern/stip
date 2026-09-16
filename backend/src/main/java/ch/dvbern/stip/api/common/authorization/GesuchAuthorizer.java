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
import ch.dvbern.stip.api.common.util.GesuchUtil;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    private final VerfuegungService verfuegungService;
    private final GesuchTrancheService gesuchTrancheService;

    private UUID getGesuchIdByTrancheId(final UUID gesuchTrancheId) {
        final GesuchTranche gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        return gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
    }

    @Transactional
    public void sbCanChangeGesuchOfTrancheStatusToInBearbeitung(final UUID gesuchTrancheId) {
        assertCanPerformStatusChange(
            getGesuchIdByTrancheId(gesuchTrancheId),
            GesuchStatusChangeEvent.IN_BEARBEITUNG_SB
        );
    }

    @Transactional
    public void sbCanChangeGesuchStatusToNegativeVerfuegung(final UUID gesuchTrancheId) {
        assertCanPerformStatusChange(
            getGesuchIdByTrancheId(gesuchTrancheId),
            GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG
        );
    }

    @Transactional
    public void sbCanCreateManuelleVerfuegungForGesuchOfTranche(final UUID gesuchTrancheId) {
        assertCanPerformStatusChange(
            getGesuchIdByTrancheId(gesuchTrancheId),
            GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG
        );
    }

    @Transactional
    public void freigabestelleCanChangeGesuchOfTrancheStatusToVerfuegt(final UUID gesuchTrancheId) {
        final var gesuchId = getGesuchIdByTrancheId(gesuchTrancheId);
        assertGesuchIsInGesuchStatus(gesuchId, Gesuchstatus.IN_FREIGABE);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.VERFUEGT);

        final var canFreigabeVerfuegen =
            gesuchStatusService.canFreigabeVerfuegen(gesuchRepository.requireById(gesuchId));
        if (!canFreigabeVerfuegen) {
            forbidden();
        }
    }

    @Transactional
    public void sbCanChangeGesuchOfTrancheStatusToVerfuegungDruckbereit(final UUID gesuchTrancheId) {
        assertCanPerformStatusChange(
            getGesuchIdByTrancheId(gesuchTrancheId),
            GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT
        );
    }

    @Transactional
    public void sbCanChangeGesuchOfTrancheStatusToVersendet(final UUID gesuchTrancheId) {
        assertCanPerformStatusChange(
            getGesuchIdByTrancheId(gesuchTrancheId),
            GesuchStatusChangeEvent.VERFUEGUNG_VERSENDET
        );
    }

    @Transactional
    public void sbCanGesuchOfTrancheFehlendeDokumenteUebermitteln(final UUID gesuchTrancheId) {
        final var gesuchId = getGesuchIdByTrancheId(gesuchTrancheId);
        assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE);
    }

    @Transactional
    public void sbCanChangeGesuchOfTrancheStatusToBereitFuerBearbeitung(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        if (gesuch.getGesuchStatus() == Gesuchstatus.IN_FREIGABE) {
            assertBenutzerIsFreigabestelle();
        }

        assertGesuchIsInOneOfGesuchStatus(
            gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche),
            Set.of(
                Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT,
                Gesuchstatus.DATENSCHUTZBRIEF_VERSANDBEREIT,
                Gesuchstatus.IN_FREIGABE
            )
        );
        assertCanPerformStatusChange(
            gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche),
            GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG
        );
    }

    @Transactional
    public void sbCanGesuchOfTrancheZurueckweisen(final UUID gesuchTrancheId) {
        assertGesuchIsInOneOfGesuchStatus(
            getGesuchIdByTrancheId(gesuchTrancheId),
            Gesuchstatus.SACHBEARBEITER_CAN_EDIT
        );
    }

    @Transactional
    public void gsCanFehlendeDokumenteEinreichen(final UUID gesuchTrancheId) {
        final var gesuchId = getGesuchIdByTrancheId(gesuchTrancheId);
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
    public void canGetBerechnungOfVerfuegung(final UUID verfuegungId) {
        final var verfuegung = verfuegungService.requireById(verfuegungId);
        gsSbFreigabestelleOrJuristCanRead(verfuegung.getGesuch().getId());
    }

    @Transactional
    public void canGetBerechnungGs(final UUID gesuchId) {
        assertCanGetBerechnungGs(gesuchId);
    }

    @Transactional
    public void canGetBerechnungSb(final UUID gesuchId) {
        assertCanGetBerechnungSb(gesuchId);
    }

    @Transactional
    public void sbOrJuristCanRead() {
        permitAll();
    }

    @Transactional
    public void gsCanReadGesuchOfTranche(final UUID gesuchTrancheId) {
        assertIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(getGesuchIdByTrancheId(gesuchTrancheId));
    }

    @Transactional
    public void gsCanRead(final UUID gesuchId) {
        assertIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
    }

    @Transactional
    public void gsSbFreigabestelleOrJuristCanRead(final UUID gesuchId) {
        if (isSbOrFreigabestelleOrJurist(benutzerService.getCurrentBenutzer())) {
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
    public void sbCanChangeGesuchsperiodeForGesuchOfTranche(final UUID gesuchTrancheId) {
        final var gesuch = gesuchRepository.requireById(getGesuchIdByTrancheId(gesuchTrancheId));

        if (!gesuchStatusService.canChangeGesuchsperiode(gesuch)) {
            forbidden();
        }
    }

    @Transactional
    public void gsCanGesuchOfTrancheEinreichen(final UUID gesuchTrancheId) {
        final var gesuchId = getGesuchIdByTrancheId(gesuchTrancheId);
        assertCanWriteAndIsGesuchstellerOfGesuchIdOrDelegatedToSozialdienst(gesuchId);
        assertGesuchIsInGesuchStatus(gesuchId, Gesuchstatus.IN_BEARBEITUNG_GS);
        assertCanPerformStatusChange(gesuchId, GesuchStatusChangeEvent.EINGEREICHT);
    }

    @Transactional
    public void sbCanGesuchOfTrancheManuellPruefen(final UUID gesuchTrancheId) {
        assertGesuchIsInOneOfGesuchStatus(
            getGesuchIdByTrancheId(gesuchTrancheId),
            Gesuchstatus.SACHBEARBEITER_CAN_TRIGGER_ANSPRUCH_CHECK
        );
    }

    @Transactional
    public void juristCanGesuchOfTrancheManuellPruefen(final UUID gesuchTrancheId) {
        assertGesuchIsInOneOfGesuchStatus(getGesuchIdByTrancheId(gesuchTrancheId), Gesuchstatus.JURIST_CAN_EDIT);
    }

    @Transactional
    public void sbCanChangeGesuchOfTrancheStatusToDatenschutzBriefDruckbereitIfStatusChangeRequired(
        UUID gesuchTrancheId
    ) {
        final GesuchTranche gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        if (gesuch.getGesuchStatus() == Gesuchstatus.BEREIT_FUER_BEARBEITUNG) {
            return;
        }
        assertCanPerformStatusChange(gesuch.getId(), GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT);
    }

    @Transactional
    public void sbCanChangeGesuchOfTrancheStatusToBearbeitungAsAenderungIfStatusChangeRequired(UUID gesuchTrancheId) {
        final var gesuch = gesuchRepository.requireById(getGesuchIdByTrancheId(gesuchTrancheId));
        if (!GesuchUtil.canSbInitAendererung(gesuch)) {
            forbidden();
        }
        assertCanPerformStatusChange(gesuch.getId(), GesuchStatusChangeEvent.SB_INITIALISIERT_AENDERUNG);
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
        assertGesuchCanCreateAenderung(gesuchId);
    }

    private void assertGesuchCanCreateAenderung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (GesuchUtil.canCreateAenderung(gesuch)) {
            return;
        }
        forbidden();
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
        var gesuch = gesuchRepository.requireById(gesuchId);
        if (!Gesuchstatus.GESUCH_VERFUEGUNG_ABGESCHLOSSEN.contains(gesuch.getGesuchStatus())) {
            assertGesuchIsInOneOfGesuchStatus(gesuchId, Gesuchstatus.SACHBEARBEITER_CAN_UPDATE_NACHFRIST);
            return;
        }

        var aenderungen = gesuch.getAenderungs()
            .filter(
                gesuchTranche -> GesuchTrancheStatus.SACHBEARBEITER_CAN_UPDATE_NACHFRIST
                    .contains(gesuchTranche.getStatus())
            )
            .toList();

        if (aenderungen.isEmpty()) {
            forbidden();
        }
    }

    @Transactional
    public void sbCanBearbeitungAbschliessenByTranche(final UUID gesuchTrancheId) {
        final GesuchTranche gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        if (!gesuchStatusService.canBearbeitungAbschliessen(gesuchTranche.getGesuch())) {
            forbidden();
        }
    }

    public void assertCanPerformStatusChange(final UUID gesuchId, GesuchStatusChangeEvent gesuchStatusChangeEvent) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        if (!gesuchStatusService.canFire(gesuch, gesuchStatusChangeEvent)) {
            forbidden();
        }
    }

    public void assertCanGetBerechnungGs(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuchStatusService.canGetBerechnungGs(gesuch)) {
            return;
        }
        forbidden();
    }

    public void assertCanGetBerechnungSb(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuchStatusService.canGetBerechnungSb(gesuch)) {
            return;
        }
        forbidden();
    }

    public void assertGesuchIsInOneOfGesuchStatus(final UUID gesuchId, final Set<Gesuchstatus> gesuchStatusSet) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (GesuchUtil.gesuchIsInOneOfGesuchStatus(gesuch, gesuchStatusSet)) {
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

    public void assertBenutzerIsFreigabestelle() {
        assertBenutzerHasRole(OidcConstants.ROLE_FREIGABESTELLE);
    }

    public void assertBenutzerHasRole(final String roleIdentifier) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();
        if (!currentBenutzer.hasRole(roleIdentifier)) {
            forbidden();
        }
    }
}
