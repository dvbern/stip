package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class GesuchUtils {
    public boolean hasAenderung(final Gesuch gesuch) {
        return gesuch.getGesuchTranchen().stream().filter(tranche -> tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN).count() > 0;
    }

    public boolean isAenderung(final GesuchTranche tranche) {
        return tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
            && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN;
    }

    public GesuchTranche getNewestTrancheWithoutAenderung(final List<GesuchTranche> tranchen) {
        return Collections.max(
            tranchen,
            Comparator.comparing(tranche -> tranche.getGueltigkeit().getGueltigBis())
        );
    }
}
