package ch.dvbern.stip.api.partner.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.entity.AbstractPerson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Partner extends AbstractPerson {
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_adresse_id"), nullable = false)
    private Adresse adresse;
    @NotNull
    @Column(nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Column(nullable = false)
    private BigDecimal jahreseinkommen;
}
