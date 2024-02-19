package ch.dvbern.stip.api.lebenslauf.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LebenslaufItemArtRequiredFieldsConstraintValidator.class)
@Documented
public @interface LebenslaufItemArtRequiredFieldsConstraint {

    String message() default VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
