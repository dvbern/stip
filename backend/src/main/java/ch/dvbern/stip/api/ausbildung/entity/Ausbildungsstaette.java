package ch.dvbern.stip.api.ausbildung.entity;

import java.util.List;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "ausbildungsstaette")
@Getter
@Setter
public class Ausbildungsstaette extends AbstractEntity {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ausbildungsstaette")
    private List<Ausbildungsgang> ausbildungsgaenge;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
    @Column(name = "name_de", nullable = false)
    private String nameDe;

    @NotNull
    @Size(max = Constants.DB_DEFAULT_MAX_LENGTH)
    @Column(name = "name_fr", nullable = false)
    private String nameFr;
}
