package ch.dvbern.stip.api.gesuch.type;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum GesuchTrancheStatus {
    IN_BEARBEITUNG_GS,
    UEBERPRUEFEN,
    MANUELLE_AENDERUNG,
    AKZEPTIERT,
    ABGELEHNT;

    public static final Set<GesuchTrancheStatus> GESUCHSTELLER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS
        )
    );

    public static final Set<GesuchTrancheStatus> SACHBEARBEITER_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            UEBERPRUEFEN,
            MANUELLE_AENDERUNG
        )
    );

    public static final Set<GesuchTrancheStatus> ADMIN_CAN_EDIT = Collections.unmodifiableSet(
        EnumSet.of(
            IN_BEARBEITUNG_GS,
            UEBERPRUEFEN,
            AKZEPTIERT,
            ABGELEHNT
        )
    );
}
