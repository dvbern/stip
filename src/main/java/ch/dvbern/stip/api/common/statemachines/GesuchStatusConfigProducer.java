package ch.dvbern.stip.api.common.statemachines;

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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jboss.logging.Logger;

@Dependent
@RequiredArgsConstructor
public class GesuchStatusConfigProducer {

	private static final Logger LOG = Logger.getLogger(GesuchStatusConfigProducer.class);

	private final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config = new StateMachineConfig<>();

	private final MailService mailService;

	@Produces
	public StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> createStateMachineConfig() {
		config.configure(Gesuchstatus.OFFEN)
				.permit(GesuchStatusChangeEvent.GESUCH_EINREICHEN_EVENT, Gesuchstatus.EINGEREICHT)
				.permit(GesuchStatusChangeEvent.DOKUMENT_FEHLT_EVENT, Gesuchstatus.NICHT_KOMPLETT_EINGEREICHT)
				.onEntry(this::logTransition);

		config.configure(Gesuchstatus.NICHT_KOMPLETT_EINGEREICHT)
				.permit(GesuchStatusChangeEvent.DOKUMENT_FEHLT_NACHFRIST_EVENT, Gesuchstatus.NICHT_KOMPLETT_EINGEREICHT_NACHFRIST)
				.onEntry(this::logTransition)
				.onEntry(this::sendGesuchNichtKomplettEingereichtEmail);

		config.configure(Gesuchstatus.NICHT_KOMPLETT_EINGEREICHT_NACHFRIST)
				.onEntry(this::logTransition)
				.onEntry(this::sendGesuchNichtKomplettEingereichtNachfristEmail);
		return config;
	}

	private void logTransition(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
		Gesuch gesuch = extractGesuchFromStateMachineArgs(args);

		LOG.infof("KSTIP: Gesuch mit id %s wurde von Status %s nach Status %s durch event %s geandert", gesuch.getId(),
				transition.getSource(), transition.getDestination(), transition.getTrigger());
	}

	private Gesuch extractGesuchFromStateMachineArgs(Object[] args) {
		if (args.length == 0 || !(args[0] instanceof Gesuch)) {
			throw new AppErrorException(
					"State Transition args sollte einen Gesuch Objekt enthalten, es gibt einen Problem in die "
							+ "Statemachine args");
		}
		return (Gesuch) args[0];
	}

	private void sendGesuchNichtKomplettEingereichtEmail(
			@NonNull Transition<Gesuchstatus, GesuchStatusChangeEvent> transition,
			@NonNull Object[] args
	) {
		GesuchTranche gesuch = extractGesuchFromStateMachineArgs(args).getGesuchTranchen().get(0); //TODO in KSTIP-530
		mailService.sendGesuchNichtKomplettEingereichtEmail(
				gesuch.getGesuchFormular().getPersonInAusbildung().getNachname(),
				gesuch.getGesuchFormular().getPersonInAusbildung().getVorname(),
				gesuch.getGesuchFormular().getPersonInAusbildung().getEmail(),
				gesuch.getGesuchFormular().getPersonInAusbildung().getKorrespondenzSprache().getLocale());
	}

	private void sendGesuchNichtKomplettEingereichtNachfristEmail(
			@NonNull Transition<Gesuchstatus, GesuchStatusChangeEvent> transition,
			@NonNull Object[] args
	) {
		GesuchTranche gesuch = extractGesuchFromStateMachineArgs(args).getGesuchTranchen().get(0); //TODO in KSTIP-530
		mailService.sendGesuchNichtKomplettEingereichtNachfristEmail(
				gesuch.getGesuchFormular().getPersonInAusbildung().getNachname(),
				gesuch.getGesuchFormular().getPersonInAusbildung().getVorname(),
				gesuch.getGesuchFormular().getPersonInAusbildung().getEmail(),
				gesuch.getGesuchFormular().getPersonInAusbildung().getKorrespondenzSprache().getLocale());
	}
}
