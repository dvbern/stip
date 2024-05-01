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
@Table(indexes = {
    @Index(name = "IX_partner_adresse_id", columnList = "adresse_id"),
    @Index(name = "IX_partner_mandant", columnList = "mandant")
})
@Getter
@Setter
public class Partner extends AbstractPerson {
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_partner_adresse_id"), nullable = false)
    private Adresse adresse;

    @NotNull
    @AhvConstraint
    @Column(nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(nullable = false)
    private boolean ausbildungMitEinkommenOderErwerbstaetig = false;

    @Nullable
    @Column(nullable = true)
    private Integer jahreseinkommen;

    @Nullable
    @Column(nullable = true)
    private Integer verpflegungskosten;

    @Nullable
    @Column(nullable = true)
    private Integer fahrkosten;
}
