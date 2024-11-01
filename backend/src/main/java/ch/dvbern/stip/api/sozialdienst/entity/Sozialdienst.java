package ch.dvbern.stip.api.sozialdienst.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.benutzer.entity.SozialdienstAdmin;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.IbanConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Getter
@Setter
@Entity
@Table(
    name = "sozialdienst",
    indexes = @Index(name = "IX_sozialdienst_mandant", columnList = "mandant")
)
public class Sozialdienst extends AbstractMandantEntity {
    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "iban", nullable = false)
    @IbanConstraint
    private String iban;

    @NotNull
    @OneToOne(optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_sozialdienst_adresse_id"))
    private Adresse adresse;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sozialdienst_admin_id", foreignKey = @ForeignKey(name = "FK_sozialdienst_sozialdienst_admin_id"))
    private SozialdienstAdmin admin;
}
