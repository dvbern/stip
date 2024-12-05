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

package ch.dvbern.stip.stipdecision.service;

import java.util.List;

import ch.dvbern.stip.api.common.type.StipDeciderResult;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.dto.StipDecisionTextDto;
import ch.dvbern.stip.stipdecision.decider.BaseStipDecider;
import ch.dvbern.stip.stipdecision.decider.StipDeciderTenant;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class StipDecisionService {
    private final TenantService tenantService;
    private final Instance<BaseStipDecider> stipDeciders;
    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final StipDecisionTextMapper stipDecisionTextMapper;

    private BaseStipDecider getDeciderForTenantId(final String tenantId) {
        final var decider = stipDeciders.stream().filter(stipDecider -> {
            final var tenantAnnotation = stipDecider.getClass().getAnnotation(StipDeciderTenant.class);
            return tenantAnnotation != null
            && tenantAnnotation.value().name().toLowerCase().equals(tenantId);
        }).findFirst();

        if (decider.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a StipDecider for tenant " + tenantId
            );
        }
        return decider.get();
    }

    public StipDeciderResult decide(GesuchTranche gesuchTranche) {
        final var decider = getDeciderForTenantId(tenantService.getCurrentTenant().getIdentifier());

        return decider.decide(gesuchTranche);
    }

    public String getTextForDecision(final StipDeciderResult decision) {
        return decision.name();
    }

    public GesuchStatusChangeEvent getGesuchStatusChangeEvent(final StipDeciderResult decision) {
        final var decider = getDeciderForTenantId(tenantService.getCurrentTenant().getIdentifier());

        return decider.getGesuchStatusChangeEvent(decision);
    }

    public List<StipDecisionTextDto> getAll() {
        return stipDecisionTextRepository.findAll().stream().map(stipDecisionTextMapper::toDto).toList();
    }
}
