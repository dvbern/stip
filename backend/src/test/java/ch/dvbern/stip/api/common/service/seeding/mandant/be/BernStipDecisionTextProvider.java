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

package ch.dvbern.stip.api.common.service.seeding.mandant.be;

import java.util.Set;

import ch.dvbern.stip.api.common.service.seeding.BaseStipDecisionTextProvider;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.stipdecision.decider.StipDeciderTenant;
import ch.dvbern.stip.stipdecision.entity.StipDecisionText;
import jakarta.inject.Singleton;

import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getArt32BBVText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getAusbildungImLebenslaufText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getAusbildungLaenger12JahreText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getAusbildungNichtAnerkanntText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getAusbildungPBIText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getEingabeFristAbgelaufenText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getKeinWohnsitzImKantonBEText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getMehrereAusbildungswechselText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getNichtBerechtigterPersonenkreisText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getPiaAelter35JahreText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getSchuljahr9Sekstufe1Text;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getZweitausbildungText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getZweiteAusbildungGleicherStufeBVSVorbildungText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getZweiteEBALehreText;
import static ch.dvbern.stip.api.common.service.seeding.mandant.be.BernStipDecisionTextSeedingUtil.getZweitesHochschulstudiumText;

@Singleton
@StipDeciderTenant(MandantIdentifier.BERN)
public class BernStipDecisionTextProvider extends BaseStipDecisionTextProvider {
    @Override
    public Set<StipDecisionText> getDecisionTexts() {
        return Set.of(
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_NICHT_ANERKANNT)
                .setTextDe(
                    getAusbildungNichtAnerkanntText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getAusbildungNichtAnerkanntText(Sprache.FRANZOESISCH).render()

                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_IM_LEBENSLAUF)
                .setTextDe(
                    getAusbildungImLebenslaufText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getAusbildungImLebenslaufText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE)
                .setTextDe(
                    getAusbildungLaenger12JahreText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getAusbildungLaenger12JahreText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.EINGABEFRIST_ABGELAUFEN)
                .setTextDe(
                    getEingabeFristAbgelaufenText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getEingabeFristAbgelaufenText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.PIA_AELTER_35_JAHRE)
                .setTextDe(getPiaAelter35JahreText(Sprache.DEUTSCH).render())
                .setTextFr(
                    getPiaAelter35JahreText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.NICHTBERECHTIGTER_PERSONENKREIS)
                .setTextDe(
                    getNichtBerechtigterPersonenkreisText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getNichtBerechtigterPersonenkreisText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.KEIN_WOHNSITZ_KANTON_BE)
                .setTextDe(
                    getKeinWohnsitzImKantonBEText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getKeinWohnsitzImKantonBEText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.SCHULJAHR_9_SEKSTUFE_1)
                .setTextDe(getSchuljahr9Sekstufe1Text(Sprache.DEUTSCH).render())
                .setTextFr(
                    getSchuljahr9Sekstufe1Text(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_PBI1)
                .setTextDe(getAusbildungPBIText(Sprache.DEUTSCH).render())
                .setTextFr(
                    getAusbildungPBIText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.ART_32_BBV)
                .setTextDe(getArt32BBVText(Sprache.DEUTSCH).render())
                .setTextFr(
                    getArt32BBVText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.ZWEITAUSBILDUNG)
                .setTextDe(getZweitausbildungText(Sprache.DEUTSCH).render())
                .setTextFr(
                    getZweitausbildungText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG)
                .setTextDe(
                    getZweiteAusbildungGleicherStufeBVSVorbildungText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getZweiteAusbildungGleicherStufeBVSVorbildungText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.EBA_LEHRE)
                .setTextDe(getZweiteEBALehreText(Sprache.DEUTSCH).render())
                .setTextFr(
                    getZweiteEBALehreText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.HOCHSCHULSTUDIUM_2)
                .setTextDe(getZweitesHochschulstudiumText(Sprache.DEUTSCH).render())
                .setTextFr(
                    getZweitesHochschulstudiumText(Sprache.FRANZOESISCH).render()
                ),
            new StipDecisionText().setStipDecision(StipDecision.MEHRERE_AUSBILDUNGSWECHSEL)
                .setTextDe(
                    getMehrereAusbildungswechselText(Sprache.DEUTSCH).render()
                )
                .setTextFr(
                    getMehrereAusbildungswechselText(Sprache.FRANZOESISCH).render()
                )
        );
    }
}
