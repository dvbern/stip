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

package ch.dvbern.stip.stipdecision.decider;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@StipDeciderTenant(MandantIdentifier.BERN)
public class BernStipDecider implements BaseStipDecider {
    @Override
    public StipDecision decide(final GesuchTranche gesuchTranche) {
        if (eingabefristAbgelaufen(gesuchTranche)) {
            return StipDecision.EINGABEFRIST_ABGELAUFEN;
        }
        if (ausbildungNichtAnerkannt(gesuchTranche)) {
            return StipDecision.AUSBILDUNG_NICHT_ANERKANNT;
        }
        if (ausbildungImLebenslauf(gesuchTranche)) {
            return StipDecision.AUSBILDUNG_IM_LEBENSLAUF;
        }
        if (ausbildungLaenger12Jahre(gesuchTranche)) {
            return StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE;
        }
        if (piaAelter35Jahre(gesuchTranche)) {
            return StipDecision.PIA_AELTER_35_JAHRE;
        }
        return StipDecision.GESUCH_VALID;
    }

    @Override
    public String getTextForDecision(final StipDecision decision, final Sprache korrespondenzSprache) {
        return switch (decision) {
            case GESUCH_VALID -> "";
            case EINGABEFRIST_ABGELAUFEN -> getEingabeFristAbgelaufenText(korrespondenzSprache).render();
            case AUSBILDUNG_NICHT_ANERKANNT -> getAusbildungNichtAnerkanntText(korrespondenzSprache).render();
            case AUSBILDUNG_IM_LEBENSLAUF -> getAusbildungImLebenslaufText(korrespondenzSprache).render();
            case AUSBILDUNGEN_LAENGER_12_JAHRE -> getAusbildungLaenger12JahreText(korrespondenzSprache).render();
            case PIA_AELTER_35_JAHRE -> getPiaAelter35JahreText(korrespondenzSprache).render();
        };
    }

    @Override
    public GesuchStatusChangeEvent getGesuchStatusChangeEvent(StipDecision decision) {
        return switch (decision) {
            case GESUCH_VALID -> GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG;
            case EINGABEFRIST_ABGELAUFEN -> GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT;
            case AUSBILDUNG_NICHT_ANERKANNT -> GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG;
            case AUSBILDUNG_IM_LEBENSLAUF, AUSBILDUNGEN_LAENGER_12_JAHRE -> GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN;
            case PIA_AELTER_35_JAHRE -> GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG;
        };
    }

    private static boolean eingabefristAbgelaufen(final GesuchTranche gesuchTranche) {
        return LocalDate.now().isAfter(gesuchTranche.getGesuch().getGesuchsperiode().getEinreichefristReduziert());
    }

    private static boolean ausbildungNichtAnerkannt(final GesuchTranche gesuchTranche) {
        return gesuchTranche.getGesuch().getAusbildung().isAusbildungNichtGefunden();
    }

    private static boolean ausbildungImLebenslauf(final GesuchTranche gesuchTranche) {
        return gesuchTranche
            .getGesuchFormular()
            .getLebenslaufItems()
            .stream()
            .anyMatch(
                lebenslaufItem -> lebenslaufItem.getBildungsart() != null
            );
    }

    private static boolean ausbildungLaenger12Jahre(final GesuchTranche gesuchTranche) {
        int monthsInAusbildung = gesuchTranche
            .getGesuchFormular()
            .getLebenslaufItems()
            .stream()
            .mapToInt(
                lebenslaufItem -> {
                    if (lebenslaufItem.getBildungsart() != null) {
                        return DateUtil.getMonthsBetween(lebenslaufItem.getVon(), lebenslaufItem.getBis());
                    }
                    return 0;
                }
            )
            .sum();

        monthsInAusbildung +=
            DateUtil.getMonthsBetween(gesuchTranche.getGesuch().getAusbildung().getAusbildungBegin(), LocalDate.now());

        return monthsInAusbildung / 12.0 >= 12.0;
    }

    private static boolean piaAelter35Jahre(final GesuchTranche gesuchTranche) {
        return DateUtil
            .getAgeInYears(gesuchTranche.getGesuchFormular().getPersonInAusbildung().getGeburtsdatum()) > 35;
    }

    private static TemplateInstance getEingabeFristAbgelaufenText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.eingabefristAbgelaufenFr();
            case DEUTSCH -> Templates.eingabefristAbgelaufenDe();
        };
    }

    private static TemplateInstance getAusbildungNichtAnerkanntText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungNichtAnerkanntFr();
            case DEUTSCH -> Templates.ausbildungNichtAnerkanntDe();
        };
    }

    private static TemplateInstance getAusbildungLaenger12JahreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungLaenger12JahreFr();
            case DEUTSCH -> Templates.ausbildungLaenger12JahreDe();
        };
    }

    private static TemplateInstance getAusbildungImLebenslaufText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungImLebenslaufFr();
            case DEUTSCH -> Templates.ausbildungImLebenslaufDe();
        };
    }

    private static TemplateInstance getPiaAelter35JahreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.piaAelter35JahreFr();
            case DEUTSCH -> Templates.piaAelter35JahreDe();
        };
    }

    private static TemplateInstance getArt32BBVText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.art32BBVFr();
            case DEUTSCH -> Templates.art32BBVDe();
        };
    }

    private static TemplateInstance getAusbildungPBIText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungBPI1Fr();
            case DEUTSCH -> Templates.ausbildungBPI1De();
        };
    }

    private static TemplateInstance getKeinWohnsitzImKantonBEText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.keinWohnsitzImKantonBEFr();
            case DEUTSCH -> Templates.keinWohnsitzImKantonBEDe();
        };
    }

    private static TemplateInstance getMehrereAusbildungswechselText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.mehrereAusbildungswechselFr();
            case DEUTSCH -> Templates.mehrereAusbildungswechselDe();
        };
    }

    private static TemplateInstance getNichtBerechtigterPersonenkreisText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.nichtBerechtigterPersonenkreisFr();
            case DEUTSCH -> Templates.nichtBerechtigterPersonenkreisDe();
        };
    }

    private static TemplateInstance getNichtEintretenText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.nichtEintretenFr();
            case DEUTSCH -> Templates.nichtEintretenDe();
        };
    }

    private static TemplateInstance getSchuljahr9Sekstufe1Text(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.schuljahr9Sekstufe1Fr();
            case DEUTSCH -> Templates.schuljahr9Sekstufe1De();
        };
    }

    private static TemplateInstance getZweitausbildungText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweitausbildungFr();
            case DEUTSCH -> Templates.zweitausbildungDe();
        };
    }

    private static TemplateInstance getZweiteAusbildungGleicherStufeBVSVorbildungText(
        final Sprache korrespondenzSprache
    ) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweiteAusbildungGleicherStufeBVSVorbildungFr();
            case DEUTSCH -> Templates.zweiteAusbildungGleicherStufeBVSVorbildungDe();
        };
    }

    private static TemplateInstance getZweiteEBALehreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweiteEbaLehreFr();
            case DEUTSCH -> Templates.zweiteEbaLehreDe();
        };
    }

    private static TemplateInstance getZweitesHochschulstudiumText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweitesHochschulstudiumFr();
            case DEUTSCH -> Templates.zweitesHochschulstudiumDe();
        };
    }

    @CheckedTemplate
    static final class Templates {
        private Templates() {}

        public static native TemplateInstance eingabefristAbgelaufenDe();

        public static native TemplateInstance eingabefristAbgelaufenFr();

        public static native TemplateInstance ausbildungNichtAnerkanntDe();

        public static native TemplateInstance ausbildungNichtAnerkanntFr();

        public static native TemplateInstance ausbildungLaenger12JahreDe();

        public static native TemplateInstance ausbildungLaenger12JahreFr();

        public static native TemplateInstance ausbildungImLebenslaufDe();

        public static native TemplateInstance ausbildungImLebenslaufFr();

        public static native TemplateInstance piaAelter35JahreDe();

        public static native TemplateInstance piaAelter35JahreFr();

        public static native TemplateInstance art32BBVDe();

        public static native TemplateInstance art32BBVFr();

        public static native TemplateInstance ausbildungBPI1De();

        public static native TemplateInstance ausbildungBPI1Fr();

        public static native TemplateInstance keinWohnsitzImKantonBEDe();

        public static native TemplateInstance keinWohnsitzImKantonBEFr();

        public static native TemplateInstance mehrereAusbildungswechselDe();

        public static native TemplateInstance mehrereAusbildungswechselFr();

        public static native TemplateInstance nichtBerechtigterPersonenkreisDe();

        public static native TemplateInstance nichtBerechtigterPersonenkreisFr();

        public static native TemplateInstance nichtEintretenDe();

        public static native TemplateInstance nichtEintretenFr();

        public static native TemplateInstance schuljahr9Sekstufe1De();

        public static native TemplateInstance schuljahr9Sekstufe1Fr();

        public static native TemplateInstance zweitausbildungDe();

        public static native TemplateInstance zweitausbildungFr();

        public static native TemplateInstance zweiteAusbildungGleicherStufeBVSVorbildungDe();

        public static native TemplateInstance zweiteAusbildungGleicherStufeBVSVorbildungFr();

        public static native TemplateInstance zweiteEbaLehreDe();

        public static native TemplateInstance zweiteEbaLehreFr();

        public static native TemplateInstance zweitesHochschulstudiumDe();

        public static native TemplateInstance zweitesHochschulstudiumFr();
    }
}
