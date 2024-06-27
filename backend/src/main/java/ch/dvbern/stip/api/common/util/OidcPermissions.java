package ch.dvbern.stip.api.common.util;

public final class OidcPermissions {
    public static final String GESUCH_CREATE = "GESUCH_CREATE";
    public static final String GESUCH_READ = "GESUCH_READ";
    public static final String GESUCH_UPDATE = "GESUCH_UPDATE";
    public static final String GESUCH_DELETE = "GESUCH_DELETE";

    public static final String FALL_CREATE = "FALL_CREATE";
    public static final String FALL_READ = "FALL_READ";
    public static final String FALL_UPDATE = "FALL_UPDATE";
    public static final String FALL_DELETE = "FALL_DELETE";

    public static final String AUSBILDUNG_CREATE = "AUSBILDUNG_CREATE";
    public static final String AUSBILDUNG_DELETE = "AUSBILDUNG_DELETE";
    public static final String AUSBILDUNG_READ = "AUSBILDUNG_READ";
    public static final String AUSBILDUNG_UPDATE = "AUSBILDUNG_UPDATE";

    public static final String STAMMDATEN_CREATE = "STAMMDATEN_CREATE";
    public static final String STAMMDATEN_DELETE = "STAMMDATEN_DELETE";
    public static final String STAMMDATEN_READ = "STAMMDATEN_READ";
    public static final String STAMMDATEN_UPDATE = "STAMMDATEN_UPDATE";

    public static final String SEND_EMAIL = "SEND_EMAIL";

    private OidcPermissions() {

    }
}
