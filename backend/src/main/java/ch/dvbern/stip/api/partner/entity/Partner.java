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

package ch.dvbern.stip.api.partner.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraint(groups = { GesuchEinreichenValidationGroup.class })
@AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraint
@Entity
@Table(
    name = "partner",
    indexes = {
        @Index(name = "IX_partner_adresse_id", columnList = "adresse_id"),
        @Index(name = "IX_partner_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Partner extends AbstractPerson {
    @NotNull
    @OneToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_partner_adresse_id"))
    private Adresse adresse;

    @NotNull
    @AhvConstraint
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "sozialversicherungsnummer", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(name = "ausbildung_mit_einkommen_oder_erwerbstaetig", nullable = false)
    private boolean ausbildungMitEinkommenOderErwerbstaetig = false;

    @Nullable
    @Column(name = "jahreseinkommen")
    private Integer jahreseinkommen;

    @Nullable
    @Column(name = "verpflegungskosten")
    private Integer verpflegungskosten;

    @Nullable
    @Column(name = "fahrkosten")
    private Integer fahrkosten;
}
