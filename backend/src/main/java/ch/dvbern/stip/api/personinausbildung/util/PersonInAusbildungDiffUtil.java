package ch.dvbern.stip.api.personinausbildung.util;

import java.util.Objects;

import ch.dvbern.stip.api.common.util.NullDiffUtil;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonInAusbildungDiffUtil {
    public boolean hasNameChanged(
        PersonInAusbildung target,
        PersonInAusbildungUpdateDto update
    ) {
        if (NullDiffUtil.hasNullChanged(target, update)) {
            return true;
        }

        String targetNachname = null;
        String toUpdateNachname = null;

        if (target != null) {
            targetNachname = target.getNachname();
        }

        if (update != null) {
            toUpdateNachname = update.getNachname();
        }

        return Objects.equals(targetNachname, toUpdateNachname);
    }
}
