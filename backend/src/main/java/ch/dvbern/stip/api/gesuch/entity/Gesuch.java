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
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
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
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@DocumentsRequiredFehlendeDokumenteConstraint(groups = {
    GesuchFehlendeDokumenteValidationGroup.class
})
@OnlyOneTrancheInBearbeitungConstraint
@Audited
@Entity
@Table(
    name = "gesuch",
    indexes = {
        @Index(name = "IX_gesuch_fall_id", columnList = "fall_id"),
        @Index(name = "IX_gesuch_gesuchsperiode_id", columnList = "gesuchsperiode_id"),
        @Index(name = "IX_gesuch_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Gesuch extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ausbildung_id", foreignKey = @ForeignKey(name = "FK_gesuch_ausbildung_id"))
    private Ausbildung ausbildung;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "gesuchsperiode_id", foreignKey = @ForeignKey(name = "FK_gesuch_gesuchsperiode_id"))
    private Gesuchsperiode gesuchsperiode;

    @NotNull
    @Column(name = "gesuch_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Audited(withModifiedFlag = true, modifiedColumnName = "gesuch_status_mod")
    private Gesuchstatus gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_GS;

    @NotNull
    @Column(name = "gesuch_nummer", nullable = false, updatable = false)
    private String gesuchNummer;

    @NotNull
    @Column(name = "gesuch_status_aenderung_datum", nullable = false)
    private LocalDateTime gesuchStatusAenderungDatum = LocalDateTime.now();

    @NotNull
    @Size(min = 1)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private @Valid List<GesuchTranche> gesuchTranchen = new ArrayList<>();

    @Nullable
    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula(value = """
        (
            SELECT gesuch_tranche.id
            FROM gesuch_tranche gesuch_tranche
            WHERE gesuch_tranche.gesuch_id = id
                AND gesuch_tranche.typ = 'TRANCHE'
            ORDER BY gesuch_tranche.gueltig_bis DESC
            LIMIT 1
        )
    """)
    @Setter(AccessLevel.NONE)
    @NotAudited
    private GesuchTranche latestGesuchTranche;

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

    public Optional<GesuchTranche> getGesuchTrancheById(UUID id) {
        return gesuchTranchen.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst();
    }

    public Optional<GesuchTranche> getTrancheValidOnDate(LocalDate date) {
        return tranchenValidOnDateStream(date)
            .filter(tranche -> tranche.getStatus() != GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .findFirst();
    }

    public Optional<GesuchTranche> getAllTranchenValidOnDate(LocalDate date) {
        return tranchenValidOnDateStream(date).findFirst();
    }

    private Stream<GesuchTranche> tranchenValidOnDateStream(LocalDate date) {
        return gesuchTranchen.stream().filter(t -> t.getGueltigkeit().contains(date));
    }

    public GesuchTranche getCurrentGesuchTranche() {
        return getAllTranchenValidOnDate(LocalDate.now()).orElseThrow();
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

    public Optional<GesuchTranche> getAenderungZuUeberpruefen() {
        return getGesuchTranchen().stream()
            .filter(tranche -> tranche.getTyp() == GesuchTrancheTyp.AENDERUNG
                && tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN
            )
            .findFirst();
    }
}
