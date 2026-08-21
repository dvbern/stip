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

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.util.StreamUtil;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.generated.dto.GesuchDokumentRefDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class GetDocumentsForKindArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final var entryId = UUID.randomUUID();
        return StreamUtil.concat(
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new KindUpdateDto().unterhaltsbeitraege(val).entryId(entryId),
                (val) -> new Kind().setUnterhaltsbeitraege(val).setEntryId(entryId),
                new GesuchDokumentRefDto().dokumentTyp(DokumentTyp.KINDER_ALIMENTENVERORDUNG).entryId(entryId)
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new KindUpdateDto().kinderUndAusbildungszulagen(val).entryId(entryId),
                (val) -> new Kind().setKinderUndAusbildungszulagen(val).setEntryId(entryId),
                new GesuchDokumentRefDto().dokumentTyp(DokumentTyp.KINDER_UND_AUSBILDUNGSZULAGEN).entryId(entryId)
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new KindUpdateDto().renten(val).entryId(entryId),
                (val) -> new Kind().setRenten(val).setEntryId(entryId),
                new GesuchDokumentRefDto().dokumentTyp(DokumentTyp.KINDER_RENTEN).entryId(entryId)
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new KindUpdateDto().ergaenzungsleistungen(val).entryId(entryId),
                (val) -> new Kind().setErgaenzungsleistungen(val).setEntryId(entryId),
                new GesuchDokumentRefDto().dokumentTyp(DokumentTyp.KINDER_ERGAENZUNGSLEISTUNGEN).entryId(entryId)
            ),

            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new KindUpdateDto().betreuungskosten(val).entryId(entryId),
                (val) -> new Kind().setBetreuungskosten(val).setEntryId(entryId),
                new GesuchDokumentRefDto().dokumentTyp(DokumentTyp.KINDER_BETREUUNGSKOSTEN).entryId(entryId)
            ),
            CreatePermutationUtil.createIntegerPermutations(
                (val) -> new KindUpdateDto().andereEinnahmen(val).entryId(entryId),
                (val) -> new Kind().setAndereEinnahmen(val).setEntryId(entryId),
                new GesuchDokumentRefDto().dokumentTyp(DokumentTyp.KINDER_ANDERE_EINNAHMEN).entryId(entryId)
            )
        );
    }
}
