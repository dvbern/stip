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

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.IbanConstraint;
import ch.dvbern.stip.api.sozialdienstadmin.entity.SozialdienstAdmin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MID_LENGTH;

@Getter
@Setter
@Entity
@Table(
    name = "sozialdienst",
    indexes = @Index(name = "IX_sozialdienst_mandant", columnList = "mandant")
)
public class Sozialdienst extends AbstractMandantEntity {
    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(max = DB_DEFAULT_MID_LENGTH)
    @Column(name = "iban", nullable = false)
    @IbanConstraint
    private String iban;

    @NotNull
    @OneToOne(optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_sozialdienst_adresse_id"))
    private Adresse adresse;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "sozialdienst_admin_id", foreignKey = @ForeignKey(name = "FK_sozialdienst_sozialdienst_admin_id")
    )
    private SozialdienstAdmin sozialdienstAdmin;
}
