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

import java.util.Arrays;
import java.util.stream.Stream;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class GetDocumentsForEinnahmenKostenArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return concat(
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().nettoerwerbseinkommen(val),
                (val) -> new EinnahmenKosten().setNettoerwerbseinkommen(val),
                DokumentTyp.EK_LOHNABRECHNUNG
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().betreuungskostenKinder(val),
                (val) -> new EinnahmenKosten().setBetreuungskostenKinder(val),
                DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().wohnkosten(val),
                (val) -> new EinnahmenKosten().setWohnkosten(val),
                DokumentTyp.EK_MIETVERTRAG
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().fahrkosten(val),
                (val) -> new EinnahmenKosten().setFahrkosten(val),
                DokumentTyp.EK_BELEG_OV_ABONNEMENT
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().eoLeistungen(val),
                (val) -> new EinnahmenKosten().setEoLeistungen(val),
                DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().renten(val),
                (val) -> new EinnahmenKosten().setRenten(val),
                DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().beitraege(val),
                (val) -> new EinnahmenKosten().setBeitraege(val),
                DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().zulagen(val),
                (val) -> new EinnahmenKosten().setZulagen(val),
                DokumentTyp.EK_BELEG_KINDERZULAGEN
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().alimente(val),
                (val) -> new EinnahmenKosten().setAlimente(val),
                DokumentTyp.EK_BELEG_ALIMENTE
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().ergaenzungsleistungen(val),
                (val) -> new EinnahmenKosten().setErgaenzungsleistungen(val),
                DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new EinnahmenKostenUpdateDto().vermoegen(val),
                (val) -> new EinnahmenKosten().setVermoegen(val),
                DokumentTyp.EK_VERMOEGEN
            )
        );
    }

    @SafeVarargs
    // Because Java Stream does not provide a concat with varargs parameter
    private static Stream<? extends Arguments> concat(final Stream<Arguments>... streams) {
        return Arrays.stream(streams).flatMap(stream -> stream);
    }
}
