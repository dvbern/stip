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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuchsperioden.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "gesuchsperiode",
    indexes = {
        @Index(name = "IX_gesuchsperiode_aufschaltdatum_gueltig_bis", columnList = "aufschaltdatum,gueltig_bis"),
        @Index(name = "IX_gesuchsperiode_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Gesuchsperiode extends AbstractMandantEntity {
    @NotNull
    @Column(nullable = false, name = "bezeichnung_de")
    private String bezeichnungDe;

    @NotNull
    @Column(nullable = false, name = "bezeichnung_fr")
    private String bezeichnungFr;

    @NotNull
    @Column(nullable = false, name = "fiskaljahr")
    private String fiskaljahr;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gesuchsjahr_id",
        foreignKey = @ForeignKey(name = "FK_gesuchsperiode_gesuchsjahr_id"),
        nullable = false
    )
    private @Valid Gesuchsjahr gesuchsjahr;

    @NotNull
    @Column(nullable = false, name = "gesuchsperiode_start")
    private LocalDate gesuchsperiodeStart;

    @NotNull
    @Column(nullable = false, name = "gesuchsperiode_stopp")
    private LocalDate gesuchsperiodeStopp;

    @NotNull
    @Column(nullable = false, name = "aufschalttermin_start")
    private LocalDate aufschaltterminStart;

    @NotNull
    @Column(nullable = false, name = "aufschalttermin_stopp")
    private LocalDate aufschaltterminStopp;

    @NotNull
    @Column(nullable = false, name = "einreichefrist_normal")
    private LocalDate einreichefristNormal;

    @NotNull
    @Column(nullable = false, name = "einreichefrist_reduziert")
    private LocalDate einreichefristReduziert;

    @NotNull
    @Column(nullable = false, name = "ausbkosten_sek_ii")
    private Integer ausbKostenSekII;

    @NotNull
    @Column(nullable = false, name = "ausbkosten_tertiaer")
    private Integer ausbKostenTertiaer;

    @NotNull
    @Column(nullable = false, name = "freibetrag_vermoegen")
    private Integer freibetragVermoegen;

    @NotNull
    @Column(nullable = false, name = "freibetrag_erwerbseinkommen")
    private Integer freibetragErwerbseinkommen;

    @NotNull
    @Column(nullable = false, name = "einkommensfreibetrag")
    private Integer einkommensfreibetrag;

    @NotNull
    @Column(nullable = false, name = "elternbeteiligungssatz")
    private Integer elternbeteiligungssatz;

    @NotNull
    @Column(nullable = false, name = "vermoegensfreibetrag")
    private Integer vermoegensfreibetrag;

    @NotNull
    @Column(nullable = false, name = "vermogen_satz_angerechnet")
    private Integer vermogenSatzAngerechnet;

    @NotNull
    @Column(nullable = false, name = "integrationszulage")
    private Integer integrationszulage;

    @NotNull
    @Column(nullable = false, name = "limite_ek_freibetrag_integrationszulag")
    private Integer limiteEkFreibetragIntegrationszulag;

    @NotNull
    @Column(nullable = false, name = "stip_limite_minimalstipendium")
    private Integer stipLimiteMinimalstipendium;

    @NotNull
    @Column(nullable = false, name = "person_1")
    private Integer person1;

    @NotNull
    @Column(nullable = false, name = "personen_2")
    private Integer personen2;

    @NotNull
    @Column(nullable = false, name = "personen_3")
    private Integer personen3;

    @NotNull
    @Column(nullable = false, name = "personen_4")
    private Integer personen4;

    @NotNull
    @Column(nullable = false, name = "personen_5")
    private Integer personen5;

    @NotNull
    @Column(nullable = false, name = "personen_6")
    private Integer personen6;

    @NotNull
    @Column(nullable = false, name = "personen_7")
    private Integer personen7;

    @NotNull
    @Column(nullable = false, name = "pro_weitere_person")
    private Integer proWeiterePerson;

    @NotNull
    @Column(nullable = false, name = "kinder_00_18")
    private Integer kinder0018;

    @NotNull
    @Column(nullable = false, name = "jugendliche_erwachsene_19_25")
    private Integer jugendlicheErwachsene1925;

    @NotNull
    @Column(nullable = false, name = "erwachsene_26_99")
    private Integer erwachsene2699;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_fam_1pers")
    private Integer wohnkostenFam1pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_fam_2pers")
    private Integer wohnkostenFam2pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_fam_3pers")
    private Integer wohnkostenFam3pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_fam_4pers")
    private Integer wohnkostenFam4pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_fam_5pluspers")
    private Integer wohnkostenFam5pluspers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_persoenlich_1pers")
    private Integer wohnkostenPersoenlich1pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_persoenlich_2pers")
    private Integer wohnkostenPersoenlich2pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_persoenlich_3pers")
    private Integer wohnkostenPersoenlich3pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_persoenlich_4pers")
    private Integer wohnkostenPersoenlich4pers;

    @NotNull
    @Column(nullable = false, name = "wohnkosten_persoenlich_5pluspers")
    private Integer wohnkostenPersoenlich5pluspers;

    @NotNull
    @Column(nullable = false, name = "gueltigkeit_status")
    @Enumerated(EnumType.STRING)
    private GueltigkeitStatus gueltigkeitStatus = GueltigkeitStatus.ENTWURF;

    @NotNull
    @Column(nullable = false, name = "preis_pro_mahlzeit")
    private Integer preisProMahlzeit;

    @NotNull
    @Column(nullable = false, name = "max_saeule_3a")
    private Integer maxSaeule3a;

    @NotNull
    @Column(nullable = false, name = "anzahl_wochen_lehre")
    private Integer anzahlWochenLehre;

    @NotNull
    @Column(nullable = false, name = "anzahl_wochen_schule")
    private Integer anzahlWochenSchule;

    @NotNull
    @Embedded
    @Valid
    private DateRange gueltigkeit = new DateRange();

    @NotNull
    @Column(name = "einreichfrist")
    private LocalDate einreichfrist;

    @NotNull
    @Column(name = "aufschaltdatum")
    private LocalDate aufschaltdatum;
}
