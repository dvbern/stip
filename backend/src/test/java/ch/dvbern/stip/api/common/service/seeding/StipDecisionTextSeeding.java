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
import ch.dvbern.stip.stipdecision.service.StipDecisionService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StipDecisionTextSeeding extends Seeder {
    private final ConfigService configService;
    private final StipDecisionTextRepository decisionTextRepository;
    private final StipDecisionService stipDecisionService;

    private Set<StipDecisionText> getDecisionTexts() {
        return Set.of(
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_NICHT_ANERKANNT)
                .setTextDe(
                    stipDecisionService.getTextForDecision(StipDecision.AUSBILDUNG_NICHT_ANERKANNT, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService
                        .getTextForDecision(StipDecision.AUSBILDUNG_NICHT_ANERKANNT, Sprache.FRANZOESISCH)
                ),

            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_IM_LEBENSLAUF)
                .setTextDe(
                    stipDecisionService.getTextForDecision(StipDecision.AUSBILDUNG_IM_LEBENSLAUF, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.AUSBILDUNG_IM_LEBENSLAUF, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE)
                .setTextDe(
                    stipDecisionService.getTextForDecision(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService
                        .getTextForDecision(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.EINGABEFRIST_ABGELAUFEN)
                .setTextDe(
                    stipDecisionService.getTextForDecision(StipDecision.EINGABEFRIST_ABGELAUFEN, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.EINGABEFRIST_ABGELAUFEN, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.PIA_AELTER_35_JAHRE)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.PIA_AELTER_35_JAHRE, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.PIA_AELTER_35_JAHRE, Sprache.FRANZOESISCH)
                ),

            new StipDecisionText().setStipDecision(StipDecision.NICHT_EINTRETEN)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.NICHT_EINTRETEN, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.NICHT_EINTRETEN, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.NICHTBERECHTIGTER_PERSONENKREIS)
                .setTextDe(
                    stipDecisionService
                        .getTextForDecision(StipDecision.NICHTBERECHTIGTER_PERSONENKREIS, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService
                        .getTextForDecision(StipDecision.NICHTBERECHTIGTER_PERSONENKREIS, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.KEIN_WOHNSITZ_KANTON_BE)
                .setTextDe(
                    stipDecisionService.getTextForDecision(StipDecision.KEIN_WOHNSITZ_KANTON_BE, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.KEIN_WOHNSITZ_KANTON_BE, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.SCHULJAHR_9_SEKSTUFE_1)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.SCHULJAHR_9_SEKSTUFE_1, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.SCHULJAHR_9_SEKSTUFE_1, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_PBI1)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.AUSBILDUNG_PBI1, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.AUSBILDUNG_PBI1, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.ART_32_BBV)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.ART_32_BBV, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.ART_32_BBV, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.ZWEITAUSBILDUNG)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.ZWEITAUSBILDUNG, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.ZWEITAUSBILDUNG, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG)
                .setTextDe(
                    stipDecisionService
                        .getTextForDecision(
                            StipDecision.AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG,
                            Sprache.DEUTSCH
                        )
                )
                .setTextFr(
                    stipDecisionService.getTextForDecision(
                        StipDecision.AUSBILDUNG_2_GLEICHE_STUFE_BVS_ODER_VORBILDUNG,
                        Sprache.FRANZOESISCH
                    )
                ),
            new StipDecisionText().setStipDecision(StipDecision.EBA_LEHRE)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.EBA_LEHRE, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.EBA_LEHRE, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.HOCHSCHULSTUDIUM_2)
                .setTextDe(stipDecisionService.getTextForDecision(StipDecision.HOCHSCHULSTUDIUM_2, Sprache.DEUTSCH))
                .setTextFr(
                    stipDecisionService.getTextForDecision(StipDecision.HOCHSCHULSTUDIUM_2, Sprache.FRANZOESISCH)
                ),
            new StipDecisionText().setStipDecision(StipDecision.MEHRERE_AUSBILDUNGSWECHSEL)
                .setTextDe(
                    stipDecisionService.getTextForDecision(StipDecision.MEHRERE_AUSBILDUNGSWECHSEL, Sprache.DEUTSCH)
                )
                .setTextFr(
                    stipDecisionService
                        .getTextForDecision(StipDecision.MEHRERE_AUSBILDUNGSWECHSEL, Sprache.FRANZOESISCH)
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
        return List.of("test");
        // return configService.getSeedTestcasesOnProfile();
    }
}
