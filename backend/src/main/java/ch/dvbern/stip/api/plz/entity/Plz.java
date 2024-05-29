package ch.dvbern.stip.api.plz.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "plz",
    uniqueConstraints = @UniqueConstraint(name = "UC_plz_ortschaftsname_kantonskuerzel", columnNames={"plz", "ortschaftsname", "kantonskuerzel"}),
    indexes = @Index(name = "IX_plz_kantonskuerzel", columnList = "kantonskuerzel")
)
@Audited
@Getter
@Setter
public class Plz extends AbstractEntity {
    @NotNull
    @Column(name = "plz", nullable = false)
    private String plz;

    @NotNull
    @Column(name = "ortschaftsname", nullable = false)
    private String ortschaftsname;

    @NotNull
    @Column(name = "kantonskuerzel", nullable = false)
    private String kantonskuerzel;

//    @Override
//    public String toString() {
//        return "Plz{" +
//            "plz='" + plz + '\'' +
//            ", ortschaftsname='" + ortschaftsname + '\'' +
//            ", kantonskuerzel='" + kantonskuerzel + '\'' +
//            '}';
//    }
}
