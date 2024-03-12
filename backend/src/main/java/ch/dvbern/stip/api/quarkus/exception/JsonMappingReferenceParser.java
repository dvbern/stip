package ch.dvbern.stip.api.quarkus.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class JsonMappingReferenceParser {

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Entry {
        @Nullable String fieldName;
        @Nullable Integer index;

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
            .map(reference -> new Entry(
                reference.getFieldName(), reference.getIndex() == -1 ? null : reference.getIndex())
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
