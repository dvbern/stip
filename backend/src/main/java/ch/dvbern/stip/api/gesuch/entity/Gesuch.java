package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Audited
@Entity
@Table(indexes = {
    @Index(name = "IX_gesuch_fall_id", columnList = "fall_id"),
    @Index(name = "IX_gesuch_gesuchsperiode_id", columnList = "gesuchsperiode_id"),
    @Index(name = "IX_gesuch_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Gesuch extends AbstractMandantEntity {
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_fall_id"))
    private Fall fall;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_gesuchsperiode_id"))
    private Gesuchsperiode gesuchsperiode;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gesuchstatus gesuchStatus = Gesuchstatus.OFFEN;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private int gesuchNummer = 0;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime gesuchStatusAenderungDatum = LocalDateTime.now();

    @NotNull
    @Size(min = 1)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "gesuch")
    private @Valid List<GesuchTranche> gesuchTranchen = new ArrayList<>();

    public Optional<GesuchTranche> getGesuchTrancheById(UUID id) {
        return gesuchTranchen.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst();
    }

    public Optional<GesuchTranche> getGesuchTrancheValidOnDate(LocalDate date) {
        return gesuchTranchen.stream()
            .filter(t -> t.getGueltigkeit().contains(date))
            .findFirst();
    }
}
