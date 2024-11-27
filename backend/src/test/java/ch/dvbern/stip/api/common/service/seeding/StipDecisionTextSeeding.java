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
import ch.dvbern.stip.stipdecision.entity.StipDecisionText;
import ch.dvbern.stip.stipdecision.entity.StipDecisionTextRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StipDecisionTextSeeding extends Seeder {
    private final ConfigService configService;
    private final StipDecisionTextRepository decisionTextRepository;
    public static final String AUSBILDUNG_NICHT_ANERKANNT_TEXT_DE =
        "Stipendien und Darlehen können gewährt werden für den Besuch von anerkannten Ausbildungen, die zu einem anerkannten Abschluss führen. Ausbildungsstätten sind nur anerkannt, soweit sie zu einem Abschluss führen, der vom Kanton, von der Eidgenossenschaft oder von einem ausländischen Staat anerkannt ist (Art. 6 Abs. 1 und Art. 8 Abs. 2 des Gesetzes über die Ausbildungsbeiträge [ABG]).  \n"
        +
        "\n" +
        "Gemäss Ihren Angaben im Gesuch führt Ihre Ausbildung nicht zu einem anerkannten Abschluss im Sinne der Ausbildungsbeitragsgesetzgebung. Deshalb können wir Ihnen leider keine Ausbildungsbeiträge gewähren.\n"
        +
        "\n";

    public static final String AUSBILDUNG_NICHT_ANERKANNT_TEXT_FR =
        "\"Des bourses et des prêts peuvent être octroyés aux personnes qui suivent une formation reconnue permettant l’obtention d’un diplôme reconnu. Les établissements de formation ne sont reconnus que s’ils délivrent un diplôme reconnu par le canton, par la Confédération ou par un Etat étranger (art. 6, al. 1 et art. 8, al. 2 de la loi sur l’octroi de subsides de formation [LSF]).\n"
        +
        "\n" +
        "D’après les informations figurant dans votre demande, la formation que vous suivez ne permet pas d’obtenir un diplôme reconnu au sens de la législation sur l’octroi de subsides de formation. C’est pourquoi nous ne pouvons malheureusement pas vous accorder de subside de formation.\n"
        +
        "\"";
    private static Set<StipDecisionText> decisionTexts = Set.of(
        new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_NICHT_ANERKANNT)
            .setTextDe(AUSBILDUNG_NICHT_ANERKANNT_TEXT_DE)
            .setTextFr(AUSBILDUNG_NICHT_ANERKANNT_TEXT_FR),
        new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNG_IM_LEBENSLAUF)
            .setTextDe(AUSBILDUNG_NICHT_ANERKANNT_TEXT_DE)
            .setTextFr(AUSBILDUNG_NICHT_ANERKANNT_TEXT_FR),
        new StipDecisionText().setStipDecision(StipDecision.AUSBILDUNGEN_LAENGER_12_JAHRE)
            .setTextDe(AUSBILDUNG_NICHT_ANERKANNT_TEXT_DE)
            .setTextFr(AUSBILDUNG_NICHT_ANERKANNT_TEXT_FR),
        new StipDecisionText().setStipDecision(StipDecision.EINGABEFRIST_ABGELAUFEN)
            .setTextDe(AUSBILDUNG_NICHT_ANERKANNT_TEXT_DE)
            .setTextFr(AUSBILDUNG_NICHT_ANERKANNT_TEXT_FR),
        new StipDecisionText().setStipDecision(StipDecision.PIA_AELTER_35_JAHRE)
            .setTextDe(AUSBILDUNG_NICHT_ANERKANNT_TEXT_DE)
            .setTextFr(AUSBILDUNG_NICHT_ANERKANNT_TEXT_FR)
    );

    @Override
    protected void doSeed() {
        LOG.info("Starting stip decision text seeding seeding");

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
