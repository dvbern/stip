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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.service.EntityCopyMapper;
import ch.dvbern.stip.api.common.util.JwtUtil;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.zahlungsverbindung.repo.ZahlungsverbindungRepository;
import ch.dvbern.stip.generated.dto.BuchhaltungEntryDto;
import ch.dvbern.stip.generated.dto.BuchhaltungOverviewDto;
import ch.dvbern.stip.generated.dto.BuchhaltungSaldokorrekturDto;
import ch.dvbern.stip.generated.dto.PaginatedFailedAuszahlungBuchhaltungDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class BuchhaltungService {
    private final BuchhaltungMapper buchhaltungMapper;
    private final BuchhaltungRepository buchhaltungRepository;
    private final GesuchRepository gesuchRepository;
    private final FallRepository fallRepository;
    private final FallMapper fallMapper;
    private final ZahlungsverbindungRepository zahlungsverbindungRepository;
    private final EntityCopyMapper entityCopyMapper;

    private int getLastEntrySaldo(List<Buchhaltung> buchhaltungList) {
        return buchhaltungList
            .stream()
            .filter(
                buchhaltung -> buchhaltung.getSapDeliverys()
                    .stream()
                    .map(SapDelivery::getSapStatus)
                    .noneMatch(
                        SapStatus.NO_SUCCESS::contains
                    )
            )
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt))
            .map(Buchhaltung::getSaldo)
            .orElse(0);
    }

    @Transactional
    public Buchhaltung getLatestBuchhaltungEntry(final UUID fallId) {
        return buchhaltungRepository.findAllForFallId(fallId)
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt))
            .orElseThrow(
                NotFoundException::new
            );
    }

    @Transactional
    public Buchhaltung getLatestNotFailedBuchhaltungEntry(final UUID fallId) {
        return buchhaltungRepository.findAllForFallId(fallId)
            .filter(buchhaltung -> buchhaltung.getSapStatus() != SapStatus.FAILURE)
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt))
            .orElseThrow(
                NotFoundException::new
            );
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
        final var lastEntrySaldo = getLastEntrySaldo(fall.getBuchhaltungs());

        final var buchhaltungEntry = new Buchhaltung()
            .setBuchhaltungType(buchhaltungType)
            .setBetrag(betrag)
            .setSaldo(lastEntrySaldo + betrag)
            .setComment(comment)
            .setGesuch(gesuch)
            .setFall(fall);

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        fall.getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungMapper.toDto(buchhaltungEntry);
    }

    private static TL getTranslator(final Locale locale) {
        final var language = AppLanguages.fromLocale(locale);
        return TLProducer.defaultBundle().forAppLanguage(language);
    }

    @Transactional
    public Buchhaltung createBuchhaltungForBusinessPartnerCreate(
        final UUID gesuchId
    ) {
        final Gesuch gesuch = gesuchRepository.requireById(gesuchId);
        final var lastEntrySaldo = getLastEntrySaldo(gesuch.getAusbildung().getFall().getBuchhaltungs());

        final TL translator = getTranslator(LocaleUtil.getLocaleFromGesuch(gesuch));

        final var buchhaltungEntry = new Buchhaltung()
            .setBuchhaltungType(BuchhaltungType.BUSINESSPARTNER_CREATE)
            .setBetrag(0)
            .setSaldo(lastEntrySaldo)
            .setComment(translator.translate("stip.businesspartner.buchhaltung.erstellen"))
            .setGesuch(gesuch)
            .setFall(gesuch.getAusbildung().getFall());

        if (Objects.nonNull(gesuch.getAusbildung().getFall().getRelevantZahlungsverbindung())) {
            final var newZahlungsverbindung = entityCopyMapper.createCopy(
                gesuch.getAusbildung().getFall().getRelevantZahlungsverbindung()
            );
            zahlungsverbindungRepository.persistAndFlush(newZahlungsverbindung);
            buchhaltungEntry.setZahlungsverbindung(newZahlungsverbindung);
        }

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        buchhaltungEntry.setUserErstellt(JwtUtil.SYSTEM_USR);
        final var fall = gesuch.getAusbildung().getFall();
        fall.getAuszahlung().setBuchhaltung(buchhaltungEntry);
        fall.getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungEntry;
    }

    @Transactional
    public Buchhaltung createAuszahlungBuchhaltungForGesuch(
        final Gesuch gesuch,
        final Integer betrag,
        final BuchhaltungType buchhaltungType
    ) {
        final String tlKey = switch (buchhaltungType) {
            case AUSZAHLUNG_INITIAL -> "stip.auszahlung.buchhaltung.initial";
            case AUSZAHLUNG_REMAINDER -> "stip.auszahlung.buchhaltung.remainder";
            default -> throw new IllegalStateException("BuchhaltungType " + buchhaltungType + " not supported");
        };

        final TL translator = getTranslator(LocaleUtil.getLocaleFromGesuch(gesuch));
        final var lastEntrySaldo = getLastEntrySaldo(gesuch.getAusbildung().getFall().getBuchhaltungs());

        final var buchhaltungEntry = new Buchhaltung()
            .setBuchhaltungType(buchhaltungType)
            .setBetrag(betrag)
            .setSaldo(lastEntrySaldo - betrag)
            .setComment(translator.translate(tlKey))
            .setGesuch(gesuch)
            .setFall(gesuch.getAusbildung().getFall());

        final var newZahlungsverbindung = entityCopyMapper.createCopy(
            gesuch.getAusbildung().getFall().getRelevantZahlungsverbindung()
        );
        zahlungsverbindungRepository.persistAndFlush(newZahlungsverbindung);
        buchhaltungEntry.setZahlungsverbindung(newZahlungsverbindung);

        buchhaltungRepository.persistAndFlush(buchhaltungEntry);
        gesuch.getAusbildung().getFall().getBuchhaltungs().add(buchhaltungEntry);
        return buchhaltungEntry;
    }

    public Optional<Buchhaltung> getLastEntryStipendiumOpt(final UUID gesuchId) {
        return buchhaltungRepository.findStipendiumsEntrysForGesuch(gesuchId)
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt));
    }

    public Optional<Buchhaltung> findLatestPendingBuchhaltungAuszahlungOpt(
        final UUID fallId,
        final BuchhaltungType buchhaltungType
    ) {
        return buchhaltungRepository.findPendingBuchhaltungEntryOfFall(fallId, buchhaltungType);
    }

    @Transactional
    public BuchhaltungEntryDto createStipendiumBuchhaltungEntry(
        final Gesuch gesuch,
        final Integer stipendiumBetrag
    ) {
        final TL translator = getTranslator(LocaleUtil.getLocaleFromGesuch(gesuch));

        final var lastEntrySaldo = getLastEntrySaldo(gesuch.getAusbildung().getFall().getBuchhaltungs());

        final var lastEntryStipendiumOpt = getLastEntryStipendiumOpt(gesuch.getId());

        String comment = translator.translate("stip.verfuegung.buchhaltung.erstgesuch");
        if (!gesuch.isErstgesuch()) {
            comment = translator.translate("stip.verfuegung.buchhaltung.folgegesuch");
        }

        int betrag = stipendiumBetrag;
        if (lastEntryStipendiumOpt.isPresent()) {
            comment = translator.translate("stip.verfuegung.buchhaltung.aenderung");
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

    public BuchhaltungOverviewDto getBuchhaltungOverviewDto(final UUID gesuchId) {
        final BuchhaltungOverviewDto buchhaltungOverviewDto = new BuchhaltungOverviewDto();
        buchhaltungOverviewDto.setBuchhaltungEntrys(getAllDtoForGesuchId(gesuchId).toList());
        buchhaltungOverviewDto.setCanRetryAuszahlung(canRetryAuszahlungBuchhaltung(gesuchId));
        return buchhaltungOverviewDto;
    }

    @Transactional
    public void deleteBuchhaltungsForGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var buchhaltungs = getAllForFallId(gesuch.getAusbildung().getFall().getId());
        for (var buchhaltung : buchhaltungs.toList()) {
            if (Objects.nonNull(buchhaltung.getGesuch()) && buchhaltung.getGesuch().equals(gesuch)) {
                buchhaltung.setGesuch(null);
            }
        }
    }

    public boolean canRetryAuszahlungBuchhaltung(final Fall fall) {
        return Objects.nonNull(fall.getFailedBuchhaltungAuszahlungType());
    }

    public boolean canRetryAuszahlungBuchhaltung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return canRetryAuszahlungBuchhaltung(
            gesuch.getAusbildung()
                .getFall()
        );
    }

    @Transactional
    public PaginatedFailedAuszahlungBuchhaltungDto getPaginatedFailedAuszahlungBuchhaltung(
        final Integer page,
        final Integer pageSize
    ) {
        final var dto = new PaginatedFailedAuszahlungBuchhaltungDto();
        dto.setEntries(
            fallRepository.findAllFallsWithFailedAuszahlungBuchhaltung(page, pageSize)
                .map(fallMapper::toFailedAuszahlungBuchhaltungDto)
                .toList()
        );
        return dto;
    }
}
