package ch.dvbern.stip.api.util;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.HttpHeaders;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
}
