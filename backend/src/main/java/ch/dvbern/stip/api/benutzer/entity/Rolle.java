package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Entity
@Table(
    name = "rolle",
    indexes = {
        @Index(name = "IX_rolle_mandant", columnList = "mandant"),
        @Index(name = "IX_keycloak_identifier", columnList = "keycloak_identifier", unique = true)
    }
)
@Audited
@Getter
@Setter
public class Rolle extends AbstractMandantEntity {
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "keycloak_identifier", unique = true)
    private String keycloakIdentifier;
}
