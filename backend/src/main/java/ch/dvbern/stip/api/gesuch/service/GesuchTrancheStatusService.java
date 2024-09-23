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
        StateMachineUtil.addExit(
            config,
            transition -> validatorService.validateGesuchTrancheForStatus(gesuchTranche, transition.getDestination()),
            GesuchTrancheStatus.values()
        );

        final var sm = new StateMachine<>(
            gesuchTranche.getStatus(),
            gesuchTranche::getStatus,
            gesuchTranche::setStatus,
            config
        );

        sm.fire(GesuchTrancheStatusChangeEventTrigger.createTrigger(event), gesuchTranche);
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
}
