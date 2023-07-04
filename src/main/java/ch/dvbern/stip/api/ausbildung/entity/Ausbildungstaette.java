package ch.dvbern.stip.api.ausbildung.entity;

import ch.dvbern.stip.api.ausbildung.type.Ausbildungsland;
import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

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
    @Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String name;
}
