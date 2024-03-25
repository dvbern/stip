package ch.dvbern.stip.api.auszahlung.util;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.common.util.NullDiffUtil;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuszahlungDiffUtil {
    public boolean hasAdresseChanged(AuszahlungUpdateDto newAuszahlung, Auszahlung toUpdate) {
        if (NullDiffUtil.hasNullChanged(newAuszahlung.getAdresse(), toUpdate.getAdresse())) {
            return true;
        }

        return newAuszahlung.getAdresse().getId() != toUpdate.getAdresse().getId();
    }
}
