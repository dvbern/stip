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

import ch.dvbern.stip.api.ausbildung.entity.AusbildungsgangOld;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungsstaetteOld;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.bildungskategorie.repo.BildungskategorieRepository;
import ch.dvbern.stip.api.util.TestConstants;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AusbildungTestSeeding extends Seeder {
    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final BildungskategorieRepository bildungskategorieRepository;

    private Bildungskategorie bildungskategorie;

    @Override
    protected void seed() {
        createBildungskategorie();
        seedUni();
        seedFh();
    }

    @Override
    protected List<String> getProfiles() {
        return List.of("test");
    }

    protected void createBildungskategorie() {
        final var bildungskategorieTertiaer = new Bildungskategorie()
            .setBezeichnungDe("Test Beschreibung")
            .setBezeichnungFr("Test Description")
            .setBfs(10);

        bildungskategorieRepository.persistAndFlush(bildungskategorieTertiaer);

        final var bildungskategorieSekundaer = new Bildungskategorie()
            .setBezeichnungDe("Test Beschreibung")
            .setBezeichnungFr("Test Description")
            .setBfs(1);

        bildungskategorieRepository.persistAndFlush(bildungskategorieSekundaer);
        bildungskategorie = bildungskategorieSekundaer;
        TestConstants.TEST_BILDUNGSKATEGORIE_ID = bildungskategorieSekundaer.getId();
    }

    private void seedUni() {
        final var uniBern = new AusbildungsstaetteOld()
            .setNameDe("Uni Bern")
            .setNameFr("Uni Berne");

        ausbildungsstaetteRepository.persistAndFlush(uniBern);
        TestConstants.TEST_AUSBILDUNGSSTAETTE_ID = uniBern.getId();

        final var uniBeGang1 = (AusbildungsgangOld) new AusbildungsgangOld()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setBildungskategorie(bildungskategorie)
            .setAusbildungsstaette(uniBern);

        final var uniBeGang2 = new AusbildungsgangOld()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
            .setBildungskategorie(bildungskategorie)
            .setAusbildungsstaette(uniBern);

        ausbildungsgangRepository.persist(List.of(uniBeGang1, uniBeGang2));
        TestConstants.TEST_AUSBILDUNGSGANG_ID = ausbildungsgangRepository.findAll().firstResult().getId();
    }

    private void seedFh() {
        final var bfh = new AusbildungsstaetteOld()
            .setNameDe("Berner Fachhochschule")
            .setNameFr("Haute école spécialisée bernoise");

        ausbildungsstaetteRepository.persistAndFlush(bfh);

        final var bfhGang1 = new AusbildungsgangOld()
            .setBezeichnungDe("Bsc. Informatik")
            .setBezeichnungFr("Bsc. Informatique")
            .setBildungskategorie(bildungskategorie)
            .setAusbildungsstaette(bfh);

        final var bfhGang2 = new AusbildungsgangOld()
            .setBezeichnungDe("Bsc. Biologie")
            .setBezeichnungFr("Bsc. Biologie")
            .setBildungskategorie(bildungskategorie)
            .setAusbildungsstaette(bfh);

        ausbildungsgangRepository.persist(List.of(bfhGang1, bfhGang2));
    }
}
