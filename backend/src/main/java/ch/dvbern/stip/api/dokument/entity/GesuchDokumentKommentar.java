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

package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;

@Audited
@Entity
@Table(
    name = "gesuch_dokument_kommentar",
    indexes = {
        @Index(name = "IX_gesuch_dokument_kommentar_gesuch_id", columnList = "gesuch_tranche_id"),
        @Index(name = "IX_gesuch_dokument_kommentar_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class GesuchDokumentKommentar extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(
        name = "gesuch_tranche_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_kommentar_gesuch_tranche_id")
    )
    private GesuchTranche gesuchTranche;

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
        name = "gesuch_dokument_id", foreignKey = @ForeignKey(name = "FK_gesuch_dokument_id"),
        nullable = false
    )
    private GesuchDokument gesuchDokument;

    @NotNull
    @Column(name = "dokumentstatus")
    @Enumerated(EnumType.STRING)
    private Dokumentstatus dokumentstatus;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @Column(name = "kommentar", nullable = true, length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String kommentar;
}
