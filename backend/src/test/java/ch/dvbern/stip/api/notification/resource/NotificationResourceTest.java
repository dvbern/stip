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

package ch.dvbern.stip.api.notification.resource;

import java.util.Arrays;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.NotificationApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.NotificationDtoSpec;
import ch.dvbern.stip.generated.dto.NotificationTypeDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class NotificationResourceTest {
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    public final NotificationApiSpec notificationApiSpec =
        NotificationApiSpec.notification(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    public final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void prepare() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void verifyNotification() {
        final var notifications = notificationApiSpec.getNotificationsForFall()
            .fallIdPath(gesuch.getFallId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(NotificationDtoSpec[].class);

        assertThat(notifications.length).isGreaterThan(0);
        assertThat(Arrays.stream(notifications).toList().get(0).getNotificationType())
            .isEqualTo(NotificationTypeDtoSpec.GESUCH_EINGEREICHT);
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void markNotificationAsRead() {
        final var notifications = notificationApiSpec.getNotificationsForFall()
            .fallIdPath(gesuch.getFallId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(NotificationDtoSpec[].class);

        final var notificationId = Arrays.stream(notifications).toList().get(0).getId();

        notificationApiSpec.markNotificationAsRead()
            .notificationIdPath(notificationId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        final var updatedNotifications = notificationApiSpec.getNotificationsForFall()
            .fallIdPath(gesuch.getFallId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .extract()
            .body()
            .as(NotificationDtoSpec[].class);

        final var updatedNotification = Arrays.stream(updatedNotifications)
            .filter(notification -> notification.getId().equals(notificationId))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Notification not found after mark-as-read call"));

        assertThat(updatedNotification.getRead()).isTrue();
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    @AlwaysRun
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }

}
