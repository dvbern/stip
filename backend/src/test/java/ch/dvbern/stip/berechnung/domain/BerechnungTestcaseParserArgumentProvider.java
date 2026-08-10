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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.service.ParseDemoDataService;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class BerechnungTestcaseParserArgumentProvider implements ArgumentsProvider {
    static final String TESTFILE_PATH = "testcase/kiBon-Stip_Testfallmatrix_Master.xlsx";

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final var resource = BerechnungTestcaseParserArgumentProvider.class.getClassLoader().getResource(TESTFILE_PATH);

        final var demoDatas = parseList(
            Paths.get(resource.toURI()).toFile(),
            true
        );

        return demoDatas.stream().map(demoData -> Arguments.argumentSet(demoData.getTestFall(), demoData));
    }

    public static List<DemoData> parseList(final File file, final Boolean ignoreBerechnungErrors) {
        try (var workbook = new ReadableWorkbook(file)) {
            final var sheet = workbook.getSheet(0).get();
            final var rowIterator = sheet.openStream().iterator();
            final var amountOfCells =
                sheet.openStream().skip(2).findFirst().get().getPhysicalCellCount();
            return new ParseDemoDataService(rowIterator, amountOfCells)
                .parseAll(ignoreBerechnungErrors);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
