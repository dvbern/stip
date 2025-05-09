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

package ch.dvbern.stip.api.quarkus.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Violation {
    String path;
    String key;
    String message;

    public static Violation of(
        String path,
        String key,
        String message
    ) {
        return new Violation(path, key, message);
    }

    public static Violation fromReferenceList(
        String message,
        String key,
        List<Reference> referenceList
    ) {
        return of(
            buildPath(referenceList),
            key,
            message
        );
    }

    private static String buildPath(List<Reference> referenceList) {
        return referenceList.stream()
            .map(Violation::toPathEntry)
            .collect(Collectors.joining("."));
    }

    private static String toPathEntry(Reference reference) {
        if (reference.getIndex() < 0) {
            return reference.getFieldName();
        }
        return Integer.toString(reference.getIndex());
    }
}
