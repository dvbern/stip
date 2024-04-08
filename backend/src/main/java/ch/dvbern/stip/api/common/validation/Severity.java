package ch.dvbern.stip.api.common.validation;

import jakarta.validation.Payload;

public final class Severity {
    public static final class Warning implements Payload {
    }

    public static final class Error implements Payload {
    }

    private Severity() {
    }
}
