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
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmeKostenRequiredDocumentsProducerUtil.EinnahmenKostenType;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.generated.dto.DemoAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoAuszahlungDto;
import ch.dvbern.stip.generated.dto.DemoEinnahmenKostenDto;
import ch.dvbern.stip.generated.dto.DemoElternteilDto;
import ch.dvbern.stip.generated.dto.DemoFamiliensituationDto;
import ch.dvbern.stip.generated.dto.DemoGeschwisterDto;
import ch.dvbern.stip.generated.dto.DemoKindDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufTaetigkeitDto;
import ch.dvbern.stip.generated.dto.DemoPartnerDto;
import ch.dvbern.stip.generated.dto.DemoPersonInAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoSteuerdatenDto;
import ch.dvbern.stip.generated.dto.DemoSteuererklaerungDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseDemoDataService {
    private static final int UNUSED_START_LINES = 2;
    private static final int FIRST_VALUE_COLOUMN = 5;
    private static final int ANZAHL_LEBENSLAUF_ITEMS_AUSBILDUNG = 4;
    private static final int ANZAHL_LEBENSLAUF_ITEMS_TAETIGKEITEN = 5;
    private static final int ANZAHL_KINDS = 5;
    private static final int ANZAHL_GESCHWISTERS = 6;
    private final Iterator<Row> rowIterator;
    private final int amountOfCells;
    private final FormulaEvaluator evaluator;

    private ParseDemoDataService(Iterator<Row> rowIterator, int amountOfCells, FormulaEvaluator evaluator) {
        this.rowIterator = rowIterator;
        this.amountOfCells = amountOfCells;
        this.evaluator = evaluator;
    }

    public static List<DemoData> parseList(final Path file) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.toFile())) {
            final var sheet = workbook.getSheetAt(0);
            final var rowIterator = sheet.iterator();
            final var amountOfCells =
                ParseDemoDataUtil.getNumberOfCells(sheet.getRow(UNUSED_START_LINES), FIRST_VALUE_COLOUMN);
            final var evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            return new ParseDemoDataService(rowIterator, amountOfCells, evaluator).parseAll();
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DemoData> parseAll() {
        skipRows(UNUSED_START_LINES);
        final var demoDataList = prepareInfo();
        final var ausbildungen = prepareAusbildung();
        final var pias = preparePersonInAusbildung();
        final var lebenslaufLists = prepareLebenslaufItems();
        final var partners = preparePartnerin();
        final var kindLists = prepareKinds();
        final var ekPia = prepareEinnahmenKostenGeneric(EinnahmenKostenType.PERSON_IN_AUSBILDUNG);
        final var ekPartner = prepareEinnahmenKostenGeneric(EinnahmenKostenType.PARTNER);
        final var familiensituation = prepareFamiliensituation();
        final var elterns = prepareElterns();
        final var steuererklaerungsSteuerdatens = prepareSteuererklaerungsAndSteuerdatens();
        final var geschwisters = prepareGeschwisters();
        final var auszahlungs = prepareAuszahlungs();

        for (int i = 0; i < demoDataList.size(); i++) {
            final var demoData = demoDataList.get(i);
            final var dto = demoData.getDemoDataDto();
            dto.setAusbildung(ausbildungen.get(i));
            dto.setPersonInAusbildung(pias.get(i));
            dto.setLebenslauf(lebenslaufLists.get(i));
            partners.get(i).ifPresent(dto::setPartner);
            dto.setKinder(kindLists.get(i));
            dto.setEinnahmenKosten(ekPia.get(i));
            dto.setEinnahmenKostenPartner(ekPartner.get(i));
            dto.setFamiliensituation(familiensituation.get(i));
            dto.setElterns(elterns.get(i));
            dto.setSteuererklaerung(steuererklaerungsSteuerdatens.steuererklaerungs.get(i));
            dto.setSteuerdaten(steuererklaerungsSteuerdatens.steuerdatens.get(i));
            dto.setGeschwister(geschwisters.get(i));
            auszahlungs.get(i).ifPresent(dto::setAuszahlung);

            demoData.serializeDemoData();
        }

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
        initList(list, "Typ", 0, c -> new DemoData().setTyp(ParseDemoEnumUtil.parseTyp(c)));
        updateList(list, "Testfall-ID", 0, (c, d) -> d.setTestFall(c.getStringCellValue()));
        ParseDemoDataUtil.skipEntries(rowIterator, 2);
        updateList(list, "Kurzbeschrieb .* Bemerkungen.*", 0, (c, d) -> {
            final var description = ParseDemoDataUtil.parseDescription(c);
            d.setName(description.getLeft());
            d.setDescription(description.getRight());
        });
        // spotless:off
        updateList(list, "Anzahl Monate", 0, (c, d) -> d.setAnzahlMonate(ParseDemoDataUtil.parseInteger(c)));
        updateList(list, "Erfasser des Testfalls", 0, (c, d) -> d.setErfasser(c.getStringCellValue()));
        updateList(list, "Gesuchsjahr", 0, (c, d) -> d.setGesuchsjahr(ParseDemoDataUtil.parseInteger(c)));
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
        updateList(list, "Beginn der Ausbildung", 1, (c, d) -> d.setAusbildungBeginn(ParseDemoDataUtil.parseMonthYear(c, false)));
        updateList(list, ".*Ende der Ausbildung", 1, (c, d) -> d.setAusbildungEnd(ParseDemoDataUtil.parseMonthYear(c, true)));
        updateList(list, "Pensum.*", 1, (c, d) -> d.setPensum(ParseDemoEnumUtil.parseAusbildungsPensum(c)));
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
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf(ParseDemoDataUtil.parseInteger(c))));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseInteger(c))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(c.getStringCellValue()));
        updateList(list, "Co-Adresse", 1, (c, d) -> d.setCoAdresse(c.getStringCellValue()));
        updateList(list, "Land", 1, (c, d) -> d.setLand(ParseDemoDataUtil.parseLandIsoCode(c)));
        updateList(list, "Identischer zivilrechtlicher.*", 1, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitz(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "PLZ", 2, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitzPLZ(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Ort", 2, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitzOrt(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Email", 1, (c, d) -> d.setEmail(c.getStringCellValue()));
        updateList(list, "Telefonnummer", 1, (c, d) -> d.setTelefonnummer(ParseDemoDataUtil.parseString(c)));
        updateList(list, "Geburtsdatum", 1, (c, d) -> d.setGeburtsdatum(ParseDemoDataUtil.parseDate(c)));
        updateList(list, "Zivilstand", 1, (c, d) -> d.setZivilstand(ParseDemoEnumUtil.parseZivilstand(c)));
        updateList(list, "Nationalität", 1, (c, d) -> d.setNationalitaet(ParseDemoDataUtil.parseLandIsoCode(c)));
        updateList(list, "Niederlassungsstatus.*", 2, (c, d) -> d.setNiederlassungsstatus(ParseDemoEnumUtil.parseNiederlassungsstatus(c)));
        updateList(list, "Einreisdatum Schweiz.*", 3, (c, d) -> d.setEinreisedatum(ParseDemoDataUtil.parseDate(c)));
        updateList(list, "Flüchtlingsstatus.*", 3, (c, d) -> {
            final var fluechtling = ParseDemoDataUtil.parseBooleanNullable(c);
            if (Boolean.TRUE.equals(fluechtling)) {
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
        updateList(list, "Wohnsitz bei", 1, (c, d) -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitz(c)));
        updateList(list, "bei Vater.*", 2, (c, d) -> d.setWohnsitzAnteilVater(ParseDemoDataUtil.parsePercentageNullable(c)));
        updateList(list, "bei Mutter.*", 2, (c, d) -> d.setWohnsitzAnteilMutter(ParseDemoDataUtil.parsePercentageNullable(c)));
        updateList(list, ".*Beistandschaft", 1, (c, d) -> d.setVormundschaft(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, "Zuständige KESB ", 2, (c, d) -> d.setZustaendigeKESB(ParseDemoEnumUtil.parseZustaendigeKESB(c)));
        updateList(list, ".*Sozialhilfebeiträge.*", 1, (c, d) -> d.setSozialhilfebeitraege(ParseDemoDataUtil.parseBoolean(c)));
        updateList(list, "Korrespondenzsprache", 1, (c, d) -> {});
        // spotless:on

        return list;
    }

    private List<DemoLebenslaufDto> prepareLebenslaufItems() {
        // region Ausbildungen
        final List<List<Optional<DemoLebenslaufAusbildungDto>>> ausbildungen = new ArrayList<>(new ArrayList<>());
        skipRows(2);

        for (var i = 0; i < ANZAHL_LEBENSLAUF_ITEMS_AUSBILDUNG; i++) {
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
            }
            // spotless:off
            else {
                // Append to the list of Ausbildungen if there is a value
                updateList(ausbildungen, "Abschluss", 1, (cell, o) -> o.add(ParseDemoDataUtil.isBlank(cell) ? Optional.empty() : Optional.of(new DemoLebenslaufAusbildungDto().abschluss(cell.getStringCellValue()))));
            }
            int index = i;
            // Only update (ifPresent) the current Ausbildung entry if the list was initialized (when a value was defined)
            updateList(ausbildungen, "Berufsbezeichnung.*Fachrichtung", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBerufsbezeichnungFachrichtung(ParseDemoDataUtil.parseStringNullable(c))));
            updateList(ausbildungen, "Beginn.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVon(ParseDemoDataUtil.parseMonthYear(c, false))));
            updateList(ausbildungen, "Ende.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBis(ParseDemoDataUtil.parseMonthYear(c, true))));
            updateList(ausbildungen, "Wohnsitz", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitzKanton(c))));
            updateList(ausbildungen, "Ausbildung abgeschlossen.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAusbildungAbgeschlossen(ParseDemoDataUtil.parseBoolean(c))));
            // spotless:on
        }
        // endregion

        // region Taetigkeiten
        final List<List<Optional<DemoLebenslaufTaetigkeitDto>>> taetigkeiten = new ArrayList<>(new ArrayList<>());
        skipRows(1);

        for (var i = 0; i < ANZAHL_LEBENSLAUF_ITEMS_TAETIGKEITEN; i++) {
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
            }
            // spotless:off
            else {
                // Append to the list of Tätigkeiten if there is a value
                updateList(taetigkeiten, "Tätigkeitstyp", 1, (cell, o) -> o.add(ParseDemoDataUtil.isBlank(cell) ? Optional.empty() : Optional.of(new DemoLebenslaufTaetigkeitDto().taetigkeitsart(ParseDemoEnumUtil.parseTaetigkeitsart(cell)))));
            }

            int index = i;
            // Only update (ifPresent) the current Ausbildung entry if the list was initialized (when a value was defined)
            updateList(taetigkeiten, "Tätigkeit.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setTaetigkeitsBeschreibung(c.getStringCellValue())));
            updateList(taetigkeiten, "Beginn.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVon(ParseDemoDataUtil.parseMonthYear(c, false))));
            updateList(taetigkeiten, "Ende.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBis(ParseDemoDataUtil.parseMonthYear(c, true))));
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
        final List<DemoPartnerDto> list = new ArrayList<>();
        initList(
            list,
            "Sozialversicherungsnummer",
            1,
            c -> new DemoPartnerDto().sozialversicherungsnummer(ParseDemoDataUtil.parseStringNullable(c))
        );
        // spotless:off
        updateList(list, "Nachname", 1, (c, d) -> d.setNachname(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Vorname", 1, (c, d) -> d.setVorname(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Strasse", 1, (c, d) -> d.setStrasse(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c))));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Co-Adresse.*", 1, (c, d) -> d.setCoAdresse(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Land", 1, (c, d) -> d.setLand(ParseDemoDataUtil.parseLandIsoCode(c)));
        updateList(list, "Geburtsdatum", 1, (c, d) -> d.setGeburtsdatum(ParseDemoDataUtil.parseDate(c)));
        updateList(list, "In Ausbildung", 1, (c, d) -> d.setInAusbildung(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, "Pensum.*", 2, (c, d) -> d.setPensum(ParseDemoEnumUtil.parseAusbildungsPensum(c)));
        // spotless:on

        return list.stream().map(d -> Optional.of(d).filter(ParseDemoDataUtil::hasValue)).toList();
    }

    private List<List<DemoKindDto>> prepareKinds() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Eigene Kinder.*", 0);
        final List<List<Optional<DemoKindDto>>> kinds = new ArrayList<>(new ArrayList<>());
        // spotless:off
        for (var i = 0; i < ANZAHL_KINDS; i++) {
            if (i == 0) {
                // Init a list for all Testfälle with max 5 Kinder
                initList(kinds, "Nachname", 1, cell -> {
                    if (ParseDemoDataUtil.isBlank(cell)) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(List.of(Optional.of(new DemoKindDto().nachname(cell.getStringCellValue()))));
                });
            }
            else {
                // Append to the list of Kinder if there is a value in the new dataset
                updateList(kinds, "Nachname", 1, (cell, o) -> o.add(ParseDemoDataUtil.isBlank(cell) ? Optional.empty() : Optional.of(new DemoKindDto().nachname(cell.getStringCellValue()))));
            }

            int index = i;
            // Only update (ifPresent) the current Ausbildung entry if the list was initialized (when a value was defined)
            updateList(kinds, "Vorname", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVorname(c.getStringCellValue())));
            updateList(kinds, "Geburtsdatum", 1, (c, o) -> o.get(index).ifPresent(d -> d.setGeburtsdatum(ParseDemoDataUtil.parseDate(c))));
            updateList(kinds, "Wieviel wohnt.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitzAnteilPia(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(kinds, "Ausbildungssituation", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAusbildungssituation(ParseDemoEnumUtil.parseAusbildungssituation(c))));
            updateList(kinds, ".*Unterhaltsbeiträge", 1, (c, o) -> o.get(index).ifPresent(d -> d.setUnterhaltsbeitraege(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(kinds, ".*Ausbildungszulagen", 1, (c, o) -> o.get(index).ifPresent(d -> d.setKinderUndAusbildungszulagen(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(kinds, "Renten", 1, (c, o) -> o.get(index).ifPresent(d -> d.setRenten(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(kinds, "Ergänzungsleistungen", 1, (c, o) -> o.get(index).ifPresent(d -> d.setErgaenzungsleistungen(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(kinds, "Andere Einnahmen", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAndereEinnahmen(ParseDemoDataUtil.parseIntegerNullable(c))));
            // spotless:on
        }

        return kinds.stream().map(l -> l.stream().flatMap(Optional::stream).toList()).toList();
    }

    private List<DemoEinnahmenKostenDto> prepareEinnahmenKostenGeneric(EinnahmenKostenType type) {
        final var expectedCellName = switch (type) {
            case EinnahmenKostenType.PERSON_IN_AUSBILDUNG -> "Einnahmen & Kosten \\(PiA\\)";
            case EinnahmenKostenType.PARTNER -> "Einnahmen & Kosten \\(Partner/in\\)";
        };
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), expectedCellName, 0);
        final List<DemoEinnahmenKostenDto> list = new ArrayList<>();
        initList(
            list,
            "Nettoerwerbseinkommen",
            1,
            c -> new DemoEinnahmenKostenDto().nettoerwerbseinkommen(ParseDemoDataUtil.parseIntegerNullable(c))
        );
        // spotless:off
        updateList(list, ".*Unterhaltsbeiträge", 1, (c, d) -> d.setUnterhaltsbeitraege(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Kinder.*Ausbildungszulagen", 1, (c, d) -> d.zulagen(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Renten", 1, (c, d) -> d.setRenten(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Leistungen EO", 1, (c, d) -> d.setEoLeistungen(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Ergänzungsleistungen", 1, (c, d) -> d.setErgaenzungsleistungen(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Beträge.*Gemeinden/Institutionen", 1, (c, d) -> d.setBeitraege(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Einnahmen BGSA", 1, (c, d) -> d.setEinnahmenBGSA(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Taggelder.*", 1, (c, d) -> d.setTaggelderAHVIV(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Andere Einnahmen", 1, (c, d) -> d.setAndereEinnahmen(ParseDemoDataUtil.parseIntegerNullable(c)));
        if (type == EinnahmenKostenType.PERSON_IN_AUSBILDUNG) {
            updateList(list, "Ausbildungskosten", 1, (c, d) -> d.setAusbildungskosten(ParseDemoDataUtil.parseIntegerNullable(c)));
        }
        updateList(list, "Betreuungskosten Kinder", 1, (c, d) -> d.setBetreuungskostenKinder(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Fahrkosten", 1, (c, d) -> d.setFahrkosten(ParseDemoDataUtil.parseIntegerNullable(c)));
        if (type == EinnahmenKostenType.PARTNER) {
            updateList(list, "Verpflegungskosten", 1, (c, d) -> d.setVerpflegungskosten(ParseDemoDataUtil.parseIntegerNullable(c)));
        }
        updateList(list, ".*Mittagessen pro Woche", 1, (c, d) -> d.setAuswaertigeMittagessenProWoche(ParseDemoDataUtil.parseIntegerNullable(c)));
        if (type == EinnahmenKostenType.PERSON_IN_AUSBILDUNG) {
            updateList(list, "monatliche Wohnkosten", 1, (c, d) -> d.setWohnkosten(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(list, "In WG wohnend", 1, (c, d) -> d.setWgWohnend(ParseDemoDataUtil.parseBooleanNullable(c)));
            updateList(list, ".*anderen Wohnform", 1, (c, d) -> d.setAlternativeWohnformWohnend(ParseDemoDataUtil.parseBooleanNullable(c)));
        }
        updateList(list, "Vermögen", 1, (c, d) -> d.setVermoegen(ParseDemoDataUtil.parseIntegerNullable(c)));
        updateList(list, "Steuern Kanton/Gemeinde.*", 1, (c, d) -> d.setSteuernKantonGemeinde(ParseDemoDataUtil.parseIntegerNullable(evaluator.evaluateInCell(c))));
        // spotless:on

        return list;
    }

    private List<DemoFamiliensituationDto> prepareFamiliensituation() {
        final List<DemoFamiliensituationDto> list = new ArrayList<>();
        // spotless:off
        initList(list, "Familiensituation", 0, c -> new DemoFamiliensituationDto());
        updateList(list, ".*Eltern verheiratet.*", 1, (c, d) -> d.setElternVerheiratetZusammen(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Alimentenregelung.*", 2, (c, d) -> d.setGerichtlicheAlimentenregelung(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Wer zahlt Alimente.*", 2, (c, d) -> d.setWerZahltAlimente(ParseDemoEnumUtil.parseElternschaftsteilung(c)));
        updateList(list, ".*Mutter wiederverheiratet.*", 3, (c, d) -> d.setMutterWiederverheiratetAlimente(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Vater wiederverheiratet.*", 3, (c, d) -> d.setVaterWiederverheiratetAlimente(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*unbekannt.*verstorben.*", 1, (c, d) -> d.setElternteilUnbekanntVerstorben(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Mutter verstorben", 2, (c, d) -> d.setMutterVerstorben(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Mutter unbekannt", 2, (c, d) -> d.setMutterUnbekannt(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Wieso.*Mutter unbekannt.*", 3, (c, d) -> d.setMutterUnbekanntGrund(ParseDemoEnumUtil.parseElternUnbekanntheitsGrund(c)));
        updateList(list, "Mutter: keine der Optionen", 2, (c, d) -> d.setMutterKeineOptionen(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Mutter wiederverheiratet.*", 3, (c, d) -> d.setMutterWiederverheiratetUnbekannt(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Vater verstorben", 2, (c, d) -> d.setVaterVerstorben(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Vater unbekannt", 2, (c, d) -> d.setVaterUnbekannt(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Wieso.*Vater unbekannt.*", 3, (c, d) -> d.setVaterUnbekanntGrund(ParseDemoEnumUtil.parseElternUnbekanntheitsGrund(c)));
        updateList(list, "Vater: keine der Optionen", 2, (c, d) -> d.setVaterKeineOptionen(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Vater wiederverheiratet.*", 3, (c, d) -> d.setVaterWiederverheiratetUnbekannt(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Mutter wiederverheiratet.*", 2, (c, d) -> d.setMutterWiederverheiratetUngewiss(ParseDemoDataUtil.parseBooleanNullable(c)));
        updateList(list, ".*Vater wiederverheiratet.*", 2, (c, d) -> d.setVaterWiederverheiratetUngewiss(ParseDemoDataUtil.parseBooleanNullable(c)));
        // spotless:on

        return list;
    }

    private List<List<DemoElternteilDto>> prepareElterns() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Eltern", 0);
        final List<List<DemoElternteilDto>> elterns = new ArrayList<>();
        var index = 0;
        for (var elterntyp : ElternTyp.values()) {
            // Init a list for all Testfälle for the 2 Elterns
            final var expected = switch (elterntyp) {
                case VATER -> "Vater";
                case MUTTER -> "Mutter";
            };
            final var i = index;

            ParseDemoDataUtil.checkCellContains(rowIterator.next(), expected, 0);

            if (index == 0) {
                initList(
                    elterns,
                    "Sozialversicherungsnummer",
                    1,
                    cell -> new ArrayList<>(
                        List.of(
                            new DemoElternteilDto()
                                .type(elterntyp)
                                .sozialversicherungsnummer(ParseDemoDataUtil.parseStringNullable(cell))
                        )
                    )
                );
            } else {
                updateList(
                    elterns,
                    "Sozialversicherungsnummer",
                    1,
                    (cell, l) -> l.add(
                        new DemoElternteilDto()
                            .type(elterntyp)
                            .sozialversicherungsnummer(ParseDemoDataUtil.parseStringNullable(cell))
                    )
                );
            }

            // spotless:off
            updateList(elterns, "Nachname", 1, (c, l) -> l.get(i).setNachname(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, "Vorname", 1, (c, l) -> l.get(i).setVorname(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, "Strasse", 1, (c, l) -> l.get(i).setStrasse(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, "Nr.", 1, (c, l) -> l.get(i).setHausnummer(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(elterns, "PLZ", 1, (c, l) -> l.get(i).setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c))));
            updateList(elterns, "Ort", 1, (c, l) -> l.get(i).setOrt(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, "Co-Adresse", 1, (c, l) -> l.get(i).setCoAdresse(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, "Land", 1, (c, l) -> l.get(i).setLand(ParseDemoDataUtil.parseLandIsoCode(c)));
            updateList(elterns, ".*zivilrechtlicher Wohnsitz", 1, (c, l) -> l.get(i).identischerZivilrechtlicherWohnsitz(ParseDemoDataUtil.parseBooleanNullable(c)));
            updateList(elterns, "PLZ", 2, (c, l) -> l.get(i).setIdentischerZivilrechtlicherWohnsitzPLZ(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, "Ort", 2, (c, l) -> l.get(i).setIdentischerZivilrechtlicherWohnsitzOrt(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, ".*Wohnkosten", 1, (c, l) -> l.get(i).setWohnkosten(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(elterns, "Geburtsdatum", 1, (c, l) -> l.get(i).geburtsdatum(ParseDemoDataUtil.parseDate(c)));
            updateList(elterns, "Telefonnummer", 1, (c, l) -> l.get(i).setTelefonnummer(ParseDemoDataUtil.parseStringNullable(c)));
            updateList(elterns, ".*Sozialhilfebeiträge.*", 1, (c, l) -> l.get(i).setSozialhilfebeitraege(ParseDemoDataUtil.parseBooleanNullable(c)));
            updateList(elterns, ".*Ausweis B.*", 1, (c, l) -> l.get(i).ausweisbFluechtling(ParseDemoDataUtil.parseBooleanNullable(c)));
            // spotless:on
            index++;
        }

        return elterns.stream().map(l -> l.stream().filter(ParseDemoDataUtil::hasValue).toList()).toList();
    }

    private DemoSteuererklaerungSteuerdatenLists prepareSteuererklaerungsAndSteuerdatens() {
        final List<List<DemoSteuererklaerungDto>> steuererklaerungs = new ArrayList<>();
        final List<List<DemoSteuerdatenDto>> steuerdatens = new ArrayList<>();
        var index = 0;
        for (var elterntyp : List.of(ElternTyp.MUTTER, ElternTyp.VATER)) {
            final var expected = switch (elterntyp) {
                case VATER -> "Vater/Familie";
                case MUTTER -> "Mutter";
            };
            final var i = index;

            ParseDemoDataUtil.checkCellContains(rowIterator.next(), ".*Angaben " + expected, 0);

            if (i == 0) {
                initList(
                    steuererklaerungs,
                    ".*Kanton Bern.*",
                    1,
                    cell -> new ArrayList<>(
                        List.of(
                            new DemoSteuererklaerungDto()
                                .type(elterntyp)
                                .steuererklaerungInBern(ParseDemoDataUtil.parseBooleanNullable(cell))
                        )
                    )
                );
            } else {
                updateList(
                    steuererklaerungs,
                    ".*Kanton Bern.*",
                    1,
                    (cell, l) -> l.add(
                        new DemoSteuererklaerungDto()
                            .type(elterntyp)
                            .steuererklaerungInBern(ParseDemoDataUtil.parseBooleanNullable(cell))
                    )
                );
            }

            // spotless:off
            updateList(steuererklaerungs, ".*Unterhaltsbeiträge", 1, (c,  l) -> l.get(i).setUnterhaltsbeitraege(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuererklaerungs, "Renten", 1, (c,  l) -> l.get(i).setRenten(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuererklaerungs, "Ergänzungsleistungen", 1, (c,  l) -> l.get(i).setErgaenzungsleistungen(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuererklaerungs, "Einnahmen BGSA", 1, (c,  l) -> l.get(i).setEinnahmenBGSA(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuererklaerungs, "Andere Einnahmen", 1, (c,  l) -> l.get(i).setAndereEinnahmen(ParseDemoDataUtil.parseIntegerNullable(c)));
            // spotless:on

            ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Steuerdaten " + expected, 0);

            if (i == 0) {
                initList(
                    steuerdatens,
                    ".*Einkünfte",
                    1,
                    cell -> new ArrayList<>(
                        List.of(
                            new DemoSteuerdatenDto()
                                .type(elterntyp)
                                .totalEinkuenfte(ParseDemoDataUtil.parseIntegerNullable(cell))
                        )
                    )
                );
            } else {
                updateList(
                    steuerdatens,
                    ".*Einkünfte",
                    1,
                    (cell, l) -> l.add(
                        new DemoSteuerdatenDto()
                            .type(elterntyp)
                            .totalEinkuenfte(ParseDemoDataUtil.parseIntegerNullable(cell))
                    )
                );
            }

            // spotless:off
            updateList(steuerdatens, "Eigenmietwert", 1, (c,  l) -> l.get(i).setEigenmietwert(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Arbeitsverhältnis", 1, (c,  l) -> l.get(i).setIsArbeitsverhaeltnisSelbstaendig(ParseDemoEnumUtil.parseArbeitsverhaeltnisSelbstaendig(c)));
            updateList(steuerdatens, "Säule 3a", 1, (c,  l) -> l.get(i).setSaeule3a(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "2. Säule", 1, (c,  l) -> l.get(i).setSaeule2(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Vermögen", 1, (c,  l) -> l.get(i).setVermoegen(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Kantons.*Gemeindesteuern", 1, (c,  l) -> l.get(i).setSteuernKantonGemeinde(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Steuern Bund", 1, (c,  l) -> l.get(i).setSteuernBund(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Fahrkosten", 1, (c,  l) -> l.get(i).setFahrkosten(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Fahrkosten Partner/in", 1, (c,  l) -> l.get(i).setFahrkostenPartner(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Verpflegung ", 1, (c,  l) -> l.get(i).setVerpflegung(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Verpflegung Partner/in", 1, (c,  l) -> l.get(i).setVerpflegungPartner(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Steuerjahr", 1, (c,  l) -> l.get(i).setSteuerjahr(ParseDemoDataUtil.parseIntegerNullable(c)));
            updateList(steuerdatens, "Status der Veranlagung", 1, (c,  l) -> l.get(i).setVeranlagungsStatus(ParseDemoDataUtil.parseStringNullable(c)));
            // spotless:on
            index++;
        }

        return new DemoSteuererklaerungSteuerdatenLists(
            steuererklaerungs.stream().map(l -> l.stream().filter(ParseDemoDataUtil::hasValue).toList()).toList(),
            steuerdatens.stream().map(l -> l.stream().filter(ParseDemoDataUtil::hasValue).toList()).toList()
        );
    }

    private List<List<DemoGeschwisterDto>> prepareGeschwisters() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Geschwister", 0);
        final List<List<Optional<DemoGeschwisterDto>>> geschwisters = new ArrayList<>(new ArrayList<>());
        // spotless:off
        for (var i = 0; i < ANZAHL_GESCHWISTERS; i++) {
            if (i == 0) {
                // Init a list for all Testfälle with max 5 Kinder
                initList(geschwisters, "Nachname", 1, cell -> {
                    if (ParseDemoDataUtil.isBlank(cell)) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(List.of(Optional.of(new DemoGeschwisterDto().nachname(cell.getStringCellValue()))));
                });
            }
            else {
                // Append to the list of Kinder if there is a value in the new dataset
                updateList(geschwisters, "Nachname", 1, (cell, o) -> o.add(ParseDemoDataUtil.isBlank(cell) ? Optional.empty() : Optional.of(new DemoGeschwisterDto().nachname(cell.getStringCellValue()))));
            }

            int index = i;
            // Only update (ifPresent) the current Ausbildung entry if the list was initialized (when a value was defined)
            updateList(geschwisters, "Vorname", 1, (c, o) -> o.get(index).ifPresent(d -> d.vorname(c.getStringCellValue())));
            updateList(geschwisters, "Geburtsdatum", 1, (c, o) -> o.get(index).ifPresent(d -> d.geburtsdatum(ParseDemoDataUtil.parseDate(c))));
            updateList(geschwisters, "Wohnsitz bei", 1, (c, o) -> o.get(index).ifPresent(d -> d.wohnsitzBei(ParseDemoEnumUtil.parseWohnsitz(c))));
            updateList(geschwisters, "bei Vater.*", 2, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitzAnteilVater(ParseDemoDataUtil.parsePercentageNullable(c))));
            updateList(geschwisters, "bei Mutter.*", 2, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitzAnteilMutter(ParseDemoDataUtil.parsePercentageNullable(c))));
            updateList(geschwisters, "Ausbildungssituation", 1, (c, o) -> o.get(index).ifPresent(d -> d.ausbildungssituation(ParseDemoEnumUtil.parseAusbildungssituation(c))));
            // spotless:on
        }

        return geschwisters.stream().map(l -> l.stream().flatMap(Optional::stream).toList()).toList();
    }

    private List<Optional<DemoAuszahlungDto>> prepareAuszahlungs() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Auszahlung", 0);
        final List<DemoAuszahlungDto> list = new ArrayList<>();
        initList(
            list,
            "Nachname",
            1,
            c -> new DemoAuszahlungDto().nachname(c.getStringCellValue())
        );
        // spotless:off
        updateList(list, "Vorname", 1, (c, d) -> d.setVorname(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Strasse", 1, (c, d) -> d.setStrasse(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c))));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Co-Adresse.*", 1, (c, d) -> d.setCoAdresse(ParseDemoDataUtil.parseStringNullable(c)));
        updateList(list, "Land", 1, (c, d) -> d.setLand(ParseDemoDataUtil.parseLandIsoCode(c)));
        updateList(list, "IBAN", 1, (c, d) -> d.setIban(ParseDemoDataUtil.parseStringNullable(c)));
        // spotless:on

        return list.stream().map(d -> Optional.of(d).filter(ParseDemoDataUtil::hasValue)).toList();
    }

    @RequiredArgsConstructor
    class DemoSteuererklaerungSteuerdatenLists {
        private final List<List<DemoSteuererklaerungDto>> steuererklaerungs;
        private final List<List<DemoSteuerdatenDto>> steuerdatens;
    }
}
