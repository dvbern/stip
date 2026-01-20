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

import java.util.Arrays;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

@UtilityClass
public class ParseDemoEnumUtil {
    public Anrede parseAnrede(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "Herr" -> Anrede.HERR;
            case "Frau" -> Anrede.FRAU;
            default -> throw invalidValue(cell);
        };
    }

    public Wohnsitz parseWohnsitz(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "Familie" -> Wohnsitz.FAMILIE;
            case "Vater und/oder Mutter" -> Wohnsitz.MUTTER_VATER;
            case "eigener Haushalt" -> Wohnsitz.EIGENER_HAUSHALT;
            default -> throw invalidValue(cell);
        };
    }

    public Zivilstand parseZivilstand(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "ledig" -> Zivilstand.LEDIG;
            case "verheiratet" -> Zivilstand.VERHEIRATET;
            case "Konkubinat" -> Zivilstand.KONKUBINAT;
            case "geschieden / gerichtlich getrennt" -> Zivilstand.GESCHIEDEN_GERICHTLICH;
            case "eingetragene Partnerschaft" -> Zivilstand.EINGETRAGENE_PARTNERSCHAFT;
            case "aufgelöste Partnerschaft" -> Zivilstand.AUFGELOESTE_PARTNERSCHAFT;
            case "verwitwet" -> Zivilstand.VERWITWET;
            default -> throw invalidValue(cell);
        };
    }

    public Niederlassungsstatus parseNiederlassungsstatus(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        return switch (cell.getStringCellValue()) {
            case "Ausweis A" -> Niederlassungsstatus.SAISONARBEITEND_A;
            case "Ausweis B" -> Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B;
            case "Ausweis C" -> Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C;
            case "Ausweis Ci" -> Niederlassungsstatus.PARTNER_ERWERBSTAETIG_UND_KIND_CI;
            case "Ausweis F" -> Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS;
            case "Ausweis G" -> Niederlassungsstatus.GRENZGAENGIG_G;
            case "Ausweis L" -> Niederlassungsstatus.KURZAUFENTHALT_L;
            case "Ausweis N" -> Niederlassungsstatus.ASYLSUCHEND_N;
            case "Ausweis S" -> Niederlassungsstatus.SCHUTZBEDUERFTIG_S;
            case "Meldepflichtig ZEMIS" -> Niederlassungsstatus.MELDEPFLICHTIG;
            case "Diplomaten" -> Niederlassungsstatus.DIPLOMATISCHE_FUNKTION;
            case "internationale Funktionäre" -> Niederlassungsstatus.INTERNATIONALE_FUNKTION;
            case "nicht zugeteilt" -> null;
            default -> throw invalidValue(cell);
        };
    }

    public ZustaendigeKESB parseZustaendigeKESB(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "Kanton Bern" -> ZustaendigeKESB.KESB_BERN;
            case "anderer Kanton" -> ZustaendigeKESB.KESB_ANDERER_KANTON;
            default -> throw invalidValue(cell);
        };
    }

    public Taetigkeitsart parseTaetigkeitsart(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "Erwerbstätigkeit" -> Taetigkeitsart.ERWERBSTAETIGKEIT;
            case "andere Tätigkeit" -> Taetigkeitsart.ANDERE_TAETIGKEIT;
            default -> throw invalidValue(cell);
        };
    }

    public AusbildungsPensum parseAusbildungsPensum(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch ((int) cell.getNumericCellValue()) {
            case 100 -> AusbildungsPensum.VOLLZEIT;
            default -> AusbildungsPensum.TEILZEIT;
        };
    }

    public Ausbildungssituation parseAusbildungssituation(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "Vorschulpflichtig" -> Ausbildungssituation.VORSCHULPFLICHTIG;
            case "Schulpflichtig" -> Ausbildungssituation.SCHULPFLICHTIG;
            case "in nachobligatorischer Ausbildung" -> Ausbildungssituation.IN_AUSBILDUNG;
            case "keine der Optionen" -> Ausbildungssituation.KEINE;
            default -> throw invalidValue(cell);
        };
    }

    public WohnsitzKanton parseWohnsitzKanton(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return Arrays
            .stream(WohnsitzKanton.values())
            .filter(w -> w.name().equals(cell.getStringCellValue()))
            .findFirst()
            .orElseThrow(() -> invalidValue(cell));
    }

    public static Elternschaftsteilung parseElternschaftsteilung(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "Vater" -> Elternschaftsteilung.VATER;
            case "Mutter" -> Elternschaftsteilung.MUTTER;
            case "Gemeinsam" -> Elternschaftsteilung.GEMEINSAM;
            default -> throw invalidValue(cell);
        };
    }

    public static ElternUnbekanntheitsGrund parseElternUnbekanntheitsGrund(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "fehlende Mutterschaftsanerkennung", "fehlende Vaterschaftsanerkennung" -> ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG;
            case "unbekannter Aufenthaltsort" -> ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT;
            default -> throw invalidValue(cell);
        };
    }

    public static Boolean parseArbeitsverhaeltnisSelbstaendig(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return switch (cell.getStringCellValue()) {
            case "selbständig" -> Boolean.TRUE;
            case "unselbständig" -> Boolean.FALSE;
            default -> throw invalidValue(cell);
        };
    }

    private IllegalStateException invalidValue(Cell cell) {
        return new IllegalStateException("Cell type '%s' is not supported".formatted(cell.getStringCellValue()));
    }
}
