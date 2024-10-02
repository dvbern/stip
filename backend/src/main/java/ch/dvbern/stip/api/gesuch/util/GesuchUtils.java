package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import lombok.experimental.UtilityClass;


@UtilityClass
public class GesuchUtils {
    public boolean isAenderung(final GesuchTranche tranche) {
        return tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN;
    }
}
