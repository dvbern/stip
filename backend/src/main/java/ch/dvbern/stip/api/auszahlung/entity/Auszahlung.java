package ch.dvbern.stip.api.auszahlung.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.common.validation.IbanConstraint;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Table(
    name = "auszahlung",
    indexes = {
        @Index(name = "IX_auszahlung_adresse_id", columnList = "adresse_id"),
        @Index(name = "IX_auszahlung_mandant", columnList = "mandant")
    }
)
@Getter
@Setter
public class Auszahlung extends AbstractMandantEntity {
    @NotNull
    @Column(name = "kontoinhaber", nullable = false)
    @Enumerated(EnumType.STRING)
    private Kontoinhaber kontoinhaber;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "nachname", nullable = false)
    private String nachname;

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_auszahlung_adresse_id"), nullable = false)
    private Adresse adresse;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "iban", nullable = false)
    @IbanConstraint
    private String iban;

    @Nullable
    @Column(name = "sap_business_partner_id", nullable = true)
    private Integer sapBusinessPartnerId;
}
