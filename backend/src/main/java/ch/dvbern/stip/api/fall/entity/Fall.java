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

package ch.dvbern.stip.api.fall.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;

@Audited
@Entity
@Table(
    name = "fall",
    indexes = {
        @Index(name = "IX_fall_gesuchsteller_id", columnList = "gesuchsteller_id"),
        @Index(name = "IX_fall_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Fall extends AbstractMandantEntity {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "fall_nummer", nullable = false, updatable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String fallNummer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gesuchsteller_id",
        foreignKey = @ForeignKey(name = "FK_fall_gesuchsteller_id"),
        nullable = false
    )
    private Benutzer gesuchsteller;

    @Nullable
    @JoinColumn(
        name = "sachbearbeiter_zuordnung_id",
        foreignKey = @ForeignKey(name = "FK_fall_sachbearbeiter_id")
    )
    @OneToOne(mappedBy = "fall", fetch = FetchType.EAGER)
    private Zuordnung sachbearbeiterZuordnung;

    @OneToMany(mappedBy = "fall", fetch = FetchType.LAZY)
    private Set<Ausbildung> ausbildungs = new HashSet<>();

    @OneToMany(mappedBy = "fall", fetch = FetchType.LAZY)
    private List<Buchhaltung> buchhaltungs = new ArrayList<>();

    @Nullable
    @OneToOne(mappedBy = "delegierterFall")
    private Delegierung delegierung;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "auszahlung_id", foreignKey = @ForeignKey(name = "FK_fall_auszahlung_id"))
    private @Valid Auszahlung auszahlung;

    public Zahlungsverbindung getRelevantZahlungsverbindung() {
        if (auszahlung.isAuszahlungAnSozialdienst()) {
            return delegierung.getSozialdienst().getZahlungsverbindung();
        }
        return auszahlung.getZahlungsverbindung();
    }
}
