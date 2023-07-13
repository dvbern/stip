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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.adresse.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_SMALL_VALUE_LENGTH;

@Entity
@Audited
@Getter
@Setter
public class Adresse extends AbstractEntity {
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Land land = Land.CH;

	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = true)
	private String coAdresse;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String strasse;

	@Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
	@Column(nullable = true)
	private String hausnummer;

	@NotNull
	@Size(max = DB_DEFAULT_SMALL_VALUE_LENGTH)
	@Column(nullable = false)
	private String plz;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String ort;

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
		Adresse adresse = (Adresse) o;
		return getLand() == adresse.getLand()
				&& getCoAdresse().equals(adresse.getCoAdresse())
				&& Objects.equals(getStrasse(), adresse.getStrasse())
				&& getHausnummer().equals(adresse.getHausnummer())
				&& Objects.equals(getPlz(), adresse.getPlz())
				&& Objects.equals(getOrt(), adresse.getOrt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				super.hashCode(),
				getLand(),
				getCoAdresse(),
				getStrasse(),
				getHausnummer(),
				getPlz(),
				getOrt());
	}
}
