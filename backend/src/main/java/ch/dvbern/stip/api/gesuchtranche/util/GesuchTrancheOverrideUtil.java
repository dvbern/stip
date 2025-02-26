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

import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
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
    public void overrideGesuchFormular(final GesuchFormular target, final GesuchFormular source) {
        // PiA und PiA Adresse
        PersonInAusbildungCopyUtil.copyValues(source.getPersonInAusbildung(), target.getPersonInAusbildung());

        AdresseCopyUtil.copyValues(
            source.getPersonInAusbildung().getAdresse(),
            target.getPersonInAusbildung().getAdresse()
        );

        // Familiensituation
        FamiliensituationCopyUtil.copyValues(source.getFamiliensituation(), target.getFamiliensituation());

        // Partner und Partner Adresse
        if (source.getPartner() == null) {
            target.setPartner(null);
        } else {
            PartnerCopyUtil.copyValuesIgnoringReferences(source.getPartner(), target.getPartner());
            AdresseCopyUtil.copyValues(source.getPartner().getAdresse(), target.getPartner().getAdresse());
        }

        // Eltern
        ElternCopyUtil.doOverrideOfSet(target.getElterns(), source.getElterns());

        final var elternAdressen = new ElternAdressen();
        for (final var eltern : target.getElterns()) {
            // get here is ok, as we already reset the set, it now only contains the original
            final var original = source.getElternteilOfTyp(eltern.getElternTyp()).get();
            final var resetAdresse = AdresseCopyUtil.createCopy(original.getAdresse());
            elternAdressen.setForTyp(original.getElternTyp(), resetAdresse);

            eltern.setAdresse(resetAdresse);
        }

        // Auszahlung
        AuszahlungCopyUtil.copyValues(source.getAuszahlung(), target.getAuszahlung());
        final var auszahlungAdresseCopy = switch (source.getAuszahlung().getKontoinhaber()) {
            case GESUCHSTELLER -> source.getAuszahlung().getAdresse();
            case MUTTER -> elternAdressen.getForTyp(ElternTyp.MUTTER);
            case VATER -> elternAdressen.getForTyp(ElternTyp.VATER);
            default -> AdresseCopyUtil.createCopy(source.getAuszahlung().getAdresse());
        };

        target.getAuszahlung().setAdresse(auszahlungAdresseCopy);

        // Einnahmen Kosten
        EinnahmenKostenCopyUtil.copyValues(source.getEinnahmenKosten(), target.getEinnahmenKosten());

        // Lebenslauf
        LebenslaufItemCopyUtil.doOverrideOfSet(target.getLebenslaufItems(), source.getLebenslaufItems());

        // Geschwister
        GeschwisterCopyUtil.doOverrideOfSet(target.getGeschwisters(), source.getGeschwisters());

        // Kinds
        KindCopyUtil.doOverrideOfSet(target.getKinds(), source.getKinds());

        // Steuerdaten - are omitted at the moment todo KSTIP-1850 ...
        // toBeReplaced.setSteuerdaten(SteuerdatenCopyUtil.createCopySet(replacement.getSteuerdaten()));

        // TODO KSTIP-1998: Reset the Dokumente as well
    }
}
