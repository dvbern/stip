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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import ch.dvbern.stip.generated.dto.DemoAuszahlungDto;
import ch.dvbern.stip.generated.dto.DemoElternteilDto;
import ch.dvbern.stip.generated.dto.DemoPartnerDto;
import ch.dvbern.stip.generated.dto.DemoSteuerdatenDto;
import ch.dvbern.stip.generated.dto.DemoSteuererklaerungDto;
import jakarta.ws.rs.BadRequestException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@UtilityClass
public class ParseDemoDataUtil {
    private final DateTimeFormatter dmyFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public int getNumberOfCells(Row row, int startCol) {
        int i;
        for (i = startCol; i < row.getLastCellNum(); i++) {
            final var cell = row.getCell(i);
            if (isBlank(cell) || cell.getStringCellValue().trim().isEmpty()) {
                break;
            }
        }
        return i - startCol;
    }

    public boolean isBlank(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK
        || (cell.getCellType() == CellType.STRING
        && (cell.getStringCellValue().trim().isEmpty() || cell.getStringCellValue()
            .trim()
            .equals(" ")));
    }

    public static Pair<String, String> parseDescription(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            throw new IllegalStateException("No description provided");
        }

        final var fullText = cellToSimpleHtml(cell);
        final var indexColon = fullText.indexOf(':');
        final var substringIndex = indexColon > 0 ? indexColon : fullText.indexOf('.');

        if (substringIndex == -1) {
            throw new IllegalStateException("No description with '.' or ':' provided");
        }

        return Pair.of(fullText.substring(0, substringIndex).trim(), fullText.substring(substringIndex + 1).trim());
    }

    public Boolean parseBoolean(Cell cell) {
        if (isBlank(cell)) {
            throw new IllegalStateException("No Ja/Nein given");
        }
        return cell.getStringCellValue().equals("Ja");
    }

    public Integer parseIntegerNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return (int) cell.getNumericCellValue();
    }

    public int parseInteger(Cell cell) {
        if (isBlank(cell)) {
            throw new IllegalStateException("No integer given");
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> (int) cell.getNumericCellValue();
            case STRING -> Integer.parseInt(cell.getStringCellValue());
            default -> throw new IllegalStateException("Unexpected value: " + cell.getCellType());
        };
    }

    public static Integer parsePercentageNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> {
                final var value = cell.getNumericCellValue();
                if (cell.getCellStyle().getDataFormatString().contains("%")) {
                    yield (int) (value * 100);
                }
                yield (int) value;
            }
            case STRING -> Integer.parseInt(cell.getStringCellValue().replace('%', ' ').trim());
            default -> throw new IllegalStateException("Unexpected value: " + cell.getCellType());
        };
    }

    public String parseDateString(Cell cell) {
        return parseDate(cell).format(ParseDemoDataUtil.dmyFormatter);
    }

    public LocalDate parseDate(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return DateUtil.getLocalDateTime(cell.getNumericCellValue()).toLocalDate();
    }

    public Boolean parseBooleanNullable(Cell cell) {
        if (isBlank(cell)) {
            return null;
        }
        return parseBoolean(cell);
    }

    public String parseString(Cell cell) {
        if (isBlank(cell)) {
            throw new IllegalStateException("No string given");
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> throw new IllegalStateException("Unexpected value: " + cell.getCellType());
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
        final var landIsoCode = cell.getStringCellValue();
        if (landIsoCode.length() != 2) {
            throw new IllegalStateException("Invalid landIsoCode: " + landIsoCode);
        }
        return landIsoCode;
    }

    public LocalDate parseMonthYear(Cell cell, boolean endOfMonth) {
        if (isBlank(cell)) {
            return null;
        }
        final var date = cell.getLocalDateTimeCellValue().toLocalDate();
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
        final var value = cell.getStringCellValue();
        final var matches = Pattern.compile(pattern, Pattern.DOTALL).matcher(value).matches();
        if (!matches) {
            throw new IllegalStateException(
                "Current cell did not match expected definition. Expected: \"%s\", got: \"%s\" [%s]"
                    .formatted(pattern, value, cell.getAddress().formatAsString())
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
        Function<Cell, T> createValue
    ) {
        final var listIterator = list.listIterator();
        checkCellContains(currentRow, pattern, column);
        for (int i = firstValueColumn; i < amountOfEntries; i++) {
            final var cell = currentRow.getCell(i);
            tryParseData(() -> {
                listIterator.add(createValue.apply(cell));
            }, cell);
        }
    }

    public <T> void updateListEntries(
        Row currentRow,
        List<T> list,
        int firstValueColumn,
        int column,
        int amountOfEntries,
        String pattern,
        BiConsumer<Cell, T> updateValue
    ) {
        final var listIterator = list.listIterator();
        checkCellContains(currentRow, pattern, column);
        for (int i = firstValueColumn; i < amountOfEntries; i++) {
            final var cell = currentRow.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (!listIterator.hasNext()) {
                throw new IllegalStateException("List hast not enough entries");
            }
            final var listItem = listIterator.next();
            tryParseData(() -> {
                updateValue.accept(cell, listItem);
            }, cell);
        }
    }

    public boolean hasValue(DemoPartnerDto partnerDto) {
        return Objects.nonNull(partnerDto.getSozialversicherungsnummer());
    }

    public boolean hasValue(DemoElternteilDto elternteilDto) {
        return Objects.nonNull(elternteilDto.getNachname());
    }

    public boolean hasValue(DemoSteuererklaerungDto steuernerklaerungDto) {
        return Objects.nonNull(steuernerklaerungDto.getSteuererklaerungInBern());
    }

    public boolean hasValue(DemoSteuerdatenDto steuerdatenDto) {
        return Objects.nonNull(steuerdatenDto.getTotalEinkuenfte());
    }

    public static boolean hasValue(DemoAuszahlungDto demoAuszahlungDto) {
        return Objects.nonNull(demoAuszahlungDto.getIban());
    }

    private void tryParseData(Runnable parser, Cell cell) {
        try {
            parser.run();
        } catch (Exception e) {
            final var formatter = new DataFormatter();
            throw new BadRequestException(
                "Cell was not accessed correctly, value: '%s' [%s]\n%s"
                    .formatted(formatter.formatCellValue(cell), cell.getAddress().formatAsString(), e.getMessage()),
                e
            );
        }
    }

    private static String cellToSimpleHtml(Cell cell) {
        if (!(cell instanceof XSSFCell) || cell.getRichStringCellValue().numFormattingRuns() == 0) {
            return cell.getStringCellValue();
        }
        final var rich = ((XSSFCell) cell).getRichStringCellValue();
        String full = rich.getString();

        StringBuilder taggedValue = new StringBuilder();

        for (int i = 0; i < rich.numFormattingRuns(); i++) {
            int start = rich.getIndexOfFormattingRun(i);
            int length = rich.getLengthOfFormattingRun(i);

            // If an empty formatting cell is found
            if (length <= 0)
                continue;

            final var text = full.substring(start, start + length);
            final var font = rich.getFontOfFormattingRun(i);

            if (Objects.isNull(font)) {
                taggedValue.append(escape(text));
                continue;
            }

            final var hasUnderline = font.getUnderline() > 0;
            final var hasBold = font.getBold();
            final var hasItalic = font.getItalic();

            if (hasUnderline)
                taggedValue.append("<u>");
            if (hasBold)
                taggedValue.append("<b>");
            if (hasItalic)
                taggedValue.append("<i>");

            taggedValue.append(escape(text));

            if (hasItalic)
                taggedValue.append("</i>");
            if (hasBold)
                taggedValue.append("</b>");
            if (hasUnderline)
                taggedValue.append("</u>");
        }

        return taggedValue.toString();
    }

    // Simple HTML escaper
    private static String escape(String s) {
        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
}
