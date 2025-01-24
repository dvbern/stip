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

package ch.dvbern.stip.api.dokument.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDto;
import ch.dvbern.stip.generated.dto.CustomDokumentTypDto;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class CustomDokumentTypService {
    private final CustomDokumentTypRepository customDocumentTypRepository;
    private final CustomDocumentTypMapper customDocumentTypMapper;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentService gesuchDokumentService;

    @Transactional
    public CustomDokumentTypDto createCustomDokumentTyp(CustomDokumentTypCreateDto dto) {
        final var customDokumentTyp = customDocumentTypMapper.toEntity(dto);
        customDocumentTypRepository.persistAndFlush(customDokumentTyp);
        return customDocumentTypMapper.toDto(customDokumentTyp);
    }

    @Transactional
    public List<CustomDokumentTypDto> getAllCustomDokumentTypDtosOfTranche(UUID gesuchTrancheId) {
        return getAllCustomDokumentTypsOfTranche(gesuchTrancheId)
            .stream()
            .map(customDocumentTypMapper::toDto)
            .toList();
    }

    @Transactional
    public List<CustomDokumentTyp> getAllCustomDokumentTypsOfTranche(UUID gesuchTrancheId) {
        final var gesuchDokuments = findByCustomDokumentTypId(gesuchTrancheId);
        return gesuchDokuments.stream().map(GesuchDokument::getCustomDokumentTyp).toList();
    }

    @Transactional
    public void deleteCustomDokumentTyp(UUID customDokumentTypId) {
        if (gesuchDokumentService.customDokumentHasGesuchDokuments(customDokumentTypId)) {
            throw new ForbiddenException("Dem generischem Dokument sind noch Files angehaenkt");
        } else {
            // clear all references to gesuchdokkument
            final var gesuchDokumenteOfCustomType =
                gesuchDokumentRepository.findAllByCustomDokumentTypeId(customDokumentTypId);
            gesuchDokumenteOfCustomType.forEach(z -> {
                gesuchDokumentRepository.deleteById(z.getId());
            });
            customDocumentTypRepository.deleteById(customDokumentTypId);
        }
    }

    private List<GesuchDokument> findByCustomDokumentTypId(UUID gesuchTrancheId) {
        return gesuchDokumentRepository.findAllOfTypeCustomByGesuchTrancheId(gesuchTrancheId);
    }
}
