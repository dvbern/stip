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

package ch.dvbern.stip.api.gesuch.service;

import java.util.UUID;

import ch.dvbern.stip.api.common.service.IdEncryptionService;
import ch.dvbern.stip.api.gesuch.repo.GesuchNummerSeqRepository;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchNummerService {
    private final GesuchNummerSeqRepository gesuchNummerSeqRepository;
    private final IdEncryptionService idEncryptionService;
    private final GesuchsperiodenService gesuchsperiodenService;
    private final GesuchsjahrService gesuchsjahrService;

    private static final String MANDANT = "BE"; // TODO KSTIP-1411: Mandantenkürzel

    @Transactional
    public String createGesuchNummer(final UUID gesuchsperiodeId) {
        final var gesuchsperiode =
            gesuchsperiodenService.getGesuchsperiode(gesuchsperiodeId).orElseThrow(NotFoundException::new);
        final var gesuchsjahr = gesuchsjahrService.getGesuchsjahr(gesuchsperiode.getGesuchsjahrId());
        final var technischesJahr = gesuchsjahr.getTechnischesJahr();

        var nextValue = gesuchNummerSeqRepository.getNextSequenceValue(MANDANT, technischesJahr);

        var encoded = idEncryptionService.encryptLengthFive(nextValue);

        return String.format("%s.%s.G.%s", technischesJahr, MANDANT, encoded);
    }

}
