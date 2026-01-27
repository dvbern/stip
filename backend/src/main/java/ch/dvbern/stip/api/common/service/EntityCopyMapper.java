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

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.darlehen.entity.DarlehenBuchhaltungEntry;
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
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class EntityCopyMapper {
    @Mapping(target = "tranche", ignore = true)
    @IgnoreStandardFields
    public abstract void copyFromTo(GesuchFormular source, @MappingTarget GesuchFormular target);

    @IgnoreStandardFields
    public abstract void copyFromTo(Adresse source, @MappingTarget Adresse target);

    @IgnoreStandardFields
    public abstract void copyFromTo(PersonInAusbildung source, @MappingTarget PersonInAusbildung target);

    @IgnoreStandardFields
    public abstract void copyFromTo(Familiensituation source, @MappingTarget Familiensituation target);

    @IgnoreStandardFields
    public abstract void copyFromTo(Partner source, @MappingTarget Partner target);

    @IgnoreStandardFields
    public abstract void copyFromTo(EinnahmenKosten source, @MappingTarget EinnahmenKosten target);

    @IgnoreStandardFields
    public abstract void copyFromTo(FreiwilligDarlehen source, @MappingTarget FreiwilligDarlehen target);

    @IgnoreStandardFields
    public abstract void copyFromTo(DarlehenBuchhaltungEntry source, @MappingTarget DarlehenBuchhaltungEntry target);

    public abstract void copyFromToEltern(Set<Eltern> source, @MappingTarget Set<Eltern> target);

    public abstract void copyFromToLebenslaufItem(
        Set<LebenslaufItem> source,
        @MappingTarget Set<LebenslaufItem> target
    );

    public abstract void copyFromToGeschwister(Set<Geschwister> source, @MappingTarget Set<Geschwister> target);

    public abstract void copyFromToKind(Set<Kind> source, @MappingTarget Set<Kind> target);

    public abstract void copyFromToSteuerdaten(Set<Steuerdaten> source, @MappingTarget Set<Steuerdaten> target);

    public abstract void copyFromToSteuererklaerung(
        Set<Steuererklaerung> source,
        @MappingTarget Set<Steuererklaerung> target
    );

    @IgnoreStandardFields
    public abstract Zahlungsverbindung createCopy(Zahlungsverbindung source);

    @IgnoreStandardFields
    public abstract Adresse createCopy(Adresse source);

    @IgnoreStandardFields
    public abstract LebenslaufItem createCopy(LebenslaufItem source);

    @IgnoreStandardFields
    public abstract Geschwister createCopy(Geschwister source);

    @IgnoreStandardFields
    public abstract Eltern createCopy(Eltern source);

    @IgnoreStandardFields
    public abstract Kind createCopy(Kind source);

    @IgnoreStandardFields
    public abstract Steuerdaten createCopy(Steuerdaten source);

    @IgnoreStandardFields
    public abstract Steuererklaerung createCopy(Steuererklaerung source);
}
