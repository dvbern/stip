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

import java.lang.reflect.InvocationTargetException;

import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchTrancheOverrideUtil {

    public void overrideGesuchFormular(GesuchFormular toBeReplaced, final GesuchFormular replacement)
    throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // PiA und PiA Adresse
        toBeReplaced.setPersonInAusbildung(
            PersonInAusbildungCopyUtil.createCopyIgnoreReferences(replacement.getPersonInAusbildung())
        );
        final var piaAdresseCopy = AdresseCopyUtil.createCopy(replacement.getPersonInAusbildung().getAdresse());
        toBeReplaced.getPersonInAusbildung().setAdresse(piaAdresseCopy);

        // Familiensituation
        FamiliensituationCopyUtil.overrideItem(replacement.getFamiliensituation(), toBeReplaced.getFamiliensituation());

        // Partner und Partner Adresse
        toBeReplaced.setPartner(PartnerCopyUtil.createCopyIgnoreReferences(replacement.getPartner()));
        if (toBeReplaced.getPartner() != null) {
            toBeReplaced.getPartner().setAdresse(AdresseCopyUtil.createCopy(replacement.getPartner().getAdresse()));
        }

        // Einnahmen Kosten
        toBeReplaced.setEinnahmenKosten(EinnahmenKostenCopyUtil.createCopy(replacement.getEinnahmenKosten()));

        // Lebenslauf
        LebenslaufItemCopyUtil.doOverrideOfSet(toBeReplaced.getLebenslaufItems(), replacement.getLebenslaufItems());

        // Geschwister
        GeschwisterCopyUtil.doOverrideOfSet(toBeReplaced.getGeschwisters(), replacement.getGeschwisters());

        // Kinds
        KindCopyUtil.doOverrideOfSet(toBeReplaced.getKinds(), replacement.getKinds());

        // Steuerdaten - are omitted at the moment...
        // toBeReplaced.setSteuerdaten(SteuerdatenCopyUtil.createCopySet(replacement.getSteuerdaten()));

    }
}
