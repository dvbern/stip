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

package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.beschwerdeentscheid.entity.BeschwerdeEntscheid;
import ch.dvbern.stip.api.beschwerdeverlauf.entity.BeschwerdeVerlaufEntry;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.api.unterschriftenblatt.entity.Unterschriftenblatt;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;

@DocumentsRequiredFehlendeDokumenteConstraint(
    groups = {
        GesuchFehlendeDokumenteValidationGroup.class
    }
)
@Audited
@Entity
@Table(
    name = "gesuch",
    indexes = {
        @Index(name = "IX_gesuch_gesuchsperiode_id", columnList = "gesuchsperiode_id"),
        @Index(name = "IX_gesuch_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
@Builder(style = BuilderStyle.STAGED)
@NoArgsConstructor
@AllArgsConstructor
public class Gesuch extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ausbildung_id", foreignKey = @ForeignKey(name = "FK_gesuch_ausbildung_id"))
    private Ausbildung ausbildung;

    @Nullable
    @Column(name = "nachfrist_dokumente")
    private LocalDate nachfristDokumente;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuchsperiode_id", foreignKey = @ForeignKey(name = "FK_gesuch_gesuchsperiode_id"))
    private Gesuchsperiode gesuchsperiode;

    @NotNull
    @Column(name = "gesuch_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Audited(withModifiedFlag = true, modifiedColumnName = "gesuch_status_mod")
    private Gesuchstatus gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_GS;

    @Opt
    @NotNull
    @Size(max = DB_DEFAULT_STRING_SMALL_LENGTH)
    @Column(name = "gesuch_nummer", nullable = false, updatable = false, length = DB_DEFAULT_STRING_SMALL_LENGTH)
    private String gesuchNummer;

    @Opt
    @NotNull
    @Column(name = "gesuch_status_aenderung_datum", nullable = false)
    private LocalDateTime gesuchStatusAenderungDatum = LocalDateTime.now();

    @NotNull
    @Size(min = 1)
    @OrderBy("timestampErstellt")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private @Valid List<GesuchTranche> gesuchTranchen = new ArrayList<>();

    @Nullable
    @Size(max = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    @Column(name = "comment", length = DB_DEFAULT_STRING_INPUT_MAX_LENGTH)
    private String comment;

    @Nullable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Valid List<GesuchNotiz> notizen = new ArrayList<>();

    public GesuchTranche getLatestGesuchTranche() {
        // There must always be at least 1 GesuchTranche
        // noinspection OptionalGetWithoutIsPresent
        return gesuchTranchen.stream()
            .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
            .max(Comparator.comparing((GesuchTranche gesuchTranche) -> gesuchTranche.getGueltigkeit().getGueltigBis()))
            .get();
    }

    public GesuchTranche getEarliestGesuchTranche() {
        return gesuchTranchen.stream()
            .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
            .min(Comparator.comparing((GesuchTranche gesuchTranche) -> gesuchTranche.getGueltigkeit().getGueltigAb()))
            .get();
    }

    @Opt
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula(value = """
        (
            SELECT gesuch_tranche.id
            FROM gesuch_tranche gesuch_tranche
            WHERE gesuch_tranche.gesuch_id = id
                AND gesuch_tranche.typ = 'AENDERUNG'
                AND gesuch_tranche.status = 'UEBERPRUEFEN'
            ORDER BY gesuch_tranche.gueltig_bis DESC
            LIMIT 1
        )
    """)
    @Setter(AccessLevel.NONE)
    @NotAudited
    private GesuchTranche aenderungZuUeberpruefen;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private List<Unterschriftenblatt> unterschriftenblaetter = new ArrayList<>();

    @Nullable
    @Column(name = "einreichedatum")
    private LocalDate einreichedatum;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private List<Datenschutzbrief> datenschutzbriefs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private List<BeschwerdeVerlaufEntry> beschwerdeVerlauf = new ArrayList<>();

    @Column(name = "beschwerde_haengig", nullable = false)
    private boolean beschwerdeHaengig;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private List<BeschwerdeEntscheid> beschwerdeEntscheids = new ArrayList<>();

    @Opt
    @Column(name = "remainder_payment_executed", nullable = false)
    private boolean remainderPaymentExecuted = false;

    /**
     * Gesuch was verfuegt at least once in the past
     */
    @Column(name = "verfuegt", nullable = false)
    private boolean verfuegt = false;

    @OrderBy("timestampErstellt DESC")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private List<Verfuegung> verfuegungs = new ArrayList<>();

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "pending_sap_action")
    private BuchhaltungType pendingSapAction;

    public Optional<GesuchTranche> getGesuchTrancheById(UUID id) {
        return gesuchTranchen.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst();
    }

    public Optional<GesuchTranche> getEingereichteGesuchTrancheValidOnDate(LocalDate date) {
        return gesuchTranchenValidOnDateStream(date)
            .filter(tranche -> (tranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS))
            .findFirst();
    }

    public Optional<GesuchTranche> getGesuchTrancheValidOnDate(LocalDate date) {
        return gesuchTranchenValidOnDateStream(date).findFirst();
    }

    private Stream<GesuchTranche> gesuchTranchenValidOnDateStream(LocalDate date) {
        return gesuchTranchen.stream()
            .filter(tranche -> tranche.getGueltigkeit().contains(date) && tranche.getTyp() == GesuchTrancheTyp.TRANCHE);
    }

    public Optional<GesuchTranche> getNewestGesuchTranche() {
        if (gesuchTranchen.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(
            Collections.max(
                gesuchTranchen,
                Comparator.comparing(x -> x.getGueltigkeit().getGueltigBis())
            )
        );
    }

    public Optional<GesuchTranche> getOldestGesuchTranche() {
        if (gesuchTranchen.isEmpty()) {
            return Optional.empty();
        }

        return gesuchTranchen.stream()
            .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE)
            .min(Comparator.comparing(gesuchTranche -> gesuchTranche.getGueltigkeit().getGueltigAb()));
    }

    public Stream<GesuchTranche> getAenderungen() {
        return getGesuchTranchen()
            .stream()
            .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG);
    }

    public Stream<GesuchTranche> getTranchenTranchen() {
        return getGesuchTranchen()
            .stream()
            .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE);
    }

    public Optional<GesuchTranche> getAenderungZuUeberpruefen() {
        return getGesuchTranchen().stream()
            .filter(
                tranche -> tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
                && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN
            )
            .findFirst();
    }

    public boolean isFirstVerfuegung() {
        return verfuegungs.size() < 2;
    }

    public boolean isErstgesuch() {
        // TODO KSTIP-1777: Implement isErstgesuch logic
        return true;
    }

    public boolean hasNeverBeenVerfuegt() {
        return isErstgesuch() && !isVerfuegt();
    }
}
