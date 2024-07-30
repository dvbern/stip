package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(
    name = "gesuch_tranche",
    indexes = {
        @Index(name = "IX_gesuch_tranche_gesuch_id", columnList = "gesuch_id"),
        @Index(name = "IX_gesuch_tranche_gesuch_formular_id", columnList = "gesuch_formular_id")
    }
)
@Getter
@Setter
public class GesuchTranche extends AbstractEntity {
    @Embedded
    private @Valid DateRange gueltigkeit = new DateRange();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(
        name = "gesuch_formular_id",
        foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_formular_id"),
        nullable = false
    )
    private @Valid GesuchFormular gesuchFormular;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "gesuch_id", foreignKey = @ForeignKey(name = "FK_gesuch_tranche_gesuch_id"), nullable = false)
    private Gesuch gesuch;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GesuchTrancheStatus status = GesuchTrancheStatus.IN_BEARBEITUNG_GS;
}
