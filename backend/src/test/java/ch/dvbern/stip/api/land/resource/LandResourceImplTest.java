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

package ch.dvbern.stip.api.land.resource;

import java.util.Arrays;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.model.land.LandDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.LandApiSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class LandResourceImplTest {
    private final LandApiSpec landApiSpec = LandApiSpec.land(RequestSpecUtil.quarkusSpec());
    private UUID createdLandId;

    @Test
    @Order(1)
    @TestAsAdmin
    void createLand() {
        final var landToCreate = LandDtoSpecModel.landDtoSpec();
        final var returned = TestUtil.executeAndExtract(LandDtoSpec.class, landApiSpec.createLand().body(landToCreate));

        assertNotNull(returned.getId());
        createdLandId = returned.getId();
    }

    @Test
    @Order(2)
    @TestAsAdmin
    void createLandWitDuplicateIso2codeFails() {
        final var landToCreate = LandDtoSpecModel.landDtoSpec();
        landToCreate.setIso2code("ZZ");
        TestUtil.executeAndAssert(
            landApiSpec.createLand().body(landToCreate),
            Response.Status.BAD_REQUEST.getStatusCode()
        );
    }

    @Test
    @Order(3)
    @TestAsAdmin
    void createLandWitDuplicateIso3codeFails() {
        final var landToCreate = LandDtoSpecModel.landDtoSpec();
        landToCreate.setIso3code("ZZZ");
        TestUtil.executeAndAssert(
            landApiSpec.createLand().body(landToCreate),
            Response.Status.BAD_REQUEST.getStatusCode()
        );
    }

    @Test
    @Order(4)
    @TestAsAdmin
    void getLaenderContainsNewLand() {
        final var laender = TestUtil.executeAndExtract(LandDtoSpec[].class, landApiSpec.getLaender());

        assertNotEquals(0, laender.length);
        final var createdLand = Arrays.stream(laender)
            .filter(land -> land.getId().equals(createdLandId))
            .findFirst()
            .orElse(null);

        assertNotNull(createdLand, "The Land created before was not returned by the API");
    }

    @Test
    @Order(5)
    @TestAsAdmin
    void landInaktivSchalten() {
        final var update = LandDtoSpecModel.landDtoSpec();
        update.setEintragGueltig(false);

        final var returned = TestUtil.executeAndExtract(
            LandDtoSpec.class,
            landApiSpec.updateLand().landIdPath(createdLandId).body(update)
        );

        assertNotNull(returned);
        assertFalse(returned.getEintragGueltig());
    }

    @Test
    @Order(6)
    @TestAsAdmin
    void getLaenderContainsInaktivesLand() {
        final var laender = TestUtil.executeAndExtract(LandDtoSpec[].class, landApiSpec.getLaender());

        assertNotEquals(0, laender.length);
        final var createdLand = Arrays.stream(laender)
            .filter(land -> land.getId().equals(createdLandId))
            .findFirst()
            .orElse(null);

        assertNotNull(createdLand, "The Land created before was not returned by the API");
        assertFalse(createdLand.getEintragGueltig());
    }

    // Just run these next tests after creating a Land to ensure we're not dependent on seeding

    @Test
    @Order(7)
    @TestAsGesuchsteller
    void getLaenderAsGS() {
        doGetLaender();
    }

    @Test
    @Order(7)
    @TestAsSachbearbeiter
    void getLaenderAsSB() {
        doGetLaender();
    }

    private void doGetLaender() {
        final var laender = TestUtil.executeAndExtract(LandDtoSpec[].class, landApiSpec.getLaender());
        assertNotEquals(0, laender.length);
    }
}
