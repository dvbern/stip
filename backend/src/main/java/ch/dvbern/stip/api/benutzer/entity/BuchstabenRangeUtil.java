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

package ch.dvbern.stip.api.benutzer.entity;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BuchstabenRangeUtil {
    public boolean contains(final String buchstabenRange, final String toCheck) {
        if (buchstabenRange == null || toCheck == null) {
            return false;
        }

        final var range = BuchstabenRange.parse(buchstabenRange);
        return range.contains(toCheck);
    }

    @Getter
    @Setter
    public class BuchstabenRange {
        private final List<Pair> pairs;

        private BuchstabenRange(final List<Pair> pairs) {
            this.pairs = pairs;
        }

        /**
         * Parses a given {@link String} into one or multiple {@link Pair} of letter ranges
         * <p>
         * Example: A-F,X -> [[ A-X ], [ X - X ]]
         * <p>
         * Example: AB-C,X -> [[ AB-C ], [ X - X ]]
         */
        public static BuchstabenRange parse(String buchstabenRange) {
            final var pairs = new ArrayList<Pair>();

            // Iterate over all parts split by commas
            final var commaSplit = buchstabenRange.split(",");
            for (final var commaPart : commaSplit) {
                // If there is exactly 1 dash, then add left and right of it
                final var dashSplit = commaPart.split("-");
                if (dashSplit.length == 2) {
                    pairs.add(new Pair(dashSplit[0], dashSplit[1]));
                    // and continue to the next commaPart
                    continue;
                }

                // Otherwise just add the comma part
                pairs.add(new Pair(commaPart, commaPart));
            }

            return new BuchstabenRange(pairs);
        }

        public boolean contains(final String toCheck) {
            return pairs.stream().anyMatch(x -> x.contains(toCheck));
        }

        private record Pair(String start, String end) {
            private Pair(final String start, final String end) {
                this.start = start.toUpperCase();
                this.end = end.toUpperCase();
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
}
