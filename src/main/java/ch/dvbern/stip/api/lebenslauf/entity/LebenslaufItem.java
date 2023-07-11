package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufTyp;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class LebenslaufItem extends AbstractEntity {


    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LebenslaufTyp lebenslaufTyp;

    @NotNull
    @Column(nullable = false)
    private String lebenslaufSubtyp;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate von;

    @NotNull
    @Column(nullable = false)
    private LocalDate bis;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WohnsitzKanton wohnsitz;
}
