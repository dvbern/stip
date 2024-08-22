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

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.validation.GesuchFehlendeDokumenteValidationGroup;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

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
    @ManyToOne(optional = false)
    @JoinColumn(name = "fall_id", foreignKey = @ForeignKey(name = "FK_gesuch_fall_id"))
    private Fall fall;

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
    @Min(0)
    @Column(name = "gesuch_nummer", nullable = false)
    private int gesuchNummer = 0;

    @NotNull
    @Column(name = "gesuch_status_aenderung_datum", nullable = false)
    private LocalDateTime gesuchStatusAenderungDatum = LocalDateTime.now();

    @NotNull
    @Size(min = 1)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private @Valid List<GesuchTranche> gesuchTranchen = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private @Valid List<GesuchDokument> gesuchDokuments;

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
}
