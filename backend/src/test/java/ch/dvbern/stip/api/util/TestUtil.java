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
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.Oper;
import ch.dvbern.stip.generated.dto.AusbildungDtoSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.FallDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentTypDtoSpec;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;

import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;

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
        final var dashboard = gesuchApiSpec.getGsDashboard()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDashboardItemDto[].class);

        for (final var fallDashboard : dashboard) {
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

    public static void fillGesuch(
        final GesuchApiSpec gesuchApiSpec,
        final DokumentApiSpec dokumentApiSpec,
        final GesuchDtoSpec gesuch
    ) {
        final var fullGesuch = GesuchTestSpecGenerator.gesuchUpdateDtoSpecFull();
        fullGesuch.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.updateGesuch()
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

    public static Optional<FallDtoSpec> getFall(final FallApiSpec fallApiSpec) {
        final var response = fallApiSpec.getFallForGs()
            .execute(PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());

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

    public static AusbildungDtoSpec createAusbildung(final AusbildungApiSpec ausbildungApiSpec, final UUID fallId) {
        var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setFallId(fallId);

        return ausbildungApiSpec.createAusbildung()
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungDtoSpec.class);
    }

    public static GesuchDtoSpec createGesuchAusbildungFall(
        final FallApiSpec fallApiSpec,
        final AusbildungApiSpec ausbildungApiSpec,
        final GesuchApiSpec gesuchApiSpec
    ) {
        final var fall = getOrCreateFall(fallApiSpec);
        final var ausbildung = createAusbildung(ausbildungApiSpec, fall.getId());
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

    public static UUID extractIdFromResponse(ValidatableResponse response) {
        var locationString = response.extract().header(HttpHeaders.LOCATION).split("/");
        var idString = locationString[locationString.length - 1];
        return UUID.fromString(idString);
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
        steuerdaten.setKinderalimente(0);
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
        dokumentApiSpec.createDokument()
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
        dokumentApiSpec.uploadCustomGesuchDokument()
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

    public static Gesuch getBaseGesuchForBerechnung(final UUID trancheUuid) {
        final var gesuch = new Gesuch().setGesuchsperiode(
            new Gesuchsperiode()
                .setGesuchsjahr(new Gesuchsjahr().setTechnischesJahr(Year.now().getValue()))
                .setMaxSaeule3a(7000)
                .setEinkommensfreibetrag(6000)
                .setFreibetragErwerbseinkommen(6000)
                .setFreibetragVermoegen(30000)
                .setStipLimiteMinimalstipendium(500)
                .setVermoegensanteilInProzent(15)
                .setAnzahlWochenLehre(42)
                .setAnzahlWochenSchule(37)
                .setPreisProMahlzeit(7)
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
                .setErwachsene2699(5400)
                .setJugendlicheErwachsene1925(4600)
                .setKinder0018(1400)
                .setEinreichefristNormal(LocalDate.now().plusMonths(5))
                .setEinreichefristReduziert(LocalDate.now().plusMonths(5))
        )
            .setGesuchTranchen(
                List.of(
                    (GesuchTranche) new GesuchTranche()
                        .setGesuchFormular(
                            new GesuchFormular()
                                .setPersonInAusbildung(
                                    new PersonInAusbildung()
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

    public static ValidatableResponse executeAndAssert(final Oper operation) {
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
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(10)
                        )
                )
        );

        final var gesuchFormular = baseGesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuchFormular.getPersonInAusbildung()
            .setZivilstand(Zivilstand.LEDIG)
            .setAdresse(new Adresse())
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1));

        gesuchFormular.setPartner(
            (Partner) new Partner()
                .setJahreseinkommen(0)
                .setFahrkosten(0)
                .setVerpflegungskosten(0)
                .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1))
        );

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(12916)
                .setErgaenzungsleistungen(1200)
                .setWohnkosten(6000)
                .setAusbildungskostenTertiaerstufe(450)
                .setFahrkosten(523)
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
                    .setWohnkosten(0)
                    .setAdresse(new Adresse().setLand(Land.CH).setPlz("3000"))
                    .setGeburtsdatum(LocalDate.now().minusYears(30)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(0)
                    .setAdresse(new Adresse().setLand(Land.CH).setPlz("3000"))
                    .setGeburtsdatum(LocalDate.now().minusYears(30))
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
                    .setSaeule2(0)
                    .setSaeule3a(0)
                    .setVermoegen(0)
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
                    .setSaeule2(0)
                    .setSaeule3a(0)
                    .setVermoegen(0)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
            )
        );

        return baseGesuch;
    }

    public static GesuchDokument setupCustomGesuchDokument() {
        CustomDokumentTyp customDokumentTyp = new CustomDokumentTyp();
        customDokumentTyp.setId(UUID.randomUUID());
        customDokumentTyp.setDescription("test");
        customDokumentTyp.setType("test");

        var customGesuchDokument = new GesuchDokument();
        customGesuchDokument.setId(UUID.randomUUID());
        customGesuchDokument.setStatus(Dokumentstatus.AUSSTEHEND)
            .setDokumente(new ArrayList<>())
            .setCustomDokumentTyp(customDokumentTyp);
        return customGesuchDokument;
    }

    public static Gesuch setupGesuchWithCustomDokument() {
        GesuchTranche gesuchTranche = new GesuchTranche();
        gesuchTranche.setGesuchDokuments(List.of(setupCustomGesuchDokument()));
        Gesuch gesuch = new Gesuch();
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.setGesuchTranchen(List.of(gesuchTranche));
        return gesuch;
    }
}
