package ch.dvbern.stip.api.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.RequiresDocumentIf;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
public class ReflectionUtil {
    public List<Pair<String, List<Pair<Object, RequiresDocumentIf>>>> getRequiresDocumentIfFields(GesuchFormular formular) {
        final var result = new ArrayList<Pair<String, List<Pair<Object, RequiresDocumentIf>>>>();
        try {
            Field[] declaredFields = formular.getClass().getDeclaredFields();
			for (final var field : declaredFields) {
                if (!AbstractMandantEntity.class.isAssignableFrom(field.getType())) {
                    continue;
                }

				if (Collection.class.isAssignableFrom(field.getType())) {
					final var getter = getter(field, formular.getClass());
                    var i = 0;
					for (final var value : (Collection<?>) getter.invoke(formular)) {
						result.add(new ImmutablePair<>(field.getName() + "[" + i + "]", getFromPage(value)));
                        i++;
					}
				} else {
					final var getter = getter(field, formular.getClass());
					result.add(new ImmutablePair<>(field.getName(), getFromPage(getter.invoke(formular))));
				}
			}

            return result;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Oops", e);
        }
    }

    private List<Pair<Object, RequiresDocumentIf>> getFromPage(Object pageObject)
        throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final var result = new ArrayList<Pair<Object, RequiresDocumentIf>>();
        if (pageObject == null) {
            return result;
        }

        for (final var field : pageObject.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(RequiresDocumentIf.class)) {
                result.add(new ImmutablePair<>(
                    getter(field, pageObject.getClass()).invoke(pageObject),
                    field.getAnnotation(RequiresDocumentIf.class))
                );
            }
        }

        return result;
    }

    private Method getter(final Field input, final Class<?> cls) throws NoSuchMethodException {
        final var name = Character.toUpperCase(input.getName().charAt(0)) + input.getName().substring(1);
        return cls.getDeclaredMethod("get" + name);
    }
}
