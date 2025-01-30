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

package ch.dvbern.stip.api.buchhaltung.service;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@Slf4j
@QuarkusTestResource(TestDatabaseEnvironment.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BuchhaltungServiceTest {
    @Inject
    BenutzerService benutzerService;

    @Inject
    FallRepository fallRepository;

    @InjectSpy
    BuchhaltungService buchhaltungService;

    Gesuch gesuch;

    private Fall getOrInitFall() {
        gesuch = GesuchGenerator.initGesuch();
        benutzerService.getOrCreateAndUpdateCurrentBenutzer();

        final var fallOpt = fallRepository.getFallForGesuchsteller(benutzerService.getCurrentBenutzer().getId());
        if (fallOpt.isPresent()) {
            return fallOpt.get();
        }

        Fall fall = new Fall();
        fall.setFallNummer("BE.T.000000");
        gesuch.getAusbildung().setFall(fall);
        fall.setGesuchsteller(benutzerService.getCurrentBenutzer());
        fallRepository.persistAndFlush(fall);
        return fall;
    }

    @Transactional
    @Test
    @TestAsSachbearbeiter
    @Order(0)
    void noBuchhaltungPresent() {
        var buchhaltungs = buchhaltungService.getAllForFallId(getOrInitFall().getId()).toList();

        assertThat(buchhaltungs.size(), is(0));
    }

    @Transactional
    @Test
    @TestAsSachbearbeiter
    @Order(1)
    void createBuchhaltung() {
        Fall fall = getOrInitFall();
        final int betrag = 525;
        final String testComment = "testtest";

        buchhaltungService.createBuchaltungForFallAndGesuch(
            fall,
            null,
            BuchhaltungType.SALDOAENDERUNG,
            betrag,
            testComment
        );

        var buchhaltungs = buchhaltungService.getAllForFallId(fall.getId()).toList();

        assertThat(buchhaltungs.size(), is(1));
        assertThat(buchhaltungs.get(0).getBetrag(), is(betrag));
        assertThat(buchhaltungs.get(0).getSaldo(), is(betrag));
        assertThat(buchhaltungs.get(0).getComment(), equalTo(testComment));
    }

    @Transactional
    @Test
    @TestAsSachbearbeiter
    @Order(2)
    void checkSaldo() {
        Fall fall = getOrInitFall();
        final int betrag = 525;
        final String testComment = "testtest";

        buchhaltungService.createBuchaltungForFallAndGesuch(
            fall,
            null,
            BuchhaltungType.SALDOAENDERUNG,
            betrag,
            testComment
        );

        var buchhaltungs = buchhaltungService.getAllForFallId(fall.getId()).toList();

        assertThat(buchhaltungs.size(), is(2));
        assertThat(buchhaltungs.get(buchhaltungs.size() - 1).getBetrag(), is(betrag));
        assertThat(buchhaltungs.get(buchhaltungs.size() - 1).getSaldo(), is(betrag * 2));
        assertThat(buchhaltungs.get(buchhaltungs.size() - 1).getComment(), equalTo(testComment));
    }
}
