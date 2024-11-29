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

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.stipdecision.entity.StipDecisionText;
import ch.dvbern.stip.stipdecision.entity.StipDecisionTextRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getArt32BBVText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getAusbildungImLebenslaufText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getAusbildungLaenger12JahreText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getAusbildungNichtAnerkanntText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getAusbildungPBIText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getEingabeFristAbgelaufenText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getKeinWohnsitzImKantonBEText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getMehrereAusbildungswechselText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getNichtBerechtigterPersonenkreisText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getPiaAelter35JahreText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getSchuljahr9Sekstufe1Text;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getZweitausbildungText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getZweiteAusbildungGleicherStufeBVSVorbildungText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getZweiteEBALehreText;
import static ch.dvbern.stip.api.common.service.seeding.BernStipDecisionTextSeedingUtil.getZweitesHochschulstudiumText;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StipDecisionTextSeeding extends Seeder {
    private final ConfigService configService;
    private final StipDecisionTextRepository decisionTextRepository;

    @Override
    public int getPriority() {
        return 0;
    }

    private Set<StipDecisionText> getDecisionTexts() {
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

    @Override
    protected void doSeed() {
        LOG.info("Starting stip decision text seeding seeding");
        final var decisionTexts = getDecisionTexts();
        decisionTexts.forEach(decisionText -> {
            decisionTextRepository.persistAndFlush(decisionText);
        });
        LOG.info("Finished stip decision text seeding seeding");

    }

    @Override
    protected List<String> getProfiles() {
        var profiles = configService.getSeedTestcasesOnProfile();
        // note: currently neeeds this profile to run unittests...
        profiles.add("test");
        return profiles;
    }

}
