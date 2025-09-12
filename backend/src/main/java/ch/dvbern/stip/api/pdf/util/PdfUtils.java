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

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PdfUtils {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.of("de", "CH"));

    public static String formatNumber(Number number) {
        return NUMBER_FORMAT.format(number);
    }

    public void makePageNumberEven(Document document) {
        if (document.getPdfDocument().getNumberOfPages() % 2 == 0) {
            return;
        }
        document.getPdfDocument().addNewPage();
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
    }
}
