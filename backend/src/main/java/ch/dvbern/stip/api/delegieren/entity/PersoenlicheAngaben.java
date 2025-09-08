/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.delegieren.entity;

import java.io.Serializable;
import java.time.LocalDate;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.EMAIL_VALIDATION_PATTERN;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EMAIL_MESSAGE;

@Getter
@Setter
@Embeddable
public class PersoenlicheAngaben implements Serializable {
    @NotNull
    @Column(name = "anrede", nullable = false)
    @Enumerated(EnumType.STRING)
    private @Valid Anrede anrede;

    @NotNull
    @Column(name = "nachname", nullable = false)
    private @Valid String nachname;

    @NotNull
    @Column(name = "vorname", nullable = false)
    private @Valid String vorname;

    @NotNull
    @Column(name = "geburtsdatum", nullable = false)
    private @Valid LocalDate geburtsdatum;

    @NotNull
    @Column(name = "email", nullable = false)
    @Pattern(
        regexp = EMAIL_VALIDATION_PATTERN, message = VALIDATION_EMAIL_MESSAGE, flags = { Pattern.Flag.CASE_INSENSITIVE }
    )
    private @Valid String email;

    @NotNull
    @Column(name = "sprache", nullable = false)
    private @Valid Sprache sprache;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "adresse_id", foreignKey = @ForeignKey(name = "FK_persoenliche_angaben_adresse_id"))
    private @Valid Adresse adresse;
}
