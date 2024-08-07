package ch.dvbern.stip.api.util;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.dto.DokumentTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;

import static ch.dvbern.stip.api.util.TestConstants.TEST_PNG_FILE_LOCATION;

public class TestUtil {

    public static UUID extractIdFromResponse(ValidatableResponse response) {
        var locationString = response.extract().header(HttpHeaders.LOCATION).split("/");
        var idString = locationString[locationString.length - 1];
        return UUID.fromString(idString);
    }

    public static ConstraintValidatorContextImpl initValidatorContext() {
        return new ConstraintValidatorContextImpl(null, PathImpl.createRootPath(), null, null,
            ExpressionLanguageFeatureLevel.DEFAULT, ExpressionLanguageFeatureLevel.DEFAULT
        );
    }

    public static GesuchCreateDtoSpec initGesuchCreateDto() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(UUID.fromString(TestConstants.FALL_TEST_ID));
        gesuchDTO.setGesuchsperiodeId(TestConstants.GESUCHSPERIODE_TEST_ID);
        return gesuchDTO;
    }

    private static final Faker FAKER = new Faker(AppLanguages.DEFAULT.javaLocale(), new RandomService());

    public static <T> T createUpdateDtoSpec(Supplier<T> supplier, Consumer<T> consumer) {
        final T model = supplier.get();
        consumer.accept(model);
        return model;
    }

    public static <T> T createUpdateDtoSpec(Supplier<T> supplier, BiConsumer<T, Faker> consumer) {
        final T model = supplier.get();
        consumer.accept(model, FAKER);
        return model;
    }

    public static <T> List<T> createUpdateDtoSpecs(Supplier<T> supplier, BiConsumer<T, Faker> consumer, int limit) {
        return Stream.generate(supplier)
            .limit(limit)
            .peek(t -> consumer.accept(t, FAKER))
            .toList();
    }

    public static LocalDate getRandomLocalDateBetween(final LocalDate begin, final LocalDate end) {
        final var faker = new Faker(new Locale("de-CH"), new RandomService());
        final var startDate = Date.from(begin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        final var endDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return faker.date().between(startDate, endDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static int getRandomInt() {
        final var faker = new Faker(new Locale("de-CH"), new RandomService());
        return (int) faker.number().randomNumber();
    }

    public static int getRandomInt(final int lower, final int upper) {
        final var faker = new Faker(new Locale("de-CH"), new RandomService());
        return (int) faker.number().numberBetween(lower, upper);
    }

    public static BigDecimal getRandomBigDecimal() {
        return getRandomBigDecimal(100, 10_000);
    }

    public static BigDecimal getRandomBigDecimal(final int min, final int max) {
        return getRandomBigDecimal(min, max, 2);
    }

    public static BigDecimal getRandomBigDecimal(final int min, final int max, final int decNum) {
        final var faker = new Faker(new Locale("de-CH"), new RandomService());
        return BigDecimal.valueOf(faker.number().randomDouble(decNum, min, max));
    }

    public static <T> T getRandomElementFromArray(final T[] tArr) {
        return tArr[new Random().nextInt(tArr.length)];
    }

    public static File getTestPng() {
        return new File(TEST_PNG_FILE_LOCATION);
    }

    public static void uploadFile(DokumentApiSpec dokumentApiSpec, UUID gesuchId, DokumentTypDtoSpec dokTyp, File file) {
        dokumentApiSpec.createDokument()
            .gesuchIdPath(gesuchId)
            .dokumentTypPath(dokTyp)
            .reqSpec(req -> {
                req.addMultiPart("fileUpload", file, "image/png");
            })
            .execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }
    public static Gesuch getBaseGesuchForBerechnung(final UUID trancheUuid) {
        final var gesuch = new Gesuch().setGesuchsperiode(
            new Gesuchsperiode()
                .setMaxSaeule3a(7000)
                .setEinkommensfreibetrag(6000)
                .setAnzahlWochenLehre(42)
                .setAnzahlWochenSchule(37)
                .setPreisProMahlzeit(7)
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
        ).setGesuchTranchen(
            List.of(
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    ).setId(trancheUuid)
            )
        );
        gesuch.getNewestGesuchTranche().get().getGesuchFormular().setTranche(gesuch.getNewestGesuchTranche().get());
        gesuch.getNewestGesuchTranche().get().setGesuch(gesuch);
        return gesuch;
    }

    public static Gesuch getGesuchForBerechnung(final UUID trancheUuid) {
        final var baseGesuch = getBaseGesuchForBerechnung(trancheUuid);
        final var gesuchFormular = baseGesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuchFormular.getPersonInAusbildung()
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

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungsart(
                            new Bildungsart()
                                .setBildungsstufe(Bildungsstufe.TERTIAER)
                        )
                )
        );

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.VATER)
                    .setWohnkosten(0)
                    .setGeburtsdatum(LocalDate.now().minusYears(30)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(0)
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
                    .setSteuernStaat(0)
                    .setTotalEinkuenfte(0)
                    .setErgaenzungsleistungen(0)
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
                    .setSteuernStaat(0)
                    .setTotalEinkuenfte(0)
                    .setErgaenzungsleistungen(0)
                    .setEigenmietwert(0)
                    .setSaeule2(0)
                    .setSaeule3a(0)
                    .setVermoegen(0)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
            )
        );

        return baseGesuch;
    }
}
