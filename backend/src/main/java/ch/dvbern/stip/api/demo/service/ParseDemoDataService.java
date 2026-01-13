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

package ch.dvbern.stip.api.demo.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.util.ParseDemoDataUtil;
import ch.dvbern.stip.api.demo.util.ParseDemoEnumUtil;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.generated.dto.DemoAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufTaetigkeitDto;
import ch.dvbern.stip.generated.dto.DemoPartnerDto;
import ch.dvbern.stip.generated.dto.DemoPersonInAusbildungDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@ApplicationScoped
@NoArgsConstructor
public class ParseDemoDataService {
    private final int UNUSED_START_LINES = 2;
    private final int FIRST_VALUE_COLOUMN = 5;
    private final int ANZAHL_LEBENSLAUF_ITEMS = 4;
    private Iterator<Row> rowIterator;
    private int amountOfCells = 0;

    public List<DemoData> parseList(final Path file) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.toFile())) {
            final var sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();
            amountOfCells = ParseDemoDataUtil.getNumberOfCells(sheet.getRow(UNUSED_START_LINES), FIRST_VALUE_COLOUMN);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        skipRows(UNUSED_START_LINES);
        final var demoDataList = prepareInfo();
        final var ausbildungen = prepareAusbildung();
        final var pias = preparePersonInAusbildung();
        final var lebenslaeufe = prepareLebenslauf();
        final var partners = preparePartnerin();

        for (int i = 0; i < demoDataList.size(); i++) {
            final var demoData = demoDataList.get(i);
            demoData.getDemoDataDto().setAusbildung(ausbildungen.get(i));
            demoData.getDemoDataDto().setPersonInAusbildung(pias.get(i));
            demoData.getDemoDataDto().setLebenslauf(lebenslaeufe.get(i));
            partners.get(i).ifPresent(partnerin -> demoData.getDemoDataDto().setPartner(partnerin));

            demoData.persistDemoData();
            final var recoveredDemoData = demoData.parseDemoDataDto();
        }

        System.out.println(ausbildungen);

        return demoDataList;
    }

    private <T> void initList(List<T> list, String pattern, int column, Function<Cell, T> createValue) {
        ParseDemoDataUtil.initListEntries(
            rowIterator.next(),
            list,
            FIRST_VALUE_COLOUMN,
            column,
            amountOfCells,
            pattern,
            createValue
        );
    }

    private <T> void updateList(List<T> list, String pattern, int column, BiConsumer<Cell, T> updateValue) {
        ParseDemoDataUtil.updateListEntries(
            rowIterator.next(),
            list,
            FIRST_VALUE_COLOUMN,
            column,
            amountOfCells,
            pattern,
            updateValue
        );
    }

    private Row skipRows(int amount) {
        return ParseDemoDataUtil.skipEntries(rowIterator, amount);
    }

    private List<DemoData> prepareInfo() {
        final List<DemoData> list = new ArrayList<>(amountOfCells);
        initList(list, "Testfall-ID", 0, c -> {
            final var demoData = new DemoData().setTestFall(c.getStringCellValue());
            return demoData;
        });
        ParseDemoDataUtil.skipEntries(rowIterator, 2);
        updateList(list, "Kurzbeschrieb .* Bemerkungen.*", 0, (c, d) -> {
            final var fullText = c.getStringCellValue();
            try {
                d.setName(fullText.substring(0, fullText.indexOf(":")))
                    .setDescription(fullText.substring(fullText.indexOf(":") + 2));
            } catch (Exception e) {
                System.out.printf("'%s'%n", fullText);
                throw e;
            }
        });
        // spotless:off
        updateList(list, "Erfasser des Testfalls", 0, (c, d) -> d.setErfasser(c.getStringCellValue()));
        updateList(list, "Gesuchsjahr", 0, (c, d) -> d.setGesuchsjahr(c.getStringCellValue()));
        updateList(list, "Gesuchseingang", 0, (c, d) -> d.setGesuchseingang(ParseDemoDataUtil.parseDateString(c)));
        // spotless:on

        return list;
    }

    private List<DemoAusbildungDto> prepareAusbildung() {
        ParseDemoDataUtil.skipEntries(rowIterator, 3);

        final List<DemoAusbildungDto> list = new ArrayList<>();
        // spotless:off
        initList(list, "Ausbildung für Stipendium.*", 0, c -> new DemoAusbildungDto());
        updateList(list, "Ausbildungsstätte", 1, (c, d) -> d.setAusbildungsstaette(c.getStringCellValue()));
        updateList(list, "Ausbildungsgang", 1, (c, d) -> d.setAusbildungsgang(c.getStringCellValue()));
        updateList(list, "Berufsbezeichnung .* Fachrichtung", 2, (c, d) -> d.setBerufsbezeichnungFachrichtung(c.getStringCellValue()));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf((int)c.getNumericCellValue())));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(c.getStringCellValue()));
        updateList(list, ".*Ausbildung im Ausland.*", 1, (c, d) -> d.setIsAusbildungAusland(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "Andere Ausbildungsstätte.*", 1, (c, d) -> d.setAusbildungNichtGefunden(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "Beginn der Ausbildung", 1, (c, d) -> d.setAusbildungBeginn(ParseDemoDataUtil.parseMonthYear(c)));
        updateList(list, ".*Ende der Ausbildung", 1, (c, d) -> d.setAusbildungEnd(ParseDemoDataUtil.parseMonthYear(c)));
        updateList(list, "Pensum.*", 1, (c, d) -> d.setPensum((int)c.getNumericCellValue()));
        // spotless:on

        return list;
    }

    private List<DemoPersonInAusbildungDto> preparePersonInAusbildung() {
        final List<DemoPersonInAusbildungDto> list = new ArrayList<>();
        // spotless:off
        initList(list, "Person in Ausbildung", 0, c -> new DemoPersonInAusbildungDto());
        updateList(list, "Sozialversicherungsnummer", 1, (c, d) -> d.setSozialversicherungsnummer(c.getStringCellValue()));
        updateList(list, "Anrede", 1, (c, d) -> d.setAnrede(ParseDemoEnumUtil.parseAnrede(c)));
        updateList(list, "Nachname", 1, (c, d) -> d.setNachname(c.getStringCellValue()));
        updateList(list, "Vorname", 1, (c, d) -> d.setVorname(c.getStringCellValue()));
        updateList(list, "Strasse", 1, (c, d) -> d.setStrasse(c.getStringCellValue()));
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf((int)c.getNumericCellValue())));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf((int)c.getNumericCellValue())));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(c.getStringCellValue()));
        updateList(list, "Co-Adresse", 1, (c, d) -> d.setCoAdresse(c.getStringCellValue()));
        updateList(list, "Land", 1, (c, d) -> d.setLand(c.getStringCellValue()));
        updateList(list, "Identischer zivilrechtlicher.*", 1, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitz(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "PLZ", 2, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitzPLZ(c.getStringCellValue()));
        updateList(list, "Ort", 2, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitzOrt(c.getStringCellValue()));
        updateList(list, "Email", 1, (c, d) -> d.setEmail(c.getStringCellValue()));
        updateList(list, "Telefonnummer", 1, (c, d) -> d.setTelefonnummer(c.getStringCellValue()));
        updateList(list, "Geburtsdatum", 1, (c, d) -> d.setGeburtsdatum(ParseDemoDataUtil.parseDateString(c)));
        updateList(list, "Zivilstand", 1, (c, d) -> d.setZivilstand(ParseDemoEnumUtil.parseZivilstand(c)));
        updateList(list, "Nationalität", 1, (c, d) -> d.setNationalitaet(c.getStringCellValue()));
        updateList(list, "Niederlassungsstatus.*", 2, (c, d) -> d.setNiederlassungsstatus(ParseDemoEnumUtil.parseNiederlassungsstatus(c)));
        updateList(list, "Einreisdatum Schweiz.*", 3, (c, d) -> d.setEinreisedatum(ParseDemoDataUtil.parseDate(c)));
        updateList(list, "Flüchtlingsstatus.*", 3, (c, d) -> {
            final var fluechtling = ParseDemoDataUtil.parseBoolean(c);
            if (fluechtling) {
                d.setNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS);
            }
        });
        updateList(list, "Zuständiger Kanton.*", 4, (c, d) -> {
            final var zustaendigerKanton = c.getStringCellValue();
            if (Objects.equals(zustaendigerKanton, "Kanton Bern")) {
                d.setNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT);
            }
        });
        updateList(list, "PLZ", 3, (c, d) -> d.setHeimatortPLZ(String.valueOf((int)c.getNumericCellValue())));
        updateList(list, "Heimatort", 3, (c, d) -> d.setHeimatort(c.getStringCellValue()));
        skipRows(1);
        updateList(list, "bei Vater.*", 2, (c, d) -> d.setWohnsitzAnteilVater((int)c.getNumericCellValue()));
        updateList(list, "bei Mutter.*", 2, (c, d) -> d.setWohnsitzAnteilMutter((int)c.getNumericCellValue()));
        updateList(list, ".*Beistandschaft", 1, (c, d) -> d.setVormundschaft(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "Zuständige KESB ", 2, (c, d) -> d.setZustaendigeKESB(ParseDemoEnumUtil.parseZustaendigeKESB(c)));
        updateList(list, ".*Sozialhilfebeiträge.*", 1, (c, d) -> d.setSozialhilfebeitraege(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "Korrespondenzsprache", 1, (c, d) -> {});
        // spotless:on

        return list;
    }

    private List<DemoLebenslaufDto> prepareLebenslauf() {
        // region Ausbildungen
        final List<List<Optional<DemoLebenslaufAusbildungDto>>> ausbildungen = new ArrayList<>(new ArrayList<>());
        skipRows(2);

        for (var i = 0; i < ANZAHL_LEBENSLAUF_ITEMS; i++) {
            if (i == 0) {
                // Init a list for all Testfälle with max 4 Ausbildungen
                initList(ausbildungen, "Abschluss", 1, cell -> {
                    if (ParseDemoDataUtil.isBlank(cell)) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(
                        List.of(Optional.of(new DemoLebenslaufAusbildungDto().abschluss(cell.getStringCellValue())))
                    );
                });
            } else {
                // Append to the list of Ausbildungen if there is a value
                updateList(
                    ausbildungen,
                    "Abschluss",
                    1,
                    (cell, o) -> {
                        o.add(
                            ParseDemoDataUtil.isBlank(cell)
                                ? Optional.empty()
                                : Optional.of(new DemoLebenslaufAusbildungDto().abschluss(cell.getStringCellValue()))
                        );
                    }
                );
            }
            int index = i;
            // spotless:off
            // Only update (ifPresent) the current Ausbildung entry if the list was initialized (when a value was defined)
            updateList(ausbildungen, "Beginn.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVon(ParseDemoDataUtil.parseMonthYear(c))));
            updateList(ausbildungen, "Ende.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBis(ParseDemoDataUtil.parseMonthYear(c))));
            updateList(ausbildungen, "Wohnsitz", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitzKanton(c))));
            updateList(ausbildungen, "Ausbildung abgeschlossen.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAusbildungAbgeschlossen(ParseDemoDataUtil.parseBoolean(c))));
            // spotless:on
        }
        // endregion

        // region Taetigkeiten
        final List<List<Optional<DemoLebenslaufTaetigkeitDto>>> taetigkeiten = new ArrayList<>(new ArrayList<>());
        skipRows(1);

        for (var i = 0; i < ANZAHL_LEBENSLAUF_ITEMS + 1; i++) {
            if (i == 0) {
                // Init a list for all Testfälle with max 4 Tätigkeiten
                initList(taetigkeiten, "Tätigkeitstyp", 1, cell -> {
                    if (ParseDemoDataUtil.isBlank(cell)) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(
                        List.of(
                            Optional.of(
                                new DemoLebenslaufTaetigkeitDto()
                                    .taetigkeitsart(ParseDemoEnumUtil.parseTaetigkeitsart(cell))
                            )
                        )
                    );
                });
            } else {
                // Append to the list of Tätigkeiten if there is a value
                updateList(
                    taetigkeiten,
                    "Tätigkeitstyp",
                    1,
                    (cell, o) -> {
                        o.add(
                            ParseDemoDataUtil.isBlank(cell)
                                ? Optional.empty()
                                : Optional.of(
                                    new DemoLebenslaufTaetigkeitDto().taetigkeitsart(
                                        ParseDemoEnumUtil.parseTaetigkeitsart(
                                            cell
                                        )
                                    )
                                )
                        );
                    }
                );
            }

            int index = i;
            // spotless:off
            // Only update (ifPresent) the current Ausbildung entry if the list was initialized (when a value was defined)
            updateList(taetigkeiten, "Tätigkeit.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setTaetigkeitsBeschreibung(c.getStringCellValue())));
            updateList(taetigkeiten, "Beginn.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVon(ParseDemoDataUtil.parseMonthYear(c))));
            updateList(taetigkeiten, "Ende.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBis(ParseDemoDataUtil.parseMonthYear(c))));
            updateList(taetigkeiten, "Wohnsitz", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitzKanton(c))));
            // spotless:on
        }
        // endregion

        final List<DemoLebenslaufDto> list = new ArrayList<>();

        for (var i = 0; i < ausbildungen.size(); i++) {
            list.add(
                new DemoLebenslaufDto()
                    .ausbildung(ausbildungen.get(i).stream().flatMap(Optional::stream).toList())
                    .taetigkeiten(taetigkeiten.get(i).stream().flatMap(Optional::stream).toList())
            );
        }

        return list;
    }

    private List<Optional<DemoPartnerDto>> preparePartnerin() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Partner/in", 0);
        final List<Optional<DemoPartnerDto>> list = new ArrayList<>();
        initList(
            list,
            "Sozialversicherungsnummer",
            1,
            c -> ParseDemoDataUtil.isBlank(c) ? Optional.empty()
                : Optional.of(new DemoPartnerDto().sozialversicherungsnummer(c.getStringCellValue()))
        );
        // spotless:off
        updateList(list, "Nachname", 1, (c, o) -> o.ifPresent(d -> d.setNachname(c.getStringCellValue())));
        updateList(list, "Vorname", 1, (c, o) -> o.ifPresent(d -> d.setVorname(c.getStringCellValue())));
        updateList(list, "Strasse", 1, (c, o) -> o.ifPresent(d -> d.setStrasse(c.getStringCellValue())));
        updateList(list, "Nr.", 1, (c, o) -> o.ifPresent(d -> d.setHausnummer(String.valueOf((int)c.getNumericCellValue()))));
        updateList(list, "PLZ", 1, (c, o) -> o.ifPresent(d -> d.setPlz(String.valueOf((int)c.getNumericCellValue()))));
        updateList(list, "Ort", 1, (c, o) -> o.ifPresent(d -> d.setOrt(c.getStringCellValue())));
        updateList(list, "Co-Adresse.*", 1, (c, o) -> o.ifPresent(d -> d.setCoAdresse(c.getStringCellValue())));
        updateList(list, "Land", 1, (c, o) -> o.ifPresent(d -> d.setLand(c.getStringCellValue())));
        updateList(list, "Geburtsdatum", 1, (c, o) -> o.ifPresent(d -> d.setGeburtsdatum(ParseDemoDataUtil.parseDateString(c))));
        updateList(list, "In Ausbildung", 1, (c, o) -> o.ifPresent(d -> d.setInAusbildung(ParseDemoDataUtil.parseBoolean(c))));
        updateList(list, "Pensum.*", 2, (c, o) -> o.ifPresent(d -> d.setPensum((int)c.getNumericCellValue())));
        // spotless:on

        return list;
    }
}
