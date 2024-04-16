package ch.dvbern.stip.api.benutzer.entity;

import java.text.Normalizer;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BuchstabenRangeUtility {
    public boolean contains(final String buchstabenRange, final String str) {
        if (buchstabenRange == null || str == null) {
            return false;
        }

        final var range = BuchstabenRange.parse(buchstabenRange);
        return range.map(value -> value.contains(str)).orElse(false);
    }

    @Getter
    @Setter
    public class BuchstabenRange {
        private final String start;
        private final String end;

        private BuchstabenRange(final String start, final String end) {
            this.start = start.toUpperCase();
            this.end = end.toUpperCase();
        }

        public static Optional<BuchstabenRange> parse(String buchstabenRange) {
            final var split = buchstabenRange.split("-");
            if (split.length != 2) {
                return Optional.empty();
            }

            return Optional.of(new BuchstabenRange(split[0], split[1]));
        }

        public boolean contains(final String toCheck) {
            // For now, we just normalize the string removing any non ASCII character
            // This needs to change, since starting in 2025 any european language/ alphabet can be used for the name
            final var normalized = Normalizer
                .normalize(toCheck.toUpperCase(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");

            // substring is needed because names can be shorter than start/ end
            return start.compareTo(normalized.substring(0, Math.min(normalized.length(), start.length()))) <= 0 &&
                end.compareTo(normalized.substring(0, Math.min(normalized.length(), end.length()))) >= 0;
        }
    }
}
