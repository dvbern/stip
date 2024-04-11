package ch.dvbern.stip.api.zuordnung.entity;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(indexes = {
    @Index(name = "IX_zuordnung_fall_id", columnList = "fall_id"),
    @Index(name = "IX_zuordnung_sachbearbeiter_id", columnList = "sachbearbeiter_id"),
    @Index(name = "IX_zuordnung_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Zuordnung extends AbstractMandantEntity {
    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fall_id", foreignKey = @ForeignKey(name = "FK_zuordnung_fall_id"), nullable = false)
    private Fall fall;

    @NotNull
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sachbearbeiter_id", foreignKey = @ForeignKey(name = "FK_zuordnung_sachbearbeiter_id"), nullable = false)
    private Benutzer sachbearbeiter;

    @NotNull
    @Column(nullable = false, name = "zuordnung_type")
    @Enumerated(EnumType.STRING)
    private ZuordnungType zuordnungType;
}
