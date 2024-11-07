/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
