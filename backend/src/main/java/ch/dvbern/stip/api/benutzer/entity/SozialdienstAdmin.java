package ch.dvbern.stip.api.benutzer.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.EMAIL_VALIDATION_PATTERN;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EMAIL_MESSAGE;

@Audited
@Getter
@Setter
@Entity
@DiscriminatorValue("1")
@Table(
    name = "sozialdienst_id",
    indexes = {
        @Index(name = "IX_benutzer_keycloak_id", columnList = "keycloak_id", unique = true),
        @Index(name = "IX_benuter_mandant", columnList = "mandant"),
        @Index(name = "IX_benutzer_benutzereinstellungen_id", columnList = "benutzereinstellungen_id"),
    }
)
public class SozialdienstAdmin extends Benutzer {
    @Nullable
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Pattern(regexp = EMAIL_VALIDATION_PATTERN, message = VALIDATION_EMAIL_MESSAGE)
    @Column(name = "email", nullable = true)
    private String email;
}
