package ch.dvbern.stip.api.generator.api;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDtoSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchsperiodeTestSpecGenerator {
    public GesuchsperiodeCreateDtoSpec gesuchsperiodeCreateDtoSpec()
        throws InvocationTargetException, IllegalAccessException {
        final var model = new GesuchsperiodeCreateDtoSpec();
        setValues(model);
        return model;
    }

    public GesuchsperiodeUpdateDtoSpec gesuchsperiodeUpdateDtoSpec()
        throws InvocationTargetException, IllegalAccessException {
        final var model = new GesuchsperiodeUpdateDtoSpec();
        setValues(model);
        return model;
    }

    private <T> void setValues(T model)
        throws InvocationTargetException, IllegalAccessException {
        final var currentDate = LocalDate.now();
        for (final var method : model.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                final var params = method.getParameters();
                if (params.length != 1) {
                    continue;
                }

                final var paramType = params[0].getType();
                if (paramType.equals(String.class)) {
                    method.invoke(model, "");
                } else if (paramType.equals(Integer.class)) {
                    method.invoke(model, 1);
                } else if (paramType.equals(LocalDate.class)) {
                    method.invoke(model, currentDate);
                }
            }
        }
    }
}
