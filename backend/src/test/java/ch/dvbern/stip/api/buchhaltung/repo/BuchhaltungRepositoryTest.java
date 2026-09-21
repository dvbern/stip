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

package ch.dvbern.stip.api.buchhaltung.repo;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.entity.SapDeliverysLengthConstraintValidator;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BuchhaltungRepositoryTest {

    @Inject
    BuchhaltungRepository buchhaltungRepository;

    @Inject
    SapDeliveryRepository sapDeliveryRepository;

    @Inject
    FallRepository fallRepository;

    @Inject
    BenutzerService benutzerService;

    @Inject
    TransactionManager tm;

    private Fall fall;

    @Transactional
    @BeforeEach
    @TestAsSachbearbeiter
    void setUp() {
        benutzerService.getOrCreateAndUpdateCurrentBenutzer();
        fall = fallRepository.findFallForGsOptional(benutzerService.getCurrentBenutzer().getId())
            .orElseGet(() -> {
                final var newFall = new Fall();
                newFall.setFallNummer("BE.T.999999");
                newFall.setGesuchsteller(benutzerService.getCurrentBenutzer());
                fallRepository.persistAndFlush(newFall);
                return newFall;
            });
    }

    @Transactional
    Buchhaltung createBuchhaltung(final BuchhaltungType type) {
        final var b = new Buchhaltung();
        b.setBuchhaltungType(type);
        b.setBetrag(100);
        b.setSaldo(100);
        b.setComment("test");
        b.setFall(fall);
        buchhaltungRepository.persistAndFlush(b);
        return b;
    }

    @Transactional
    void addSapDelivery(final Buchhaltung buchhaltung, final SapStatus status) {
        final var delivery = new SapDelivery();
        delivery.setSapStatus(status);
        delivery.setBuchhaltung(buchhaltung);
        sapDeliveryRepository.persistAndFlush(delivery);
        buchhaltung.getSapDeliverys().add(delivery);
    }

    @Test
    @Transactional
    @TestAsSachbearbeiter
    @SneakyThrows
    void businesspartnerAction_noDeliveries() {
        final var b = createBuchhaltung(BuchhaltungType.BUSINESSPARTNER_CREATE);

        final var result = buchhaltungRepository
            .findPendingBusinesspartnerActionBuchhaltung()
            .toList();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), org.hamcrest.Matchers.is(b.getId()));
        tm.setRollbackOnly();
    }

    @Test
    @Transactional
    @TestAsSachbearbeiter
    @SneakyThrows
    void businesspartnerAction_inProgressDelivery() {
        final var b = createBuchhaltung(BuchhaltungType.BUSINESSPARTNER_CREATE);
        addSapDelivery(b, SapStatus.IN_PROGRESS);

        final var result = buchhaltungRepository
            .findPendingBusinesspartnerActionBuchhaltung()
            .toList();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), org.hamcrest.Matchers.is(b.getId()));
        tm.setRollbackOnly();
    }

    @Test
    @Transactional
    @TestAsSachbearbeiter
    @SneakyThrows
    void businesspartnerAction_oneFailureDelivery() {
        final var b = createBuchhaltung(BuchhaltungType.BUSINESSPARTNER_CREATE);
        addSapDelivery(b, SapStatus.FAILURE);

        final var result = buchhaltungRepository
            .findPendingBusinesspartnerActionBuchhaltung()
            .toList();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), org.hamcrest.Matchers.is(b.getId()));
        tm.setRollbackOnly();
    }

    @Test
    @Transactional
    @TestAsSachbearbeiter
    @SneakyThrows
    void businesspartnerAction_successDelivery() {
        final var b = createBuchhaltung(BuchhaltungType.BUSINESSPARTNER_CREATE);
        addSapDelivery(b, SapStatus.SUCCESS);

        final var result = buchhaltungRepository
            .findPendingBusinesspartnerActionBuchhaltung()
            .toList();

        assertThat(result, empty());
        tm.setRollbackOnly();
    }

    @Test
    @Transactional
    @TestAsSachbearbeiter
    @SneakyThrows
    void businesspartnerAction_maxFailedDeliveries() {
        final var b = createBuchhaltung(BuchhaltungType.BUSINESSPARTNER_CREATE);
        for (int i = 0; i < SapDeliverysLengthConstraintValidator.MAX_SAP_DELIVERYS_BUSINESSPARTNER_ACTION; i++) {
            addSapDelivery(b, SapStatus.FAILURE);
        }

        final var result = buchhaltungRepository
            .findPendingBusinesspartnerActionBuchhaltung()
            .toList();

        assertThat(result, empty());
        tm.setRollbackOnly();
    }

    @Test
    @Transactional
    @TestAsSachbearbeiter
    @SneakyThrows
    void businesspartnerAction_successAndFailureDeliveries() {
        final var b = createBuchhaltung(BuchhaltungType.BUSINESSPARTNER_CREATE);
        addSapDelivery(b, SapStatus.FAILURE);
        addSapDelivery(b, SapStatus.SUCCESS);

        final var result = buchhaltungRepository
            .findPendingBusinesspartnerActionBuchhaltung()
            .toList();

        assertThat(result, empty());
        tm.setRollbackOnly();
    }
}
