package ch.dvbern.stip.ausbildung.model;

import ch.dvbern.stip.persistence.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

import static ch.dvbern.stip.shared.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Ausbildung extends AbstractEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildung_ausbildungsgang_id"))
    private Ausbildungsgang ausbildungsgang;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildung_ausbildungstaette_id"))
    private Ausbildungstaette ausbildungstaette;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Aubildungsland ausbildungsland;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String alternativeAusbildungsgang;

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String AlternativeAusbildungstaette;

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
    private LocalDate ausbildungEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AusbildungsPensum pensum;
}
