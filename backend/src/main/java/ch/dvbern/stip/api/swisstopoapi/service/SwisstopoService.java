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

package ch.dvbern.stip.api.swisstopoapi.service;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.Statisticsdata;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.swisstopoapi.entity.SwisstopoApiFindAddrResponse.SwisstopoApiFindAddrResponseElement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jose4j.json.internal.json_simple.JSONObject;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class SwisstopoService {
    private static final String ADDR_NO_SEARCH_LAYER_DEF_KEY = "ch.swisstopo.amtliches-gebaeudeadressverzeichnis";
    private static final String ADDR_NO_SEARCH_LAYER_DEF_SEARCH_STR = "adr_number ilike '%s'";

    @Inject
    @RestClient
    SwisstopoApiRestService swisstopoApiRestService;

    public void getGemeindeDataOfGesuch(final Gesuch gesuch) {
        final GesuchTranche trancheToUse = gesuch.getLatestGesuchTranche();
        final Adresse adresse = trancheToUse.getGesuchFormular().getPersonInAusbildung().getAdresse();
        final var buildingNoSearchPartJson = new JSONObject();
        buildingNoSearchPartJson.put(
            ADDR_NO_SEARCH_LAYER_DEF_KEY,
            String.format(ADDR_NO_SEARCH_LAYER_DEF_SEARCH_STR, adresse.getHausnummer())
        );
        try {
            final var result =
                swisstopoApiRestService.findAllMatchingBuildings(
                    adresse.getStrasse(),
                    buildingNoSearchPartJson.toString()
                );
            result.getResults()
                .stream()
                .map(SwisstopoApiFindAddrResponseElement::getAttributes)
                .filter(
                    swisstopoApiFindAddrResponseElementAttributes -> swisstopoApiFindAddrResponseElementAttributes
                        .getZip_label()
                        .contains(adresse.getPlz())
                    || swisstopoApiFindAddrResponseElementAttributes.getZip_label().contains(adresse.getOrt())
                )
                .findFirst()
                .ifPresentOrElse(
                    swisstopoApiFindAddrResponseElementAttributes -> {
                        gesuch.setStatisticsdata(new Statisticsdata());
                        gesuch.getStatisticsdata()
                            .setGemeindeBfsNr(swisstopoApiFindAddrResponseElementAttributes.getCom_fosnr());
                        gesuch.getStatisticsdata()
                            .setGemeindeName(swisstopoApiFindAddrResponseElementAttributes.getCom_name());
                    },
                    () -> LOG.error(
                        String.format(
                            "Could not find Building in Swisstopo data with properties, Street: %s, No: %s",
                            adresse.getStrasse(),
                            adresse.getHausnummer()
                        )
                    )
                );
        } catch (Exception e) {
            LOG.error(
                String.format(
                    "Could not perform Building lookup in Swisstopo data with properties, Street: %s, No: %s",
                    adresse.getStrasse(),
                    adresse.getHausnummer()
                ),
                e
            );
        }
    }
}
