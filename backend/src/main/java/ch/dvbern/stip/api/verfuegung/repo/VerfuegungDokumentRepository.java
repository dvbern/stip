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

package ch.dvbern.stip.api.verfuegung.repo;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class VerfuegungDokumentRepository implements BaseRepository<VerfuegungDokument> {

    // todo: review find method
    public Optional<VerfuegungDokument> findByVerfuegungAndTyp(
        final UUID verfuegungId,
        final VerfuegungDokumentTyp typ
    ) {
        return find(
            "verfuegung.id = ?1 AND dokumentTyp = ?2",
            verfuegungId,
            typ
        ).firstResultOptional();
    }
}
