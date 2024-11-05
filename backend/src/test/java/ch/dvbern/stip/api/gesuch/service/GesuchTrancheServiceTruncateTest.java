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

package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GesuchTrancheServiceTruncateTest {
    @Test
    void oneExistingTruncated() {
        // Arrange
        final var existingTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
            )
        );
        final var gesuch = new Gesuch();
        gesuch.getGesuchTranchen().add(existingTranche);

        final var newTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var service = getDummyTrancheService();

        // Act
        service.truncateExistingTranchen(gesuch, newTranche);
        gesuch.getGesuchTranchen().add(newTranche);

        // Assert
        assertThat(gesuch.getGesuchTranchen().size(), is(2));
        assertThat(existingTranche.getGueltigkeit().getGueltigBis(), is(LocalDate.of(2024, 5, 31)));
    }

    @Test
    void oneExistingSplitInsert() {
        // Arrange
        final var existingTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var gesuch = new Gesuch();
        gesuch.getGesuchTranchen().add(existingTranche);
        gesuch.setGesuchsperiode(
            new Gesuchsperiode()
                .setGesuchsperiodeStart(LocalDate.of(2024, 1, 1))
                .setGesuchsperiodeStopp(LocalDate.of(2024, 12, 31))
        );

        existingTranche.setGesuch(gesuch);

        final var newTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 5, 31)
            )
        );

        final var service = getDummyTrancheService();

        // Act
        service.truncateExistingTranchen(gesuch, newTranche);
        gesuch.getGesuchTranchen().add(newTranche);

        // Assert
        assertThat(gesuch.getGesuchTranchen().size(), is(3));
    }

    @Test
    void twoExistingBothTruncated() {
        // Arrange
        final var existingTranche1 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
            )
        );

        final var existingTranche2 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var gesuch = new Gesuch();
        gesuch.getGesuchTranchen().addAll(List.of(existingTranche1, existingTranche2));

        final var newTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 5, 31)
            )
        );

        final var service = getDummyTrancheService();

        // Act
        service.truncateExistingTranchen(gesuch, newTranche);
        gesuch.getGesuchTranchen().add(newTranche);

        // Assert
        assertThat(gesuch.getGesuchTranchen().size(), is(3));
        assertThat(
            existingTranche1.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2024, 2, 1).with(lastDayOfMonth()))
        );
        assertThat(
            existingTranche2.getGueltigkeit().getGueltigAb(),
            is(LocalDate.of(2024, 6, 1))
        );
    }

    @Test
    void twoExistingOneTruncated() {
        // Arrange
        final var oneId = UUID.fromString("12154079-9622-4869-b752-435604368cde");
        final var existingTranche1 = (GesuchTranche) getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
            )
        )
            .setId(oneId);

        final var existingTranche2 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var gesuch = new Gesuch();
        gesuch.getGesuchTranchen().addAll(List.of(existingTranche1, existingTranche2));

        final var newTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var service = getDummyTrancheService();

        // Act
        service.truncateExistingTranchen(gesuch, newTranche);
        gesuch.getGesuchTranchen().add(newTranche);

        // Assert
        assertThat(gesuch.getGesuchTranchen().size(), is(3));
        assertThat(
            existingTranche1.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2024, 3, 31))
        );
        assertThat(
            existingTranche2.getGueltigkeit().getGueltigAb(),
            is(LocalDate.of(2024, 4, 1))
        );
        assertThat(
            existingTranche2.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2024, 4, 30))
        );
    }

    @Test
    void twoExistingOneTruncatedOneDeleted() {
        // Arrange
        final var existingTranche1 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
            )
        );

        final var existingTranche2 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var gesuch = new Gesuch();
        gesuch.getGesuchTranchen().addAll(List.of(existingTranche1, existingTranche2));

        final var newTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 12, 31)
            )
        );

        final var service = getDummyTrancheService();

        // Act
        service.truncateExistingTranchen(gesuch, newTranche);
        gesuch.getGesuchTranchen().add(newTranche);

        // Assert
        assertThat(gesuch.getGesuchTranchen().size(), is(2));
        assertThat(
            existingTranche1.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2024, 2, 1).with(lastDayOfMonth()))
        );
        assertThat(
            existingTranche2.getGueltigkeit().getMonths(),
            is(0)
        );
    }

    @Test
    void threeExistingTwoTruncated() {
        // Arrange
        final var existingTranche1 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 11, 1).with(lastDayOfMonth())
            )
        );

        final var existingTranche2 = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2025, 2, 1).with(lastDayOfMonth())
            )
        );

        final var existingTranche3 = getDummyTranche(
            new DateRange(
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 12, 1).with(lastDayOfMonth())
            )
        );

        final var gesuch = new Gesuch();
        gesuch.getGesuchTranchen().addAll(List.of(existingTranche1, existingTranche2, existingTranche3));

        final var newTranche = getDummyTranche(
            new DateRange(
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 1).with(lastDayOfMonth())
            )
        );

        final var service = getDummyTrancheService();

        // Act
        service.truncateExistingTranchen(gesuch, newTranche);
        gesuch.getGesuchTranchen().add(newTranche);

        // Assert
        assertThat(gesuch.getGesuchTranchen().size(), is(4));

        // Tranche 1 bis changed
        assertThat(
            existingTranche1.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2024, 10, 1).with(lastDayOfMonth()))
        );

        // Tranche 2 unchanged
        assertThat(
            existingTranche2.getGueltigkeit().getGueltigAb(),
            is(LocalDate.of(2024, 12, 1))
        );
        assertThat(
            existingTranche2.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2025, 2, 1).with(lastDayOfMonth()))
        );

        // Tranche 3 unchanged
        assertThat(
            existingTranche3.getGueltigkeit().getGueltigAb(),
            is(LocalDate.of(2025, 3, 1))
        );
        assertThat(
            existingTranche3.getGueltigkeit().getGueltigBis(),
            is(LocalDate.of(2025, 12, 1).with(lastDayOfMonth()))
        );
    }

    private GesuchTrancheTruncateService getDummyTrancheService() {
        final var mockTrancheRepo = Mockito.mock(GesuchTrancheRepository.class);
        Mockito.doNothing().when(mockTrancheRepo).delete(Mockito.any());

        return new GesuchTrancheTruncateService(
            mockTrancheRepo
        );
    }

    private GesuchTranche getDummyTranche(final DateRange gueltigkeit) {
        return ((GesuchTranche) new GesuchTranche()
            .setGueltigkeit(gueltigkeit)
            .setStatus(GesuchTrancheStatus.UEBERPRUEFEN)
            .setTyp(GesuchTrancheTyp.TRANCHE)
            .setGesuchFormular(getDummyFormular())
            .setId(UUID.randomUUID()));
    }

    private GesuchFormular getDummyFormular() {
        return new GesuchFormular()
            .setPersonInAusbildung(new PersonInAusbildung().setAdresse(new Adresse()))
            .setAusbildung(new Ausbildung())
            .setFamiliensituation(new Familiensituation())
            .setPartner(null)
            .setAuszahlung(new Auszahlung().setKontoinhaber(Kontoinhaber.GESUCHSTELLER))
            .setEinnahmenKosten(new EinnahmenKosten());
    }
}
