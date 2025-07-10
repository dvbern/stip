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

package ch.dvbern.stip.api.ausbildung.entity;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.ausbildung.type.FerienTyp;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Range;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;

@Audited
@Entity
@Table(name = "abschluss", indexes = @Index(name = "IX_abschluss_mandant", columnList = "mandant"))
@Getter
@Setter
public class Abschluss extends AbstractMandantEntity {
    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "bezeichnung_de", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungDe;

    @NotNull
    @Size(max = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    @Column(name = "bezeichnung_fr", nullable = false, length = DB_DEFAULT_STRING_MEDIUM_LENGTH)
    private String bezeichnungFr;

    @NotNull
    @Column(name = "ausbildungskategorie", nullable = false)
    @Enumerated(EnumType.STRING)
    private Ausbildungskategorie ausbildungskategorie;

    @NotNull
    @Column(name = "bildungskategorie", nullable = false)
    @Enumerated(EnumType.STRING)
    private Bildungskategorie bildungskategorie;

    @NotNull
    @Column(name = "bildungsrichtung", nullable = false)
    @Enumerated(EnumType.STRING)
    private Bildungsrichtung bildungsrichtung;

    @NotNull
    @Range(max = 10)
    @Column(name = "bfs_kategorie", nullable = false)
    private Integer bfsKategorie;

    @NotNull
    @Column(name = "berufsbefaehigender_abschluss", nullable = false)
    private boolean berufsbefaehigenderAbschluss;

    @NotNull
    @Column(name = "ferien", nullable = false)
    @Enumerated(EnumType.STRING)
    private FerienTyp ferien;

    @Nullable
    @Column(name = "zusatzfrage")
    @Enumerated(EnumType.STRING)
    private AbschlussZusatzfrage zusatzfrage;

    @NotNull
    @Column(name = "aktiv", nullable = false)
    private boolean aktiv = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "abschluss")
    private List<Ausbildungsgang> ausbildungsgaenge;
}
