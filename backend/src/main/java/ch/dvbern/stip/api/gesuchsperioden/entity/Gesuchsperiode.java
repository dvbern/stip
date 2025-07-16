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

package ch.dvbern.stip.api.gesuchsperioden.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import jakarta.persistence.Column;
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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(
    name = "gesuchsperiode",
    indexes = {
        @Index(name = "IX_gesuchsperiode_aufschalttermin_start", columnList = "aufschalttermin_start"),
        @Index(name = "IX_gesuchsperiode_mandant", columnList = "mandant"),
        @Index(name = "IX_gesuchsperiode_gesuchsjahr_id", columnList = "gesuchsjahr_id")
    }
)
@Getter
@Setter
public class Gesuchsperiode extends AbstractMandantEntity {
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(nullable = false, name = "bezeichnung_de", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungDe;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(nullable = false, name = "bezeichnung_fr", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungFr;

    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(nullable = false, name = "fiskaljahr", length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String fiskaljahr;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuchsperiode_gesuchsjahr_id"), nullable = false)
    private @Valid Gesuchsjahr gesuchsjahr;

    @Column(nullable = false, name = "gesuchsperiode_start")
    private LocalDate gesuchsperiodeStart;

    @Column(nullable = false, name = "gesuchsperiode_stopp")
    private LocalDate gesuchsperiodeStopp;

    @Column(nullable = false, name = "aufschalttermin_start")
    private LocalDate aufschaltterminStart;

    @Column(nullable = false, name = "einreichefrist_normal")
    private LocalDate einreichefristNormal;

    @Column(nullable = false, name = "einreichefrist_reduziert")
    private LocalDate einreichefristReduziert;

    @Column(nullable = false, name = "ausbkosten_sek_ii")
    private Integer ausbKostenSekII;

    @Column(nullable = false, name = "ausbkosten_tertiaer")
    private Integer ausbKostenTertiaer;

    @Column(nullable = false, name = "freibetrag_vermoegen")
    private Integer freibetragVermoegen;

    @Column(nullable = false, name = "freibetrag_erwerbseinkommen")
    private Integer freibetragErwerbseinkommen;

    @Column(nullable = false, name = "einkommensfreibetrag")
    private Integer einkommensfreibetrag;

    @Column(nullable = false, name = "elternbeteiligungssatz")
    private Integer elternbeteiligungssatz;

    @Column(nullable = false, name = "vermogen_satz_angerechnet")
    private Integer vermogenSatzAngerechnet;

    @Column(nullable = false, name = "integrationszulage")
    private Integer integrationszulage;

    @Column(nullable = false, name = "limite_ek_freibetrag_integrationszulage")
    private Integer limiteEkFreibetragIntegrationszulage;

    @Column(nullable = false, name = "stip_limite_minimalstipendium")
    private Integer stipLimiteMinimalstipendium;

    @Column(nullable = false, name = "person_1")
    private Integer person1;

    @Column(nullable = false, name = "personen_2")
    private Integer personen2;

    @Column(nullable = false, name = "personen_3")
    private Integer personen3;

    @Column(nullable = false, name = "personen_4")
    private Integer personen4;

    @Column(nullable = false, name = "personen_5")
    private Integer personen5;

    @Column(nullable = false, name = "personen_6")
    private Integer personen6;

    @Column(nullable = false, name = "personen_7")
    private Integer personen7;

    @Column(nullable = false, name = "pro_weitere_person")
    private Integer proWeiterePerson;

    @Column(nullable = false, name = "kinder_00_18")
    private Integer kinder0018;

    @Column(nullable = false, name = "jugendliche_erwachsene_19_25")
    private Integer jugendlicheErwachsene1925;

    @Column(nullable = false, name = "erwachsene_26_99")
    private Integer erwachsene2699;

    @Column(nullable = false, name = "wohnkosten_fam_1pers")
    private Integer wohnkostenFam1pers;

    @Column(nullable = false, name = "wohnkosten_fam_2pers")
    private Integer wohnkostenFam2pers;

    @Column(nullable = false, name = "wohnkosten_fam_3pers")
    private Integer wohnkostenFam3pers;

    @Column(nullable = false, name = "wohnkosten_fam_4pers")
    private Integer wohnkostenFam4pers;

    @Column(nullable = false, name = "wohnkosten_fam_5pluspers")
    private Integer wohnkostenFam5pluspers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_1pers")
    private Integer wohnkostenPersoenlich1pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_2pers")
    private Integer wohnkostenPersoenlich2pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_3pers")
    private Integer wohnkostenPersoenlich3pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_4pers")
    private Integer wohnkostenPersoenlich4pers;

    @Column(nullable = false, name = "wohnkosten_persoenlich_5pluspers")
    private Integer wohnkostenPersoenlich5pluspers;

    @Column(nullable = false, name = "gueltigkeit_status")
    @Enumerated(EnumType.STRING)
    private GueltigkeitStatus gueltigkeitStatus = GueltigkeitStatus.ENTWURF;

    @Column(nullable = false, name = "preis_pro_mahlzeit")
    private Integer preisProMahlzeit;

    @Column(nullable = false, name = "max_saeule_3a")
    private Integer maxSaeule3a;

    @Column(nullable = false, name = "anzahl_wochen_lehre")
    private Integer anzahlWochenLehre;

    @Column(nullable = false, name = "anzahl_wochen_schule")
    private Integer anzahlWochenSchule;

    @Min(0)
    @Max(100)
    @Column(nullable = false, name = "vermoegensanteil_in_prozent")
    private Integer vermoegensanteilInProzent;

    @Column(nullable = false, name = "reduzierung_des_grundbedarfs")
    private Integer reduzierungDesGrundbedarfs;

    @Column(nullable = false, name = "limite_alter_antragssteller_halbierung_elternbeitrag")
    private Integer limiteAlterAntragsstellerHalbierungElternbeitrag;

    @Min(0)
    @Max(11)
    @Column(name = "zweiter_auszahlungstermin_monat", nullable = false)
    private Integer zweiterAuszahlungsterminMonat;

    @Min(1)
    @Max(28)
    @Column(name = "zweiter_auszahlungstermin_tag", nullable = false)
    private Integer zweiterAuszahlungsterminTag;

    @NotNull
    @Min(0)
    @Column(name = "frist_nachreichen_dokumente", nullable = false)
    private Integer fristNachreichenDokumente;

    @NotNull
    @Column(name = "stichtag_volljaehrigkeit_medizinische_grundversorgung", nullable = false)
    private LocalDate stichtagVolljaehrigkeitMedizinischeGrundversorgung;

    public boolean isActiveFor(final LocalDate date) {
        return DateUtil.between(
            getAufschaltterminStart(),
            getGesuchsperiodeStopp(),
            date,
            true
        );
    }
}
