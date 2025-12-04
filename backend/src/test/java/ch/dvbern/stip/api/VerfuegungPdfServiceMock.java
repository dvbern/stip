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

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.BerechnungsblattService;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.tenancy.service.TenantConfigService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;

@Mock
@RequestScoped
public class VerfuegungPdfServiceMock extends VerfuegungPdfService {
    public VerfuegungPdfServiceMock() {
        super(null, null, null, null, null);
    }

    public VerfuegungPdfServiceMock(
    StipDecisionTextRepository stipDecisionTextRepository,
    TenantConfigService tenantConfigService,
    BuchhaltungService buchhaltungService,
    VerfuegungService verfuegungService,
    BerechnungsblattService berechnungsblattService
    ) {
        super(
            stipDecisionTextRepository,
            tenantConfigService,
            buchhaltungService,
            verfuegungService,
            berechnungsblattService
        );
    }

    @Override
    public ByteArrayOutputStream createVerfuegungOhneAnspruchPdf(Verfuegung verfuegung) {
        return new ByteArrayOutputStream();
    }

    @Override
    public void createVerfuegungsDocuments(Gesuch gesuch, BerechnungsresultatDto stipendien) {
        var versendeteVerfuegung = new VerfuegungDokument();
        versendeteVerfuegung.setTyp(VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG);
        versendeteVerfuegung.setObjectId(UUID.randomUUID().toString());

        var currentVerfuegung = gesuch.getVerfuegungs().getFirst();
        versendeteVerfuegung.setVerfuegung(currentVerfuegung);
        currentVerfuegung.getDokumente().add(versendeteVerfuegung);
    }

    @Override
    public void createPdfForNegtativeVerfuegung(Verfuegung verfuegung) {}
}
