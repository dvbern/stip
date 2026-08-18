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

package ch.dvbern.stip.api;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.darlehen.service.DarlehenService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.VerfuegungBriefPdfService;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfComposerService;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.integration.pdf.domain.port.PdfPortFactory;
import ch.dvbern.stip.integration.pdf.domain.service.BerechnungCopyMapper;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;

@Mock
@RequestScoped
public class VerfuegungPdfComposerServiceMock extends VerfuegungPdfComposerService {
    public VerfuegungPdfComposerServiceMock() {
        super(null, null, null, null, null);
    }

    public VerfuegungPdfComposerServiceMock(
    VerfuegungService verfuegungService,
    DarlehenService darlehenService,
    PdfPortFactory pdfPortFactory,
    BerechnungCopyMapper berechnungCopyMapper,
    VerfuegungBriefPdfService verfuegungBriefPdfService
    ) {
        super(
            verfuegungService,
            darlehenService,
            pdfPortFactory,
            berechnungCopyMapper,
            verfuegungBriefPdfService
        );
    }

    @Override
    public void createVerfuegungsDocuments(Gesuch gesuch, Optional<BerechnungsresultatDto> stipendien) {
        var versendeteVerfuegung = new VerfuegungDokument();
        versendeteVerfuegung.setTyp(VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG);
        versendeteVerfuegung.setObjectId(UUID.randomUUID().toString());

        var currentVerfuegung = gesuch.getVerfuegungs().getFirst();
        versendeteVerfuegung.setVerfuegung(currentVerfuegung);
        currentVerfuegung.getDokumente().add(versendeteVerfuegung);
    }
}
