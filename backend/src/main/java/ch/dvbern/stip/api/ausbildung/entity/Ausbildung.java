package ch.dvbern.stip.api.ausbildung.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.annotation.Nullable;
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
@AusbildungsortRequiredIfSwissConstraint
@Entity
@Table(
    name = "ausbildung",
    indexes = {
        @Index(name = "IX_ausbildung_ausbildungsgang_id", columnList = "ausbildungsgang_id"),
        @Index(name = "IX_ausbildung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Ausbildung extends AbstractMandantEntity {
    @ManyToOne
    @JoinColumn(name = "ausbildungsgang_id", foreignKey = @ForeignKey(name = "FK_ausbildung_ausbildungsgang_id"))
    private Ausbildungsgang ausbildungsgang;

    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "alternative_ausbildungsgang")
    private String alternativeAusbildungsgang;

    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "alternative_ausbildungsstaette")
    private String alternativeAusbildungsstaette;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "fachrichtung", nullable = false)
    private String fachrichtung;

    @Column(name = "ausbildung_nicht_gefunden", nullable = false)
    private boolean ausbildungNichtGefunden = false;

    @NotNull
    @Column(name = "ausbildung_begin", nullable = false)
    private LocalDate ausbildungBegin;

    @NotNull
    @Column(name = "ausbildung_end", nullable = false)
    @Future
    private LocalDate ausbildungEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pensum", nullable = false)
    private AusbildungsPensum pensum;

    @Nullable
    @Column(name = "ausbildungsort")
    private String ausbildungsort;

    @NotNull
    @Column(name = "is_ausbildung_ausland")
    private boolean isAusbildungAusland = false;
}
