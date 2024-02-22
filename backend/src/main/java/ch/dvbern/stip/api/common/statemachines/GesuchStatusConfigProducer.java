package ch.dvbern.stip.api.common.statemachines;

import java.util.EnumSet;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;

@Dependent
@RequiredArgsConstructor
@Slf4j
public class GesuchStatusConfigProducer {
    private final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config = new StateMachineConfig<>();

    private final MailService mailService;

    @Produces
    public StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> createStateMachineConfig() {
        config.configure(Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.EINREICHEN, Gesuchstatus.KOMPLETT_EINGEREICHT);

        config.configure(Gesuchstatus.KOMPLETT_EINGEREICHT)
            .permit(GesuchStatusChangeEvent.FEHLERHAFT_EINGEREICHT, Gesuchstatus.FEHLERHAFT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.FEHLERHAFT)
            .permit(GesuchStatusChangeEvent.NEGATIVER_ENTSCHEID, Gesuchstatus.NEGATIVER_ENTSCHEID)
            .permit(GesuchStatusChangeEvent.IN_REVIEW, Gesuchstatus.IN_REVIEW)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG, Gesuchstatus.IN_BEARBEITUNG_SB);

        config.configure(Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.IN_FREIGABE, Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.IN_REVIEW, Gesuchstatus.IN_REVIEW)
            .permit(GesuchStatusChangeEvent.ABKLAERUNG_MIT_GS, Gesuchstatus.ABKLAERUNG_MIT_GS)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE);

        config.configure(Gesuchstatus.IN_REVIEW)
            .permit(GesuchStatusChangeEvent.ABKLAERUNG_MIT_GS, Gesuchstatus.ABKLAERUNG_MIT_GS)
            .permit(GesuchStatusChangeEvent.NEGATIVER_ENTSCHEID, Gesuchstatus.NEGATIVER_ENTSCHEID)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.ABKLAERUNG_MIT_GS)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.ZURUECKGEZOGEN, Gesuchstatus.ZURUECKGEZOGEN);

        config.configure(Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_NACHFRIST, Gesuchstatus.FEHLENDE_DOKUMENTE_NACHFRIST)
            .permit(GesuchStatusChangeEvent.ZURUECKGEZOGEN, Gesuchstatus.ZURUECKGEZOGEN);

        config.configure(Gesuchstatus.FEHLENDE_DOKUMENTE_NACHFRIST)
            .permit(GesuchStatusChangeEvent.ZURUECKGEZOGEN, Gesuchstatus.ZURUECKGEZOGEN)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.ZURUECKGEWIESEN, Gesuchstatus.ZURUECKGEWIESEN)
            .permit(
                GesuchStatusChangeEvent.WARTEN_AUF_UNTERSCHRIFTENBLATT, Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT
            );

        config.configure(Gesuchstatus.ZURUECKGEWIESEN)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG, Gesuchstatus.IN_BEARBEITUNG_SB);

        config.configure(Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT);

        config.configure(Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.NEGATIVER_ENTSCHEID, Gesuchstatus.NEGATIVER_ENTSCHEID)
            .permit(GesuchStatusChangeEvent.STIPENDIUM_AKZEPTIERT, Gesuchstatus.STIPENDIUM_AKZEPTIERT);

        config.configure(Gesuchstatus.STIPENDIUM_AKZEPTIERT)
            .permit(GesuchStatusChangeEvent.STIPENDIUM_AUSBEZAHLT, Gesuchstatus.STIPENDIUM_AUSBEZAHLT);

        config.configure(Gesuchstatus.STIPENDIUM_AUSBEZAHLT);

        for (final var status : EnumSet.allOf(Gesuchstatus.class)) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.warn("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(this::logTransition);
        }

        return config;
    }

    private void logTransition(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
        Gesuch gesuch = extractGesuchFromStateMachineArgs(args);

        LOG.info("KSTIP: Gesuch mit id {} wurde von Status {} nach Status {} durch event {} geandert", gesuch.getId(),
            transition.getSource(), transition.getDestination(), transition.getTrigger()
        );
    }

    private Gesuch extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof Gesuch)) {
            throw new AppErrorException(
                "State Transition args sollte einen Gesuch Objekt enthalten, es gibt einen Problem in die "
                    + "Statemachine args");
        }
        return (Gesuch) args[0];
    }

    @SuppressWarnings("java:S1135")
    private void sendGesuchNichtKomplettEingereichtEmail(
        @NonNull Transition<Gesuchstatus, GesuchStatusChangeEvent> transition,
        @NonNull Object[] args
    ) {
        //TODO in KSTIP-530
        GesuchTranche gesuch = extractGesuchFromStateMachineArgs(args).getGesuchTranchen().get(0);
        mailService.sendGesuchNichtKomplettEingereichtEmail(
            gesuch.getGesuchFormular().getPersonInAusbildung().getNachname(),
            gesuch.getGesuchFormular().getPersonInAusbildung().getVorname(),
            gesuch.getGesuchFormular().getPersonInAusbildung().getEmail(),
            gesuch.getGesuchFormular().getPersonInAusbildung().getKorrespondenzSprache().getLocale()
        );
    }

    @SuppressWarnings("java:S1135")
    private void sendGesuchNichtKomplettEingereichtNachfristEmail(
        @NonNull Transition<Gesuchstatus, GesuchStatusChangeEvent> transition,
        @NonNull Object[] args
    ) {
        //TODO in KSTIP-530
        GesuchTranche gesuch = extractGesuchFromStateMachineArgs(args).getGesuchTranchen().get(0);
        mailService.sendGesuchNichtKomplettEingereichtNachfristEmail(
            gesuch.getGesuchFormular().getPersonInAusbildung().getNachname(),
            gesuch.getGesuchFormular().getPersonInAusbildung().getVorname(),
            gesuch.getGesuchFormular().getPersonInAusbildung().getEmail(),
            gesuch.getGesuchFormular().getPersonInAusbildung().getKorrespondenzSprache().getLocale()
        );
    }
}
