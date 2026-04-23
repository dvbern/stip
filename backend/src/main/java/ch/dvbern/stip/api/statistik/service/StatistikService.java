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

package ch.dvbern.stip.api.statistik.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.statistik.repo.StatistikRepository;
import ch.dvbern.stip.api.statistik.util.StatistikConstants;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.StatistikDto;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class StatistikService {
    private final Scheduler scheduler;
    private final StatistikRepository statistikRepository;
    private final StatistikMapper statistikMapper;
    private final DokumentDownloadService dokumentDownloadService;
    private final BenutzerService benutzerService;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final S3AsyncClient s3AsyncClient;

    public void createStatistikJob(final int year) {
        final JobDetail jobDetail = JobBuilder.newJob(StatistikCSVJob.class)
            .withIdentity(
                StatistikConstants.STATISTIK_JOB_PREFIX + year + "-" + System.currentTimeMillis(),
                "statistik"
            )
            .usingJobData(StatistikConstants.STATISTIK_JOB_YEAR_KEY, year)
            .build();

        final Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(
                StatistikConstants.STATISTIK_JOB_PREFIX + "trigger-" + year + "-" + System.currentTimeMillis(),
                "statistik"
            )
            .startNow()
            .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("Could not schedule statistik job", e);
        }
    }

    public List<StatistikDto> getAllStatistiks() {
        final var statistiks = statistikRepository.findAll();
        return statistiks.stream()
            .map(statistikMapper::fromEntity)
            .toList();
    }

    public RestMulti<Buffer> getStatistikDownload(String token) {
        final var statistikId = dokumentDownloadService.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            StatistikConstants.STATISTIK_FILE_DOWNLOAD_TOKEN_CLAIM_ID
        );

        final var statistik = statistikRepository.requireById(statistikId);

        return dokumentDownloadService.getDokument(
            s3AsyncClient,
            configService.getBucketName(),
            statistik.getObjectId(),
            statistik.getFilepath(),
            statistik.getFilename()
        );
    }

    public FileDownloadTokenDto getStatistikDownloadToken(UUID statistikId) {
        return dokumentDownloadService.getFileDownloadToken(
            statistikId,
            StatistikConstants.STATISTIK_FILE_DOWNLOAD_TOKEN_CLAIM_ID,
            benutzerService,
            configService
        );
    }
}
