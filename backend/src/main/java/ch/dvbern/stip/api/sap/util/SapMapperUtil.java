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

package ch.dvbern.stip.api.sap.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Comparator;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.sap.generated.general.SenderParms;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SapMapperUtil {
    private static int EXT_ID_UNIQUE_ID_NUM_DIGITS = 4;

    public PersonInAusbildung getPia(
        Fall fall
    ) {
        final var gesuch = fall.getAusbildungs()
            .stream()
            .max(Comparator.comparing(Ausbildung::getTimestampErstellt))
            .orElseThrow()
            .getGesuchs()
            .stream()
            .max(
                Comparator.comparing(
                    Gesuch::getTimestampErstellt
                )
            )
            .orElseThrow();
        final var gesuchTranche =
            gesuch.getGesuchTrancheValidOnDate(LocalDate.now()).orElse(gesuch.getLatestGesuchTranche());
        return gesuchTranche.getGesuchFormular().getPersonInAusbildung();
    }

    public String getExtId(Fall fall) {
        return String.format(
            "%s.%d",
            fall.getFallNummer(),
            Math.abs(fall.getId().getMostSignificantBits()) % Math.round(Math.pow(10, EXT_ID_UNIQUE_ID_NUM_DIGITS))
        );
    }

    public String getAhvNr(Fall fall) {
        final var ahv = getPia(fall).getSozialversicherungsnummer();

        // Remove the dots, as the SAP Endpoint won't accept it otherwise
        return ahv.replace(".", "");
    }

    public String getAccountHolder(Zahlungsverbindung zahlungsverbindung) {
        // TODO KSTIP-2927: Change this here
        final var accountHolder = String.format(
            "%s %s",
            zahlungsverbindung.getVorname(),
            zahlungsverbindung.getNachname()
        );

        // Truncate to a max length of 60, as the ACCOUNTHOLDER property in SAP is limited to that
        final var end = Math.min(accountHolder.length(), 60);
        return accountHolder.substring(0, end);
    }

    public ch.dvbern.stip.api.sap.generated.business_partner.SenderParmsDelivery getBusinessPartnerSenderParmsDelivery(
        BigInteger sysid,
        BigDecimal deliveryid
    ) {
        final ch.dvbern.stip.api.sap.generated.business_partner.SenderParmsDelivery sender =
            new ch.dvbern.stip.api.sap.generated.business_partner.SenderParmsDelivery();
        sender.setSYSID(sysid);
        sender.setDELIVERYID(deliveryid);
        return sender;
    }

    public SenderParms getImportStatusReadSenderParms(
        BigInteger sysid
    ) {
        final SenderParms sender = new SenderParms();
        sender.setSYSID(sysid);
        return sender;
    }
}
