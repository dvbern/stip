package ch.dvbern.stip.ausbildung.entity;

import ch.dvbern.stip.persistence.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.shared.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Ausbildungsgang extends AbstractEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ausbildungsgang_ausbildungstaette_id"), nullable = true)
    private Ausbildungstaette ausbildungstaette;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String bezeichnungDe;


    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column
    private String bezeichnungFr;
}
