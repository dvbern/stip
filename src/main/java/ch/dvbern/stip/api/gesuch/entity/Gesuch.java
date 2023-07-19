package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Audited
@Entity
@Getter
@Setter
@EqualsAndHashCode
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
}