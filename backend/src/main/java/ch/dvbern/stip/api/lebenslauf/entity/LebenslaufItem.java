package ch.dvbern.stip.api.lebenslauf.entity;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@LebenslaufItemArtRequiredFieldsConstraint
@LebenslaufItemAusbildungBerufsbezeichnungConstraint
@LebenslaufItemAusbildungFachrichtungConstraint
@LebenslaufItemAusbildungTitelDesAbschlussesConstraint
@Entity
@Table(
    name = "lebenslauf_item",
    indexes = @Index(name = "IX_lebenslauf_item_mandant", columnList = "mandant")
)
@Getter
@Setter
public class LebenslaufItem extends AbstractMandantEntity {
    @Column(name = "bildungsart")
    @Enumerated(EnumType.STRING)
    private LebenslaufAusbildungsArt bildungsart;

    @NotNull
    @Column(name = "von", nullable = false)
    private LocalDate von;

    @NotNull
    @Column(name = "bis", nullable = false)
    private LocalDate bis;

    @Column(name = "taetigkeitsart")
    @Enumerated(EnumType.STRING)
    private Taetigkeitsart taetigkeitsart;

    @Nullable
    @Column(name = "taetigkeits_beschreibung")
    private String taetigkeitsBeschreibung;

    @Nullable
    @Column(name = "berufsbezeichnung")
    private String berufsbezeichnung;

    @Nullable
    @Column(name = "fachrichtung")
    private String fachrichtung;

    @Nullable
    @Column(name = "titel_des_abschlusses")
    private String titelDesAbschlusses;

    @Column(name = "ausbildung_abgeschlossen", nullable = false)
    private boolean ausbildungAbgeschlossen = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wohnsitz", nullable = false)
    private WohnsitzKanton wohnsitz;

    @Nullable
    @Column(name = "copy_of_id")
    private UUID copyOfId;
}
