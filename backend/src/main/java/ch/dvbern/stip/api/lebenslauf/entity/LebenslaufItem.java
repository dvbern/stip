package ch.dvbern.stip.api.lebenslauf.entity;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.lebenslauf.type.Taetigskeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
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
@Table(indexes = @Index(name = "IX_lebenslauf_item_mandant", columnList = "mandant"))
@Getter
@Setter
public class LebenslaufItem extends AbstractMandantEntity {

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private LebenslaufAusbildungsArt bildungsart;

    @NotNull
    @Column(nullable = false)
    private LocalDate von;

    @NotNull
    @Column(nullable = false)
    private LocalDate bis;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Taetigskeitsart taetigskeitsart;

    @Column(nullable = true)
    private String taetigkeitsBeschreibung;

    @Column(nullable = true)
    private String berufsbezeichnung;

    @Column(nullable = true)
    private String fachrichtung;

    @Column(nullable = true)
    private String titelDesAbschlusses;

    @Column(nullable = false)
    private boolean ausbildungAbgeschlossen = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WohnsitzKanton wohnsitz;

    @Column(nullable = true)
    private UUID copyOfId;
}
