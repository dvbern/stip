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

package ch.dvbern.stip.api.sozialdienst.resource;

import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.sozialdienst.SozialdienstAdminCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.sozialdienst.SozialdienstCreateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.SozialdienstApiSpec;
import ch.dvbern.stip.generated.dto.SozialdienstDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDtoSpec;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SozialdienstSetInaktivResourceTest {
    private final SozialdienstApiSpec sozialdienstApi = SozialdienstApiSpec.sozialdienst(RequestSpecUtil.quarkusSpec());

    private UUID sozialdienstId;

    @Order(1)
    @Test
    @TestAsAdmin
    void createSozialdienst() {
        final var admin = SozialdienstAdminCreateDtoSpecModel.sozialdienstAdminCreateDtoSpec();
        final var sozialdienst = SozialdienstCreateDtoSpecModel.sozialdienstCreateDtoSpec(admin);

        final var createdSozialdienst = TestUtil.executeAndExtract(
            SozialdienstDtoSpec.class,
            sozialdienstApi.createSozialdienst().body(sozialdienst)
        );

        assertThat(createdSozialdienst.getAktiv(), is(true));
        sozialdienstId = createdSozialdienst.getId();
    }

    @Order(2)
    @Test
    @TestAsGesuchsteller
    void getAllForDelegationReturnsNewSozialdienst() {
        final var sozialdienste = TestUtil.executeAndExtract(
            SozialdienstSlimDtoSpec[].class,
            sozialdienstApi.getAllSozialdiensteForDelegation()
        );

        assertThat(sozialdienste.length, greaterThan(0));
        final var created = Arrays.stream(sozialdienste)
            .filter(x -> x.getId().equals(sozialdienstId))
            .findFirst()
            .orElse(null);

        assertThat(created, is(notNullValue()));
    }

    @Order(3)
    @Test
    @TestAsAdmin
    void getAllForDashboardReturnsNewSozialdienst() {
        createdSozialdienstIsReturnedForDashboard();
    }

    @Order(4)
    @Test
    @TestAsSachbearbeiter
    void setSozialdienstToInaktivAsNonAdminFails() {
        TestUtil.executeAndAssert(
            sozialdienstApi.setSozialdienstAktivTo()
                .sozialdienstIdPath(sozialdienstId)
                .aktivPath(false),
            Status.FORBIDDEN.getStatusCode()
        );
    }

    @Order(5)
    @Test
    @TestAsAdmin
    void setSozialdienstToInaktiv() {
        final var sozialdienst = TestUtil.executeAndExtract(
            SozialdienstDtoSpec.class,
            sozialdienstApi.setSozialdienstAktivTo()
                .sozialdienstIdPath(sozialdienstId)
                .aktivPath(false)
        );

        assertThat(sozialdienst.getAktiv(), is(false));
    }

    @Order(6)
    @Test
    @TestAsGesuchsteller
    void getAllForDelegationDoesReturnsInaktivSozialdienst() {
        final var sozialdienste = TestUtil.executeAndExtract(
            SozialdienstSlimDtoSpec[].class,
            sozialdienstApi.getAllSozialdiensteForDelegation()
        );

        assertThat(sozialdienste.length, greaterThan(0));
        final var created = Arrays.stream(sozialdienste)
            .filter(x -> x.getId().equals(sozialdienstId))
            .findFirst()
            .get();

        assertThat(created.getAktiv(), is(false));
    }

    @Order(7)
    @Test
    @TestAsAdmin
    void getAllForDashboardReturnsInaktivSozialdienst() {
        createdSozialdienstIsReturnedForDashboard();
    }

    private void createdSozialdienstIsReturnedForDashboard() {
        final var sozialdienste = TestUtil.executeAndExtract(
            SozialdienstDtoSpec[].class,
            sozialdienstApi.getAllSozialdienste()
        );

        // Can't deduplicate these lines because sozialdienste is a "fat" array here and "slim" above
        assertThat(sozialdienste.length, greaterThan(0));
        final var created = Arrays.stream(sozialdienste)
            .filter(x -> x.getId().equals(sozialdienstId))
            .findFirst()
            .orElse(null);

        assertThat(created, is(notNullValue()));
    }
}
