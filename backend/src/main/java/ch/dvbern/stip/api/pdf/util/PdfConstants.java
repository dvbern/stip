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

package ch.dvbern.stip.api.pdf.util;

import java.text.NumberFormat;
import java.util.Locale;

import com.itextpdf.kernel.geom.PageSize;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PdfConstants {
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.of("de", "CH"));
    public static final String FONT_PATH = "/fonts/arial.ttf";
    public static final String FONT_BOLD_PATH = "/fonts/arial_bold.ttf";
    public static final String FONT_ITALIC_PATH = "/fonts/arial_italic.ttf";
    public static final PageSize PAGE_SIZE = PageSize.A4;

    public static final String RECHTSMITTELBELEHRUNG_TITLE_KEY = "stip.pdf.rechtsmittelbelehrung.title";
    public static final String AUSBILDUNGSBEITRAEGE_LINK = "www.be.ch/ausbildungsbeitraege";
    public static final String LOGO_PATH = "/images/bern_logo.svg";

    public static final int SPACING_BIG = 30;
    public static final int SPACING_MEDIUM = 20;
    public static final int SPACING_SMALL = 10;
    public static final float FONT_SIZE_BIG = 10.5f;
    public static final float FONT_SIZE_MEDIUM = 8.5f;
    public static final float FONT_SIZE_SMALL = 6.5f;
}
