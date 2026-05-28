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

package ch.dvbern.stip.api.statistik.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.StatistikAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import ch.dvbern.stip.api.statistik.service.StatistikService;
import ch.dvbern.stip.generated.api.StatistikResource;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.StatistikDto;
import io.smallrye.common.annotation.Blocking;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;

import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@Validated
@RequestScoped
@RequiredArgsConstructor
public class StatistikResourceImpl implements StatistikResource {
    private final StatistikAuthorizer statistikAuthorizer;
    private final StatistikService statistikService;
    private final GesuchsjahrService gesuchsjahrService;

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public void createStatistikJob(Integer year) {
        statistikAuthorizer.canStatistik();
        statistikService.createStatistikJob(year);
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public List<Integer> getAllStatistikYears() {
        statistikAuthorizer.canStatistik();
        return gesuchsjahrService.getGesuchsjahreIntList();
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public List<StatistikDto> getAllStatistiks() {
        statistikAuthorizer.canStatistik();
        return statistikService.getAllStatistiks();
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public FileDownloadTokenDto getStatistikDownloadToken(UUID statistikId) {
        statistikAuthorizer.canStatistik();
        return statistikService.getStatistikDownloadToken(statistikId);
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> getStatistikDownload(String token) {
        return statistikService.getStatistikDownload(token);
    }
}
