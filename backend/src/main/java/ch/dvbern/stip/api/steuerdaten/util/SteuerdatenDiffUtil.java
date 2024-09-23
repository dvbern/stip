package ch.dvbern.stip.api.steuerdaten.util;

import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SteuerdatenDiffUtil {
    public static boolean hasArbeitsverhaeltnissChangedToUnselbstaendig(
        final SteuerdatenUpdateDto updateDto,
        final Steuerdaten toUpdate
    ) {
        if (updateDto.getIsArbeitsverhaeltnisSelbstaendig() == null) {
            // This shouldn't happen unless when using stubbed test data
            return false;
        }

        if (updateDto.getIsArbeitsverhaeltnisSelbstaendig().equals(toUpdate.getIsArbeitsverhaeltnisSelbstaendig())) {
            return false;
        }

        return !updateDto.getIsArbeitsverhaeltnisSelbstaendig();
    }
}
