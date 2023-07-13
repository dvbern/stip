package ch.dvbern.stip.api.geschwister.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.geschwister.type.Ausbildungssituation;
import ch.dvbern.stip.api.personinausbildung.type.Wohnsitz;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Geschwister extends AbstractEntity {
    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String nachname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String vorname;

    @NotNull
    @Column(nullable = false)
    private LocalDate geburtsdatum;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wohnsitz wohnsitz;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ausbildungssituation ausbildungssituation;

    @Column(nullable = true)
    private BigDecimal wohnsitzAnteilMutter;

    @Column(nullable = true)
    private BigDecimal wohnsitzAnteilVater;

}
