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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentKommentarCopyUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarService {
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;
    private final GesuchDokumentKommentarHistoryRepository gesuchDokumentKommentarHistoryRepository;
    private final GesuchDokumentKommentarMapper gesuchDokumentKommentarMapper;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentHistoryRepository gesuchDokumentHistoryRepository;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @Transactional
    public void deleteForGesuchDokument(UUID gesuchDokumentId) {
        gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokumentId);
    }

    @Transactional
    public void deleteForGesuchTrancheId(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuchDokuments = gesuchTranche.getGesuchDokuments();
        gesuchDokuments
            .forEach(dokument -> gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(dokument.getId()));
    }

    @Transactional
    public void copyKommentareToTranche(
        final List<GesuchDokumentKommentar> gesuchDokumentKommentars,
        final GesuchTranche toTranche
    ) {
        copyKommentareToTranche(gesuchDokumentKommentars, toTranche, false);
    }

    @Transactional
    public void copyKommentareToTranche(
        final List<GesuchDokumentKommentar> gesuchDokumentKommentars,
        final GesuchTranche toTranche,
        final boolean override
    ) {
        final var toGesuchDokuments = toTranche.getGesuchDokuments();

        for (final var fromKommentar : gesuchDokumentKommentars) {
            for (final var toGesuchDokument : toGesuchDokuments) {
                final var fromGesuchDokument = fromKommentar.getGesuchDokument();

                if (fromGesuchDokument.getDokumentTyp() != null) {
                    if (fromGesuchDokument.getReference().equals(toGesuchDokument.getReference())) {
                        if (override) {
                            toGesuchDokument.getGesuchDokumentKommentare().clear();
                        }

                        final var newKommentar =
                            GesuchDokumentKommentarCopyUtil.createCopy(fromKommentar, toGesuchDokument);
                        gesuchDokumentKommentarRepository.persist(newKommentar);
                    }
                } else if (
                    fromGesuchDokument.getCustomDokumentTyp() != null && toGesuchDokument.getCustomDokumentTyp() != null
                    && (Objects.equals(
                        fromGesuchDokument.getCustomDokumentTyp().getType(),
                        toGesuchDokument.getCustomDokumentTyp().getType()
                    )
                    && (Objects.equals(
                        fromGesuchDokument.getCustomDokumentTyp().getDescription(),
                        toGesuchDokument.getCustomDokumentTyp().getDescription()
                    )))
                ) {
                    if (override) {
                        toGesuchDokument.getGesuchDokumentKommentare().clear();
                    }

                    final var newKommentar =
                        GesuchDokumentKommentarCopyUtil.createCopy(fromKommentar, toGesuchDokument);
                    toGesuchDokument.getCustomDokumentTyp().setGesuchDokument(toGesuchDokument);
                    gesuchDokumentKommentarRepository.persist(newKommentar);
                }
            }
        }
    }

    @Transactional
    public void overrideKommentareOnTranche(
        final List<GesuchDokumentKommentar> gesuchDokumentKommentars,
        final GesuchTranche toTranche
    ) {
        copyKommentareToTranche(gesuchDokumentKommentars, toTranche, true);
    }

    @Transactional
    public void copyKommentareFromTrancheToTranche(final GesuchTranche fromTranche, final GesuchTranche toTranche) {
        List<GesuchDokumentKommentar> gesuchDokumentKommentars = fromTranche.getGesuchDokuments()
            .stream()
            .flatMap(
                gesuchDokument -> gesuchDokumentKommentarRepository.getByGesuchDokumentId(gesuchDokument.getId())
                    .stream()
            )
            .toList();
        copyKommentareToTranche(gesuchDokumentKommentars, toTranche);
    }

    @Transactional
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchDokumentGS(
        final UUID gesuchDokumentId
    ) {
        var gesuchDokument = gesuchDokumentRepository.findById(gesuchDokumentId);
        if (Objects.isNull(gesuchDokument)) {
            gesuchDokument = gesuchDokumentHistoryRepository.findInHistoryById(gesuchDokumentId);
        }

        var gesuchTrancheRevision =
            gesuchTrancheHistoryService.getHistoricalTrancheRevisionForGS(gesuchDokument.getGesuchTranche().getId());

        List<GesuchDokumentKommentar> gesuchDokumentKommentars =
            gesuchDokumentKommentarRepository.getByGesuchDokumentId(gesuchDokumentId);

        if (gesuchTrancheRevision.isPresent()) {
            gesuchDokumentKommentars = gesuchDokumentKommentarHistoryRepository
                .getGesuchDokumentKommentarOfGesuchDokumentAtRevision(gesuchDokumentId, gesuchTrancheRevision.get())
                .stream()
                .filter(
                    gesuchDokumentKommentars::contains
                )
                .toList();
        }

        return gesuchDokumentKommentars.stream()
            .map(gesuchDokumentKommentarMapper::toDto)
            .toList();
    }

    @Transactional
    public List<GesuchDokumentKommentarDto> getAllKommentareForGesuchDokumentSB(
        final UUID gesuchDokumentId
    ) {
        final var gesuchDokumentKommentars =
            gesuchDokumentKommentarRepository
                .getByGesuchDokumentId(gesuchDokumentId);

        if (gesuchDokumentKommentars != null) {
            return gesuchDokumentKommentars.stream()
                .map(gesuchDokumentKommentarMapper::toDto)
                .toList();
        }
        return List.of();
    }

    @Transactional
    public void createKommentarForGesuchDokument(
        final GesuchDokument gesuchDokument,
        final GesuchDokumentKommentarDto gesuchDokumentKommentarDto
    ) {
        final var kommentar = gesuchDokumentKommentarMapper.toEntity(gesuchDokumentKommentarDto);
        if (gesuchDokumentKommentarDto != null) {
            gesuchDokument.addGesuchKommentar(kommentar);
            gesuchDokumentKommentarRepository.persistAndFlush(kommentar);
        }
    }

    private List<String> getAllFehlendeDokumenteKommentarsForGesuch(
        final Gesuch gesuch,
        final List<GesuchDokumentKommentarRepository.GesuchDokumentKommentarSlim> kommentarSlims
    ) {
        final Locale locale = LocaleUtil.getLocale(gesuch);
        final TL translator = TLProducer.defaultBundle().forAppLanguageJson(AppLanguages.fromLocale(locale));

        return kommentarSlims.stream()
            .map(
                dokument -> {
                    final String dokumentName = Optional.ofNullable(dokument.customDokumentTyp())
                        .orElseGet(() -> translator.translate("contract.file.%s".formatted(dokument.typ())));
                    return "%s: %s"
                        .formatted(
                            dokumentName,
                            dokument.kommentar().trim()
                        );
                }
            )
            .toList();
    }

    @Transactional
    public List<String> getAllFehlendeDokumenteKommentarsForGesuch(final Gesuch gesuch) {
        return getAllFehlendeDokumenteKommentarsForGesuch(
            gesuch,
            gesuchDokumentKommentarRepository.getAllNewestAbgelehntKommentarOfGesuch(gesuch.getId())
        );
    }

    @Transactional
    public List<String> getAllFehlendeDokumenteKommentarsForAenderung(final GesuchTranche aenderung) {
        return getAllFehlendeDokumenteKommentarsForGesuch(
            aenderung.getGesuch(),
            gesuchDokumentKommentarRepository.getAllNewestAbgelehntKommentarOfAenderung(aenderung.getId())
        );
    }
}
