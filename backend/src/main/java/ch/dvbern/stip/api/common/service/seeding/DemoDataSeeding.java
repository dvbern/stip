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

package ch.dvbern.stip.api.common.service.seeding;

import java.io.FileNotFoundException;
import java.util.List;

import ch.dvbern.stip.api.common.exception.CancelInvocationException;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.demo.repo.DemoDataRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Singleton
@RequiredArgsConstructor
// @Slf4j
public class DemoDataSeeding extends Seeder {
    private final ConfigService configService;
    private final DemoDataRepository demoDataRepository;

    private static final String PATH_TO_CSV_DEMO_DATA = "/seeding/demo/test_faelle.xlsx";
    private static final int UNUSED_START_LINES = 2;

    @Override
    protected void seed() {
//        if (demoDataRepository.count() > 0) {
//            return;
//        }
//
//        try {
//            // LOG.info("Seeding Demo data");
//            final var list = getDemoData();
//            demoDataRepository.flush();
//        } catch (Exception e) {
//            throw new CancelInvocationException(e);
//        }
    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedOnProfile();
    }

    @SneakyThrows
    public List<?> getDemoData() {
        try (final var resource = getClass().getResourceAsStream(PATH_TO_CSV_DEMO_DATA)) {
            if (resource == null) {
                throw new FileNotFoundException("Could not load CSV for demo data: " + PATH_TO_CSV_DEMO_DATA);
            }

            final var workbook =
                new XSSFWorkbook(resource);

            final List<String> list = List.of();
            final var sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                final var rowNum = row.getRowNum();
                if (rowNum <= UNUSED_START_LINES) {
                    continue;
                }

                if (rowNum > 5) {
                    break;
                }

                StringBuilder values = new StringBuilder();
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    values.append(row.getCell(i).getStringCellValue()).append("; ");
                }
                list.add(values.toString());
            }

            return list;
        }
    }
}
