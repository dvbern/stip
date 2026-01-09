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

package ch.dvbern.stip.api.util;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuerdatenUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.generator.entities.service.LandGenerator;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.Oper;
import ch.dvbern.stip.generated.api.SteuerdatenApiSpec;
import ch.dvbern.stip.generated.dto.AusbildungCreateResponseDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDokumentDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDtoSpec;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.hamcrest.Matchers;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_MUTTER;
import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PARTNER;
import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG;
import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_VATER;
import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN);

    public static final Function<io.restassured.response.Response, io.restassured.response.Response> PEEK_IF_ENV_SET =
        response -> {
            final var env = System.getenv("STIP_TESTING_PEEK_RESPONSE");
            if (env != null && env.equals("true")) {
                response.prettyPeek();
            }

            return response;
        };

    public static void deleteGesuch(final GesuchApiSpec gesuchApiSpec, final UUID gesuchId) {
        gesuchApiSpec.deleteGesuch()
            .gesuchIdPath(gesuchId)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    public static void deleteAusbildung(final GesuchApiSpec gesuchApiSpec, final UUID ausbildungId) {
        final var fallDashboard = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);

        for (
            final var ausbildungDashboards : fallDashboard.getAusbildungDashboardItems()
                .stream()
                .filter(ausbildungDashboard -> ausbildungDashboard.getId().equals(ausbildungId))
                .toList()
        ) {
            for (final var gesuch : ausbildungDashboards.getGesuchs()) {
                gesuchApiSpec.deleteGesuch()
                    .gesuchIdPath(gesuch.getId())
                    .execute(TestUtil.PEEK_IF_ENV_SET)
                    .then()
                    .assertThat()
                    .statusCode(Status.NO_CONTENT.getStatusCode());
            }
        }
    }

    public static void deleteGesucheOfSb(final GesuchApiSpec gesuchApiSpec) {
        final var gesuche = gesuchApiSpec.getGesucheGs()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);
        for (final var gesuch : gesuche) {
            deleteGesuch(gesuchApiSpec, gesuch.getId());
        }
    }

    public static void updateGesuchSB(
        final GesuchApiSpec gesuchApiSpec,
        final GesuchDtoSpec gesuch
    ) {
        final var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten();
        fullGesuch.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.updateGesuchSB()
            .gesuchIdPath(gesuch.getId())
            .body(fullGesuch)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    public static void fillGesuchWithAuszahlung(
        final GesuchApiSpec gesuchApiSpec,
        final DokumentApiSpec dokumentApiSpec,
        final AuszahlungApiSpec auszahlungApiSpec,
        final GesuchDtoSpec gesuch
    ) {
        fillGesuch(gesuchApiSpec, dokumentApiSpec, gesuch);
        fillAuszahlung(gesuch.getFallId(), auszahlungApiSpec, getAuszahlungUpdateDtoSpec());
    }

    public static void fillGesuchNoElterns(
        final GesuchApiSpec gesuchApiSpec,
        final DokumentApiSpec dokumentApiSpec,
        final GesuchDtoSpec gesuch
    ) {
        var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFullNoElterns();
        fullGesuch.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.updateGesuchGS()
            .gesuchIdPath(gesuch.getId())
            .body(fullGesuch)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuch.getGesuchTrancheToWorkWith().getId(), dokTyp, file);
        }
    }

    public static void fillGesuch(
        final GesuchApiSpec gesuchApiSpec,
        final DokumentApiSpec dokumentApiSpec,
        final GesuchDtoSpec gesuch
    ) {
        final var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();
        fullGesuch.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        fillGesuch(
            gesuchApiSpec,
            fullGesuch,
            dokumentApiSpec,
            gesuch
        );
    }

    public static void fillGesuch(
        final GesuchApiSpec gesuchApiSpec,
        final GesuchUpdateDtoSpec fullGesuch,
        final DokumentApiSpec dokumentApiSpec,
        final GesuchDtoSpec gesuch
    ) {
        gesuchApiSpec.updateGesuchGS()
            .gesuchIdPath(gesuch.getId())
            .body(fullGesuch)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        for (final var dokTyp : DokumentTypDtoSpec.values()) {
            final var file = TestUtil.getTestPng();
            TestUtil.uploadFile(dokumentApiSpec, gesuch.getGesuchTrancheToWorkWith().getId(), dokTyp, file);
        }
    }

    public static void fillAuszahlung(
        final UUID fallId,
        final AuszahlungApiSpec auszahlungApiSpec,
        final AuszahlungUpdateDtoSpec auszahlung
    ) {
        auszahlungApiSpec.createAuszahlungForGesuch()
            .fallIdPath(fallId)
            .body(auszahlung)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    public static AuszahlungUpdateDtoSpec getAuszahlungUpdateDtoSpec() {
        var zahlungsverbindungDtoSpec = new ZahlungsverbindungDtoSpec();
        zahlungsverbindungDtoSpec.adresse(AdresseSpecModel.adresseDtoSpec());
        zahlungsverbindungDtoSpec.setIban(TestConstants.IBAN_CH_NUMMER_VALID);
        zahlungsverbindungDtoSpec.setNachname("Mustermann");
        zahlungsverbindungDtoSpec.setVorname("Max");
        return new AuszahlungUpdateDtoSpec().auszahlungAnSozialdienst(false)
            .zahlungsverbindung(
                zahlungsverbindungDtoSpec
            );
    }

    public static Optional<FallDtoSpec> getFall(final FallApiSpec fallApiSpec) {
        final var response = fallApiSpec.getFallForGs()
            .execute(PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Matchers.oneOf(Status.OK.getStatusCode(), Status.NO_CONTENT.getStatusCode()));

        var stringBody = response.extract().body().asString();
        if (stringBody == null || stringBody.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(response.extract().body().as(FallDtoSpec.class));
    }

    public static FallDtoSpec getOrCreateFall(final FallApiSpec fallApiSpec) {
        final var fall = getFall(fallApiSpec);
        return fall.orElseGet(
            () -> fallApiSpec.createFallForGs()
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .body()
                .as(FallDtoSpec.class)
        );
    }

    public static AusbildungDtoSpec createAusbildung(
        final AusbildungApiSpec ausbildungApiSpec,
        final AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec,
        final UUID fallId
    ) {
        ausbildungUpdateDtoSpec.setFallId(fallId);

        final var response = ausbildungApiSpec.createAusbildung()
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungCreateResponseDtoSpec.class);

        if (response.getError() != null) {
            throw new IllegalStateException(
                "The server has seeded wrong data for the tests, error: " + response.getError().getType()
            );
        }

        return response.getAusbildung();
    }

    public static GesuchDtoSpec createGesuchAusbildungFallWithAusbildung(
        final FallApiSpec fallApiSpec,
        final AusbildungApiSpec ausbildungApiSpec,
        final AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec,
        final GesuchApiSpec gesuchApiSpec
    ) {
        final var fall = getOrCreateFall(fallApiSpec);
        final var fallDashboardItemDtos = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto.class);

        if (fallDashboardItemDtos.getAusbildungDashboardItems().isEmpty()) {
            createAusbildung(ausbildungApiSpec, ausbildungUpdateDtoSpec, fall.getId());
        }

        final var gesuche = gesuchApiSpec.getGesucheGs()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec[].class);

        return gesuche[0];
    }

    public static GesuchDtoSpec createGesuchAusbildungFall(
        final FallApiSpec fallApiSpec,
        final AusbildungApiSpec ausbildungApiSpec,
        final GesuchApiSpec gesuchApiSpec
    ) {
        return createGesuchAusbildungFallWithAusbildung(
            fallApiSpec,
            ausbildungApiSpec,
            AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec(),
            gesuchApiSpec
        );
    }

    public static GesuchDtoSpec createAndSubmitGesuch(
        final FallApiSpec fallApiSpec,
        final AusbildungApiSpec ausbildungApiSpec,
        final GesuchApiSpec gesuchApiSpec,
        final AuszahlungApiSpec auszahlungApiSpec,
        final DokumentApiSpec dokumentApiSpec
    ) {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        TestUtil.fillGesuchWithAuszahlung(gesuchApiSpec, dokumentApiSpec, auszahlungApiSpec, gesuch);
        var foundGesuch = gesuchApiSpec.gesuchEinreichenGs()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.DATENSCHUTZBRIEF_DRUCKBEREIT));
        return foundGesuch;
    }

    public static void gesuchToInFreigabe(
        final GesuchDtoSpec gesuch,
        final GesuchApiSpec gesuchApiSpec,
        final SteuerdatenApiSpec steuerdatenApiSpec,
        final DokumentApiSpec dokumentApiSpec,
        final GesuchTrancheApiSpec gesuchTrancheApiSpec
    ) {
        gesuchApiSpec.changeGesuchStatusToBereitFuerBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);
        final var foundGesuch = gesuchApiSpec.changeGesuchStatusToInBearbeitung()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchWithChangesDtoSpec.class);

        assertThat(foundGesuch.getGesuchStatus(), is(GesuchstatusDtoSpec.IN_BEARBEITUNG_SB));

        final var steuerdatenUpdateDto =
            SteuerdatenUpdateTabsDtoSpecModel.steuerdatenDtoSpec(SteuerdatenTypDtoSpec.FAMILIE);
        steuerdatenApiSpec.updateSteuerdaten()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .body(List.of(steuerdatenUpdateDto))
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

        final var gesuchdokuments = gesuchTrancheApiSpec.getGesuchDokumenteSB()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDokumentDtoSpec[].class);

        for (var dokument : gesuchdokuments) {
            dokumentApiSpec.gesuchDokumentAkzeptieren()
                .gesuchDokumentIdPath(dokument.getId())
                .execute(TestUtil.PEEK_IF_ENV_SET)
                .then()
                .assertThat()
                .statusCode(Status.NO_CONTENT.getStatusCode());
        }

        uploadUnterschriftenblatt(
            dokumentApiSpec,
            gesuch.getId(),
            UnterschriftenblattDokumentTypDtoSpec.GEMEINSAM,
            TestUtil.getTestPng()
        );

        gesuchApiSpec.bearbeitungAbschliessen()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
        gesuchApiSpec.changeGesuchStatusToVerfuegt()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        gesuchApiSpec.changeGesuchStatusToVersendet()
            .gesuchTrancheIdPath(gesuch.getGesuchTrancheToWorkWith().getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    public static ConstraintValidatorContextImpl initValidatorContext() {
        return new ConstraintValidatorContextImpl(
            null, PathImpl.createRootPath(), null, null,
            ExpressionLanguageFeatureLevel.DEFAULT, ExpressionLanguageFeatureLevel.DEFAULT
        );
    }

    public static Steuerdaten prepareSteuerdaten() {
        Steuerdaten steuerdaten = new Steuerdaten();
        steuerdaten.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
        steuerdaten.setEigenmietwert(0);
        steuerdaten.setVerpflegung(0);
        steuerdaten.setIsArbeitsverhaeltnisSelbstaendig(false);
        steuerdaten.setTotalEinkuenfte(0);
        steuerdaten.setFahrkosten(0);
        steuerdaten.setSteuernBund(0);
        steuerdaten.setSteuernKantonGemeinde(0);
        steuerdaten.setVermoegen(0);
        steuerdaten.setSteuerjahr(0);
        return steuerdaten;
    }

    public static <T> T createUpdateDtoSpec(Supplier<T> supplier, Consumer<T> consumer) {
        final T model = supplier.get();
        consumer.accept(model);
        return model;
    }

    public static <T> List<T> createUpdateDtoSpecs(Supplier<T> supplier, Consumer<T> consumer, int amount) {
        return Stream.generate(supplier)
            .limit(amount)
            .peek(consumer)
            .toList();
    }

    public static LocalDate getRandomLocalDateBetween(final LocalDate begin, final LocalDate end) {
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(begin.toEpochDay(), end.toEpochDay()));
    }

    public static int getRandomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int getRandomInt(final int lower, final int upper) {
        return ThreadLocalRandom.current().nextInt(lower, upper);
    }

    public static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static BigDecimal getRandomBigDecimal() {
        return getRandomBigDecimal(100, 10_000);
    }

    public static BigDecimal getRandomBigDecimal(final int min, final int max) {
        return getRandomBigDecimal(min, max, 2);
    }

    public static BigDecimal getRandomBigDecimal(final int min, final int max, final int decNum) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble());
    }

    public static <T> T getRandomElementFromArray(final T[] tArr) {
        return tArr[new Random().nextInt(tArr.length)];
    }

    public static File getTestPng() {
        return new File(TEST_PNG_FILE_LOCATION);
    }

    public static void uploadFile(
        DokumentApiSpec dokumentApiSpec,
        UUID gesuchTrancheId,
        DokumentTypDtoSpec dokTyp,
        File file
    ) {
        dokumentApiSpec.createDokumentGS()
            .gesuchTrancheIdPath(gesuchTrancheId)
            .dokumentTypPath(dokTyp)
            .reqSpec(req -> {
                req.addMultiPart("fileUpload", file, "image/png");
            })
            .execute(PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    public static void uploadCustomDokumentFile(
        DokumentApiSpec dokumentApiSpec,
        UUID customDokumentTypId,
        File file
    ) {
        dokumentApiSpec.uploadCustomGesuchDokumentGS()
            .customDokumentTypIdPath(customDokumentTypId)
            .reqSpec(req -> {
                req.addMultiPart("fileUpload", file, "image/png");
            })
            .execute(PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    public static ValidatableResponse uploadUnterschriftenblatt(
        final DokumentApiSpec dokumentApiSpec,
        final UUID gesuchId,
        final UnterschriftenblattDokumentTypDtoSpec unterschriftenblattDokumentTyp,
        final File file
    ) {
        return dokumentApiSpec.createUnterschriftenblatt()
            .gesuchIdPath(gesuchId)
            .unterschriftenblattTypPath(unterschriftenblattDokumentTyp)
            .reqSpec(req -> req.addMultiPart("fileUpload", file, "image/png"))
            .execute(PEEK_IF_ENV_SET)
            .then();
    }

    public static ValidatableResponse uploadBeschwerdeEntscheid(
        final GesuchApiSpec gesuchApiSpec,
        final UUID gesuchId,
        final Boolean isBeschwerdeErfolgreich,
        final String kommentar,
        final File fileUpload
    ) {
        return gesuchApiSpec.createBeschwerdeEntscheid()
            .gesuchIdPath(gesuchId)
            .beschwerdeErfolgreichForm(isBeschwerdeErfolgreich)
            .kommentarForm(kommentar)
            // .fileUploadMultiPart(fileUpload)
            .reqSpec(req -> req.addMultiPart("fileUpload", fileUpload, "image/png"))
            .execute(PEEK_IF_ENV_SET)
            .then();
    }

    public static Gesuch getBaseGesuchForBerechnung(final UUID trancheUuid) {
        final var gueltigkeit = DateRange.getFruehlingOrHerbst(LocalDate.now());
        final var gesuch = new Gesuch().setGesuchsperiode(
            new Gesuchsperiode()
                .setGesuchsjahr(new Gesuchsjahr().setTechnischesJahr(Year.now().getValue()))
                .setMaxSaeule3a(7000)
                .setEinkommensfreibetrag(6000)
                .setFreibetragErwerbseinkommen(6000)
                .setFreibetragVermoegen(30000)
                .setStipLimiteMinimalstipendium(500)
                .setVermoegensanteilInProzent(15)
                .setAnzahlWochenLehre(47)
                .setAnzahlWochenSchule(38)
                .setPreisProMahlzeit(10)
                .setIntegrationszulage(2400)
                .setLimiteEkFreibetragIntegrationszulage(13200)
                .setPerson1(11724)
                .setPersonen2(17940)
                .setPersonen3(21816)
                .setPersonen4(25080)
                .setPersonen5(28368)
                .setPersonen6(31656)
                .setPersonen7(34944)
                .setProWeiterePerson(3288)
                .setWohnkostenPersoenlich1pers(10009)
                .setWohnkostenPersoenlich2pers(13536)
                .setWohnkostenPersoenlich3pers(16260)
                .setWohnkostenPersoenlich4pers(19932)
                .setWohnkostenPersoenlich5pluspers(25260)
                .setWohnkostenFam1pers(13536)
                .setWohnkostenFam2pers(16260)
                .setWohnkostenFam3pers(16260)
                .setWohnkostenFam4pers(19932)
                .setWohnkostenFam5pluspers(25260)
                .setAusbKostenSekII(2000)
                .setAusbKostenTertiaer(3000)
                .setErwachsene2599(5400)
                .setJugendlicheErwachsene1824(4600)
                .setKinder0017(1400)
                .setEinreichefristNormal(LocalDate.now().plusMonths(5))
                .setEinreichefristReduziert(LocalDate.now().plusMonths(5))
                .setLimiteAlterAntragsstellerHalbierungElternbeitrag(25)
                .setStichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate.of(Year.now().getValue(), 12, 31))
        )
            .setGesuchTranchen(
                List.of(
                    (GesuchTranche) new GesuchTranche()
                        .setTyp(GesuchTrancheTyp.TRANCHE)
                        .setGueltigkeit(
                            new DateRange(
                                gueltigkeit.getGueltigAb(),
                                gueltigkeit.getGueltigAb().plusYears(1).minusDays(1)
                            )
                        )
                        .setGesuchFormular(
                            new GesuchFormular()
                                .setPersonInAusbildung(
                                    new PersonInAusbildung()
                                        .setNationalitaet(LandGenerator.initSwitzerland())
                                )
                        )
                        .setId(trancheUuid)
                )
            )
            .setEinreichedatum(LocalDate.now().plusMonths(5));

        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setTranche(gesuch.getNewestGesuchTranche().get());
        gesuch.getNewestGesuchTranche().get().setGesuch(gesuch);
        return gesuch;
    }

    public static Gesuch getGesuchForDecision(final UUID trancheUuid) {
        final var baseGesuch = getGesuchForBerechnung(trancheUuid);
        baseGesuch.getGesuchsperiode().setEinreichefristReduziert(LocalDate.now().plusMonths(1));
        baseGesuch.getAusbildung().setAusbildungBegin(LocalDate.now().minusYears(1));
        return baseGesuch;
    }

    public static ValidatableResponse executeAndAssertOk(final Oper operation) {
        return executeAndAssert(operation, Response.Status.OK.getStatusCode());
    }

    public static ValidatableResponse executeAndAssert(final Oper operation, final int expectedStatusCode) {
        return operation.execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(expectedStatusCode);
    }

    public static <T> T executeAndExtract(Class<T> clazz, final Oper operation) {
        return executeAndExtract(clazz, operation, Response.Status.OK.getStatusCode());
    }

    public static <T> T executeAndExtract(Class<T> clazz, final Oper operation, final int expectedStatusCode) {
        return executeAndAssert(operation, expectedStatusCode).extract().body().as(clazz);
    }

    public static Gesuch getGesuchForBerechnung(final UUID trancheUuid) {
        final var baseGesuch = getBaseGesuchForBerechnung(trancheUuid);
        baseGesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                )
                .setAusbildungBegin(LocalDate.now().minusYears(1))
                .setAusbildungEnd(LocalDate.now().plusYears(5))
        );
        baseGesuch.getAusbildung()
            .getAusbildungsgang()
            .setAbschluss(
                new Abschluss().setBfsKategorie(9)
                    .setBildungskategorie(
                        Bildungskategorie.TERTIAERSTUFE_B
                    )
                    .setBildungsrichtung(Bildungsrichtung.HOCHSCHULE)
                    .setAusbildungsgaenge(List.of(baseGesuch.getAusbildung().getAusbildungsgang()))
            );
        baseGesuch.getAusbildung()
            .getAusbildungsgang()
            .setAusbildungsstaette(
                new Ausbildungsstaette().setAusbildungsgaenge(List.of(baseGesuch.getAusbildung().getAusbildungsgang()))
            );

        final var gesuchFormular = baseGesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuchFormular.getPersonInAusbildung()
            .setNationalitaet(LandGenerator.initSwitzerland())
            .setZivilstand(Zivilstand.VERHEIRATET)
            .setSozialversicherungsnummer(AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG)
            .setAdresse(
                new Adresse().setPlz("1321")
                    .setLand(LandGenerator.initSwitzerland())
                    .setStrasse("asd")
                    .setHausnummer("1")
                    .setOrt("asd")
            )
            .setHeimatort("Bern")
            .setHeimatortPLZ("3006")
            .setAnrede(Anrede.HERR)
            .setTelefonnummer("0987654321")
            .setEmail("asd@asd.ch")
            .setKorrespondenzSprache(Sprache.DEUTSCH)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setNachname("a")
            .setVorname("a")
            .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(12916)
                .setErgaenzungsleistungen(1200)
                .setWohnkosten(6000)
                .setAusbildungskosten(450)
                .setFahrkosten(523)
                .setZulagen(0)
                .setBetreuungskostenKinder(0)
                .setSteuerjahr(2000)
                .setRenten(0)
                .setVermoegen(null)
                .setArbeitspensumProzent(100)
        );

        gesuchFormular.setPartner(
            (Partner) new Partner()
                .setSozialversicherungsnummer(AHV_NUMMER_VALID_PARTNER)
                .setAdresse(
                    new Adresse().setPlz("1321")
                        .setLand(LandGenerator.initSwitzerland())
                        .setStrasse("asd")
                        .setHausnummer("1")
                        .setOrt("asd")
                )
                .setNachname("a")
                .setVorname("a")
                .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1))
        );

        gesuchFormular.setEinnahmenKostenPartner(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(12916)
                .setErgaenzungsleistungen(1200)
                .setFahrkosten(523)
                .setZulagen(0)
                .setBetreuungskostenKinder(0)
                .setSteuerjahr(2000)
                .setRenten(0)
                .setArbeitspensumProzent(100)
                .setVerpflegungskosten(5)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(false)
                .setVaterWiederverheiratet(false)
                .setMutterWiederverheiratet(true)
        );

        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50))
                    .setGeburtsdatum(LocalDate.now())
                    .setNachname("a")
                    .setVorname("a"),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(30))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(70))
                    .setGeburtsdatum(LocalDate.now())
                    .setNachname("a")
                    .setVorname("a"),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(100))
                    .setGeburtsdatum(LocalDate.now())
                    .setNachname("a")
                    .setVorname("a")
            )
        );

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.VATER)
                    .setSozialversicherungsnummer(AHV_NUMMER_VALID_VATER)
                    .setWohnkosten(0)
                    .setTelefonnummer("0987654321")
                    .setAusweisbFluechtling(false)
                    .setAdresse(
                        new Adresse().setLand(LandGenerator.initSwitzerland())
                            .setPlz("3000")
                            .setStrasse("asd")
                            .setHausnummer("1")
                            .setOrt("asd")
                    )
                    .setNachname("a")
                    .setVorname("a")
                    .setGeburtsdatum(LocalDate.now().minusYears(30)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setSozialversicherungsnummer(AHV_NUMMER_VALID_MUTTER)
                    .setWohnkosten(0)
                    .setTelefonnummer("0987654321")
                    .setAusweisbFluechtling(false)
                    .setAdresse(
                        new Adresse().setLand(LandGenerator.initSwitzerland())
                            .setPlz("3000")
                            .setStrasse("asd")
                            .setHausnummer("1")
                            .setOrt("asd")
                    )
                    .setNachname("a")
                    .setVorname("a")
                    .setGeburtsdatum(LocalDate.now().minusYears(30))
            )
        );

        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER),
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.VATER)
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setTotalEinkuenfte(0)
                    .setEigenmietwert(0)
                    .setSaeule2(null)
                    .setSaeule3a(null)
                    .setVermoegen(0)
                    .setSteuerjahr(2000)
                    .setVeranlagungsStatus(TestConstants.VERANLAGUNGSSTATUS_EXAMPLE_VALUE)
                    .setIsArbeitsverhaeltnisSelbstaendig(false),
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setTotalEinkuenfte(0)
                    .setEigenmietwert(0)
                    .setSaeule2(null)
                    .setSaeule3a(null)
                    .setVermoegen(0)
                    .setSteuerjahr(2000)
                    .setVeranlagungsStatus(TestConstants.VERANLAGUNGSSTATUS_EXAMPLE_VALUE)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
            )
        );

        return baseGesuch;
    }

    public static Gesuch getFullGesuch() {
        var gesuch = getGesuchForBerechnung(UUID.randomUUID());
        gesuch.setGesuchNummer("asd");
        var ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().plusMonths(1));
        ausbildung.setAusbildungEnd(LocalDate.now().plusYears(1));
        var fall = new Fall();
        ausbildung.setFall(fall);
        fall.setAusbildungs(Set.of(ausbildung));
        gesuch.setAusbildung(ausbildung);

        gesuch.getNewestGesuchTranche().get().setTyp(GesuchTrancheTyp.TRANCHE);
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuchFormular.setLebenslaufItems(
            Set.of(
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                    .setVon(LocalDate.now().minusYears(2))
                    .setWohnsitz(WohnsitzKanton.BE)
                    .setBis(LocalDate.now().minusYears(1))
                    .setTaetigkeitsBeschreibung("Gymnasiale Maturität")
            )
        );

        Kind kind1 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind1")
            .setGeburtsdatum(LocalDate.of(2013, 9, 1));
        kind1.setWohnsitzAnteilPia(100);
        kind1.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        Kind kind2 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind2")
            .setGeburtsdatum(LocalDate.of(2019, 6, 1));
        kind2.setWohnsitzAnteilPia(100);
        kind2.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        gesuchFormular.setKinds(
            Set.of(
                kind1,
                kind2
            )
        );

        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setSteuererklaerungInBern(true),
                new Steuererklaerung()
                    .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setSteuererklaerungInBern(true)
            )
        );

        var zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung
            .setAdresse(gesuchFormular.getPersonInAusbildung().getAdresse())
            .setVorname("asd")
            .setNachname("qwe")
            .setIban("CH2289144464431833761");

        gesuchFormular.getTranche()
            .getGesuch()
            .getAusbildung()
            .getFall()
            .setAuszahlung(
                new Auszahlung()
                    .setZahlungsverbindung(zahlungsverbindung)
                    .setAuszahlungAnSozialdienst(false)
                    .setSapBusinessPartnerId(9887965)
            );

        return gesuch;
    }

    public static GesuchDokument setupCustomGesuchDokument() {
        CustomDokumentTyp customDokumentTyp = new CustomDokumentTyp();
        customDokumentTyp.setId(UUID.randomUUID());
        customDokumentTyp.setDescription("test");
        customDokumentTyp.setType("test");

        var customGesuchDokument = new GesuchDokument();
        customGesuchDokument.setId(UUID.randomUUID());
        customGesuchDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND)
            .setDokumente(new ArrayList<>())
            .setCustomDokumentTyp(customDokumentTyp);
        return customGesuchDokument;
    }

    public static Gesuch setupGesuchWithCustomDokument() {
        GesuchTranche gesuchTranche = new GesuchTranche();
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchTranche.setGesuchDokuments(List.of(setupCustomGesuchDokument()));
        Gesuch gesuch = new Gesuch();
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.setGesuchTranchen(List.of(gesuchTranche));
        gesuch.setAusbildung(new Ausbildung().setFall(new Fall()));
        return gesuch;
    }
}
