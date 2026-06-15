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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.steuererklaerung.service.SteuererklaerungMapper;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    config = MappingConfig.class,
    uses = GesuchFormularMapper.class
)
public abstract class GesuchTrancheMapper {
    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    @interface ToDtoDefaultMapping {
    }

    @Inject
    ElternMapper elternMapper;

    @Inject
    SteuererklaerungMapper steuererklaerungMapper;

    @Inject
    FamiliensituationMapper familiensituationMapper;

    @Inject
    GeschwisterMapper geschwisterMapper;

    @ToDtoDefaultMapping
    public abstract GesuchTrancheDto toDtoWithElevatedPermissions(GesuchTranche gesuch, @Context GesuchTranche context);

    public GesuchTrancheDto toDtoWithElevatedPermissions(GesuchTranche gesuch) {
        return toDtoWithElevatedPermissions(gesuch, gesuch);
    }

    @ToDtoDefaultMapping
    @BeanMapping(qualifiedByName = "afterMappingWithoutElevatedPermissionFields")
    public abstract GesuchTrancheDto toDtoWithoutElevatedPermissions(
        GesuchTranche gesuchTranche,
        @Context GesuchTranche context
    );

    public GesuchTrancheDto toDtoWithoutElevatedPermissions(GesuchTranche gesuchTranche) {
        return toDtoWithoutElevatedPermissions(gesuchTranche, gesuchTranche);
    }

    @ToDtoDefaultMapping
    public abstract GesuchTrancheSlimDto toSlimDto(GesuchTranche gesuchTranche);

    @Mapping(source = "gesuchTranche.gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gesuchTranche.gueltigkeit.gueltigBis", target = "gueltigBis")
    public abstract GesuchTrancheSlimDto toSlimDto(GesuchTranche gesuchTranche, int revision);

    public List<GesuchTrancheSlimDto> toSlimDto(List<GesuchTranche> gesuchTranches, int revision) {
        return gesuchTranches.stream()
            .map(gesuchTranche -> toSlimDto(gesuchTranche, revision))
            .toList();
    }

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        qualifiedByName = "centralMappingWithOverrideFieldsThatRequireElevatedPermissions"
    )
    public abstract GesuchTranche partialUpdateOverrideFieldsThatRequireElevatedPermissions(
        GesuchTrancheUpdateDto gesuchUpdateDto,
        @MappingTarget GesuchTranche gesuch
    );

    public abstract GesuchTranche partialUpdateAcceptFieldsThatRequireElevatedPermissions(
        GesuchTrancheUpdateDto gesuchUpdateDto,
        @MappingTarget GesuchTranche gesuch
    );

    public GesuchTranche partialUpdate(
        final GesuchTrancheUpdateDto gesuchUpdateDto,
        final GesuchTranche gesuch,
        final boolean requiresElevatedPermissions
    ) {
        if (requiresElevatedPermissions) {
            return partialUpdateOverrideFieldsThatRequireElevatedPermissions(gesuchUpdateDto, gesuch);
        } else {
            return partialUpdateAcceptFieldsThatRequireElevatedPermissions(gesuchUpdateDto, gesuch);
        }
    }

    @Named("afterMappingWithoutElevatedPermissionFields")
    @AfterMapping
    protected void afterMappingWithoutElevatedPermissionFields(
        @MappingTarget GesuchTrancheDto gesuchTrancheDto,
        @Context GesuchTranche context
    ) {
        removeHiddenElternsData(gesuchTrancheDto, context);
        removeHiddenGeschwistersData(gesuchTrancheDto, context);
    }

    protected void removeHiddenElternsData(
        GesuchTrancheDto gesuchTrancheDto,
        GesuchTranche context
    ) {
        Stream.of(
            gesuchTrancheDto.getGesuchFormular().getEinnahmenKosten(),
            gesuchTrancheDto.getGesuchFormular().getEinnahmenKostenPartner()
        )
            .filter(Objects::nonNull)
            .forEach(ek -> ek.setSteuern(null));

        final var eltern = gesuchTrancheDto.getGesuchFormular().getElterns();
        final var versteckteEltern = context.getGesuchFormular().getVersteckteEltern();
        if (eltern != null) {
            eltern.removeIf(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()));
        }

        final var steuererklaerungen = gesuchTrancheDto.getGesuchFormular().getSteuererklaerung();
        if (steuererklaerungen != null) {
            steuererklaerungen.removeIf(steuererklaerung -> switch (steuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> versteckteEltern.contains(ElternTyp.MUTTER);
                case VATER -> versteckteEltern.contains(ElternTyp.VATER);
                case FAMILIE -> !versteckteEltern.isEmpty();
            }
            );
        }
    }

    protected void removeHiddenGeschwistersData(
        GesuchTrancheDto gesuchTrancheDto,
        GesuchTranche context
    ) {
        final var hiddenGeschwistersUUID = context.getGesuchFormular()
            .getGeschwisters()
            .stream()
            .filter(Geschwister::isHidden)
            .map(Geschwister::getId)
            .toList();
        gesuchTrancheDto.getGesuchFormular()
            .getGeschwisters()
            .removeIf(geschwisterDto -> hiddenGeschwistersUUID.contains(geschwisterDto.getId()));
    }

    @Named("centralMappingWithOverrideFieldsThatRequireElevatedPermissions")
    @BeforeMapping
    protected void centralBeforeMappingWithOverrideFieldsThatRequireElevatedPermissions(
        final GesuchTrancheUpdateDto newTranche,
        @MappingTarget final GesuchTranche gesuchTranche
    ) {
        beforeMappingOverrideSteuern(newTranche, gesuchTranche);
        beforeMappingOverrideIncomingVersteckteEltern(newTranche, gesuchTranche);
        beforeMappingAddHiddenGeschwisters(newTranche, gesuchTranche);
    }

    protected void beforeMappingOverrideSteuern(
        final GesuchTrancheUpdateDto newTranche,
        @MappingTarget final GesuchTranche gesuchTranche
    ) {
        final var ekDto = newTranche.getGesuchFormular().getEinnahmenKosten();
        final var ek = gesuchTranche.getGesuchFormular().getEinnahmenKosten();
        if (Objects.nonNull(ekDto) && Objects.nonNull(ek)) {
            ekDto.setSteuern(ek.getSteuern());
        }

        final var ekPartnerDto = newTranche.getGesuchFormular().getEinnahmenKostenPartner();
        final var ekPartner = gesuchTranche.getGesuchFormular().getEinnahmenKostenPartner();
        if (Objects.nonNull(ekPartnerDto) && Objects.nonNull(ekPartner)) {
            ekPartnerDto.setSteuern(ekPartner.getSteuern());
        }
    }

    protected void beforeMappingOverrideIncomingVersteckteEltern(
        final GesuchTrancheUpdateDto newTranche,
        @MappingTarget final GesuchTranche gesuchTranche
    ) {
        final var versteckteEltern = gesuchTranche.getGesuchFormular().getVersteckteEltern();
        if (versteckteEltern.isEmpty()) {
            return;
        }

        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            // Override incoming Familiensituation for Aenderungen with versteckte Elternteile
            final var replacementFamiliensituation =
                familiensituationMapper.toUpdateDto(gesuchTranche.getGesuchFormular().getFamiliensituation());
            newTranche.getGesuchFormular().setFamiliensituation(replacementFamiliensituation);
        }

        if (gesuchTranche.getGesuchFormular().getElterns() == null) {
            return;
        }

        // Load and find Eltern to replace the incoming one(s) (i.e. ignoring incoming changes)
        final var replacementEltern = gesuchTranche.getGesuchFormular()
            .getElterns()
            .stream()
            .filter(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()))
            .map(elternMapper::toUpdateDto)
            .toList();

        for (final var replacement : replacementEltern) {
            final var newFormular = newTranche.getGesuchFormular();
            newFormular.getElterns()
                .removeIf(eltern -> eltern.getElternTyp() == replacement.getElternTyp());

            newFormular.getElterns().add(replacement);
        }

        // Load and find Steuererklaerungen to replace the incoming one(s)
        final var replacementSteuererklaerungen = gesuchTranche.getGesuchFormular()
            .getSteuererklaerung()
            .stream()
            .filter(steuererklaerung -> switch (steuererklaerung.getSteuerdatenTyp()) {
                case null -> false;
                case MUTTER -> versteckteEltern.contains(ElternTyp.MUTTER);
                case VATER -> versteckteEltern.contains(ElternTyp.VATER);
                case FAMILIE -> versteckteEltern.isEmpty();
            }
            )
            .map(steuererklaerungMapper::toUpdateDto)
            .toList();

        for (final var replacementSteuererklaerung : replacementSteuererklaerungen) {
            final var newFormular = newTranche.getGesuchFormular();
            newFormular.getSteuererklaerung()
                .removeIf(
                    steuererklaerung -> steuererklaerung.getSteuerdatenTyp() == replacementSteuererklaerung
                        .getSteuerdatenTyp()
                );

            newFormular.getSteuererklaerung().add(replacementSteuererklaerung);
        }
    }

    protected void beforeMappingAddHiddenGeschwisters(
        final GesuchTrancheUpdateDto newTranche,
        @MappingTarget final GesuchTranche gesuchTranche
    ) {
        final var hiddenGeschwisters =
            gesuchTranche.getGesuchFormular().getGeschwisters().stream().filter(Geschwister::isHidden);
        final var hiddenGeschwistersDtos = hiddenGeschwisters.map(geschwisterMapper::toUpdateDto).toList();
        newTranche.getGesuchFormular().getGeschwisters().addAll(hiddenGeschwistersDtos);
    }
}
