package ch.dvbern.stip.api.gesuch.util;

import java.util.Collection;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidatorContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchValidatorUtil {
    public boolean addProperty(final @Nullable ConstraintValidatorContext context, @Nonnull final String property) {
		if (context != null) {
			return addProperty(context, context.getDefaultConstraintMessageTemplate(), property);
		} else {
            return false;
        }
	}

    public boolean addProperty(
        final @Nullable ConstraintValidatorContext context,
        final @Nonnull String template,
        final @Nonnull String property
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

    public boolean addProperties(
        final @Nullable ConstraintValidatorContext context,
        final @Nonnull String template,
        final @Nonnull Collection<String> properties
    ) {
        for (final var property : properties) {
            addProperty(context, template, property);
        }

        return false;
    }
}
