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

package ch.dvbern.stip.api.demo.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import ch.dvbern.stip.api.demo.type.DemoDataParseContext;
import ch.dvbern.stip.generated.dto.DemoAuszahlungDto;
import ch.dvbern.stip.generated.dto.DemoElternteilDto;
import ch.dvbern.stip.generated.dto.DemoPartnerDto;
import jakarta.ws.rs.BadRequestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.CellType;
import org.dhatim.fastexcel.reader.Row;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Slf4j
@UtilityClass
public class ParseDemoDataUtil {
    public final DateTimeFormatter dmyFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public final DateTimeFormatter dmFormatter = DateTimeFormatter.ofPattern("dd.MM");

    public boolean isBlank(Cell cell) {
        return cell == null || cell.getType() == CellType.EMPTY
        || (cell.getType() == CellType.STRING
        && (cell.asString().trim().isEmpty() || cell.asString()
            .trim()
            .equals(" ")));
    }

    public static Pair<String, String> parseDescription(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            throw new BadRequestException("No description provided");
        }

        final var fullText = cell.getText();
        final var indexColon = fullText.indexOf(':');
        final var substringIndex = indexColon > 0 ? indexColon : fullText.indexOf('.');

        if (substringIndex == -1) {
            throw new BadRequestException("No description with '.' or ':' provided");
        }

        return Pair.of(fullText.substring(0, substringIndex).trim(), fullText.substring(substringIndex + 1).trim());
    }

    public Boolean parseBoolean(Cell cell) {
        if (isBlank(cell)) {
            throw new BadRequestException("No Ja/Nein given");
        }
        return cell.asString().equals("Ja");
    }

    public Integer parseIntegerNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        if (cell.getType() == CellType.FORMULA) {
            try {
                final var value = (BigDecimal) cell.getValue();
                return value == null ? null : value.intValue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return cell.asNumber().intValue();
    }

    public int parseInteger(Cell cell) {
        if (isBlank(cell)) {
            throw new BadRequestException("No integer given");
        }
        return switch (cell.getType()) {
            case NUMBER -> cell.asNumber().intValue();
            case STRING -> Integer.parseInt(cell.asString());
            default -> throw new BadRequestException("Unexpected value: " + cell.getType());
        };
    }

    public static Integer parsePercentageNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return switch (cell.getType()) {
            case NUMBER -> {
                final var value = cell.asNumber();
                if (cell.getRawValue().contains("%")) {
                    yield value.intValue() * 100;
                }
                yield value.intValue();
            }
            case STRING -> Integer.parseInt(cell.asString().replace('%', ' ').trim());
            default -> throw new BadRequestException("Unexpected value: " + cell.getType());
        };
    }

    public String parseDateString(Cell cell) {
        return parseDate(cell).format(ParseDemoDataUtil.dmyFormatter);
    }

    public String parseGeburtsdatum(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return parseDate(cell).format(ParseDemoDataUtil.dmFormatter);
    }

    public LocalDate parseDate(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return cell.asDate().toLocalDate();
    }

    public Boolean parseBooleanNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return parseBoolean(cell);
    }

    public String parseString(Cell cell) {
        if (isBlank(cell)) {
            throw new BadRequestException("No string given");
        }
        return switch (cell.getType()) {
            case STRING -> cell.asString();
            case NUMBER -> String.valueOf(cell.asNumber());
            default -> throw new BadRequestException("Unexpected value: " + cell.getType());
        };
    }

    public String parseStringNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return parseString(cell);
    }

    public String parseLandIsoCode(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        final var landIsoCode = cell.asString();
        if (landIsoCode.length() != 2) {
            throw new BadRequestException("Invalid landIsoCode: " + landIsoCode);
        }
        return landIsoCode;
    }

    public LocalDate parseMonthYear(Cell cell, boolean endOfMonth) {
        if (isBlank(cell)) {
            return null;
        }
        final var date = cell.asDate().toLocalDate();
        if (endOfMonth) {
            return date.with(lastDayOfMonth());
        }
        return date.with(firstDayOfMonth());
    }

    public <T> T skipEntries(Iterator<T> iterator, int amount) {
        T item = null;
        for (var i = 0; iterator.hasNext() && i < amount; i++) {
            item = iterator.next();
        }

        return item;
    }

    public void checkCellContains(Row currentRow, String pattern, int column) {
        final var cell = currentRow.getCell(column);
        final var value = cell.asString();
        final var matches = Pattern.compile(pattern, Pattern.DOTALL).matcher(value).matches();
        if (!matches) {
            throw new BadRequestException(
                "Current cell did not match expected definition. Expected: \"%s\", got: \"%s\" [%s]"
                    .formatted(pattern, value, cell.getAddress().toString())
            );
        }
    }

    public <T> void initListEntries(
        Row currentRow,
        List<T> list,
        int firstValueColumn,
        int column,
        int amountOfEntries,
        String pattern,
        Function<DemoDataParseContext, T> createValue
    ) {
        final var listIterator = list.listIterator();
        checkCellContains(currentRow, pattern, column);
        for (int i = firstValueColumn; i < amountOfEntries; i++) {
            final var cell = currentRow.getCell(i);
            final var index = i - firstValueColumn;
            tryParseData(
                () -> {
                    listIterator.add(createValue.apply(new DemoDataParseContext(cell, index)));
                },
                cell
            );
        }
    }

    public <T> void updateListEntries(
        Row currentRow,
        List<T> list,
        int firstValueColumn,
        int column,
        int amountOfEntries,
        String pattern,
        BiConsumer<DemoDataParseContext, T> updateValue
    ) {
        final var listIterator = list.listIterator();
        checkCellContains(currentRow, pattern, column);
        for (int i = firstValueColumn; i < amountOfEntries; i++) {
            final var cell = currentRow.getCell(i);
            if (!listIterator.hasNext()) {
                throw new BadRequestException("List hast not enough entries");
            }
            final var listItem = listIterator.next();
            final var index = i - firstValueColumn;
            tryParseData(
                () -> {
                    updateValue.accept(new DemoDataParseContext(cell, index), listItem);
                },
                cell
            );
        }
    }

    public boolean hasValue(DemoPartnerDto partnerDto) {
        return Objects.nonNull(partnerDto.getSozialversicherungsnummer());
    }

    public boolean hasValue(DemoElternteilDto elternteilDto) {
        return Objects.nonNull(elternteilDto.getNachname());
    }

    public static boolean hasValue(DemoAuszahlungDto demoAuszahlungDto) {
        return Objects.nonNull(demoAuszahlungDto.getIban());
    }

    private void tryParseData(Runnable parser, Cell cell) {
        try {
            parser.run();
        } catch (Exception e) {
            LOG.error("Parse error:", e);
            throw new BadRequestException(
                "Cell was not accessed correctly, value: '%s' [%s]\n%s"
                    .formatted(cell.getRawValue(), cell.getAddress().toString(), e.getMessage()),
                e
            );
        }
    }
}
