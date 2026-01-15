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

package ch.dvbern.stip.api.common.service;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class EntityOverrideMapper {
    @Mapping(target = "fall", ignore = true)
    @Mapping(target = "gesuchs", ignore = true)
    @Mapping(target = "status", ignore = true)
    @IgnoreStandardFields
    public abstract void overrideFromTo(Ausbildung source, @MappingTarget Ausbildung target);

    @Mapping(target = "tranche", ignore = true)
    @IgnoreStandardFields
    public abstract void overrideFromTo(GesuchFormular source, @MappingTarget GesuchFormular target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Adresse source, @MappingTarget Adresse target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(PersonInAusbildung source, @MappingTarget PersonInAusbildung target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Familiensituation source, @MappingTarget Familiensituation target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Partner source, @MappingTarget Partner target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(EinnahmenKosten source, @MappingTarget EinnahmenKosten target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(FreiwilligDarlehen source, @MappingTarget FreiwilligDarlehen target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Eltern source, @MappingTarget Eltern target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(LebenslaufItem source, @MappingTarget LebenslaufItem target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Geschwister source, @MappingTarget Geschwister target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Kind source, @MappingTarget Kind target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Steuerdaten source, @MappingTarget Steuerdaten target);

    @IgnoreStandardFields
    public abstract void overrideFromTo(Steuererklaerung source, @MappingTarget Steuererklaerung target);

    public void overrideFromToEltern(Set<Eltern> source, @MappingTarget Set<Eltern> target) {
        overrideSetIdentifier(source, target, Eltern::new, this::overrideFromTo, Eltern::getElternTyp);
    }

    public void overrideFromToLebenslaufItem(Set<LebenslaufItem> source, @MappingTarget Set<LebenslaufItem> target) {
        overrideSetById(source, target, LebenslaufItem::new, this::overrideFromTo);
    }

    public void overrideFromToGeschwister(Set<Geschwister> source, @MappingTarget Set<Geschwister> target) {
        overrideSetById(source, target, Geschwister::new, this::overrideFromTo);
    }

    public void overrideFromToKind(Set<Kind> source, @MappingTarget Set<Kind> target) {
        overrideSetById(source, target, Kind::new, this::overrideFromTo);
    }

    public void overrideFromToSteuerdaten(Set<Steuerdaten> source, @MappingTarget Set<Steuerdaten> target) {
        overrideSetById(source, target, Steuerdaten::new, this::overrideFromTo);
    }

    public void overrideFromToSteuererklaerung(
        Set<Steuererklaerung> source,
        @MappingTarget Set<Steuererklaerung> target
    ) {
        overrideSetById(source, target, Steuererklaerung::new, this::overrideFromTo);
    }

    /**
     * Override any set by updating already existing items with source elements, removing superfluous values and
     * adding new values to the set
     * Uses {@link AbstractEntity#getId()} as identifier getter function
     *
     * @param source
     * @param target
     * @param create The constructor or factory used to create the given entity
     * @param overrideMethod Which method should be used to override the target values with the source values
     * @param <T>
     */
    private <T extends AbstractEntity> void overrideSetById(
        final Set<T> source,
        final Set<T> target,
        final Supplier<T> create,
        final BiConsumer<T, T> overrideMethod
    ) {
        overrideSetIdentifier(source, target, create, overrideMethod, AbstractEntity::getId);
    }

    /**
     * Override any set by updating already existing items with source elements, removing superfluous values and
     * adding new values to the set by comparing values with given identifier function
     *
     * @param source
     * @param target
     * @param create The constructor or factory used to create the given entity
     * @param overrideMethod Which method should be used to override the target values with the source values
     * @param identifier The function used to get the identifier used to check if target contains source element
     * @param <T> The set base type
     * @param <R> The type of the return value from the identifier function
     */
    private <T extends AbstractEntity, R> void overrideSetIdentifier(
        final Set<T> source,
        final Set<T> target,
        final Supplier<T> create,
        final BiConsumer<T, T> overrideMethod,
        final Function<T, R> identifier
    ) {
        final var sourceMap = source.stream().collect(Collectors.toMap(identifier, Function.identity()));
        final var targetMap = target.stream().collect(Collectors.toMap(identifier, Function.identity()));
        target.removeIf(t -> !sourceMap.containsKey(identifier.apply(t)));

        for (T sourceItem : source) {
            final var targetItem = targetMap.getOrDefault(identifier.apply(sourceItem), null);
            if (targetItem == null) {
                final var newItem = create.get();
                overrideMethod.accept(sourceItem, newItem);
                target.add(newItem);
            } else {
                overrideMethod.accept(sourceItem, targetItem);
            }
        }
    }
}
