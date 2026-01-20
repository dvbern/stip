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

package ch.dvbern.stip.api.darlehen.entity;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.darlehen.type.DarlehenGrund;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.validation.FreiwilligDarlehenEinreichenValidationGroup;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;

@Entity
@Audited
@FreiwilligDarlehenDocumentsRequiredConstraint(
    groups = { FreiwilligDarlehenEinreichenValidationGroup.class }
)
@Table(
    name = "freiwillig_darlehen",
    indexes = {
        @Index(name = "IX_freiwillig_darlehen_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class FreiwilligDarlehen extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "fall_id", nullable = false)
    private Fall fall;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuch_id", nullable = false)
    private Gesuch relatedGesuch;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DarlehenStatus status = DarlehenStatus.IN_BEARBEITUNG_GS;

    @Nullable
    @Column(name = "gewaehren")
    private Boolean gewaehren;

    @Nullable
    @Min(value = 0)
    @Column(name = "betrag")
    private Integer betrag;

    @Nullable
    @Size(max = DB_DEFAULT_STRING_MAX_LENGTH)
    @Column(name = "kommentar", length = DB_DEFAULT_STRING_MAX_LENGTH)
    private String kommentar;

    @Nullable
    @NotNull(groups = { FreiwilligDarlehenEinreichenValidationGroup.class })
    @Min(value = 0)
    @Column(name = "betrag_gewuenscht")
    private Integer betragGewuenscht;

    @Min(value = 0)
    @Nullable
    @NotNull(groups = { FreiwilligDarlehenEinreichenValidationGroup.class })
    @Column(name = "schulden")
    private Integer schulden;

    @Min(value = 0)
    @Nullable
    @NotNull(groups = { FreiwilligDarlehenEinreichenValidationGroup.class })
    @Column(name = "anzahl_betreibungen")
    private Integer anzahlBetreibungen;

    @Size(min = 1, groups = { FreiwilligDarlehenEinreichenValidationGroup.class })
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "freiwillig_darlehen_grund")
    @CollectionTable(
        name = "freiwillig_darlehen_darlehen_grund", joinColumns = @JoinColumn(name = "freiwillig_darlehen_id")
    )
    private Set<DarlehenGrund> gruende = new LinkedHashSet<>();

    @OneToMany(mappedBy = "freiwilligDarlehen", orphanRemoval = true)
    private Set<FreiwilligDarlehenDokument> dokumente = new LinkedHashSet<>();

    @Nullable
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(
        name = "freiwillig_darlehen_verfuegung_id",
        foreignKey = @ForeignKey(name = "FK_darlehen_verfuegung_dokument_id"),
        nullable = true
    )
    private Dokument darlehenVerfuegung;

    @Nullable
    @Column(name = "eingabedatum")
    private LocalDate eingabedatum;

    @Transient
    public String getDarlehenNr() {
        // remove everything except numbers from fallNr.
        // and fill missing spots with zeros (total length of 8 required)
        final var fallNummer = this.getFall().getFallNummer();
        final int darlehenNo = Integer.parseInt(fallNummer.substring(fallNummer.lastIndexOf('.') + 1));

        return String.format("%08d", darlehenNo);
    }

}
