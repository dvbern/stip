package ch.dvbern.stip.api.partner.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraint
@AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraint
@Entity
@Table(
    name = "partner",
    indexes = {
        @Index(name = "IX_partner_adresse_id", columnList = "adresse_id"),
        @Index(name = "IX_partner_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Partner extends AbstractPerson {
    @NotNull
    @OneToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_partner_adresse_id"))
    private Adresse adresse;

    @NotNull
    @AhvConstraint
    @Column(name = "sozialversicherungsnummer", nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(name = "ausbildung_mit_einkommen_oder_erwerbstaetig", nullable = false)
    private boolean ausbildungMitEinkommenOderErwerbstaetig = false;

    @Nullable
    @Column(name = "jahreseinkommen")
    private Integer jahreseinkommen;

    @Nullable
    @Column(name = "verpflegungskosten")
    private Integer verpflegungskosten;

    @Nullable
    @Column(name = "fahrkosten")
    private Integer fahrkosten;
}
