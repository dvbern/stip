package ch.dvbern.stip.api.common.exception;

import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ExceptionId implements Serializable {

    @Serial
    private static final long serialVersionUID = -3399892679761234356L;

    String id;

    public static ExceptionId random() {
        var id = shortUUIDString(UUID.randomUUID());

        return new ExceptionId(id);
    }

    @JsonCreator
    public static ExceptionId fromString(String id) {
        Objects.requireNonNull(StringUtils.trimToNull(id), "id must not be null or empty");

        return new ExceptionId(id);
    }

    public static ExceptionId fromUUID(UUID id) {
        return new ExceptionId(shortUUIDString(id));
    }

    @JsonValue
    @Override
    public String toString() {
        return id;
    }

    /**
     * Shorten the string representation of a UUID.
     */
    private static String shortUUIDString(UUID id) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());

        return Base64.getMimeEncoder()
            .withoutPadding()
            .encodeToString(bb.array());
    }
}
