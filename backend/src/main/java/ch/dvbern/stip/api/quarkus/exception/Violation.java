package ch.dvbern.stip.api.quarkus.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

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
