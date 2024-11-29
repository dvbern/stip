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

package ch.dvbern.stip.api.common.service.seeding;

import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BernStipDecisionTextSeedingUtil {
    public static TemplateInstance getEingabeFristAbgelaufenText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.eingabefristAbgelaufenFr();
            case DEUTSCH -> Templates.eingabefristAbgelaufenDe();
        };
    }

    public static TemplateInstance getAusbildungNichtAnerkanntText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungNichtAnerkanntFr();
            case DEUTSCH -> Templates.ausbildungNichtAnerkanntDe();
        };
    }

    public static TemplateInstance getAusbildungLaenger12JahreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungLaenger12JahreFr();
            case DEUTSCH -> Templates.ausbildungLaenger12JahreDe();
        };
    }

    public static TemplateInstance getAusbildungImLebenslaufText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungImLebenslaufFr();
            case DEUTSCH -> Templates.ausbildungImLebenslaufDe();
        };
    }

    public static TemplateInstance getPiaAelter35JahreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.piaAelter35JahreFr();
            case DEUTSCH -> Templates.piaAelter35JahreDe();
        };
    }

    public static TemplateInstance getArt32BBVText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.art32BBVFr();
            case DEUTSCH -> Templates.art32BBVDe();
        };
    }

    public static TemplateInstance getAusbildungPBIText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.ausbildungBPI1Fr();
            case DEUTSCH -> Templates.ausbildungBPI1De();
        };
    }

    public static TemplateInstance getKeinWohnsitzImKantonBEText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.keinWohnsitzImKantonBEFr();
            case DEUTSCH -> Templates.keinWohnsitzImKantonBEDe();
        };
    }

    public static TemplateInstance getMehrereAusbildungswechselText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.mehrereAusbildungswechselFr();
            case DEUTSCH -> Templates.mehrereAusbildungswechselDe();
        };
    }

    public static TemplateInstance getNichtBerechtigterPersonenkreisText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.nichtBerechtigterPersonenkreisFr();
            case DEUTSCH -> Templates.nichtBerechtigterPersonenkreisDe();
        };
    }

    public static TemplateInstance getSchuljahr9Sekstufe1Text(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.schuljahr9Sekstufe1Fr();
            case DEUTSCH -> Templates.schuljahr9Sekstufe1De();
        };
    }

    public static TemplateInstance getZweitausbildungText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweitausbildungFr();
            case DEUTSCH -> Templates.zweitausbildungDe();
        };
    }

    public static TemplateInstance getZweiteAusbildungGleicherStufeBVSVorbildungText(
        final Sprache korrespondenzSprache
    ) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweiteAusbildungGleicherStufeBVSVorbildungFr();
            case DEUTSCH -> Templates.zweiteAusbildungGleicherStufeBVSVorbildungDe();
        };
    }

    public static TemplateInstance getZweiteEBALehreText(final Sprache korrespondenzSprache) {
        return switch (korrespondenzSprache) {
            case FRANZOESISCH -> Templates.zweiteEbaLehreFr();
            case DEUTSCH -> Templates.zweiteEbaLehreDe();
        };
    }

    public static TemplateInstance getZweitesHochschulstudiumText(final Sprache korrespondenzSprache) {
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
