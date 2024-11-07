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

package ch.dvbern.stip.api.fall.service;

import java.util.List;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.service.IdEncryptionService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallNummerSeqRepository;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.generated.dto.FallDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class FallService {
    private final FallMapper fallMapper;
    private final FallRepository fallRepository;
    private final BenutzerService benutzerService;
    private final IdEncryptionService idEncryptionService;
    private final FallNummerSeqRepository fallNummerSeqRepository;

    @Transactional
    public FallDto createFallForGs() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var fall = new Fall();
        fall.setGesuchsteller(benutzer);
        fall.setFallNummer(createFallNummer());
        fallRepository.persistAndFlush(fall);
        return fallMapper.toDto(fall);
    }

    public List<FallDto> findFaelleForSb() {
        final var sachbearbeiterId = benutzerService.getCurrentBenutzer().getId();
        return fallRepository.findFaelleForSb(sachbearbeiterId).map(fallMapper::toDto).toList();
    }

    public FallDto findFallForGs() {
        final var gesuchstellerId = benutzerService.getCurrentBenutzer().getId();
        return fallMapper.toDto(
            fallRepository.findFallForGsOptional(gesuchstellerId).orElse(null)
        );
    }

    private String createFallNummer() {
        // TODO KSTIP-1411: Mandantenkürzel
        var nextValue = fallNummerSeqRepository.getNextValue("BE");
        var encoded = idEncryptionService.encryptLengthSix(nextValue);

        // TODO KSTIP-1411: Mandantenkürzel
        return String.format("BE.F.%s", encoded);
    }
}
