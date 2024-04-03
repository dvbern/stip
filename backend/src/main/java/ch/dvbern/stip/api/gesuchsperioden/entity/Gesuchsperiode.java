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
import ch.dvbern.stip.api.common.util.DateRange;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(indexes = {
    @Index(name = "IX_gesuchsperiode_aufschaltdatum_gueltig_bis", columnList = "aufschaltdatum,gueltig_bis"),
    @Index(name = "IX_gesuchsperiode_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Gesuchsperiode extends AbstractMandantEntity {

    @Column(nullable = false, name = "bezeichnung_de")
    private String bezeichnungDe;

    @Column(nullable = false, name = "bezeichnung_fr")
    private String bezeichnungFr;

    @Column(nullable = false, name = "fiskaljahr")
    private String fiskaljahr;

    @Column(nullable = false, name = "gesuchsjahr")
    private Integer gesuchsjahr;

    @Column(nullable = false, name = "gesuchsperiode_start")
    private LocalDate gesuchsperiodeStart;

    @Column(nullable = false, name = "gesuchsperiode_stopp")
    private LocalDate gesuchsperiodeStopp;

    @Column(nullable = false, name = "aufschalttermin_start")
    private LocalDate aufschaltterminStart;

    @Column(nullable = false, name = "aufschalttermin_stopp")
    private LocalDate aufschaltterminStopp;

    @Column(nullable = false, name = "einreichefrist_normal")
    private LocalDate einreichefristNormal;

    @Column(nullable = false, name = "einreichefrist_reduziert")
    private LocalDate einreichefristReduziert;

    @Column(nullable = false, name = "ausbkosten_sek_ii")
    private Integer ausbKosten_SekII;

    @Column(nullable = false, name = "ausbkosten_tertiaer")
    private Integer ausbKosten_Tertiaer;

    @Column(nullable = false, name = "freibetrag_vermoegen")
    private Integer freibetrag_vermoegen;

    @Column(nullable = false, name = "freibetrag_erwerbseinkommen")
    private Integer freibetrag_erwerbseinkommen;

    @Column(nullable = false, name = "einkommensfreibetrag")
    private Integer einkommensfreibetrag;

    @Column(nullable = false, name = "elternbeteiligungssatz")
    private Integer elternbeteiligungssatz;

    @Column(nullable = false, name = "f_einkommensfreibetrag")
    private Integer f_Einkommensfreibetrag;

    @Column(nullable = false, name = "f_vermoegensfreibetrag")
    private Integer f_Vermoegensfreibetrag;

    @Column(nullable = false, name = "f_vermogen_satz_angerechnet")
    private Integer f_VermogenSatzAngerechnet;

    @Column(nullable = false, name = "integrationszulage")
    private Integer integrationszulage;

    @Column(nullable = false, name = "limite_ek_freibetrag_integrationszulag")
    private Integer limite_EkFreibetrag_Integrationszulag;

    @Column(nullable = false, name = "stip_limite_minimalstipendium")
    private Integer stipLimite_Minimalstipendium;

    @Column(nullable = false, name = "person_1")
    private Integer person_1;

    @Column(nullable = false, name = "personen_2")
    private Integer personen_2;

    @Column(nullable = false, name = "personen_3")
    private Integer personen_3;

    @Column(nullable = false, name = "personen_4")
    private Integer personen_4;

    @Column(nullable = false, name = "personen_5")
    private Integer personen_5;

    @Column(nullable = false, name = "personen_6")
    private Integer personen_6;

    @Column(nullable = false, name = "personen_7")
    private Integer personen_7;

    @Column(nullable = false, name = "pro_weitere_person")
    private Integer proWeiterePerson;

    @Column(nullable = false, name = "kinder_00_18")
    private Integer kinder_00_18;

    @Column(nullable = false, name = "jugendliche_erwachsene_19_25")
    private Integer jugendliche_erwachsene_19_25;

    @Column(nullable = false, name = "erwachsene_26_99")
    private Integer erwachsene_26_99;

    @Column(nullable = false, name = "wohnkosten_fam_1pers")
    private Integer wohnkosten_fam_1pers;

    @Column(nullable = false, name = "wohnkosten_fam_2pers")
    private Integer wohnkosten_fam_2pers;

    @Column(nullable = false, name = "wohnkosten_fam_3pers")
    private Integer wohnkosten_fam_3pers;

    @Column(nullable = false, name = "wohnkosten_fam_4pers")
    private Integer wohnkosten_fam_4pers;

    @Column(nullable = false, name = "wohnkosten_fam_5pluspers")
    private Integer wohnkosten_fam_5pluspers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_1pers")
    private Integer wohnkosten_persoenlich_1pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_2pers")
    private Integer wohnkosten_persoenlich_2pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_3pers")
    private Integer wohnkosten_persoenlich_3pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_4pers")
    private Integer wohnkosten_persoenlich_4pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_5pluspers")
    private Integer wohnkosten_persoenlich_5pluspers;

    @NotNull
    @Embedded
    @Valid
    @AttributeOverride(name = "gueltigBis", column = @Column(name = "gueltig_bis"))
    private DateRange gueltigkeit = new DateRange();

    @Column(nullable = true)
    private LocalDate einreichfrist;

    @Column(nullable = true)
    private LocalDate aufschaltdatum;
}
