package ch.dvbern.stip.api.gesuch.util;

import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchValidatorUtil {
    public boolean addProperty(final @Nullable ConstraintValidatorContext context, @NotNull final String property) {
		if (context != null) {
			return addProperty(context, context.getDefaultConstraintMessageTemplate(), property);
		} else {
            return false;
        }
	}

    public boolean addProperty(
        final @Nullable ConstraintValidatorContext context,
        @NotNull final String template,
        @NotNull final String property
    ) {
        if (context == null|| property.trim().isEmpty() || template.trim().isEmpty()) {
            return false;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(template)
            .addPropertyNode(property)
            .addConstraintViolation();

        return false;
    }
}
