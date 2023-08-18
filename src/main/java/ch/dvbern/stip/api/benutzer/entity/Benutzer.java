package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.validation.AhvConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Entity
@Table(
        indexes = {@Index(name = "IX_benutzer_keycloak_id", columnList = "keycloak_id", unique = true),
				   @Index(name = "IX_benuter_mandant", columnList = "mandant")}
)
@Audited
@Getter
@Setter
public class Benutzer extends AbstractEntity {

    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "keycloak_id", nullable = true, unique = true)
    private String keycloakId;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String nachname;

    @NotNull
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(nullable = false)
    private String vorname;

    @AhvConstraint(optional = true)
    @Column(nullable = true)
	private String sozialversicherungsnummer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BenutzerStatus benutzerStatus;
}
