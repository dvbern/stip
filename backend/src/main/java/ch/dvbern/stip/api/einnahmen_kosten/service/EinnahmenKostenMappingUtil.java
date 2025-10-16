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

package ch.dvbern.stip.api.einnahmen_kosten.service;

import java.util.Objects;

import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EinnahmenKostenMappingUtil {
    public Integer calculateVermoegen(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return null;
        }
        Integer vermoegen = gesuchFormular.getEinnahmenKosten().getVermoegen();
        if (GesuchFormularCalculationUtil.wasGSOlderThan18(gesuchFormular)) {
            return vermoegen;
        }
        return null;
    }

    public int calculateSteuern(final EinnahmenKosten einnahmenKosten, final boolean isQuellenbesteuert) {
        if (
            Objects.isNull(einnahmenKosten)
            || Objects.isNull(einnahmenKosten.getNettoerwerbseinkommen())
            || isQuellenbesteuert
        ) {
            return 0;
        }

        final int einkommen = einnahmenKosten.getNettoerwerbseinkommen();
        return calculateSteuern(einkommen);
    }

    public boolean isQuellenBesteuert(final PersonInAusbildung personInAusbildung) {
        final var isNotCh =
            !personInAusbildung.getNationalitaet().getLaendercodeBfs().equals(WellKnownLand.CHE.getLaendercodeBfs());
        final var hasNoAusweisC = Objects.isNull(personInAusbildung.getNiederlassungsstatus())
        || !personInAusbildung.getNiederlassungsstatus().equals(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);
        return isNotCh && hasNoAusweisC;
    }

    private int calculateSteuern(final int einkommen) {
        if (einkommen < Constants.CH_STEUERN_EINKOMMEN_LIMIT) {
            return 0;
        }
        return (int) (einkommen * Constants.CH_STEUERN_PERCENTAGE);
    }
}
