package ch.dvbern.stip.test.util;

import io.restassured.response.ValidatableResponse;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.ws.rs.core.HttpHeaders;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;

import java.util.UUID;

public class TestUtil {

    public static UUID extractIdFromResponse(ValidatableResponse response) {
        var locationString = response.extract().header(HttpHeaders.LOCATION).split("/");
        var idString = locationString[locationString.length-1];
        return UUID.fromString(idString);
    }

    public static ConstraintValidatorContext initValidatorContext() {
        return new ConstraintValidatorContextImpl(null, PathImpl.createRootPath(),null,null,
                ExpressionLanguageFeatureLevel.DEFAULT, ExpressionLanguageFeatureLevel.DEFAULT);
    }
}
