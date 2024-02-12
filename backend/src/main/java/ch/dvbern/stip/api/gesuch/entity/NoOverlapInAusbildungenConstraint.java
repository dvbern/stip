package ch.dvbern.stip.api.gesuch.entity;


import ch.dvbern.stip.api.personinausbildung.entity.LandCHRequiredConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NO_OVERLAP_INAUSBILDUNGEN;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoOverlapInAusbildungenConstraintValidator.class)
@Documented
public @interface NoOverlapInAusbildungenConstraint {
    String message() default VALIDATION_NO_OVERLAP_INAUSBILDUNGEN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
