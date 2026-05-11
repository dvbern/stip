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

package ch.dvbern.stip.api.gesuch.service;

import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.Statisticsdata;
import ch.dvbern.stip.api.gesuch.repo.StatisticsdataRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class StatisticsdataService {
    public final StatisticsdataRepository statisticsdataRepository;
    public final GesuchService gesuchService;

    public void setOrCreateGemeindeStatisticsDataOfGesuch(
        final UUID gesuchId,
        final Integer com_fosnr,
        final String com_name
    ) {
        final Gesuch gesuch = gesuchService.getGesuchById(gesuchId);
        Statisticsdata statisticsdata = gesuch.getStatisticsdata();
        if (Objects.isNull(statisticsdata)) {
            statisticsdata = new Statisticsdata();
            statisticsdata
                .setGesuch(gesuch);
        }
        statisticsdata
            .setGemeindeBfsNr(com_fosnr)
            .setGemeindeName(com_name);
        gesuch.setStatisticsdata(statisticsdata);
        statisticsdataRepository.persist(statisticsdata);
    }

    public Statisticsdata getStatisticsdataOfGesuch(final UUID gesuchId) {
        return statisticsdataRepository.findByGesuchId(gesuchId);
    }

    public void deleteForGesuch(final UUID gesuchId) {
        final Gesuch gesuch = gesuchService.getGesuchById(gesuchId);
        if (Objects.isNull(gesuch.getStatisticsdata())) {
            return;
        }
        statisticsdataRepository.delete(gesuch.getStatisticsdata());
        gesuch.setStatisticsdata(null);
    }

}
