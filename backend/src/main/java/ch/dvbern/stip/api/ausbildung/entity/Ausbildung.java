package ch.dvbern.stip.api.ausbildung.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@AusbildungNichtGefundenRequiredFieldsConstraint
@AusbildungNichtGefundenRequiredNullFieldsConstraint
@AusbildungEndDateMustBeAfterStartConstraint
@Entity
@Table(indexes = {
    @Index(name = "IX_ausbildung_ausbildungsgang_id", columnList = "ausbildungsgang_id"),
    @Index(name = "IX_ausbildung_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Ausbildung extends AbstractMandantEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildung_ausbildungsgang_id"))
    private Ausbildungsgang ausbildungsgang;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String alternativeAusbildungsgang;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String alternativeAusbildungsstaette;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String alternativeAusbildungsland;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String fachrichtung;

    @Column(nullable = false)
    private boolean ausbildungNichtGefunden = false;

    @NotNull
    @Column(nullable = false)
    private LocalDate ausbildungBegin;

    @NotNull
    @Column(nullable = false)
    @Future
    private LocalDate ausbildungEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AusbildungsPensum pensum;
}
