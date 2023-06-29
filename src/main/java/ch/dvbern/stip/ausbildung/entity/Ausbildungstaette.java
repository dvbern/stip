package ch.dvbern.stip.ausbildung.entity;

import ch.dvbern.stip.persistence.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

import static ch.dvbern.stip.shared.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Ausbildungstaette extends AbstractEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ausbildungstaette")
    private List<Ausbildungsgang> ausbildungsgaenge;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ausbildungsland ausbildungsland;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String name;
}
