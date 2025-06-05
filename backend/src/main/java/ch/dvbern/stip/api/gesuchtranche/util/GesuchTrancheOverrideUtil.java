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

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.darlehen.util.DarlehenCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.eltern.util.ElternCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.util.GesuchTrancheCopyUtil.ElternAdressen;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenCopyUtil;
import ch.dvbern.stip.api.steuererklaerung.util.SteuererklaerungCopyUtil;
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
            if (target.getPartner() == null) {
                target.setPartner(new Partner().setAdresse(new Adresse()));
            }

            PartnerCopyUtil.copyValuesIgnoringReferences(source.getPartner(), target.getPartner());
            AdresseCopyUtil.copyValues(source.getPartner().getAdresse(), target.getPartner().getAdresse());
        }

        // Eltern
        ElternCopyUtil.doOverrideOfSet(target.getElterns(), source.getElterns());
        final var elternAdressen = ElternAdressen.fromGesuchFormular(target);
        // todo KSTIP-2026: is elternAdressen still used?

        // Einnahmen Kosten
        EinnahmenKostenCopyUtil.copyValues(source.getEinnahmenKosten(), target.getEinnahmenKosten());

        // Lebenslauf
        LebenslaufItemCopyUtil.doOverrideOfSet(target.getLebenslaufItems(), source.getLebenslaufItems());

        // Geschwister
        GeschwisterCopyUtil.doOverrideOfSet(target.getGeschwisters(), source.getGeschwisters());

        // Kinds
        KindCopyUtil.doOverrideOfSet(target.getKinds(), source.getKinds());

        // Steuerdaten
        SteuerdatenCopyUtil.doOverrideOfSet(target.getSteuerdaten(), source.getSteuerdaten());

        // Steuererklaerung
        SteuererklaerungCopyUtil.doOverrideOfSet(target.getSteuererklaerung(), source.getSteuererklaerung());

        // Darlehen
        DarlehenCopyUtil.copyValues(source.getDarlehen(), target.getDarlehen());
    }
}
