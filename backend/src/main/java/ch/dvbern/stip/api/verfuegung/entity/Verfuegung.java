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

package ch.dvbern.stip.api.verfuegung.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.StipDecision;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.stipdecision.type.Kanton;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(
    name = "verfuegung",
    indexes = {
        @Index(name = "IX_verfuegung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Verfuegung extends AbstractMandantEntity {

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filename")
    private String filename;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filepath")
    private String filepath;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "filesize")
    private String filesize;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "object_id")
    private String objectId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stip_decision", nullable = false)
    private StipDecision stipDecision;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_id", nullable = false)
    private Gesuch gesuch;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz_kanton")
    private Kanton kanton;
}
