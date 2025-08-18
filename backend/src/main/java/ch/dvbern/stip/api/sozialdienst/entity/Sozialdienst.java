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

package ch.dvbern.stip.api.sozialdienst.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Getter
@Setter
@Entity
@Table(
    name = "sozialdienst",
    indexes = @Index(name = "IX_sozialdienst_mandant", columnList = "mandant")
)
public class Sozialdienst extends AbstractMandantEntity {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "name", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String name;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "sozialdienst_admin_id", foreignKey = @ForeignKey(name = "FK_sozialdienst_sozialdienst_admin_id")
    )
    private SozialdienstBenutzer sozialdienstAdmin;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sozialdienst_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "FK_sozialdienst_benutzer_id")
    )
    private List<SozialdienstBenutzer> sozialdienstBenutzers = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "sozialdienst")
    private List<Delegierung> delegierungen = new ArrayList<>();

    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zahlungsverbindung_id", foreignKey = @ForeignKey(name = "sozialdienst_zahlungsverbindung_id"))
    private Zahlungsverbindung zahlungsverbindung;

    @NotNull
    @Column(name = "aktiv", nullable = false)
    private boolean aktiv = true;

    @Transient
    public boolean isBenutzerAdmin(final SozialdienstBenutzer sozialdienstBenutzer) {
        return Objects.equals(getSozialdienstAdmin().getId(), sozialdienstBenutzer.getId());
    }
}
