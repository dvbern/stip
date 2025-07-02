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

package ch.dvbern.stip.api.common.statemachines.gesuchstatus;

import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.AenderungFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.AenderungZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.FehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.FehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.GesuchFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.GesuchStatusStateChangeHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.GesuchZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.KomplettEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.NegativeVerfuegungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.StipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.VerfuegtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.VersandbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.VersendetHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class GesuchStatusConfigProducer {

    public StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> createStateMachineConfig(
        Instance<GesuchStatusStateChangeHandler> handlers
    ) {
        final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config = new StateMachineConfig<>();

        config.configure(Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.EINGEREICHT, Gesuchstatus.EINGEREICHT)
            .onEntryFrom(
                selectHandlerForClass(handlers, KomplettEingereichtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.EINGEREICHT),
                (gesuch) -> selectHandlerForClass(handlers, KomplettEingereichtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.EINGEREICHT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(
                GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
                Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG
            )
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN, Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT, Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT);

        config.configure(Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG)
            .permit(GesuchStatusChangeEvent.EINGEREICHT, Gesuchstatus.EINGEREICHT)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .onEntryFrom(
                selectHandlerForClass(handlers, KomplettEingereichtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.EINGEREICHT),
                (gesuch) -> selectHandlerForClass(handlers, KomplettEingereichtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, NegativeVerfuegungHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG),
                (gesuch) -> selectHandlerForClass(handlers, NegativeVerfuegungHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .onEntryFrom(
                selectHandlerForClass(handlers, NegativeVerfuegungHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG),
                (gesuch) -> selectHandlerForClass(handlers, NegativeVerfuegungHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .onEntryFrom(
                selectHandlerForClass(handlers, NegativeVerfuegungHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG),
                (gesuch) -> selectHandlerForClass(handlers, NegativeVerfuegungHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_SB, Gesuchstatus.IN_BEARBEITUNG_SB);

        config.configure(Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.IN_FREIGABE, Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_GS, Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .onEntryFrom(
                selectHandlerForClass(handlers, GesuchZurueckweisenHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.IN_BEARBEITUNG_GS),
                (gesuch) -> selectHandlerForClass(handlers, GesuchZurueckweisenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, NegativeVerfuegungHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG),
                (gesuch) -> selectHandlerForClass(handlers, NegativeVerfuegungHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH,
                Gesuchstatus.KEIN_STIPENDIENANSPRUCH
            )
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH,
                Gesuchstatus.STIPENDIENANSPRUCH
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, AenderungZurueckweisenHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH),
                (gesuch) -> selectHandlerForClass(handlers, AenderungZurueckweisenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, AenderungZurueckweisenHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH),
                (gesuch) -> selectHandlerForClass(handlers, AenderungZurueckweisenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, FehlendeDokumenteHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE),
                (gesuch) -> selectHandlerForClass(handlers, FehlendeDokumenteHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, VerfuegtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.VERFUEGT),
                (gesuch) -> selectHandlerForClass(handlers, VerfuegtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_GS, Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH,
                Gesuchstatus.KEIN_STIPENDIENANSPRUCH
            )
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH,
                Gesuchstatus.STIPENDIENANSPRUCH
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, GesuchZurueckweisenHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.IN_BEARBEITUNG_GS),
                (gesuch) -> selectHandlerForClass(handlers, GesuchZurueckweisenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, FehlendeDokumenteEinreichenHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG),
                (gesuch) -> selectHandlerForClass(handlers, FehlendeDokumenteEinreichenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, AenderungFehlendeDokumenteNichtEingereichtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH),
                (gesuch) -> selectHandlerForClass(handlers, AenderungFehlendeDokumenteNichtEingereichtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, AenderungFehlendeDokumenteNichtEingereichtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_OR_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH),
                (gesuch) -> selectHandlerForClass(handlers, AenderungFehlendeDokumenteNichtEingereichtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, FehlendeDokumenteEinreichenHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG),
                (gesuch) -> selectHandlerForClass(handlers, FehlendeDokumenteEinreichenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            .onEntryFrom(
                selectHandlerForClass(handlers, GesuchFehlendeDokumenteNichtEingereichtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.IN_BEARBEITUNG_GS),
                (gesuch) -> selectHandlerForClass(handlers, GesuchFehlendeDokumenteNichtEingereichtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.WARTEN_AUF_UNTERSCHRIFTENBLATT, Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERSANDBEREIT, Gesuchstatus.VERSANDBEREIT)
            .onEntryFrom(
                selectHandlerForClass(handlers, VersandbereitHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.VERSANDBEREIT),
                (gesuch) -> selectHandlerForClass(handlers, VersandbereitHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .onEntryFrom(
                selectHandlerForClass(handlers, VerfuegtHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.VERFUEGT),
                (gesuch) -> selectHandlerForClass(handlers, VerfuegtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERSANDBEREIT, Gesuchstatus.VERSANDBEREIT)
            .onEntryFrom(
                selectHandlerForClass(handlers, VersandbereitHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.VERSANDBEREIT),
                (gesuch) -> selectHandlerForClass(handlers, VersandbereitHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.VERSANDBEREIT)
            .permit(GesuchStatusChangeEvent.VERSENDET, Gesuchstatus.VERSENDET)
            .onEntryFrom(
                selectHandlerForClass(handlers, VersendetHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.VERSENDET),
                (gesuch) -> selectHandlerForClass(handlers, VersendetHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.VERSENDET)
            .permit(GesuchStatusChangeEvent.KEIN_STIPENDIENANSPRUCH, Gesuchstatus.KEIN_STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.STIPENDIENANSPRUCH, Gesuchstatus.STIPENDIENANSPRUCH)
            .onEntryFrom(
                selectHandlerForClass(handlers, StipendienAnspruchHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.STIPENDIENANSPRUCH),
                (gesuch) -> selectHandlerForClass(handlers, StipendienAnspruchHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        config.configure(Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .permit(GesuchStatusChangeEvent.VERSANDBEREIT, Gesuchstatus.VERSANDBEREIT)
            .onEntryFrom(
                selectHandlerForClass(handlers, VersandbereitHandler.class).get()
                    .trigger(config, GesuchStatusChangeEvent.VERSANDBEREIT),
                (gesuch) -> selectHandlerForClass(handlers, VersandbereitHandler.class)
                    .ifPresent(handler -> handler.handle(gesuch))
            )
            ;

        // These aren't strictly necessary, but the Statusdiagramm isn't 100% complete yet and these are likely needed
        config.configure(Gesuchstatus.NICHT_BEITRAGSBERECHTIGT);

        config.configure(Gesuchstatus.KEIN_STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN, Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN, Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.GESUCH_ABGELEHNT);

        for (final var status : Gesuchstatus.values()) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(GesuchStatusConfigProducer::logTransition);
        }

        return config;
    }

    @SuppressWarnings("unchecked")
    private <T extends GesuchStatusStateChangeHandler> Optional<T> selectHandlerForClass(
        final Instance<GesuchStatusStateChangeHandler> handlers,
        final Class<T> forClass
    ) {
        return handlers.stream()
            .filter(handler -> handler.getClass().equals(forClass))
            .map(handler -> (T) handler)
            .findFirst();
    }

    private void logTransition(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
        Gesuch gesuch = extractGesuchFromStateMachineArgs(args);

        LOG.info(
            "KSTIP: Gesuch mit id {} wurde von Status {} nach Status {} durch event {} geandert",
            gesuch.getId(),
            transition.getSource(),
            transition.getDestination(),
            transition.getTrigger()
        );
    }

    private Gesuch extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof Gesuch)) {
            throw new AppErrorException(
                "State Transition args sollten einen Gesuch Objekt enthalten, es gibt ein Problem in den "
                + "Statemachine args"
            );
        }
        return (Gesuch) args[0];
    }
}
