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
import java.util.List;
import java.util.Optional;
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

    private int getLastEntrySaldo(List<Buchhaltung> buchhaltungList) {
        return buchhaltungList
            .stream()
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt))
            .map(Buchhaltung::getSaldo)
            .orElse(0);
    }

    @Transactional
    public BuchhaltungEntryDto createBuchhaltungSaldokorrekturForFall(
        final UUID gesuchId,
        final BuchhaltungSaldokorrekturDto buchhaltungSaldokorrekturDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return createBuchhaltungSaldokorrekturForFall(
            gesuch,
            buchhaltungSaldokorrekturDto
        );
    }

    @Transactional
    public BuchhaltungEntryDto createBuchhaltungSaldokorrekturForFall(
        final Gesuch gesuch,
        final BuchhaltungSaldokorrekturDto buchhaltungSaldokorrekturDto
    ) {
        final Fall fall = fallRepository.requireById(gesuch.getAusbildung().getFall().getId());
        final var lastEntrySaldo = getLastEntrySaldo(fall.getBuchhaltungs());

        final var buchhaltungEntry = new Buchhaltung()
            .setSaldo(lastEntrySaldo + buchhaltungSaldokorrekturDto.getBetrag())
            .setBetrag(buchhaltungSaldokorrekturDto.getBetrag())
            .setComment(buchhaltungSaldokorrekturDto.getComment())
            .setBuchhaltungType(BuchhaltungType.SALDOAENDERUNG)
            .setFall(fall)
            .setGesuch(gesuch);

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
        final var lastEntrySaldo = getLastEntrySaldo(fall.getBuchhaltungs());

        final var buchhaltungEntry = new Buchhaltung()
            .setBuchhaltungType(buchhaltungType)
            .setBetrag(betrag)
            .setSaldo(lastEntrySaldo + betrag)
            .setSapDeliveryId(sapDeliveryId)
            .setSapStatus(sapStatus)
            .setComment(comment)
            .setGesuch(gesuch)
            .setFall(fall);

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        fall.getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungMapper.toDto(buchhaltungEntry);
    }

    public Optional<Buchhaltung> getLastEntryStipendiumOpt(final UUID gesuchId) {
        return buchhaltungRepository.findStipendiumsEntrysForGesuch(gesuchId)
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt));
    }

    // TODO: KSTIP-1622: Unittest this -> resourcetest
    @Transactional
    public BuchhaltungEntryDto createStipendiumBuchhaltungEntry(
        final Gesuch gesuch,
        final Integer stipendiumBetrag,
        final String comment
    ) {
        final var lastEntrySaldo = getLastEntrySaldo(gesuch.getAusbildung().getFall().getBuchhaltungs());

        final var lastEntryStipendiumOpt = getLastEntryStipendiumOpt(gesuch.getId());

        int betrag = 0;
        if (lastEntryStipendiumOpt.isPresent()) {
            final var lastEntryStipendium = lastEntryStipendiumOpt.get();
            final int lastStipendiumBetrag =
                lastEntryStipendium.getStipendium() != null
                    ? lastEntryStipendium.getStipendium()
                    : 0;
            betrag = stipendiumBetrag - lastStipendiumBetrag;
        }

        final var buchhaltungEntry = new Buchhaltung()
            .setBuchhaltungType(BuchhaltungType.STIPENDIUM)
            .setBetrag(betrag)
            .setSaldo(lastEntrySaldo + betrag)
            .setStipendium(stipendiumBetrag)
            .setComment(comment)
            .setGesuch(gesuch)
            .setFall(gesuch.getAusbildung().getFall());

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        gesuch.getAusbildung().getFall().getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungMapper.toDto(buchhaltungEntry);
    }

    public Stream<Buchhaltung> getAllForFallId(final UUID fallId) {
        return buchhaltungRepository.findAllForFallId(fallId);
    }

    public Stream<BuchhaltungEntryDto> getAllDtoForGesuchId(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return getAllForFallId(gesuch.getAusbildung().getFall().getId()).map(buchhaltungMapper::toDto);
    }
}
