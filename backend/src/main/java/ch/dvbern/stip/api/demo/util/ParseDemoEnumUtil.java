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
import java.util.List;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;

@UtilityClass
public class ParseDemoEnumUtil {
    public GesuchTrancheTyp parseTyp(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            GesuchTrancheTyp.class,
            (wohnsitz) -> switch (wohnsitz) {
                case TRANCHE -> List.of("Gesuch");
                case AENDERUNG -> List.of("Änderung");
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public Anrede parseAnrede(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Anrede.class,
            (anrede) -> switch (anrede) {
                case HERR -> List.of("Herr");
                case FRAU -> List.of("Frau");
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public Wohnsitz parseWohnsitz(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Wohnsitz.class,
            (wohnsitz) -> switch (wohnsitz) {
                case FAMILIE -> List.of("Familie");
                case MUTTER_VATER -> List.of("Vater und/oder Mutter");
                case EIGENER_HAUSHALT -> List.of("eigener Haushalt");
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public Zivilstand parseZivilstand(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Zivilstand.class,
            (zivilstand) -> switch (zivilstand) {
                case LEDIG -> List.of("ledig");
                case VERHEIRATET -> List.of("verheiratet");
                case KONKUBINAT -> List.of("Konkubinat");
                case GESCHIEDEN_GERICHTLICH -> List.of("geschieden / gerichtlich getrennt");
                case EINGETRAGENE_PARTNERSCHAFT -> List.of("eingetragene Partnerschaft");
                case AUFGELOESTE_PARTNERSCHAFT -> List.of("aufgelöste Partnerschaft");
                case VERWITWET -> List.of("verwitwet");
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public Niederlassungsstatus parseNiederlassungsstatus(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Niederlassungsstatus.class,
            (niederlassungstatus) -> switch (niederlassungstatus) {
                case SAISONARBEITEND_A -> List.of("Ausweis A");
                case AUFENTHALTSBEWILLIGUNG_B -> List.of("Ausweis B");
                case NIEDERLASSUNGSBEWILLIGUNG_C -> List.of("Ausweis C");
                case PARTNER_ERWERBSTAETIG_UND_KIND_CI -> List.of("Ausweis Ci");
                case VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS -> List.of("Ausweis F");
                case GRENZGAENGIG_G -> List.of("Ausweis G");
                case KURZAUFENTHALT_L -> List.of("Ausweis L");
                case ASYLSUCHEND_N -> List.of("Ausweis N");
                case SCHUTZBEDUERFTIG_S -> List.of("Ausweis S");
                case MELDEPFLICHTIG -> List.of("Meldepflichtig ZEMIS");
                case DIPLOMATISCHE_FUNKTION -> List.of("Diplomaten");
                case INTERNATIONALE_FUNKTION -> List.of("internationale Funktionäre");
                case NICHT_ZUGETEILT -> List.of("nicht zugeteilt");
                // Handled seperately using specific values
                case VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT, VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON -> null;
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public ZustaendigeKESB parseZustaendigeKESB(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            ZustaendigeKESB.class,
            (kesb) -> switch (kesb) {
                case KESB_BERN -> List.of("Kanton Bern");
                case KESB_ANDERER_KANTON -> List.of("anderer Kanton");
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public Taetigkeitsart parseTaetigkeitsart(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Taetigkeitsart.class,
            (taetigkeitsart) -> switch (taetigkeitsart) {
                case ERWERBSTAETIGKEIT -> List.of("Erwerbstätigkeit");
                case ANDERE_TAETIGKEIT -> List.of("andere Tätigkeit");
                case BETREUUNG_FAMILIENMITGLIEDER_EIGENER_HAUSHALT -> List
                    .of("Betreuung von Familienmitglieder im eigenen Haushalt");
            }
        ).orElseThrow(() -> invalidValue(cell));
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
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Ausbildungssituation.class,
            (ausbildungssituation) -> switch (ausbildungssituation) {
                case VORSCHULPFLICHTIG -> List.of("Vorschulpflichtig");
                case SCHULPFLICHTIG -> List.of("Schulpflichtig");
                case IN_AUSBILDUNG -> List.of("in nachobligatorischer Ausbildung");
                case KEINE -> List.of("keine der Optionen");
            }
        ).orElseThrow(() -> invalidValue(cell));
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
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            Elternschaftsteilung.class,
            (eltenrschaftsteilung) -> switch (eltenrschaftsteilung) {
                case VATER -> List.of("Vater");
                case MUTTER -> List.of("Mutter");
                case GEMEINSAM -> List.of("Gemeinsam");
            }
        ).orElseThrow(() -> invalidValue(cell));
    }

    public static ElternUnbekanntheitsGrund parseElternUnbekanntheitsGrund(Cell cell) {
        if (ParseDemoDataUtil.isBlank(cell)) {
            return null;
        }
        return FindEnum.findEnumValue(
            cell.getStringCellValue(),
            ElternUnbekanntheitsGrund.class,
            (elternUnbekanntheitsGrund) -> switch (elternUnbekanntheitsGrund) {
                case FEHLENDE_ANERKENNUNG -> List
                    .of("fehlende Mutterschaftsanerkennung", "fehlende Vaterschaftsanerkennung");
                case UNBEKANNTER_AUFENTHALTSORT -> List.of("unbekannter Aufenthaltsort");
            }
        ).orElseThrow(() -> invalidValue(cell));
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
