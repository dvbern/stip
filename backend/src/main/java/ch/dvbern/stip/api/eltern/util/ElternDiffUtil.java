package ch.dvbern.stip.api.eltern.util;

import java.util.Objects;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ElternDiffUtil {
    public static boolean hasWohnkostenChanged(final ElternUpdateDto updateDto, final Eltern target) {
        if (updateDto == null || target == null) {
            return false;
        }

        return !Objects.equals(updateDto.getWohnkosten(), target.getWohnkosten());
    }
}
