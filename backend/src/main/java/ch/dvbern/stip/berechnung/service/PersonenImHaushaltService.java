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

package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.util.HashMap;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltRequestBuilder;
import ch.dvbern.stip.berechnung.dto.PersonenImHaushaltResult;
import ch.dvbern.stip.berechnung.util.DmnRequestContextUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PersonenImHaushaltService {
    private static final String PERSONEN_IM_HAUSHALT_MODEL_NAME = "PersonenImHaushalt";
    private static final String PERSONEN_IM_HAUSHALT_DECISION_NAME = "personenImHaushaltOutput";

    private final Instance<PersonenImHaushaltRequestBuilder> personenImHaushaltRequestBuilders;
    private final DMNService dmnService;
    private final TenantService tenantService;

    public DmnRequest getPersonenImHaushaltRequest(
        final int majorVersion,
        final int minorVersion,
        final GesuchFormular gesuchFormular,
        final ElternTyp elternToPrioritize
    ) {
        final var builder = personenImHaushaltRequestBuilders.stream().filter(personenImHaushaltRequestBuilder -> {
            final var versionAnnotation =
                personenImHaushaltRequestBuilder.getClass().getAnnotation(DmnModelVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == majorVersion) &&
            (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (builder.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a builder for version "
                + majorVersion
                + '.'
                + minorVersion
            );
        }

        return builder.get().buildRequest(gesuchFormular, elternToPrioritize);
    }

    public PersonenImHaushaltResult calculatePersonenImHaushalt(final DmnRequest request) {
        final var models = dmnService.loadModelsForTenantAndVersionByName(
            tenantService.getCurrentTenantIdentifier(),
            request.getVersion(),
            PERSONEN_IM_HAUSHALT_MODEL_NAME
        );

        final var result = dmnService.evaluateModel(models, DmnRequestContextUtil.toContext(request));

        @SuppressWarnings("unchecked") // It's fine
        final var personenImHaushaltOutput = (HashMap<String, BigDecimal>) result
            .getDecisionResultByName(PERSONEN_IM_HAUSHALT_DECISION_NAME)
            .getResult();
        if (personenImHaushaltOutput == null) {
            throw new AppErrorException("Result of PersonenImHaushalt decision was null");
        }

        return PersonenImHaushaltResult.builder()
            .noBudgetsRequired(personenImHaushaltOutput.get("noBudgetsRequired").intValue())
            .kinderImHaushalt1(personenImHaushaltOutput.get("kinderImHaushalt1").intValue())
            .kinderImHaushalt2(personenImHaushaltOutput.get("kinderImHaushalt2").intValue())
            .personenImHaushalt1(personenImHaushaltOutput.get("personenImHaushalt1").intValue())
            .personenImHaushalt2(personenImHaushaltOutput.get("personenImHaushalt2").intValue())
            .decisionResults(result.getDecisionResults())
            .build();
    }
}
