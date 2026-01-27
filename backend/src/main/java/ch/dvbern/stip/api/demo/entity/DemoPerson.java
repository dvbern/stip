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

package ch.dvbern.stip.api.demo.entity;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntityBuilders;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.common.entity.AbstractPersonBuilders;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.entity.ElternBuilders;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.geschwister.entity.GeschwisterBuilders;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.kind.entity.KindBuilders;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.partner.entity.PartnerBuilders;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildungBuilders;

public class DemoPerson {
    private static void applyBaseValues(
        final AbstractPerson person,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        final var basePerson = abstractPersonBuilders.build();
        person.setVorname(basePerson.getVorname());
        person.setNachname(basePerson.getNachname());
        person.setGeburtsdatum(basePerson.getGeburtsdatum());
    }

    private static void applyBaseValues(
        final AbstractFamilieEntity person,
        final AbstractFamilieEntityBuilders.Optionals abstractFamilieEntityBuilders,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        applyBaseValues(person, abstractPersonBuilders);
        final var baseFamilieEntity = abstractFamilieEntityBuilders.build();
        person.setWohnsitz(baseFamilieEntity.getWohnsitz());
        person.setWohnsitzAnteilMutter(baseFamilieEntity.getWohnsitzAnteilMutter());
        person.setWohnsitzAnteilVater(baseFamilieEntity.getWohnsitzAnteilVater());
    }

    public static PersonInAusbildung createPersonInAusbildung(
        final PersonInAusbildungBuilders.Optionals personInAusbildungBuilders,
        final AbstractFamilieEntityBuilders.Optionals abstractFamilieEntityBuilders,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        final var personInAusbildung = personInAusbildungBuilders.build();

        applyBaseValues(personInAusbildung, abstractFamilieEntityBuilders, abstractPersonBuilders);

        return personInAusbildung;
    }

    public static Geschwister createGeschwister(
        final GeschwisterBuilders.Optionals geschwisterBuilder,
        final AbstractFamilieEntityBuilders.Optionals abstractFamilieEntityBuilders,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        final var geschwister = geschwisterBuilder.build();

        applyBaseValues(geschwister, abstractFamilieEntityBuilders, abstractPersonBuilders);

        return geschwister;
    }

    // Eltern Kind Partner

    public static Eltern createEltern(
        final ElternBuilders.Optionals elternBuilders,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        final var eltern = elternBuilders.build();

        applyBaseValues(eltern, abstractPersonBuilders);

        return eltern;
    }

    public static Kind createKind(
        final KindBuilders.Optionals kindBuilders,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        final var kind = kindBuilders.build();

        applyBaseValues(kind, abstractPersonBuilders);

        return kind;
    }

    public static Partner createPartner(
        final PartnerBuilders.Optionals partnerBuilders,
        final AbstractPersonBuilders.Optionals abstractPersonBuilders
    ) {
        final var partner = partnerBuilders.build();

        applyBaseValues(partner, abstractPersonBuilders);

        return partner;
    }
}
