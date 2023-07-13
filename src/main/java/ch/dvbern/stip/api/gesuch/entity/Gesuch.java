package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.Objects;

@Audited
@Entity
@Getter
@Setter
public class Gesuch extends AbstractEntity {
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

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_cotainer_gesuch_freigabe_copy_id"), nullable = true)
    private GesuchFormular gesuchFormularFreigabeCopy;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_gesuch_cotainer_gesuch_to_work_with_id"), nullable = true)
    private GesuchFormular gesuchFormularToWorkWith;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Gesuch gesuch = (Gesuch) o;
        return getGesuchNummer() == gesuch.getGesuchNummer()
                && Objects.equals(getFall(), gesuch.getFall())
                && Objects.equals(getGesuchsperiode(), gesuch.getGesuchsperiode())
                && getGesuchStatus() == gesuch.getGesuchStatus()
                && Objects.equals(getGesuchStatusAenderungDatum(), gesuch.getGesuchStatusAenderungDatum())
                && Objects.equals(getGesuchFormularFreigabeCopy(), gesuch.getGesuchFormularFreigabeCopy())
                && Objects.equals(getGesuchFormularToWorkWith(), gesuch.getGesuchFormularToWorkWith());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getFall(),
                getGesuchsperiode(),
                getGesuchStatus(),
                getGesuchNummer(),
                getGesuchStatusAenderungDatum(),
                getGesuchFormularFreigabeCopy(),
                getGesuchFormularToWorkWith());
    }
}
