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

package ch.dvbern.stip.api.gesuchformular.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.darlehen.service.DarlehenMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMappingUtil;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternDiffUtil;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularDiffUtil;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.api.steuererklaerung.service.SteuererklaerungMapper;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    config = MappingConfig.class,
    uses = {
        AdresseMapper.class,
        PersonInAusbildungMapper.class,
        FamiliensituationMapper.class,
        AusbildungMapper.class,
        LebenslaufItemMapper.class,
        PartnerMapper.class,
        AuszahlungMapper.class,
        GeschwisterMapper.class,
        ElternMapper.class,
        KindMapper.class,
        EinnahmenKostenMapper.class,
        SteuererklaerungMapper.class,
        DarlehenMapper.class,
    }
)
public abstract class GesuchFormularMapper extends EntityUpdateMapper<GesuchFormularUpdateDto, GesuchFormular> {
    @Inject
    SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    @Inject
    FamiliensituationMapper familiensituationMapper;

    @Inject
    GesuchDokumentService gesuchDokumentService;

    @Inject
    UnterschriftenblattService unterschriftenblattService;

    public abstract GesuchFormular toEntity(GesuchFormularDto gesuchFormularDto);

    public abstract GesuchFormularDto toDto(GesuchFormular gesuchFormular);

    @AfterMapping
    protected void calculateSteuerdatenTabs(
        final GesuchFormular entity,
        final @MappingTarget GesuchFormularDto dto
    ) {
        if (entity.getFamiliensituation() == null) {
            return;
        }

        dto.setSteuerdatenTabs(steuerdatenTabBerechnungsService.calculateTabs(entity.getFamiliensituation()));
    }

    @AfterMapping
    public void setCalculatedPropertiesOnDto(
        GesuchFormular gesuchFormular,
        @MappingTarget GesuchFormularDto gesuchFormularDto
    ) {
        if (gesuchFormularDto.getEinnahmenKosten() != null) {
            final var ek = gesuchFormularDto.getEinnahmenKosten();
            ek.setVermoegen(EinnahmenKostenMappingUtil.calculateVermoegen(gesuchFormular));
            ek.setSteuernKantonGemeinde(EinnahmenKostenMappingUtil.calculateSteuern(gesuchFormular));
        }
    }

    /**
     * partial update mapper for the Gesuchssteller
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract GesuchFormular partialUpdate(
        GesuchFormularUpdateDto gesuchFormularUpdateDto,
        @MappingTarget GesuchFormular gesuchFormular
    );

    @BeforeMapping
    protected void setAuszahlungAdresseBeforeUpdate(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
        if (newFormular.getAuszahlung() == null) {
            return;
        }

        switch (newFormular.getAuszahlung().getKontoinhaber()) {
            case GESUCHSTELLER -> setPiaAdresse(newFormular);
            case VATER, MUTTER -> setForEltern(newFormular);
            case SOZIALDIENST_INSTITUTION, ANDERE -> {
                /* Nur gesuchsteller und eltern haben verlinkte adressen */}
        }
    }

    void setPiaAdresse(final GesuchFormularUpdateDto newFormular) {
        if (
            newFormular.getPersonInAusbildung() == null ||
            newFormular.getPersonInAusbildung().getAdresse().getId() == null
        ) {
            return;
        }

        newFormular.getAuszahlung().setAdresse(newFormular.getPersonInAusbildung().getAdresse());
    }

    void setForEltern(final GesuchFormularUpdateDto newFormular) {
        final Function<ElternTyp, ElternUpdateDto> getElternteilOfTyp = elternTyp -> {
            if (newFormular.getElterns() == null || newFormular.getElterns().isEmpty()) {
                return null;
            }

            return newFormular.getElterns()
                .stream()
                .filter(elternTeil -> elternTeil.getElternTyp() == elternTyp)
                .findFirst()
                .orElse(null);
        };

        final Consumer<ElternUpdateDto> setAdresseOfElternteil = elternteil -> {
            AdresseDto adresse = null;
            if (elternteil != null) {
                adresse = elternteil.getAdresse();
            }

            newFormular.getAuszahlung().setAdresse(adresse);
        };

        switch (newFormular.getAuszahlung().getKontoinhaber()) {
            case VATER -> setAdresseOfElternteil.accept(getElternteilOfTyp.apply(ElternTyp.VATER));
            case MUTTER -> setAdresseOfElternteil.accept(getElternteilOfTyp.apply(ElternTyp.MUTTER));
            case GESUCHSTELLER, ANDERE, SOZIALDIENST_INSTITUTION -> {
                /* Wir setzen hier nur adressen für eltern */}
        }
    }

    @BeforeMapping
    protected void mirrorWohnkosten(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
        if (
            newFormular.getFamiliensituation() == null ||
            !newFormular.getFamiliensituation().getElternVerheiratetZusammen() ||
            newFormular.getElterns() == null ||
            newFormular.getElterns().isEmpty()
        ) {
            return;
        }

        // Get Elternteil where wohnkosten changed
        ElternUpdateDto changed = null;
        for (final var elternDto : newFormular.getElterns()) {
            // If the DTOs ID is null, then it's a new Eltern entity
            if (elternDto.getId() == null) {
                changed = elternDto;
                break;
            }

            // Compare to DB Entities and set changed if wohnkosten changed
            for (final var eltern : targetFormular.getElterns()) {
                if (
                    elternDto.getId().equals(eltern.getId()) &&
                    ElternDiffUtil.hasWohnkostenChanged(elternDto, eltern)
                ) {
                    changed = elternDto;
                    break;
                }
            }

            if (changed != null) {
                break;
            }
        }

        if (changed == null) {
            return;
        }

        // Mirror Wohnkosten to the other Elternteil
        final var finalChanged = changed;
        final var other = newFormular.getElterns()
            .stream()
            .filter(elternteil -> elternteil.getElternTyp() != finalChanged.getElternTyp())
            .findFirst()
            .orElse(null);

        if (other != null) {
            other.setWohnkosten(changed.getWohnkosten());
        }
    }

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
        resetEinnahmenKosten(newFormular, targetFormular);
        resetEltern(newFormular, targetFormular);
        resetLebenslaufItems(newFormular, targetFormular);
        resetPartner(newFormular, targetFormular);
        resetSteuererklaerungTabs(newFormular, targetFormular);
        resetSteuererdatenTabs(newFormular, targetFormular);
    }

    @AfterMapping
    protected void resetDependentDataAfterUpdate(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
        resetDarlehen(targetFormular);
        resetUnterschriftenblaetter(targetFormular);
    }

    @AfterMapping
    protected void resetDependentDataAfterUpdate(
        final GesuchFormular newFormular
    ) {
        resetSteuerdatenAfterUpdate(newFormular);
    }

    void resetEinnahmenKosten(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.isUpdateToEigenerHaushalt(newFormular),
            "Clear AuswaertigeMittagessenProWochen because Wohnsitz changed to not EIGENER_HAUSHALT",
            () -> {
                if (newFormular.getEinnahmenKosten() == null) {
                    return;
                }

                newFormular.getEinnahmenKosten().setAuswaertigeMittagessenProWoche(null);
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasGerichtlicheAlimenteregelungChanged(targetFormular, newFormular),
            "Clear Alimente because GerichtlicheAlimenteregelung has changed",
            () -> {
                if (newFormular.getEinnahmenKosten() == null) {
                    return;
                }

                newFormular.getEinnahmenKosten().setAlimente(null);
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasWohnsitzChanged(newFormular, targetFormular),
            "Clear Wohnkosten because wohnsitz changed",
            () -> {
                if (newFormular.getPersonInAusbildung() == null) {
                    return;
                }

                if (
                    newFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT &&
                    newFormular.getEinnahmenKosten() != null
                ) {
                    newFormular.getEinnahmenKosten().setWohnkosten(null);
                    newFormular.getEinnahmenKosten().setWgWohnend(null);
                }
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasKinderChanged(newFormular, targetFormular),
            "Clear Betreuungskosten eigener Kinder because no Kinder",
            () -> {
                final var ek = newFormular.getEinnahmenKosten();
                if (ek != null && newFormular.getKinds().isEmpty()) {
                    ek.setBetreuungskostenKinder(null);
                }
            }
        );

        resetFieldIf(
            () -> (newFormular.getEinnahmenKosten() != null &&
            !GesuchFormularCalculationUtil
                .isPersonInAusbildungVolljaehrig(newFormular)),
            "Reset Vermoegen if Person in Ausbildung is < 18 years old",
            () -> {
                newFormular.getEinnahmenKosten().setVermoegen(null);
            }
        );
    }

    void resetDarlehen(
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> !GesuchFormularCalculationUtil.isPersonInAusbildungVolljaehrig(targetFormular),
            "Set Darlehen to null because pia is not volljaehrig",
            () -> targetFormular.setDarlehen(null)
        );
    }

    void resetUnterschriftenblaetter(final GesuchFormular targetFormular) {
        resetFieldIf(
            () -> true,
            "Delete not required Unterschriftenblaetter",
            () -> {
                final var tranche = targetFormular.getTranche();
                if (tranche == null) {
                    return;
                }

                final var gesuch = tranche.getGesuch();
                if (gesuch == null) {
                    return;
                }

                if (gesuch.getId() == null) {
                    return;
                }

                unterschriftenblattService.deleteNotRequiredForGesuch(gesuch);
            }
        );
    }

    void resetEltern(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasWerZahltAlimenteChanged(newFormular, targetFormular),
            "Clear Elternteil because werZahltAlimente changed",
            () -> {
                final var famsit = newFormular.getFamiliensituation();
                if (famsit == null) {
                    return;
                }

                final var werZahlt = newFormular.getFamiliensituation().getWerZahltAlimente();
                if (werZahlt == null) {
                    return;
                }

                switch (werZahlt) {
                    case VATER -> removeElternOfTyp(newFormular.getElterns(), ElternTyp.VATER);
                    case MUTTER -> removeElternOfTyp(newFormular.getElterns(), ElternTyp.MUTTER);
                    case GEMEINSAM -> {
                        removeElternOfTyp(newFormular.getElterns(), ElternTyp.MUTTER);
                        removeElternOfTyp(newFormular.getElterns(), ElternTyp.VATER);
                    }
                }
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasElternteilVerstorbenOrUnbekanntChanged(newFormular, targetFormular),
            "Clear Eltern because ElternteilUnbekanntVerstorben changed",
            () -> {
                if (newFormular.getFamiliensituation() == null) {
                    return;
                }

                if (
                    newFormular.getFamiliensituation().getMutterUnbekanntVerstorben() != null &&
                    newFormular.getFamiliensituation()
                        .getMutterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH
                ) {
                    removeElternOfTyp(newFormular.getElterns(), ElternTyp.MUTTER);
                }

                if (
                    newFormular.getFamiliensituation().getVaterUnbekanntVerstorben() != null &&
                    newFormular.getFamiliensituation()
                        .getVaterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH
                ) {
                    removeElternOfTyp(newFormular.getElterns(), ElternTyp.VATER);
                }
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasElternVerheiratetZusammenChanged(newFormular, targetFormular),
            "Clear Mietvertrag/ Hypothekarzins Dokument because ElternVerheiratetZusammen changed",
            () -> {
                Set<DokumentTyp> dokumentTypsToRemove;

                if (newFormular.getFamiliensituation().getElternVerheiratetZusammen()) {
                    // Delete Mutter/ Vater Dokument(e)
                    dokumentTypsToRemove = Set.of(
                        DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_MUTTER,
                        DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER
                    );
                } else {
                    // Delete Familie Dokument
                    dokumentTypsToRemove = Set.of(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
                }

                targetFormular.getTranche()
                    .getGesuchDokuments()
                    .stream()
                    .filter(gesuchDokument -> Objects.nonNull(gesuchDokument.getDokumentTyp()))
                    .filter(gesuchDokument -> dokumentTypsToRemove.contains(gesuchDokument.getDokumentTyp()))
                    .forEach(gesuchDokument -> gesuchDokumentService.removeGesuchDokument(gesuchDokument));
            }
        );
    }

    void resetLebenslaufItems(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasGeburtsdatumOfPersonInAusbildungChanged(targetFormular, newFormular),
            "Clear LebenslaufItems because Geburtsdatum has changed",
            () -> newFormular.setLebenslaufItems(new ArrayList<>())
        );
    }

    void resetPartner(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasZivilstandChangedToOnePerson(targetFormular, newFormular),
            "Clear Partner because Zivilstand changed to one person",
            () -> {
                targetFormular.setPartner(null);
                newFormular.setPartner(null);
            }
        );
    }

    void resetSteuererklaerungTabs(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        if (newFormular.getSteuererklaerung() == null || newFormular.getSteuererklaerung().isEmpty()) {
            return;
        }

        if (newFormular.getFamiliensituation() == null) {
            newFormular.getSteuererklaerung().clear();
        } else {
            if (targetFormular.getFamiliensituation() == null) {
                return;
            }

            final var targetFamsit = familiensituationMapper.partialUpdate(
                newFormular.getFamiliensituation(),
                targetFormular.getFamiliensituation()
            );

            final var requiredTabs = new HashSet<>(
                steuerdatenTabBerechnungsService.calculateTabs(targetFamsit)
            );

            newFormular.getSteuererklaerung()
                .removeAll(
                    newFormular.getSteuererklaerung()
                        .stream()
                        .filter(newTab -> !requiredTabs.contains(newTab.getSteuerdatenTyp()))
                        .toList()
                );
        }
    }

    void resetSteuererdatenTabs(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        if (newFormular.getFamiliensituation() == null) {
            targetFormular.getSteuerdaten().clear();
        } else {
            if (targetFormular.getFamiliensituation() == null) {
                return;
            }

            final var targetFamsit = familiensituationMapper.partialUpdate(
                newFormular.getFamiliensituation(),
                targetFormular.getFamiliensituation()
            );

            final var requiredTabs = new HashSet<>(
                steuerdatenTabBerechnungsService.calculateTabs(targetFamsit)
            );

            targetFormular.getSteuerdaten()
                .removeAll(
                    targetFormular.getSteuerdaten()
                        .stream()
                        .filter(newTab -> !requiredTabs.contains(newTab.getSteuerdatenTyp()))
                        .toList()
                );
        }
    }

    void resetSteuerdatenAfterUpdate(
        final GesuchFormular gesuchFormular
    ) {
        gesuchFormular.getSteuerdaten().forEach(steuerdaten -> {
            if (!steuerdaten.getIsArbeitsverhaeltnisSelbstaendig()) {
                steuerdaten.setSaeule2(null);
                steuerdaten.setSaeule3a(null);
            }
        });
    }

    void removeElternOfTyp(final List<ElternUpdateDto> eltern, final ElternTyp typ) {
        if (eltern == null) {
            return;
        }

        eltern.removeAll(eltern.stream().filter(x -> x.getElternTyp() == typ).toList());
    }
}
