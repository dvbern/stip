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

package ch.dvbern.stip.berechnung.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class BerechnungServiceTestArgumentProvider implements ArgumentsProvider {
    static final String SERIALIZED_TESTCASES_PATH = "testcase/serialized-testcases";

    @Getter
    @RequiredArgsConstructor
    public class GesuchWithResultat {
        private final Gesuch gesuch;
        private final Integer stipendien;
        private final Integer darlehen;
        private final String testFall;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final var resource = BerechnungServiceTest.class.getClassLoader().getResource(SERIALIZED_TESTCASES_PATH);

        if (resource == null) {
            throw new IllegalStateException("Could not find serialized testcases at " + SERIALIZED_TESTCASES_PATH);
        }

        final var mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return Files.list(Paths.get(resource.toURI()))
            .filter(path -> path.getFileName().toString().endsWith(".json"))
            .sorted(Comparator.comparing(path -> path.getFileName().toString()))
            .map(path -> readGesuchTestcase(mapper, path))
            .map(testcase -> Arguments.argumentSet(testcase.testFall, testcase));
    }

    private GesuchWithResultat readGesuchTestcase(final ObjectMapper mapper, final Path path) {
        try {
            final var root = mapper.readTree(path.toFile());
            final var gesuch = mapper.treeToValue(root.get("gesuch"), Gesuch.class);
            final var stipendien = mapper.treeToValue(root.get("stipendien"), Integer.class);
            final var darlehen = mapper.treeToValue(root.get("darlehen"), Integer.class);

            for (final GesuchTranche tranche : gesuch.getGesuchTranchen()) {
                tranche.setGesuch(gesuch);
                tranche.getGesuchFormular().setTranche(tranche);
            }

            return new GesuchWithResultat(gesuch, stipendien, darlehen, path.getFileName().toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not read serialized testcase " + path, e);
        }
    }
}
