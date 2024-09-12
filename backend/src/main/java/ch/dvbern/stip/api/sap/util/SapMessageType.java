package ch.dvbern.stip.api.sap.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SapMessageType {
    SUCCESS("success"), ERROR("no action | failure");

    private final String description;

    public static SapMessageType parse(final String raw) {
        return switch (raw) {
            case "S" -> SapMessageType.SUCCESS;
            case "E" -> SapMessageType.ERROR;
            default -> throw new IllegalStateException("Unexpected value: " + raw);
        };
    }

}
