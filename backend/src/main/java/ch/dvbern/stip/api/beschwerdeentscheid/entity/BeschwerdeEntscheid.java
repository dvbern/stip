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

package ch.dvbern.stip.api.beschwerdeentscheid.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;

@Entity
@Table(
    name = "beschwerde_entscheid",
    indexes = {
        @Index(name = "IX_beschwerde_entscheid_mandant", columnList = "mandant")
    }
)
@Audited
@Getter
@Setter
public class BeschwerdeEntscheid extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_beschwerde_entscheid__gesuch_id"), nullable = false
    )
    private Gesuch gesuch;

    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @NotNull
    @Column(name = "kommentar", nullable = false, length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String kommentar;

    @NotNull
    @Column(name = "is_beschwerde_erfolgreich", nullable = false)
    private Boolean isBeschwerdeErfolgreich;

    @OneToMany
    @JoinTable(
        name = "beschwerde_entscheid_dokument",
        joinColumns = @JoinColumn(
            name = "beschwerde_entscheid_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_beschwerde_entscheid_dokumente")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "dokument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_dokument_beschwerde_entscheid")
        ),
        indexes = {
            @Index(
                name = "beschwerde_entscheid_dokument_beschwerde_entscheid_id", columnList = "beschwerde_entscheid_id"
            ),
            @Index(name = "beschwerde_entscheid_dokument_dokument_id", columnList = "dokument_id")
        }
    )
    private List<Dokument> dokumente = new ArrayList<>();

}
