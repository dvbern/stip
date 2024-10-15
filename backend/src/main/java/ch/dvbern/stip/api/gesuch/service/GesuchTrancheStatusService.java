package ch.dvbern.stip.api.gesuch.service;

import java.util.HashSet;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.statemachines.StateMachineUtil;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.generated.dto.KommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheStatusService {
    private final StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> config;
    private final GesuchTrancheValidatorService validatorService;

    @Transactional
    public void triggerStateMachineEvent(
        final GesuchTranche gesuchTranche,
        final GesuchTrancheStatusChangeEvent event
    ) {
        final var sm = createStateMachine(gesuchTranche);
        sm.fire(GesuchTrancheStatusChangeEventTrigger.createTrigger(event), gesuchTranche);
    }

    @Transactional
    public void triggerStateMachineEventWithComment(
        final GesuchTranche gesuchTranche,
        final GesuchTrancheStatusChangeEvent event,
        final KommentarDto kommentarDto
    ) {
        final var sm = createStateMachine(gesuchTranche);
        sm.fire(
            GesuchTrancheStatusChangeEventTriggerWithComment.createTrigger(event),
            gesuchTranche,
            kommentarDto.getText()
        );

        // TODO: KSTIP-1216 - Save kommentarDto.getText() in Nachricht and Protokoll
    }

    public boolean benutzerCanEdit(final Benutzer benutzer, final GesuchTrancheStatus gesuchTrancheStatus) {
        final var identifiers = benutzer.getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());
        final var editStates = new HashSet<GesuchTrancheStatus>();
        if (identifiers.contains(OidcConstants.ROLE_GESUCHSTELLER)) {
            editStates.addAll(GesuchTrancheStatus.GESUCHSTELLER_CAN_EDIT);
        }

        if (identifiers.contains(OidcConstants.ROLE_SACHBEARBEITER)) {
            editStates.addAll(GesuchTrancheStatus.SACHBEARBEITER_CAN_EDIT);
        }

        if (identifiers.contains(OidcConstants.ROLE_ADMIN)) {
            editStates.addAll(GesuchTrancheStatus.ADMIN_CAN_EDIT);
        }

        return editStates.contains(gesuchTrancheStatus);
    }

    StateMachine<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachine(
        final GesuchTranche gesuchTranche
    ) {
        StateMachineUtil.addExit(
            config,
            transition -> validatorService.validateGesuchTrancheForStatus(gesuchTranche, transition.getDestination()),
            GesuchTrancheStatus.values()
        );

        return new StateMachine<>(
            gesuchTranche.getStatus(),
            gesuchTranche::getStatus,
            gesuchTranche::setStatus,
            config
        );
    }
}
