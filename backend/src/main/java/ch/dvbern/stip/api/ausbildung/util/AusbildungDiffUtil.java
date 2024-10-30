package ch.dvbern.stip.api.ausbildung.util;

import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AusbildungDiffUtil {
    public boolean hasIsAusbildungAuslandChanged(
        final Ausbildung target,
        final AusbildungUpdateDto update
    ) {
        if (target.getId() == null) {
            return false;
        }

        return !Objects.equals(target.getIsAusbildungAusland(), update.getIsAusbildungAusland());
    }
}
