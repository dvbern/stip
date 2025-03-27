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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.bildungskategorie.repo.BildungskategorieRepository;
import ch.dvbern.stip.api.config.service.ConfigService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class AusbildungSeeding extends Seeder {
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final BildungskategorieRepository bildungskategorieRepository;
    private final ConfigService configService;

    @Override
    protected void seed() {
        if (ausbildungsstaetteRepository.count() == 0) {
            LOG.info("Seeding Uni and FH");

            final var bildungskategorien = getBildungskategorien();
            bildungskategorieRepository.persist(bildungskategorien);
            bildungskategorieRepository.flush();

            final var ausbildungsstaetten = getAusbildungsstaetten();
            ausbildungsstaetteRepository.persist(ausbildungsstaetten);
            ausbildungsstaetteRepository.flush();

            final var ausbildunggaenge = getAusbildungsgaenge(
                ausbildungsstaetten,
                bildungskategorien
            );

            ausbildungsgangRepository.persist(ausbildunggaenge);
            ausbildungsgangRepository.flush();
        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    private static List<Bildungskategorie> getBildungskategorien() {
        return List.of(
            // Keep this order, or update dependencies as well
            createBildungskategorie("Gymnasiale Maturitätsschulen", "Ecoles de maturité gymnasiale", 2),
            createBildungskategorie("Schulen für Allgemeinbildung (Andere)", "Autres formations générales", 3),
            createBildungskategorie("Vollzeitberufsschulen", "Ecoles prof. à plein temps", 4),
            createBildungskategorie(
                "Berufslehren und Anlehren",
                "Apprentissages et form. professionelles pratiques",
                5
            ),
            createBildungskategorie(
                "Nach Berufslehre erworbene Berufsmaturitäten",
                "Maturités professionnelles accomplies après l'app.",
                6
            ),
            createBildungskategorie(
                "Höhere (nicht universitäre) Berufsbildung",
                "Formations professionelles supérieures",
                7
            ),
            createBildungskategorie("Fachhochschulen", "Hautes écoles spécialisées", 8),
            createBildungskategorie(
                "Universitäten und Eidg. Technische Hochschulen",
                "Universités et Ecoles polytechniques fédérales",
                9
            )
        );
    }

    private static Bildungskategorie createBildungskategorie(
        final String bezeichnungDe,
        final String bezeichnungFr,
        final int bfs
    ) {
        return new Bildungskategorie()
            .setBezeichnungDe(bezeichnungDe)
            .setBezeichnungFr(bezeichnungFr)
            .setBfs(bfs);
    }

    private static List<Ausbildungsstaette> getAusbildungsstaetten() {
        return List.of(
            // Keep this order, or update dependencies as well
            createAusbildungsstaette("Berner Fachhochschule", "Haute école spécialisée bernoise"),
            createAusbildungsstaette("Universität Bern", "Université de Berne"),
            createAusbildungsstaette("Lehrbetrieb", "Établissement"),
            createAusbildungsstaette("Gymnasium Lebermatt", "Lycée Lebermatt"),
            createAusbildungsstaette("BFF Bern", "BFF Berne"),
            createAusbildungsstaette("Universität Lausanne", "Université Lausanne")
        );
    }

    private static Ausbildungsstaette createAusbildungsstaette(final String nameDe, final String nameFr) {
        return new Ausbildungsstaette()
            .setNameDe(nameDe)
            .setNameFr(nameFr);
    }

    private static List<Ausbildungsgang> getAusbildungsgaenge(
        final List<Ausbildungsstaette> ausbildungsstaetten,
        final List<Bildungskategorie> bildungskategorien
    ) {
        return List.of(
            createAusbildungsgang("Bachelor", "Bachelor", ausbildungsstaetten.get(0), bildungskategorien.get(6)),
            createAusbildungsgang("Bachelor", "Bachelor", ausbildungsstaetten.get(1), bildungskategorien.get(7)),
            createAusbildungsgang("Master", "Master", ausbildungsstaetten.get(1), bildungskategorien.get(7)),
            createAusbildungsgang(
                "Lehre EBA",
                "Apprentissage AFP",
                ausbildungsstaetten.get(2),
                bildungskategorien.get(3)
            ),
            createAusbildungsgang(
                "Vorlehre",
                "Préapprentissage",
                ausbildungsstaetten.get(2),
                bildungskategorien.get(3)
            ),
            createAusbildungsgang("Maturität", "Maturité", ausbildungsstaetten.get(3), bildungskategorien.get(0)),
            createAusbildungsgang(
                "Lehre EFZ",
                "Apprentissage CFC",
                ausbildungsstaetten.get(2),
                bildungskategorien.get(3)
            ),
            createAusbildungsgang(
                "Berufsvorbereitendes Schuljahr",
                "Année scolaire de préparation professionnelle",
                ausbildungsstaetten.get(4),
                bildungskategorien.get(2)
            )
        );
    }

    private static Ausbildungsgang createAusbildungsgang(
        final String bezeichnungDe,
        final String bezeichnungFr,
        final Ausbildungsstaette ausbildungsstaette,
        final Bildungskategorie bildungskategorie
    ) {
        return new Ausbildungsgang()
            .setBezeichnungDe(bezeichnungDe)
            .setBezeichnungFr(bezeichnungFr)
            .setAusbildungsstaette(ausbildungsstaette)
            .setBildungskategorie(bildungskategorie);
    }
}
