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
import ch.dvbern.stip.api.demo.type.DemoDataParseContext;
import ch.dvbern.stip.api.demo.util.ParseDemoDataUtil;
import ch.dvbern.stip.api.demo.util.ParseDemoEnumUtil;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKostenRequiredDokumentsProducerUtil.EinnahmenKostenType;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.dto.DemoAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoAuszahlungDto;
import ch.dvbern.stip.generated.dto.DemoDarlehenDto;
import ch.dvbern.stip.generated.dto.DemoDarlehenGruendeDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValuesDto;
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
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

@Slf4j
public class ParseDemoDataService {

    @RequiredArgsConstructor
    class DemoSteuererklaerungSteuerdatenLists {
        private final List<List<DemoSteuererklaerungDto>> steuererklaerungs;
        private final List<List<DemoSteuerdatenDto>> steuerdatens;
    }

    private static final int UNUSED_START_LINES = 2;
    private static final int FIRST_VALUE_COLOUMN = 5;
    private static final int ANZAHL_LEBENSLAUF_ITEMS_AUSBILDUNG = 4;
    private static final int ANZAHL_LEBENSLAUF_ITEMS_TAETIGKEITEN = 5;
    private static final int ANZAHL_KINDS = 5;
    private static final int ANZAHL_GESCHWISTERS = 6;
    private final Iterator<Row> rowIterator;
    private final int amountOfCells;

    public ParseDemoDataService(Iterator<Row> rowIterator, int amountOfCells) {
        this.rowIterator = rowIterator;
        this.amountOfCells = amountOfCells;
    }

    public static List<DemoData> parseList(final Path file, final Boolean ignoreBerechnungErrors) {
        try (var workbook = new ReadableWorkbook(file.toFile())) {
            final var sheet = workbook.getSheet(0).get();
            final var rowIterator = sheet.openStream().iterator();
            final var amountOfCells =
                sheet.openStream().skip(UNUSED_START_LINES).findFirst().get().getPhysicalCellCount();
            return new ParseDemoDataService(rowIterator, amountOfCells)
                .parseAll(ignoreBerechnungErrors);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DemoData> parseAll(final Boolean ignoreBerechnungErrors) {
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
        final var steuererklaerungsSteuerdatens = prepareSteuererklaerungsAndSteuerdatens(familiensituation);
        final var geschwisters = prepareGeschwisters();
        final var auszahlungs = prepareAuszahlungs();
        final var darlehens = prepareDarlehens();
        final var berechnungValues = prepareBerechnungValues(ignoreBerechnungErrors);

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
            darlehens.get(i).ifPresent(dto::setDarlehen);
            dto.setBerechnungValues(berechnungValues.get(i));

            demoData.serializeDemoData();
        }

        return demoDataList;
    }

    private <T> void initList(List<T> list, String pattern, int column, Function<DemoDataParseContext, T> createValue) {
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

    private <T> void updateList(
        List<T> list,
        String pattern,
        int column,
        BiConsumer<DemoDataParseContext, T> updateValue
    ) {
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
        initList(list, "Typ", 0, (c) -> new DemoData().setTyp(ParseDemoEnumUtil.parseTyp(c.getCell())));
        updateList(list, "Testfall-ID", 0, (c, d) -> d.setTestFall(ParseDemoDataUtil.parseString(c.getCell())));
        ParseDemoDataUtil.skipEntries(rowIterator, 2);
        updateList(list, "Kurzbeschrieb .* Bemerkungen.*", 0, (c, d) -> {
            final var description = ParseDemoDataUtil.parseDescription(c.getCell());
            d.setName(description.getLeft());
            d.setDescription(description.getRight());
        });
        // spotless:off
        updateList(list, "Anzahl Monate", 0, (c, d) -> d.setAnzahlMonate(ParseDemoDataUtil.parseInteger(c.getCell())));
        updateList(list, "Erfasser des Testfalls", 0, (c, d) -> d.setErfasser(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Gesuchsjahr", 0, (c, d) -> d.setGesuchsjahr(ParseDemoDataUtil.parseInteger(c.getCell())));
        updateList(list, "Gesuchseingang", 0, (c, d) -> d.setGesuchseingang(ParseDemoDataUtil.parseDateString(c.getCell())));
        // spotless:on

        return list;
    }

    private List<DemoAusbildungDto> prepareAusbildung() {
        ParseDemoDataUtil.skipEntries(rowIterator, 3);

        final List<DemoAusbildungDto> list = new ArrayList<>();
        // spotless:off
        initList(list, "Ausbildung für Stipendium.*", 0, (c) -> new DemoAusbildungDto());
        updateList(list, "Ausbildungsstätte", 1, (c, d) -> d.setAusbildungsstaette(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Ausbildungsgang", 1, (c, d) -> d.setAusbildungsgang(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Berufsbezeichnung .* Fachrichtung", 2, (c, d) -> d.setBerufsbezeichnungFachrichtung(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, ".*Ausbildung im Ausland.*", 1, (c, d) -> d.setIsAusbildungAusland(ParseDemoDataUtil.parseBoolean(c.getCell())));
        updateList(list, "Andere Ausbildungsstätte.*", 1, (c, d) -> d.setAusbildungNichtGefunden(ParseDemoDataUtil.parseBoolean(c.getCell())));
        updateList(list, "Beginn der Ausbildung", 1, (c, d) -> d.setAusbildungBeginn(ParseDemoDataUtil.parseMonthYear(c.getCell(), false)));
        updateList(list, ".*Ende der Ausbildung", 1, (c, d) -> d.setAusbildungEnd(ParseDemoDataUtil.parseMonthYear(c.getCell(), true)));
        updateList(list, "Pensum.*", 1, (c, d) -> d.setPensum(ParseDemoEnumUtil.parseAusbildungsPensum(c.getCell())));
        // spotless:on

        return list;
    }

    private List<DemoPersonInAusbildungDto> preparePersonInAusbildung() {
        final List<DemoPersonInAusbildungDto> list = new ArrayList<>();
        // spotless:off
        initList(list, "Person in Ausbildung", 0, (c) -> new DemoPersonInAusbildungDto());
        updateList(list, "Sozialversicherungsnummer", 1, (c, d) -> d.setSozialversicherungsnummer(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Anrede", 1, (c, d) -> d.setAnrede(ParseDemoEnumUtil.parseAnrede(c.getCell())));
        updateList(list, "Nachname", 1, (c, d) -> d.setNachname(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Vorname", 1, (c, d) -> d.setVorname(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Strasse", 1, (c, d) -> d.setStrasse(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf(ParseDemoDataUtil.parseInteger(c.getCell()))));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseInteger(c.getCell()))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Co-Adresse", 1, (c, d) -> d.setCoAdresse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Land", 1, (c, d) -> d.setLand(ParseDemoDataUtil.parseLandIsoCode(c.getCell())));
        updateList(list, "Identischer zivilrechtlicher.*", 1, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitz(ParseDemoDataUtil.parseBoolean(c.getCell())));
        updateList(list, "PLZ", 2, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitzPLZ(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Ort", 2, (c, d) -> d.setIdentischerZivilrechtlicherWohnsitzOrt(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Email", 1, (c, d) -> d.setEmail(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Telefonnummer", 1, (c, d) -> d.setTelefonnummer(ParseDemoDataUtil.parseString(c.getCell())));
        updateList(list, "Geburtsdatum", 1, (c, d) -> d.setGeburtsdatum(ParseDemoDataUtil.parseGeburtsdatum(c.getCell())));
        updateList(list, "Alter.*", 1, (c, d) -> d.setAlter(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Zivilstand", 1, (c, d) -> d.setZivilstand(ParseDemoEnumUtil.parseZivilstand(c.getCell())));
        updateList(list, "Nationalität", 1, (c, d) -> d.setNationalitaet(ParseDemoDataUtil.parseLandIsoCode(c.getCell())));
        updateList(list, "Niederlassungsstatus.*", 2, (c, d) -> d.setNiederlassungsstatus(ParseDemoEnumUtil.parseNiederlassungsstatus(c.getCell())));
        updateList(list, "Einreisdatum Schweiz.*", 3, (c, d) -> d.setEinreisedatum(ParseDemoDataUtil.parseDate(c.getCell())));
        updateList(list, "Flüchtlingsstatus.*", 3, (c, d) -> {
            final var fluechtling = ParseDemoDataUtil.parseBooleanNullable(c.getCell());
            if (Boolean.TRUE.equals(fluechtling)) {
                d.setNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS);
            }
        });
        updateList(list, "Zuständiger Kanton.*", 4, (c, d) -> {
            final var zustaendigerKanton = ParseDemoDataUtil.parseStringNullable(c.getCell());
            if (Objects.equals(zustaendigerKanton, "Kanton Bern")) {
                d.setNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_TENANT);
            }
        });
        updateList(list, "PLZ", 3, (c, d) -> d.setHeimatortPLZ(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
        updateList(list, "Heimatort", 3, (c, d) -> d.setHeimatort(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Wohnsitz bei", 1, (c, d) -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitz(c.getCell())));
        updateList(list, "bei Vater.*", 2, (c, d) -> d.setWohnsitzAnteilVater(ParseDemoDataUtil.parsePercentageNullable(c.getCell())));
        updateList(list, "bei Mutter.*", 2, (c, d) -> d.setWohnsitzAnteilMutter(ParseDemoDataUtil.parsePercentageNullable(c.getCell())));
        updateList(list, ".*Beistandschaft", 1, (c, d) -> d.setVormundschaft(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, "Zuständige KESB ", 2, (c, d) -> d.setZustaendigeKESB(ParseDemoEnumUtil.parseZustaendigeKESB(c.getCell())));
        updateList(list, ".*Sozialhilfebeiträge.*", 1, (c, d) -> d.setSozialhilfebeitraege(ParseDemoDataUtil.parseBoolean(c.getCell())));
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
                initList(ausbildungen, "Abschluss", 1, (c) -> {
                    if (ParseDemoDataUtil.isBlank(c.getCell())) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(
                        List.of(
                            Optional
                                .of(
                                    new DemoLebenslaufAusbildungDto()
                                        .abschluss(ParseDemoDataUtil.parseString(c.getCell()))
                                )
                        )
                    );
                });
            }
            // spotless:off
            else {
                // Append to the list of Ausbildungen if there is a value
                updateList(ausbildungen, "Abschluss", 1, (c, o) -> o.add(ParseDemoDataUtil.isBlank(c.getCell()) ? Optional.empty() : Optional.of(new DemoLebenslaufAusbildungDto().abschluss(ParseDemoDataUtil.parseString(c.getCell())))));
            }
            int index = i;
            // Only update (ifPresent) the current entry if the list was initialized (when a value was defined)
            updateList(ausbildungen, "Berufsbezeichnung.*Fachrichtung", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBerufsbezeichnungFachrichtung(ParseDemoDataUtil.parseStringNullable(c.getCell()))));
            updateList(ausbildungen, "Beginn.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVon(ParseDemoDataUtil.parseMonthYear(c.getCell(), false))));
            updateList(ausbildungen, "Ende.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBis(ParseDemoDataUtil.parseMonthYear(c.getCell(), true))));
            updateList(ausbildungen, "Wohnsitz", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitzKanton(c.getCell()))));
            updateList(ausbildungen, "Ausbildung abgeschlossen.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAusbildungAbgeschlossen(ParseDemoDataUtil.parseBoolean(c.getCell()))));
            // spotless:on
        }
        // endregion

        // region Taetigkeiten
        final List<List<Optional<DemoLebenslaufTaetigkeitDto>>> taetigkeiten = new ArrayList<>(new ArrayList<>());
        skipRows(1);

        for (var i = 0; i < ANZAHL_LEBENSLAUF_ITEMS_TAETIGKEITEN; i++) {
            if (i == 0) {
                // Init a list for all Testfälle with max 4 Tätigkeiten
                initList(taetigkeiten, "Tätigkeitstyp", 1, (c) -> {
                    if (ParseDemoDataUtil.isBlank(c.getCell())) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(
                        List.of(
                            Optional.of(
                                new DemoLebenslaufTaetigkeitDto()
                                    .taetigkeitsart(ParseDemoEnumUtil.parseTaetigkeitsart(c.getCell()))
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
                    (c, o) -> o.add(
                        ParseDemoDataUtil.isBlank(c.getCell()) ? Optional.empty()
                            : Optional.of(
                                new DemoLebenslaufTaetigkeitDto()
                                    .taetigkeitsart(ParseDemoEnumUtil.parseTaetigkeitsart(c.getCell()))
                            )
                    )
                );
            }

            int index = i;
            // spotless:off
            // Only update (ifPresent) the current entry if the list was initialized (when a value was defined)
            updateList(taetigkeiten, "Tätigkeit.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setTaetigkeitsBeschreibung(ParseDemoDataUtil.parseStringNullable(c.getCell()))));
            updateList(taetigkeiten, "Beginn.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVon(ParseDemoDataUtil.parseMonthYear(c.getCell(), false))));
            updateList(taetigkeiten, "Ende.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBis(ParseDemoDataUtil.parseMonthYear(c.getCell(), true))));
            updateList(taetigkeiten, "Wohnsitz", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitz(ParseDemoEnumUtil.parseWohnsitzKanton(c.getCell()))));
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
            (c) -> new DemoPartnerDto().sozialversicherungsnummer(ParseDemoDataUtil.parseStringNullable(c.getCell()))
        );
        // spotless:off
        updateList(list, "Nachname", 1, (c, d) -> d.setNachname(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Vorname", 1, (c, d) -> d.setVorname(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Strasse", 1, (c, d) -> d.setStrasse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Co-Adresse.*", 1, (c, d) -> d.setCoAdresse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Land", 1, (c, d) -> d.setLand(ParseDemoDataUtil.parseLandIsoCode(c.getCell())));
        updateList(list, "Geburtsdatum", 1, (c, d) -> d.setGeburtsdatum(ParseDemoDataUtil.parseGeburtsdatum(c.getCell())));
        updateList(list, "Alter.*", 1, (c, d) -> d.setAlter(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "In Ausbildung", 1, (c, d) -> d.setInAusbildung(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, "Pensum.*", 2, (c, d) -> d.setPensum(ParseDemoEnumUtil.parseAusbildungsPensum(c.getCell())));
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
                initList(kinds, "Nachname", 1, (c) -> {
                    if (ParseDemoDataUtil.isBlank(c.getCell())) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(List.of(Optional.of(new DemoKindDto().nachname(ParseDemoDataUtil.parseString(c.getCell())))));
                });
            }
            else {
                // Append to the list of Kinder if there is a value in the new dataset
                updateList(kinds, "Nachname", 1, (c, o) -> o.add(ParseDemoDataUtil.isBlank(c.getCell()) ? Optional.empty() : Optional.of(new DemoKindDto().nachname(ParseDemoDataUtil.parseString(c.getCell())))));
            }

            int index = i;
            // Only update (ifPresent) the current entry if the list was initialized (when a value was defined)
            updateList(kinds, "Vorname", 1, (c, o) -> o.get(index).ifPresent(d -> d.setVorname(ParseDemoDataUtil.parseString(c.getCell()))));
            updateList(kinds, "Geburtsdatum", 1, (c, o) -> o.get(index).ifPresent(d -> d.setGeburtsdatum(ParseDemoDataUtil.parseGeburtsdatum(c.getCell()))));
            updateList(kinds, "Alter.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAlter(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, "Wieviel wohnt.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitzAnteilPia(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, "Ausbildungssituation", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAusbildungssituation(ParseDemoEnumUtil.parseAusbildungssituation(c.getCell()))));
            updateList(kinds, ".*Unterhaltsbeiträge", 1, (c, o) -> o.get(index).ifPresent(d -> d.setUnterhaltsbeitraege(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, ".*Ausbildungszulagen", 1, (c, o) -> o.get(index).ifPresent(d -> d.setKinderUndAusbildungszulagen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, "Renten", 1, (c, o) -> o.get(index).ifPresent(d -> d.setRenten(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, "Betreuungskosten", 1, (c, o) -> o.get(index).ifPresent(d -> d.setBetreuungskosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, "Ergänzungsleistungen", 1, (c, o) -> o.get(index).ifPresent(d -> d.setErgaenzungsleistungen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(kinds, "Andere Einnahmen", 1, (c, o) -> o.get(index).ifPresent(d -> d.setAndereEinnahmen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
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
            (c) -> new DemoEinnahmenKostenDto()
                .nettoerwerbseinkommen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))
        );
        // spotless:off
        updateList(list, ".*Unterhaltsbeiträge", 1, (c, d) -> d.setUnterhaltsbeitraege(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Kinder.*Ausbildungszulagen", 1, (c, d) -> d.zulagen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Renten", 1, (c, d) -> d.setRenten(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Leistungen EO", 1, (c, d) -> d.setEoLeistungen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Ergänzungsleistungen", 1, (c, d) -> d.setErgaenzungsleistungen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Beträge.*Gemeinden/Institutionen", 1, (c, d) -> d.setBeitraege(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Einnahmen BGSA", 1, (c, d) -> d.setEinnahmenBGSA(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Taggelder.*", 1, (c, d) -> d.setTaggelderAHVIV(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Andere Einnahmen", 1, (c, d) -> d.setAndereEinnahmen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        if (type == EinnahmenKostenType.PERSON_IN_AUSBILDUNG) {
            updateList(list, "Ausbildungskosten", 1, (c, d) -> d.setAusbildungskosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        }
        updateList(list, "Betreuungskosten Kinder", 1, (c, d) -> {});
        updateList(list, "Fahrkosten", 1, (c, d) -> d.setFahrkosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        if (type == EinnahmenKostenType.PARTNER) {
            updateList(list, "Verpflegungskosten", 1, (c, d) -> d.setVerpflegungskosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        }
        updateList(list, ".*Mittagessen pro Woche", 1, (c, d) -> d.setAuswaertigeMittagessenProWoche(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        if (type == EinnahmenKostenType.PERSON_IN_AUSBILDUNG) {
            updateList(list, "jährliche Wohnkosten", 1, (c, d) -> d.setWohnkosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            updateList(list, "In WG wohnend", 1, (c, d) -> d.setWgWohnend(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
            updateList(list, ".*anderen Wohnform", 1, (c, d) -> d.setAlternativeWohnformWohnend(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        }
        updateList(list, "Vermögen", 1, (c, d) -> d.setVermoegen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        updateList(list, "Steuern Kanton/Gemeinde.*", 1, (c, d) -> d.setSteuernKantonGemeinde(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
        // spotless:on

        return list;
    }

    private List<DemoFamiliensituationDto> prepareFamiliensituation() {
        final List<DemoFamiliensituationDto> list = new ArrayList<>();
        // spotless:off
        initList(list, "Familiensituation", 0, (c) -> new DemoFamiliensituationDto());
        updateList(list, ".*Eltern verheiratet.*", 1, (c, d) -> d.setElternVerheiratetZusammen(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Alimentenregelung.*", 2, (c, d) -> d.setGerichtlicheAlimentenregelung(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Wer zahlt Alimente.*", 2, (c, d) -> d.setWerZahltAlimente(ParseDemoEnumUtil.parseElternschaftsteilung(c.getCell())));
        updateList(list, ".*unbekannt.*verstorben.*", 1, (c, d) -> d.setElternteilUnbekanntVerstorben(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Mutter verstorben", 2, (c, d) -> d.setMutterVerstorben(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Mutter unbekannt", 2, (c, d) -> d.setMutterUnbekannt(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Wieso.*Mutter unbekannt.*", 3, (c, d) -> d.setMutterUnbekanntGrund(ParseDemoEnumUtil.parseElternUnbekanntheitsGrund(c.getCell())));
        updateList(list, "Mutter: keine der Optionen", 2, (c, d) -> d.setMutterKeineOptionen(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Vater verstorben", 2, (c, d) -> d.setVaterVerstorben(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Vater unbekannt", 2, (c, d) -> d.setVaterUnbekannt(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
        updateList(list, ".*Wieso.*Vater unbekannt.*", 3, (c, d) -> d.setVaterUnbekanntGrund(ParseDemoEnumUtil.parseElternUnbekanntheitsGrund(c.getCell())));
        updateList(list, "Vater: keine der Optionen", 2, (c, d) -> d.setVaterKeineOptionen(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
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
                    (c) -> new ArrayList<>(
                        List.of(
                            new DemoElternteilDto()
                                .type(elterntyp)
                                .sozialversicherungsnummer(ParseDemoDataUtil.parseStringNullable(c.getCell()))
                        )
                    )
                );
            } else {
                updateList(
                    elterns,
                    "Sozialversicherungsnummer",
                    1,
                    (c, l) -> l.add(
                        new DemoElternteilDto()
                            .type(elterntyp)
                            .sozialversicherungsnummer(ParseDemoDataUtil.parseStringNullable(c.getCell()))
                    )
                );
            }

            // spotless:off
            updateList(elterns, "Nachname", 1, (c, l) -> l.get(i).setNachname(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, "Vorname", 1, (c, l) -> l.get(i).setVorname(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, "Strasse", 1, (c, l) -> l.get(i).setStrasse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, "Nr.", 1, (c, l) -> l.get(i).setHausnummer(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(elterns, "PLZ", 1, (c, l) -> l.get(i).setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(elterns, "Ort", 1, (c, l) -> l.get(i).setOrt(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, "Co-Adresse", 1, (c, l) -> l.get(i).setCoAdresse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, "Land", 1, (c, l) -> l.get(i).setLand(ParseDemoDataUtil.parseLandIsoCode(c.getCell())));
            updateList(elterns, ".*zivilrechtlicher Wohnsitz", 1, (c, l) -> l.get(i).identischerZivilrechtlicherWohnsitz(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
            updateList(elterns, "PLZ", 2, (c, l) -> l.get(i).setIdentischerZivilrechtlicherWohnsitzPLZ(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, "Ort", 2, (c, l) -> l.get(i).setIdentischerZivilrechtlicherWohnsitzOrt(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, ".*Wohnkosten", 1, (c, l) -> l.get(i).setWohnkosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            updateList(elterns, "Geburtsdatum", 1, (c, l) -> l.get(i).geburtsdatum(ParseDemoDataUtil.parseGeburtsdatum(c.getCell())));
            updateList(elterns, "Alter.*", 1, (c, l) -> l.get(i).alter(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            updateList(elterns, "Telefonnummer", 1, (c, l) -> l.get(i).setTelefonnummer(ParseDemoDataUtil.parseStringNullable(c.getCell())));
            updateList(elterns, ".*Sozialhilfebeiträge.*", 1, (c, l) -> l.get(i).setSozialhilfebeitraege(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
            updateList(elterns, ".*Ausweis B.*", 1, (c, l) -> l.get(i).ausweisbFluechtling(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
            updateList(elterns, ".*wiederverheiratet.*", 1, (c, l) -> l.get(i).wiederverheiratet(ParseDemoDataUtil.parseBooleanNullable(c.getCell())));
            // spotless:on
            index++;
        }

        return elterns.stream().map(l -> l.stream().filter(ParseDemoDataUtil::hasValue).toList()).toList();
    }

    private DemoSteuererklaerungSteuerdatenLists prepareSteuererklaerungsAndSteuerdatens(
        List<DemoFamiliensituationDto> demoFamiliensituationDtos
    ) {
        final List<List<Optional<DemoSteuererklaerungDto>>> steuererklaerungs = new ArrayList<>();
        final List<List<Optional<DemoSteuerdatenDto>>> steuerdatens = new ArrayList<>();
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
                    (c) -> {
                        final var steuererklaerungInBern = ParseDemoDataUtil.parseBooleanNullable(c.getCell());
                        if (Objects.isNull(steuererklaerungInBern)) {
                            return new ArrayList<>(List.of(Optional.empty()));
                        }
                        if (demoFamiliensituationDtos.get(c.getIndex()).getElternVerheiratetZusammen()) {
                            throw new IllegalStateException(
                                "Cannot have Values for Mutter if Familiensituation -> ElternVerheiratetZusammen"
                            );
                        }
                        return new ArrayList<>(
                            List.of(
                                Optional.of(
                                    new DemoSteuererklaerungDto()
                                        .type(elterntyp.getSteuerdatenTyp())
                                        .steuererklaerungInBern(steuererklaerungInBern)
                                )
                            )
                        );
                    }
                );
            } else {
                updateList(
                    steuererklaerungs,
                    ".*Kanton Bern.*",
                    1,
                    (c, l) -> {
                        final var steuererklaerungInBern = ParseDemoDataUtil.parseBooleanNullable(c.getCell());
                        if (Objects.isNull(steuererklaerungInBern)) {
                            l.add(Optional.empty());
                        } else {
                            l.add(
                                Optional.of(
                                    new DemoSteuererklaerungDto()
                                        .type(
                                            demoFamiliensituationDtos.get(c.getIndex()).getElternVerheiratetZusammen()
                                                ? SteuerdatenTyp.FAMILIE
                                                : elterntyp.getSteuerdatenTyp()
                                        )
                                        .steuererklaerungInBern(steuererklaerungInBern)
                                )
                            );
                        }
                    }
                );
            }

            // spotless:off
            updateList(steuererklaerungs, ".*Unterhaltsbeiträge", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setUnterhaltsbeitraege(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuererklaerungs, "Renten", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setRenten(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuererklaerungs, "Ergänzungsleistungen", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setErgaenzungsleistungen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuererklaerungs, "Einnahmen BGSA", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setEinnahmenBGSA(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuererklaerungs, "Andere Einnahmen", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setAndereEinnahmen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            // spotless:on

            ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Steuerdaten " + expected, 0);

            if (i == 0) {
                initList(
                    steuerdatens,
                    ".*Einkünfte",
                    1,
                    (c) -> {
                        final var einkuenfte = ParseDemoDataUtil.parseIntegerNullable(c.getCell());
                        if (Objects.isNull(einkuenfte)) {
                            return new ArrayList<>(List.of(Optional.empty()));
                        }
                        return new ArrayList<>(
                            List.of(
                                Optional.of(
                                    new DemoSteuerdatenDto()
                                        .type(elterntyp.getSteuerdatenTyp())
                                        .totalEinkuenfte(einkuenfte)
                                )
                            )
                        );
                    }
                );
            } else {
                updateList(
                    steuerdatens,
                    ".*Einkünfte",
                    1,
                    (c, l) -> {
                        final var einkuenfte = ParseDemoDataUtil.parseIntegerNullable(c.getCell());
                        if (Objects.isNull(einkuenfte)) {
                            l.add(Optional.empty());
                        } else {
                            l.add(
                                Optional.of(
                                    new DemoSteuerdatenDto()
                                        .type(
                                            demoFamiliensituationDtos.get(c.getIndex()).getElternVerheiratetZusammen()
                                                ? SteuerdatenTyp.FAMILIE
                                                : elterntyp.getSteuerdatenTyp()
                                        )
                                        .totalEinkuenfte(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))
                                )
                            );
                        }
                    }
                );
            }

            // spotless:off
            updateList(steuerdatens, "Eigenmietwert", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setEigenmietwert(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Arbeitsverhältnis", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setIsArbeitsverhaeltnisSelbstaendig(ParseDemoEnumUtil.parseArbeitsverhaeltnisSelbstaendig(c.getCell()))));
            updateList(steuerdatens, "Säule 3a", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setSaeule3a(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "2. Säule", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setSaeule2(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Vermögen", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setVermoegen(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Kantons.*Gemeindesteuern", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setSteuernKantonGemeinde(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Steuern Bund", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setSteuernBund(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Fahrkosten", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setFahrkosten(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Fahrkosten Partner/in", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setFahrkostenPartner(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Verpflegung ", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setVerpflegung(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Verpflegung Partner/in", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setVerpflegungPartner(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Steuerjahr", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setSteuerjahr(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(steuerdatens, "Status der Veranlagung", 1, (c,  l) -> l.get(i).ifPresent(d -> d.setVeranlagungsStatus(ParseDemoDataUtil.parseStringNullable(c.getCell()))));
            // spotless:on
            index++;
        }

        return new DemoSteuererklaerungSteuerdatenLists(
            steuererklaerungs.stream().map(l -> l.stream().flatMap(Optional::stream).toList()).toList(),
            steuerdatens.stream().map(l -> l.stream().flatMap(Optional::stream).toList()).toList()
        );
    }

    private List<List<DemoGeschwisterDto>> prepareGeschwisters() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Geschwister", 0);
        final List<List<Optional<DemoGeschwisterDto>>> geschwisters = new ArrayList<>(new ArrayList<>());
        // spotless:off
        for (var i = 0; i < ANZAHL_GESCHWISTERS; i++) {
            if (i == 0) {
                // Init a list for all Testfälle with max 5 Kinder
                initList(geschwisters, "Nachname", 1, (c) -> {
                    if (ParseDemoDataUtil.isBlank(c.getCell())) {
                        return new ArrayList<>(List.of(Optional.empty()));
                    }
                    return new ArrayList<>(List.of(Optional.of(new DemoGeschwisterDto().nachname(ParseDemoDataUtil.parseString(c.getCell())))));
                });
            }
            else {
                // Append to the list of Kinder if there is a value in the new dataset
                updateList(geschwisters, "Nachname", 1, (c, o) -> o.add(ParseDemoDataUtil.isBlank(c.getCell()) ? Optional.empty() : Optional.of(new DemoGeschwisterDto().nachname(ParseDemoDataUtil.parseString(c.getCell())))));
            }

            int index = i;
            // Only update (ifPresent) the current entry if the list was initialized (when a value was defined)
            updateList(geschwisters, "Vorname", 1, (c, o) -> o.get(index).ifPresent(d -> d.vorname(ParseDemoDataUtil.parseString(c.getCell()))));
            updateList(geschwisters, "Geburtsdatum", 1, (c, o) -> o.get(index).ifPresent(d -> d.geburtsdatum(ParseDemoDataUtil.parseGeburtsdatum(c.getCell()))));
            updateList(geschwisters, "Alter.*", 1, (c, o) -> o.get(index).ifPresent(d -> d.alter(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
            updateList(geschwisters, "Wohnsitz bei", 1, (c, o) -> o.get(index).ifPresent(d -> d.wohnsitzBei(ParseDemoEnumUtil.parseWohnsitz(c.getCell()))));
            updateList(geschwisters, "bei Vater.*", 2, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitzAnteilVater(ParseDemoDataUtil.parsePercentageNullable(c.getCell()))));
            updateList(geschwisters, "bei Mutter.*", 2, (c, o) -> o.get(index).ifPresent(d -> d.setWohnsitzAnteilMutter(ParseDemoDataUtil.parsePercentageNullable(c.getCell()))));
            updateList(geschwisters, "Ausbildungssituation", 1, (c, o) -> o.get(index).ifPresent(d -> d.ausbildungssituation(ParseDemoEnumUtil.parseAusbildungssituation(c.getCell()))));
            updateList(geschwisters, "GeschwisterTyp", 1, (c, o) -> o.get(index).ifPresent(d -> d.geschwisterTyp(ParseDemoEnumUtil.parseGeschwisterTyp(c.getCell()))));
            updateList(geschwisters, "ElternTyp", 1, (c, o) -> {});
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
            (c) -> new DemoAuszahlungDto().nachname(ParseDemoDataUtil.parseStringNullable(c.getCell()))
        );
        // spotless:off
        updateList(list, "Vorname", 1, (c, d) -> d.setVorname(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Strasse", 1, (c, d) -> d.setStrasse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Nr.", 1, (c, d) -> d.setHausnummer(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
        updateList(list, "PLZ", 1, (c, d) -> d.setPlz(String.valueOf(ParseDemoDataUtil.parseIntegerNullable(c.getCell()))));
        updateList(list, "Ort", 1, (c, d) -> d.setOrt(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Co-Adresse.*", 1, (c, d) -> d.setCoAdresse(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        updateList(list, "Land", 1, (c, d) -> d.setLand(ParseDemoDataUtil.parseLandIsoCode(c.getCell())));
        updateList(list, "IBAN", 1, (c, d) -> d.setIban(ParseDemoDataUtil.parseStringNullable(c.getCell())));
        // spotless:on

        return list.stream().map(d -> Optional.of(d).filter(ParseDemoDataUtil::hasValue)).toList();
    }

    private List<Optional<DemoDarlehenDto>> prepareDarlehens() {
        ParseDemoDataUtil.checkCellContains(rowIterator.next(), "Darlehen", 0);
        final List<Optional<DemoDarlehenDto>> darlehens = new ArrayList<>();
        initList(
            darlehens,
            "Wünschen.*Darlehen.*",
            1,
            (c) -> Optional.ofNullable(ParseDemoDataUtil.parseBooleanNullable(c.getCell()))
                .filter(Boolean.TRUE::equals)
                .map((willDarlehen) -> new DemoDarlehenDto().willDarlehen(willDarlehen))
        );
        // spotless:off
        // Only update (ifPresent) the current entry if the list was initialized (when a value was defined)
        updateList(darlehens, "Gewünschter Betrag", 2, (c, o) -> o.ifPresent(d -> d.setBetragGewuenscht(ParseDemoDataUtil.parseInteger(c.getCell()))));
        updateList(darlehens, "Andere Schulden", 2, (c, o) -> o.ifPresent(d -> d.setSchulden(ParseDemoDataUtil.parseInteger(c.getCell()))));
        updateList(darlehens, "Anzahl Betreibungen", 2, (c, o) -> o.ifPresent(d -> d.setAnzahlBetreibungen(ParseDemoDataUtil.parseInteger(c.getCell()))));
        updateList(darlehens, ".*Eltern.*", 2, (c, o) -> o.ifPresent(d -> d.setGruende(new DemoDarlehenGruendeDto().grundNichtBerechtigt(ParseDemoDataUtil.parseBoolean(c.getCell())))));
        updateList(darlehens, ".*12 Jahren.*", 2, (c, o) -> o.ifPresent(d -> d.getGruende().setGrundAusbildungZwoelfJahre(ParseDemoDataUtil.parseBoolean(c.getCell()))));
        updateList(darlehens, ".*hohe.*gebühren", 2, (c, o) -> o.ifPresent(d -> d.getGruende().setGrundHoheGebuehren(ParseDemoDataUtil.parseBoolean(c.getCell()))));
        updateList(darlehens, ".*Anschaffungen.*", 2, (c, o) -> o.ifPresent(d -> d.getGruende().setGrundAnschaffungenFuerAusbildung(ParseDemoDataUtil.parseBoolean(c.getCell()))));
        updateList(darlehens, ".*Zweitausbildung.*", 2, (c, o) -> o.ifPresent(d -> d.getGruende().setGrundZweitausbildung(ParseDemoDataUtil.parseBoolean(c.getCell()))));
        // spotless:on

        return darlehens;
    }

    private List<DemoDataTestBerechnungValuesDto> prepareBerechnungValues(final Boolean ignoreBerechnungErrors) {
        final List<DemoDataTestBerechnungValuesDto> list = new ArrayList<>();
        initList(
            list,
            "Stipendienanspruch \\(Status\\)",
            0,
            (c) -> new DemoDataTestBerechnungValuesDto().status(ParseDemoEnumUtil.parseVerfuegungstatus(c.getCell()))
        );
        try {
            // spotless:off
            updateList(list, "Stipendienanspruch.*", 0, (c, d) -> d.ungekuerztStipendien(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            updateList(list, "Darlehensanspruch.*", 0, (c, d) -> d.ungekuerztDarlehen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            skipRows(1);
            updateList(list, "Stipendienanspruch.*", 0, (c, d) -> d.stipendien(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            updateList(list, "Darlehensanspruch.*", 0, (c, d) -> d.darlehen(ParseDemoDataUtil.parseIntegerNullable(c.getCell())));
            // spotless:on
        } catch (Exception e) {
            if (Boolean.TRUE.equals(ignoreBerechnungErrors)) {
                return list;
            }
            throw e;
        }

        return list;
    }
}
