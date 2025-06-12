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

package ch.dvbern.stip.api.delegieren.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller2;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.generator.api.model.delegieren.DelegierungCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.sozialdienst.SozialdienstAdminCreateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.sozialdienst.SozialdienstCreateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DelegierenApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.SozialdienstApiSpec;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class DelegierenResourceImplTest {
    private final SozialdienstApiSpec sozialdienstApi = SozialdienstApiSpec.sozialdienst(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final DelegierenApiSpec delegierenApiSpec = DelegierenApiSpec.delegieren(RequestSpecUtil.quarkusSpec());

    private final FallRepository fallRepository;
    private final DelegierungRepository delegierungRepository;

    private SozialdienstDtoSpec sozialdienst;
    private GesuchDtoSpec gesuch;
    private FallDtoSpec fall;

    @Test
    @Order(1)
    @TestAsAdmin
    void createSozialdienst() {
        final var sozialdienstCreate = SozialdienstCreateDtoSpecModel.sozialdienstCreateDtoSpec(
            SozialdienstAdminCreateDtoSpecModel.sozialdienstAdminCreateDtoSpec(),
            AdresseSpecModel.adresseDtoSpec()
        );

        sozialdienst = TestUtil.executeAndExtract(
            SozialdienstDtoSpec.class,
            sozialdienstApi.createSozialdienst().body(sozialdienstCreate)
        );
    }

    @Test
    @Order(2)
    @TestAsGesuchsteller
    void createGesuch() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        fall = TestUtil.getFall(fallApiSpec).orElseThrow(() -> new RuntimeException("Failed to create/ get fall"));
    }

    @Test
    @Order(3)
    @TestAsGesuchsteller2
    void delegateNotOwnGesuchFails() {
        TestUtil.executeAndAssert(
            delegierenApiSpec.fallDelegieren()
                .fallIdPath(fall.getId())
                .sozialdienstIdPath(sozialdienst.getId())
                .body(DelegierungCreateDtoSpecModel.delegierungCreateDto()),
            Status.FORBIDDEN.getStatusCode()
        );
    }

    @Test
    @Order(4)
    @TestAsGesuchsteller
    void delegateGesuch() {
        TestUtil.executeAndAssert(
            delegierenApiSpec.fallDelegieren()
                .fallIdPath(fall.getId())
                .sozialdienstIdPath(sozialdienst.getId())
                .body(DelegierungCreateDtoSpecModel.delegierungCreateDto()),
            Response.Status.NO_CONTENT.getStatusCode()
        );
    }

    @Test
    @Order(99)
    @TestAsSuperUser
    @Transactional
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
        final var dbFall = fallRepository.requireById(fall.getId());
        final var dbDelegierung = dbFall.getDelegierung();
        dbFall.setDelegierung(null);

        delegierungRepository.delete(dbDelegierung);
    }
}
