package ch.dvbern.stip.api.gesuch.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.dvbern.stip.api.common.validation.HasPageValidation;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PageValidationUtil {
    /**
     * Gets the list of groups to apply to validate only the pages which the user has filled out.
     * */
    public List<Class<?>> getGroupsFromGesuchFormular(final @NotNull GesuchFormular gesuch) {
        final var validationGroups = new ArrayList<Class<?>>();
        for (final var field : gesuch.getClass().getDeclaredFields()) {
            try {
                final var pageValidation = field.getAnnotation(HasPageValidation.class);
                if (pageValidation == null) {
                    continue;
                }

                final var name = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                final var getter = gesuch.getClass().getDeclaredMethod("get" + name);
                final var value = getter.invoke(gesuch);
                if (value == null ||
                    (Collection.class.isAssignableFrom(value.getClass()) && ((Collection<?>) value).isEmpty())) {
                    continue;
                }

                validationGroups.addAll(Arrays.asList(pageValidation.value()));
            } catch (IllegalAccessException e) {
                LOG.error("Illegal access of field {}", field);
            } catch (NoSuchMethodException e) {
                LOG.error("Field with 'HasPageValidation' annotation exists but does not have a getter: {}", field);
            } catch (InvocationTargetException e) {
                LOG.error("Failed to invoke getter for field {}", field);
            }
        }

        return validationGroups;
    }
}
