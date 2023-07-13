package ch.dvbern.stip.api.partner.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Partner extends AbstractEntity {
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_person_in_ausbildung_adresse_id"), nullable = false)
    private Adresse adresse;
    @NotNull
    @Column(nullable = false)
    private String sozialversicherungsnummer;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String nachname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String vorname;

    @NotNull
    @Column(nullable = false)
    private LocalDate geburtsdatum;

    @NotNull
    @Column(nullable = false)
    private BigDecimal jahreseinkommen;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Partner partner = (Partner) o;
        return Objects.equals(getAdresse(), partner.getAdresse())
                && Objects.equals(
                getSozialversicherungsnummer(),
                partner.getSozialversicherungsnummer())
                && Objects.equals(getNachname(), partner.getNachname())
                && Objects.equals(getVorname(), partner.getVorname())
                && Objects.equals(getGeburtsdatum(), partner.getGeburtsdatum())
                && Objects.equals(getJahreseinkommen(), partner.getJahreseinkommen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getAdresse(),
                getSozialversicherungsnummer(),
                getNachname(),
                getVorname(),
                getGeburtsdatum(),
                getJahreseinkommen());
    }
}
