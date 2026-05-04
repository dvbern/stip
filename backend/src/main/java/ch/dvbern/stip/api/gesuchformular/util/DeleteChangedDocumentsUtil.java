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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentRefDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.KindUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.SteuererklaerungUpdateDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DeleteChangedDocumentsUtil {
    public ArrayList<GesuchDokumentRefDto> getChangedDocumentsToDelete(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular oldFormular
    ) {
        if (oldFormular.getTranche().getTyp() != GesuchTrancheTyp.AENDERUNG) {
            LOG.info("Skipping deleting documents on fields that changed for Tranche that is not AENDERUNG");
            return new ArrayList<>();
        }

        final var documentTypesToDelete = new ArrayList<GesuchDokumentRefDto>();
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForPersonInAusbildung(
                newFormular.getPersonInAusbildung(),
                oldFormular.getPersonInAusbildung()
            )
        );

        for (final var oldEltern : oldFormular.getElterns()) {
            final var newEltern = newFormular.getElterns()
                .stream()
                .filter(
                    elternUpdateDto -> elternUpdateDto.getElternTyp() == oldEltern.getElternTyp()
                )
                .findFirst();

            if (newEltern.isPresent()) {
                documentTypesToDelete.addAll(
                    getDocumentsToDeleteForEltern(newEltern.get(), oldEltern)
                );
            } else {
                documentTypesToDelete.add(
                    switch (oldEltern.getElternTyp()) {
                        case MUTTER -> toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER);
                        case VATER -> toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER);
                    }
                );
            }
        }

        for (final var oldWeitereAngaben : oldFormular.getSteuererklaerung()) {
            final var newWeitereAngaben = newFormular.getSteuererklaerung()
                .stream()
                .filter(
                    steuererklaerungUpdateDto -> steuererklaerungUpdateDto.getSteuerdatenTyp() == oldWeitereAngaben
                        .getSteuerdatenTyp()
                )
                .findFirst();

            newWeitereAngaben.ifPresent(
                steuererklaerungUpdateDto -> documentTypesToDelete.addAll(
                    getDocumentsToDeleteForWeitereAngabenElternteil(steuererklaerungUpdateDto, oldWeitereAngaben)
                )
            );
        }

        documentTypesToDelete.addAll(
            getDocumentsToDeleteForFamiliensituation(
                newFormular.getFamiliensituation(),
                oldFormular.getFamiliensituation()
            )
        );

        for (final var oldKind : oldFormular.getKinds()) {
            final var newKind = newFormular.getKinds()
                .stream()
                .filter(
                    kindUpdateDto -> kindUpdateDto.getEntryId().equals(oldKind.getEntryId())
                )
                .findFirst();

            newKind.ifPresent(
                kindUpdateDto -> documentTypesToDelete.addAll(
                    getDocumentsToDeleteForKind(kindUpdateDto, oldKind)
                )
            );
        }

        documentTypesToDelete.addAll(
            getDocumentsToDeleteForEinnahmenKosten(
                newFormular.getEinnahmenKosten(),
                oldFormular.getEinnahmenKosten(),
                false
            )
        );
        documentTypesToDelete.addAll(
            getDocumentsToDeleteForEinnahmenKosten(
                newFormular.getEinnahmenKostenPartner(),
                oldFormular.getEinnahmenKostenPartner(),
                true
            )
        );

        return documentTypesToDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForPersonInAusbildung(
        final PersonInAusbildungUpdateDto newPia,
        final PersonInAusbildung oldPia
    ) {
        final var toDelete = new ArrayList<GesuchDokumentRefDto>();
        if (!Objects.equals(newPia.getGeburtsdatum(), oldPia.getGeburtsdatum())) {
            toDelete.add(toRefDto(DokumentTyp.PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN));
        }

        return toDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForKind(
        final KindUpdateDto newKind,
        final Kind oldKind
    ) {
        final var toDelete = new ArrayList<GesuchDokumentRefDto>();
        if (hasChangedAndNewIsGreaterThanZero(newKind.getUnterhaltsbeitraege(), oldKind.getUnterhaltsbeitraege())) {
            toDelete.add(toRefDto(DokumentTyp.KINDER_ALIMENTENVERORDUNG, newKind.getEntryId()));
        }
        if (
            hasChangedAndNewIsGreaterThanZero(
                newKind.getKinderUndAusbildungszulagen(),
                oldKind.getKinderUndAusbildungszulagen()
            )
        ) {
            toDelete.add(toRefDto(DokumentTyp.KINDER_UND_AUSBILDUNGSZULAGEN, newKind.getEntryId()));
        }
        if (hasChangedAndNewIsGreaterThanZero(newKind.getRenten(), oldKind.getRenten())) {
            toDelete.add(toRefDto(DokumentTyp.KINDER_RENTEN, newKind.getEntryId()));
        }
        if (hasChangedAndNewIsGreaterThanZero(newKind.getErgaenzungsleistungen(), oldKind.getErgaenzungsleistungen())) {
            toDelete.add(toRefDto(DokumentTyp.KINDER_ERGAENZUNGSLEISTUNGEN, newKind.getEntryId()));
        }
        if (hasChangedAndNewIsGreaterThanZero(newKind.getAndereEinnahmen(), oldKind.getAndereEinnahmen())) {
            toDelete.add(toRefDto(DokumentTyp.KINDER_ANDERE_EINNAHMEN, newKind.getEntryId()));
        }

        return toDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForEinnahmenKosten(
        final EinnahmenKostenUpdateDto newEk,
        final EinnahmenKosten oldEk,
        final boolean isPartner
    ) {
        if (Objects.isNull(oldEk) || Objects.isNull(newEk)) {
            return List.of();
        }
        final var toDelete = new ArrayList<GesuchDokumentRefDto>();
        if (hasChangedAndNewIsGreaterThanZero(oldEk.getNettoerwerbseinkommen(), newEk.getNettoerwerbseinkommen())) {
            toDelete.add(toRefDto(!isPartner ? DokumentTyp.EK_LOHNABRECHNUNG : DokumentTyp.EK_PARTNER_LOHNABRECHNUNG));
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getBetreuungskostenKinder(), newEk.getBetreuungskostenKinder())) {
            toDelete.add(
                toRefDto(
                    !isPartner ? DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER
                        : DokumentTyp.EK_PARTNER_BELEG_BETREUUNGSKOSTEN_KINDER
                )
            );
        }

        if (!isPartner && hasChangedAndNewIsGreaterThanZero(oldEk.getWohnkosten(), newEk.getWohnkosten())) {
            toDelete.add(toRefDto(DokumentTyp.EK_MIETVERTRAG));
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getFahrkosten(), newEk.getFahrkosten())) {
            toDelete.add(
                toRefDto(!isPartner ? DokumentTyp.EK_BELEG_OV_ABONNEMENT : DokumentTyp.EK_PARTNER_BELEG_OV_ABONNEMENT)
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getEoLeistungen(), newEk.getEoLeistungen())) {
            toDelete.add(
                toRefDto(
                    !isPartner ? DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
                        : DokumentTyp.EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
                )
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getRenten(), newEk.getRenten())) {
            toDelete.add(
                toRefDto(
                    !isPartner ? DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN : DokumentTyp.EK_PARTNER_BELEG_BEZAHLTE_RENTEN
                )
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getBeitraege(), newEk.getBeitraege())) {
            toDelete.add(
                toRefDto(
                    !isPartner ? DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION
                        : DokumentTyp.EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION
                )
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getEinnahmenBGSA(), newEk.getEinnahmenBGSA())) {
            toDelete.add(
                toRefDto(!isPartner ? DokumentTyp.EK_BELEG_EINNAHMEN_BGSA : DokumentTyp.EK_PARTNER_BELEG_EINNAHMEN_BGSA)
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getTaggelderAHVIV(), newEk.getTaggelderAHVIV())) {
            toDelete
                .add(
                    toRefDto(
                        !isPartner ? DokumentTyp.EK_BELEG_TAGGELDER_AHV_IV
                            : DokumentTyp.EK_PARTNER_BELEG_TAGGELDER_AHV_IV
                    )
                );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getAndereEinnahmen(), newEk.getAndereEinnahmen())) {
            toDelete
                .add(
                    toRefDto(
                        !isPartner ? DokumentTyp.EK_BELEG_ANDERE_EINNAHMEN
                            : DokumentTyp.EK_PARTNER_BELEG_ANDERE_EINNAHMEN
                    )
                );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getZulagen(), newEk.getZulagen())) {
            toDelete.add(
                toRefDto(!isPartner ? DokumentTyp.EK_BELEG_KINDERZULAGEN : DokumentTyp.EK_PARTNER_BELEG_KINDERZULAGEN)
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getUnterhaltsbeitraege(), newEk.getUnterhaltsbeitraege())) {
            toDelete.add(
                toRefDto(
                    !isPartner ? DokumentTyp.EK_BELEG_UNTERHALTSBEITRAEGE
                        : DokumentTyp.EK_PARTNER_BELEG_UNTERHALTSBEITRAEGE
                )
            );
        }

        if (hasChangedAndNewIsGreaterThanZero(oldEk.getErgaenzungsleistungen(), newEk.getErgaenzungsleistungen())) {
            toDelete.add(
                toRefDto(
                    !isPartner ? DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN
                        : DokumentTyp.EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN
                )
            );
        }
        if (hasChangedAndNewIsGreaterThanZero(oldEk.getVermoegen(), newEk.getVermoegen())) {
            toDelete.add(toRefDto(!isPartner ? DokumentTyp.EK_VERMOEGEN : DokumentTyp.EK_PARTNER_VERMOEGEN));
        }

        return toDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForDarlehen(
        final FreiwilligDarlehenDto newFreiwilligDarlehen,
        final FreiwilligDarlehen oldFreiwilligDarlehen
    ) {
        if (newFreiwilligDarlehen == null || oldFreiwilligDarlehen == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<GesuchDokumentRefDto>();
        if (
            hasChangedAndNewIsGreaterThanZero(
                oldFreiwilligDarlehen.getAnzahlBetreibungen(),
                newFreiwilligDarlehen.getAnzahlBetreibungen()
            )
        ) {
            toDelete.add(toRefDto(DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG));
        }

        return toDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForEltern(
        final ElternUpdateDto newEltern,
        final Eltern oldEltern
    ) {
        if (newEltern == null || oldEltern == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<GesuchDokumentRefDto>();
        if (
            hasChangedAndNewIsGreaterThanZero(oldEltern.getWohnkosten(), newEltern.getWohnkosten())
        ) {
            final var toDeleteDoc = switch (newEltern.getElternTyp()) {
                case MUTTER -> toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER);
                case VATER -> toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER);
            };
            toDelete.add(toDeleteDoc);
            toDelete.add(toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE));
        }
        return toDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForWeitereAngabenElternteil(
        final SteuererklaerungUpdateDto newSteuererklaerung,
        final Steuererklaerung oldSteuererklaerung
    ) {
        if (newSteuererklaerung == null || oldSteuererklaerung == null) {
            return List.of();
        }

        final var toDelete = new ArrayList<GesuchDokumentRefDto>();
        if (
            hasChangedAndNewIsGreaterThanZero(
                oldSteuererklaerung.getUnterhaltsbeitraege(),
                newSteuererklaerung.getUnterhaltsbeitraege()
            )
        ) {
            final var toDeleteDoc = switch (newSteuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_MUTTER);
                case VATER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_VATER);
                case FAMILIE -> toRefDto(DokumentTyp.STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_FAMILIE);
            };
            toDelete.add(toDeleteDoc);
        }
        if (
            hasChangedAndNewIsGreaterThanZero(oldSteuererklaerung.getRenten(), newSteuererklaerung.getRenten())
        ) {
            final var toDeleteDoc = switch (newSteuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_RENTEN_MUTTER);
                case VATER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_RENTEN_VATER);
                case FAMILIE -> toRefDto(DokumentTyp.STEUERERKLAERUNG_RENTEN_FAMILIE);
            };
            toDelete.add(toDeleteDoc);
        }
        if (
            hasChangedAndNewIsGreaterThanZero(
                oldSteuererklaerung.getErgaenzungsleistungen(),
                newSteuererklaerung.getErgaenzungsleistungen()
            )
        ) {
            final var toDeleteDoc = switch (newSteuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_MUTTER);
                case VATER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_VATER);
                case FAMILIE -> toRefDto(DokumentTyp.STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_FAMILIE);
            };
            toDelete.add(toDeleteDoc);
        }
        if (
            hasChangedAndNewIsGreaterThanZero(
                oldSteuererklaerung.getEinnahmenBGSA(),
                newSteuererklaerung.getEinnahmenBGSA()
            )
        ) {
            final var toDeleteDoc = switch (newSteuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_MUTTER);
                case VATER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_VATER);
                case FAMILIE -> toRefDto(DokumentTyp.STEUERERKLAERUNG_EINNAHMEN_BGSA_FAMILIE);
            };
            toDelete.add(toDeleteDoc);
        }
        if (
            hasChangedAndNewIsGreaterThanZero(
                oldSteuererklaerung.getAndereEinnahmen(),
                newSteuererklaerung.getAndereEinnahmen()
            )
        ) {
            final var toDeleteDoc = switch (newSteuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_MUTTER);
                case VATER -> toRefDto(DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_VATER);
                case FAMILIE -> toRefDto(DokumentTyp.STEUERERKLAERUNG_ANDERE_EINNAHMEN_FAMILIE);
            };
            toDelete.add(toDeleteDoc);
        }

        return toDelete;
    }

    List<GesuchDokumentRefDto> getDocumentsToDeleteForFamiliensituation(
        final FamiliensituationUpdateDto newFamiliensituation,
        final Familiensituation oldFamiliensituation
    ) {
        if (newFamiliensituation == null || oldFamiliensituation == null) {
            return List.of();
        }
        final var toDelete = new ArrayList<GesuchDokumentRefDto>();

        if (
            !Objects.equals(
                newFamiliensituation.getElternVerheiratetZusammen(),
                oldFamiliensituation.getElternVerheiratetZusammen()
            )
        ) {
            toDelete.add(toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER));
            toDelete.add(toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER));
            toDelete.add(toRefDto(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE));
        }
        return toDelete;
    }

    private boolean hasChangedAndNewIsGreaterThanZero(final Integer oldVal, final Integer newVal) {
        return !Objects.equals(oldVal, newVal) && oldVal != null && oldVal != 0 && newVal != null && newVal > 0;
    }

    private GesuchDokumentRefDto toRefDto(final DokumentTyp dokumentTyp) {
        return new GesuchDokumentRefDto().dokumentTyp(dokumentTyp);
    }

    private GesuchDokumentRefDto toRefDto(final DokumentTyp dokumentTyp, final UUID entryId) {
        return toRefDto(dokumentTyp).entryId(entryId);
    }
}
