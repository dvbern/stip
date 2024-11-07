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
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.nullness.qual.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class JsonMappingReferenceParser {
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Entry {
        @Nullable
        String fieldName;
        @Nullable
        Integer index;

        public String toPathEntry() {
            if (getIndex() != null) {
                return Integer.toString(getIndex());
            }

            return Objects.requireNonNull(getFieldName());
        }
    }

    private final List<Entry> entries;

    public static JsonMappingReferenceParser fromReferenceList(List<Reference> referenceList) {
        var entries = referenceList.stream()
            .map(
                reference -> new Entry(
                    reference.getFieldName(), reference.getIndex() == -1 ? null : reference.getIndex()
                )
            )
            .toList();

        return new JsonMappingReferenceParser(entries);
    }

    public String parse() {
        return entries.stream()
            .map(Entry::toPathEntry)
            .collect(Collectors.joining("."));
    }
}
