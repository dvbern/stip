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

package ch.dvbern.stip.api.notiz.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.GesuchNotizAntwortConstraint;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@GesuchNotizAntwortConstraint
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "gesuch_notizen",
    indexes = {
        @Index(name = "IX_gesuch_notiz_mandant", columnList = "mandant"),
        @Index(name = "IX_gesuch_notiz_gesuch_id", columnList = "gesuch_id")
    }
)
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GesuchNotiz extends AbstractMandantEntity {
    @ManyToOne
    @JoinColumn(
        name = "gesuch_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_notiz_gesuch_id")
    )
    private Gesuch gesuch;

    @Column(name = "betreff")
    @Size(max = DB_DEFAULT_MAX_LENGTH)

    private String betreff;

    @Column(name = "text")
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    private String text;

    @NotNull
    @Column(name = "notiz_typ", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotizTyp notizTyp;

    @Column(name = "antwort")
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    private String antwort;
}
