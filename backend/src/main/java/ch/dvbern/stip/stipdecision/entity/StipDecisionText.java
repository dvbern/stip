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

package ch.dvbern.stip.stipdecision.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.common.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Table(name = "stip_decision_text")
@Audited
@Entity
public class StipDecisionText extends AbstractMandantEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private StipDecision stipDecision;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "text_de", nullable = false, length = Constants.DB_DEFAULT_STRING_MAX_LENGTH)
    private String textDe;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "text_fr", nullable = false, length = Constants.DB_DEFAULT_STRING_MAX_LENGTH)
    private String textFr;
}
