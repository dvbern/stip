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

package ch.dvbern.stip.api.darlehen.resource;

import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsFreigabestelle;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsSuperUser;
import ch.dvbern.stip.api.darlehen.repo.FreiwilligDarlehenRepository;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DarlehenApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungEntryDtoSpec;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungEntryKategorieDtoSpec;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungOverviewDtoSpec;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungSaldokorrekturDtoSpec;
import ch.dvbern.stip.generated.dto.DarlehenDokumentTypeDtoSpec;
import ch.dvbern.stip.generated.dto.DarlehenGrundDtoSpec;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDtoSpec;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenUpdateGsDtoSpec;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenUpdateSbDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.KommentarDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DarlehenResourceImplTest {
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final DarlehenApiSpec darlehenApiSpec = DarlehenApiSpec.darlehen(RequestSpecUtil.quarkusSpec());

    private GesuchDtoSpec gesuch;
    private FreiwilligDarlehenDtoSpec darlehen;

    private final FreiwilligDarlehenRepository freiwilligDarlehenRepository;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void gesuchErstellen() {
        gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void darlehenErstellenFailGesuchNichtFreigegeben() {
        darlehenApiSpec.createFreiwilligDarlehen()
            .fallIdPath(gesuch.getFallId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.FORBIDDEN.getStatusCode()
            );
    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void gesuchEinreichen() {
        gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void darlehenErstellen() {
        darlehen = darlehenApiSpec.createFreiwilligDarlehen()
            .fallIdPath(gesuch.getFallId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.OK.getStatusCode()
            )
            .extract()
            .body()
            .as(FreiwilligDarlehenDtoSpec.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void darlehenEingebenFail() {
        darlehenApiSpec.freiwilligDarlehenEingeben()
            .darlehenIdPath(darlehen.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.BAD_REQUEST.getStatusCode()
            );
    }

    @Test
    @TestAsGesuchsteller
    @Order(6)
    void darlehenUpdatenMissingFile() {
        final var darlehenUpdateDtoSpec = new FreiwilligDarlehenUpdateGsDtoSpec();
        darlehenUpdateDtoSpec.setBetragGewuenscht(2000);
        darlehenUpdateDtoSpec.setSchulden(0);
        darlehenUpdateDtoSpec.setAnzahlBetreibungen(0);
        darlehenUpdateDtoSpec.setGruende(List.of(DarlehenGrundDtoSpec.HOHE_GEBUEHREN));
        darlehenApiSpec.freiwilligDarlehenUpdateGs()
            .darlehenIdPath(darlehen.getId())
            .body(darlehenUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(
                Status.BAD_REQUEST.getStatusCode()
            );
    }

    @Test
    @TestAsGesuchsteller
    @Order(7)
    void darlehenUpdaten() {
        darlehenApiSpec.createDarlehenDokument()
            .darlehenIdPath(darlehen.getId())
            .dokumentTypePath(
                DarlehenDokumentTypeDtoSpec.BETREIBUNGS_AUSZUG
            )
            .reqSpec(req -> {
                req.addMultiPart("fileUpload", TestUtil.getTestPng(), "image/png");
            })
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode());
        final var darlehenUpdateDtoSpec = new FreiwilligDarlehenUpdateGsDtoSpec();
        darlehenUpdateDtoSpec.setBetragGewuenscht(2000);
        darlehenUpdateDtoSpec.setSchulden(0);
        darlehenUpdateDtoSpec.setAnzahlBetreibungen(0);
        darlehenUpdateDtoSpec.setGruende(List.of(DarlehenGrundDtoSpec.AUSBILDUNG_ZWOELF_JAHRE));
        darlehenApiSpec.freiwilligDarlehenUpdateGs()
            .darlehenIdPath(darlehen.getId())
            .body(darlehenUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(8)
    void darlehenEingeben() {
        darlehenApiSpec.freiwilligDarlehenEingeben()
            .darlehenIdPath(darlehen.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(9)
    void darlehenUpdateSb() {
        final var updateDto = new FreiwilligDarlehenUpdateSbDtoSpec();
        updateDto.setBetrag(0);
        updateDto.setGewaehren(false);
        updateDto.setKommentar("asd");
        darlehenApiSpec.freiwilligDarlehenUpdateSb()
            .darlehenIdPath(darlehen.getId())
            .body(updateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(10)
    void darlehenZurueckweisen() {
        final var kommentarDto = new KommentarDtoSpec();
        kommentarDto.setText("Test");
        darlehenApiSpec.freiwilligDarlehenZurueckweisen()
            .darlehenIdPath(darlehen.getId())
            .body(kommentarDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(11)
    void darlehenEingebenAgian() {
        darlehenApiSpec.freiwilligDarlehenEingeben()
            .darlehenIdPath(darlehen.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(12)
    void darlehenUpdateSbAkzeptieren() {
        final var updateDto = new FreiwilligDarlehenUpdateSbDtoSpec();
        updateDto.setBetrag(2500);
        updateDto.setGewaehren(true);
        updateDto.setKommentar("asd");
        darlehenApiSpec.freiwilligDarlehenUpdateSb()
            .darlehenIdPath(darlehen.getId())
            .body(updateDto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
        darlehenApiSpec.freiwilligDarlehenFreigeben()
            .darlehenIdPath(darlehen.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsFreigabestelle
    @Order(13)
    void darlehenFreigeben() {
        darlehenApiSpec.freiwilligDarlehenAkzeptieren()
            .darlehenIdPath(darlehen.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsSachbearbeiter
    @Order(14)
    void createManuellDarlehenBuchhaltungEntry() {
        final var dto = new DarlehenBuchhaltungSaldokorrekturDtoSpec();
        dto.setBetrag(123);
        dto.setComment("comment");
        var darlehenBuchhaltungEntryDtoSpec = darlehenApiSpec.createDarlehenBuchhaltungSaldokorrektur()
            .gesuchIdPath(gesuch.getId())
            .body(dto)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DarlehenBuchhaltungEntryDtoSpec.class);

        assertEquals(darlehenBuchhaltungEntryDtoSpec.getBetrag(), dto.getBetrag());
        assertEquals(darlehenBuchhaltungEntryDtoSpec.getKommentar(), dto.getComment());
        assertEquals(
            darlehenBuchhaltungEntryDtoSpec.getKategorie(),
            DarlehenBuchhaltungEntryKategorieDtoSpec.MANUELLE_KORREKTUR
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(14)
    void getDarlehenBuchhatlungOverview() {
        var darlehenBuchhaltungOverviewDtoSpec = darlehenApiSpec.getDarlehenBuchhaltungEntrys()
            .gesuchIdPath(gesuch.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(DarlehenBuchhaltungOverviewDtoSpec.class);

        assertEquals(123 + 2500, darlehenBuchhaltungOverviewDtoSpec.getTotal());
        assertEquals(2500, darlehenBuchhaltungOverviewDtoSpec.getTotalFreiwillig());
        assertEquals(2, darlehenBuchhaltungOverviewDtoSpec.getDarlehenBuchhaltungEntrys().size());
    }

    @Test
    @Order(98)
    @Transactional
    void deleteDarlehen() {
        freiwilligDarlehenRepository.deleteById(darlehen.getId());
    }

    @Test
    @TestAsSuperUser
    @Order(99)
    void test_delete_gesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuch.getId());
    }
}
