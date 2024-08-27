package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.GesuchStateMachineUtil;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchStatusService {
    private final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config;
    private final GesuchValidatorService validationService;

    @Transactional
    public void triggerStateMachineEvent(final Gesuch gesuch, final GesuchStatusChangeEvent event) {
        GesuchStateMachineUtil.addExit(
            config,
            transition -> validationService.validateGesuchForStatus(gesuch, transition.getDestination())
        );

        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );

        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(event), gesuch);
    }

    public boolean benutzerCanEdit(final Benutzer benutzer, final Gesuchstatus gesuchstatus) {
        final var identifiers = benutzer.getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());
        final var editStates = new HashSet<Gesuchstatus>();
        if (identifiers.contains(OidcConstants.ROLE_GESUCHSTELLER)) {
            editStates.addAll(Gesuchstatus.GESUCHSTELLER_CAN_EDIT);
        }
        if (identifiers.contains(OidcConstants.ROLE_SACHBEARBEITER)) {
            editStates.addAll(Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
        }
        if (identifiers.contains(OidcConstants.ROLE_ADMIN)) {
            editStates.addAll(Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
        }

        return editStates.contains(gesuchstatus);
    }
}
