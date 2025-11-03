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

package ch.dvbern.stip.api.gesuchformular.util;

import java.util.stream.Stream;

import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class GetDocumentsForPartnerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final var jahreseinkommen = CreatePermutationUtil.createIntegerPermutations(
            (val) -> new PartnerUpdateDto(),
            (val) -> new Partner(),
            // todo KSTIP-2779 set correct Dokumenttyp
            // DokumentTyp.PARTNER_AUSBILDUNG_LOHNABRECHNUNG
            null
        );

        final var fahrkosten = CreatePermutationUtil.createIntegerPermutations(
            (val) -> new PartnerUpdateDto(),
            (val) -> new Partner(),
            // todo KSTIP-2779 set correct Dokumenttyp
            // DokumentTyp.PARTNER_BELEG_OV_ABONNEMENT
            null
        );

        return Stream.concat(jahreseinkommen, fahrkosten);
    }
}
