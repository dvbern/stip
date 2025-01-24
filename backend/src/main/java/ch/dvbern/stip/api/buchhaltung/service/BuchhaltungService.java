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

package ch.dvbern.stip.api.buchhaltung.service;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.generated.dto.BuchhaltungEntryDto;
import ch.dvbern.stip.generated.dto.BuchhaltungSaldokorrekturDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BuchhaltungService {
    private final BuchhaltungMapper buchhaltungMapper;
    private final BuchhaltungRepository buchhaltungRepository;
    private final GesuchRepository gesuchRepository;
    private final FallRepository fallRepository;

    @Transactional
    public BuchhaltungEntryDto createBuchhaltungSaldokorrekturForFall(
        final UUID fallId,
        final BuchhaltungSaldokorrekturDto buchhaltungSaldokorrekturDto
    ) {
        return createBuchhaltungSaldokorrekturForFall(fallRepository.requireById(fallId), buchhaltungSaldokorrekturDto);
    }

    @Transactional
    public BuchhaltungEntryDto createBuchhaltungSaldokorrekturForFall(
        final Fall fall,
        final BuchhaltungSaldokorrekturDto buchhaltungSaldokorrekturDto
    ) {
        final var firstEntrySaldo = fall.getBuchhaltungs()
            .stream()
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt))
            .map(Buchhaltung::getSaldo)
            .orElse(0);

        final var buchhaltungEntry = new Buchhaltung()
            .setSaldo(firstEntrySaldo + buchhaltungSaldokorrekturDto.getBetrag())
            .setBetrag(buchhaltungSaldokorrekturDto.getBetrag())
            .setComment(buchhaltungSaldokorrekturDto.getComment())
            .setBuchhaltungType(BuchhaltungType.SALDOAENDERUNG)
            .setFall(fall)
            .setGesuch(gesuchRepository.requireById(buchhaltungSaldokorrekturDto.getGesuchId()));

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        fall.getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungMapper.toDto(buchhaltungEntry);
    }

    @Transactional
    public BuchhaltungEntryDto createBuchaltungForFallAndGesuch(
        final Fall fall,
        final Gesuch gesuch,
        final BuchhaltungType buchhaltungType,
        final Integer betrag,
        final String comment
    ) {
        return createBuchaltungForFallAndGesuch(fall, gesuch, buchhaltungType, betrag, comment, null, null);
    }

    @Transactional
    public BuchhaltungEntryDto createBuchaltungForFallAndGesuch(
        final Fall fall,
        final Gesuch gesuch,
        final BuchhaltungType buchhaltungType,
        final Integer betrag,
        final String comment,
        final Integer sapDeliveryId,
        final SapStatus sapStatus
    ) {
        final var firstEntrySaldo = fall.getBuchhaltungs()
            .stream()
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt))
            .map(Buchhaltung::getSaldo)
            .orElse(0);

        final var buchhaltungEntry = new Buchhaltung()
            .setBuchhaltungType(buchhaltungType)
            .setBetrag(betrag)
            .setSaldo(firstEntrySaldo + betrag)
            .setSapDeliveryId(sapDeliveryId)
            .setSapStatus(sapStatus)
            .setComment(comment)
            .setGesuch(gesuch)
            .setFall(fall);

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        fall.getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungMapper.toDto(buchhaltungEntry);
    }

    public Stream<Buchhaltung> getAllForFallId(final UUID fallId) {
        return buchhaltungRepository.findAllForFallId(fallId);
    }

    public Stream<BuchhaltungEntryDto> getAllDtoForFallId(final UUID fallId) {
        return getAllForFallId(fallId).map(buchhaltungMapper::toDto);
    }
}
