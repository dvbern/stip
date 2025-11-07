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

package ch.dvbern.stip.api.common.statemachines.gesuch;

import java.util.EnumMap;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.AenderungZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.BereitFuerBearbeitungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.FehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.GesuchZurueckweisenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.JuristischeAbklaerungDurchPruefungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.KomplettEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.NegativeVerfuegungHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.StipendienAnspruchHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungDruckbereitHandler;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungVersendetHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchStatusConfigProducer {
    private final GesuchFehlendeDokumenteNichtEingereichtHandler gesuchFehlendeDokumenteNichtEingereichtHandler;
    private final GesuchZurueckweisenHandler gesuchZurueckweisenHandler;
    private final KomplettEingereichtHandler komplettEingereichtHandler;
    private final FehlendeDokumenteEinreichenHandler fehlendeDokumenteEinreichenHandler;
    private final FehlendeDokumenteHandler fehlendeDokumenteHandler;
    private final VerfuegungDruckbereitHandler verfuegungDruckbereitHandler;
    private final VerfuegungVersendetHandler verfuegungVersendetHandler;
    private final NegativeVerfuegungHandler negativeVerfuegungHandler;
    private final AenderungZurueckweisenHandler aenderungZurueckweisenHandler;
    private final AenderungFehlendeDokumenteNichtEingereichtHandler aenderungFehlendeDokumenteNichtEingereichtHandler;
    private final StipendienAnspruchHandler stipendienAnspruchHandler;
    private final JuristischeAbklaerungDurchPruefungHandler juristischeAbklaerungDurchPruefungHandler;
    private final StatusprotokollService statusprotokollService;
    private final BereitFuerBearbeitungHandler bereitFuerBearbeitungHandler;

    public StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> createStateMachineConfig() {
        final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config = new StateMachineConfig<>();
        final var triggers =
            new EnumMap<GesuchStatusChangeEvent, TriggerWithParameters1<Gesuch, GesuchStatusChangeEvent>>(
                GesuchStatusChangeEvent.class
            );

        for (GesuchStatusChangeEvent event : GesuchStatusChangeEvent.values()) {
            triggers.put(event, config.setTriggerParameters(event, Gesuch.class));
        }

        config.configure(Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.EINGEREICHT, Gesuchstatus.EINGEREICHT)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT),
                gesuchFehlendeDokumenteNichtEingereichtHandler::handle
            )
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.GESUCH_ZURUECKWEISEN),
                gesuchZurueckweisenHandler::handle
            );

        config.configure(Gesuchstatus.EINGEREICHT)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_PRUEFEN, Gesuchstatus.ANSPRUCH_PRUEFEN)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.EINGEREICHT),
                komplettEingereichtHandler::handle
            );

        config.configure(Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_PRUEFEN, Gesuchstatus.ANSPRUCH_PRUEFEN)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG);

        config.configure(Gesuchstatus.ANSPRUCH_PRUEFEN)
            .permit(
                GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
                Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG
            )
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG_DURCH_PRUEFUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN, Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT, Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT, Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT);

        config.configure(Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .permit(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT, Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT);

        config.configure(Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT, Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG);

        config.configure(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_SB, Gesuchstatus.IN_BEARBEITUNG_SB)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN),
                fehlendeDokumenteEinreichenHandler::handle
            )
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG),
                bereitFuerBearbeitungHandler::handle
            );

        config.configure(Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.IN_FREIGABE, Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.GESUCH_ZURUECKWEISEN, Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_PRUEFEN, Gesuchstatus.ANSPRUCH_PRUEFEN)
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_KEIN_STIPENDIENANSPRUCH,
                Gesuchstatus.KEIN_STIPENDIENANSPRUCH
            )
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_STIPENDIENANSPRUCH,
                Gesuchstatus.STIPENDIENANSPRUCH
            );

        config.configure(Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT, Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH,
                Gesuchstatus.KEIN_STIPENDIENANSPRUCH
            )
            .permit(
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH,
                Gesuchstatus.STIPENDIENANSPRUCH
            )
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE),
                fehlendeDokumenteHandler::handle
            );

        config.configure(Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG_DURCH_PRUEFUNG),
                juristischeAbklaerungDurchPruefungHandler::handle
            )
            .permit(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT, Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT);

        config.configure(Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(
                GesuchStatusChangeEvent.DATENSCHUTZBRIEF_AM_GENERIEREN,
                Gesuchstatus.DATENSCHUTZBRIEF_AM_GENERIEREN
            );

        config.configure(Gesuchstatus.DATENSCHUTZBRIEF_AM_GENERIEREN)
            .permit(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_VERSANDBEREIT, Gesuchstatus.DATENSCHUTZBRIEF_VERSANDBEREIT)
            .permit(GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT, Gesuchstatus.DATENSCHUTZBRIEF_DRUCKBEREIT);

        config.configure(Gesuchstatus.DATENSCHUTZBRIEF_VERSANDBEREIT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.WARTEN_AUF_UNTERSCHRIFTENBLATT, Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT, Gesuchstatus.VERFUEGUNG_DRUCKBEREIT);

        config.configure(Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT, Gesuchstatus.VERFUEGUNG_DRUCKBEREIT);

        config.configure(Gesuchstatus.VERFUEGUNG_DRUCKBEREIT)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT),
                verfuegungDruckbereitHandler::handle
            )
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_AM_GENERIEREN, Gesuchstatus.VERFUEGUNG_AM_GENERIEREN)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_VERSENDET, Gesuchstatus.VERFUEGUNG_VERSENDET);

        config.configure(Gesuchstatus.VERFUEGUNG_AM_GENERIEREN)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_VERSANDBEREIT, Gesuchstatus.VERFUEGUNG_VERSANDBEREIT)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT, Gesuchstatus.VERFUEGUNG_DRUCKBEREIT);

        config.configure(Gesuchstatus.VERFUEGUNG_VERSANDBEREIT)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_VERSENDET, Gesuchstatus.VERFUEGUNG_VERSENDET);

        config.configure(Gesuchstatus.VERFUEGUNG_VERSENDET)
            .permit(GesuchStatusChangeEvent.KEIN_STIPENDIENANSPRUCH, Gesuchstatus.KEIN_STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.STIPENDIENANSPRUCH, Gesuchstatus.STIPENDIENANSPRUCH)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.VERFUEGUNG_VERSENDET),
                verfuegungVersendetHandler::handle
            );

        config.configure(Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .permit(GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT, Gesuchstatus.VERFUEGUNG_DRUCKBEREIT)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG),
                negativeVerfuegungHandler::handle
            );
        // These aren't strictly necessary, but the Statusdiagramm isn't 100% complete yet and these are likely needed
        config.configure(Gesuchstatus.NICHT_BEITRAGSBERECHTIGT);

        config.configure(Gesuchstatus.KEIN_STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN, Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .onEntryFrom(
                triggers.get(
                    GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_KEIN_STIPENDIENANSPRUCH
                ),
                aenderungZurueckweisenHandler::handle
            )
            .onEntryFrom(
                triggers.get(
                    GesuchStatusChangeEvent.GESUCH_AENDERUNG_FEHLENDE_DOKUMENTE_KEIN_STIPENDIENANSPRUCH
                ),
                aenderungFehlendeDokumenteNichtEingereichtHandler::handle
            );

        config.configure(Gesuchstatus.STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.AENDERUNG_AKZEPTIEREN, Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .onEntryFrom(
                triggers.get(GesuchStatusChangeEvent.STIPENDIENANSPRUCH),
                stipendienAnspruchHandler::handle
            )
            .onEntryFrom(
                triggers.get(
                    GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_STIPENDIENANSPRUCH
                ),
                aenderungZurueckweisenHandler::handle
            )
            .onEntryFrom(
                triggers.get(
                    GesuchStatusChangeEvent.GESUCH_AENDERUNG_FEHLENDE_DOKUMENTE_STIPENDIENANSPRUCH
                ),
                aenderungFehlendeDokumenteNichtEingereichtHandler::handle
            );

        config.configure(Gesuchstatus.GESUCH_ABGELEHNT);

        for (final var status : Gesuchstatus.values()) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(this::logTransition);
        }

        return config;
    }

    private void logTransition(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
        Gesuch gesuch = extractGesuchFromStateMachineArgs(args);

        statusprotokollService.createStatusprotokoll(
            transition.getDestination().toString(),
            transition.getSource().toString(),
            StatusprotokollEntryTyp.GESUCH,
            gesuch.getComment(),
            gesuch
        );

        LOG.info(
            "KSTIP: Gesuch mit id {} wurde von Status {} nach Status {} durch event {} geandert",
            gesuch.getId(),
            transition.getSource(),
            transition.getDestination(),
            transition.getTrigger()
        );
    }

    private Gesuch extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof Gesuch gesuch)) {
            throw new AppErrorException(
                "State Transition args sollte ein Gesuch Objekt enthalten, es gibt ein Problem in den "
                + "Statemachine args"
            );
        }
        return gesuch;
    }
}
