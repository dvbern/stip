package ch.dvbern.stip.api.sozialdienst.entity;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.EMAIL_VALIDATION_PATTERN;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EMAIL_MESSAGE;

@Getter
@Setter
@Entity
@Table(
    name = "sozialdienst_admin",
    indexes = @Index(name = "IX_sozialdienst_admin_mandant", columnList = "mandant")
)public class SozialdienstAdmin extends AbstractMandantEntity {
    @NotNull
    @Pattern(regexp = EMAIL_VALIDATION_PATTERN, message = VALIDATION_EMAIL_MESSAGE)
    @Size(max = DB_DEFAULT_MAX_LENGTH)
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "vorname",nullable = false)
    private String vorname;

    @NotNull
    @Column(name = "nachname",nullable = false)
    private String nachname;


}
