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

package ch.dvbern.stip.api.gesuchtranche.util;

import java.util.Optional;
import java.util.function.Function;

import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.util.GesuchTrancheCopyUtil.ElternAdressen;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchTrancheOverrideUtil {
    public void overrideGesuchFormular(final GesuchFormular toBeReplaced, final GesuchFormular replacement) {
        // PiA und PiA Adresse
        toBeReplaced.setPersonInAusbildung(
            PersonInAusbildungCopyUtil.createCopyIgnoreReferences(replacement.getPersonInAusbildung())
        );
        final var piaAdresseCopy = AdresseCopyUtil.createCopy(replacement.getPersonInAusbildung().getAdresse());
        toBeReplaced.getPersonInAusbildung().setAdresse(piaAdresseCopy);

        // Familiensituation
        FamiliensituationCopyUtil.overrideItem(replacement, toBeReplaced);

        // Partner und Partner Adresse
        toBeReplaced.setPartner(PartnerCopyUtil.createCopyIgnoreReferences(replacement.getPartner()));
        if (toBeReplaced.getPartner() != null) {
            toBeReplaced.getPartner().setAdresse(AdresseCopyUtil.createCopy(replacement.getPartner().getAdresse()));
        }

        // Eltern
        ElternCopyUtil.doOverrideOfSet(toBeReplaced.getElterns(), replacement.getElterns());
        final Function<ElternTyp, Optional<Eltern>> findOriginalElternOfTyp =
            typ -> replacement.getElterns().stream().filter(eltern -> eltern.getElternTyp() == typ).findFirst();

        final var elternAdressen = new ElternAdressen();
        for (final var eltern : toBeReplaced.getElterns()) {
            // get here is ok, as we already reset the set, it now only contains the original
            final var original = findOriginalElternOfTyp.apply(eltern.getElternTyp()).get();
            final var resetAdresse = AdresseCopyUtil.createCopy(original.getAdresse());
            elternAdressen.setForTyp(original.getElternTyp(), resetAdresse);

            eltern.setAdresse(resetAdresse);
        }

        // Auszahlung
        if (replacement.getAuszahlung() != null) {
            toBeReplaced.setAuszahlung(AuszahlungCopyUtil.createCopyIgnoreReferences(replacement.getAuszahlung()));
            final var auszahlungAdresseCopy = switch (replacement.getAuszahlung().getKontoinhaber()) {
                case GESUCHSTELLER -> piaAdresseCopy;
                case MUTTER -> elternAdressen.getForTyp(ElternTyp.MUTTER);
                case VATER -> elternAdressen.getForTyp(ElternTyp.VATER);
                default -> AdresseCopyUtil.createCopy(replacement.getAuszahlung().getAdresse());
            };
            toBeReplaced.getAuszahlung().setAdresse(auszahlungAdresseCopy);
        }

        // Einnahmen Kosten
        toBeReplaced.setEinnahmenKosten(EinnahmenKostenCopyUtil.createCopy(replacement.getEinnahmenKosten()));

        // Lebenslauf
        LebenslaufItemCopyUtil.doOverrideOfSet(toBeReplaced.getLebenslaufItems(), replacement.getLebenslaufItems());

        // Geschwister
        GeschwisterCopyUtil.doOverrideOfSet(toBeReplaced.getGeschwisters(), replacement.getGeschwisters());

        // Kinds
        KindCopyUtil.doOverrideOfSet(toBeReplaced.getKinds(), replacement.getKinds());

        // Steuerdaten - are omitted at the moment todo KSTIP-1850 ...
        // toBeReplaced.setSteuerdaten(SteuerdatenCopyUtil.createCopySet(replacement.getSteuerdaten()));

    }
}
