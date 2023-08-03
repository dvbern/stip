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

package ch.dvbern.stip.api.fall.entity;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(indexes = {
		@Index(name = "IX_fall_gesuchsteller_id", columnList = "gesuchsteller_id"),
		@Index(name = "IX_fall_sachbearbeiter_id", columnList = "sachbearbeiter_id"),
		// @Index(name = "IX_fall_sachbearbeiter_id", columnList = "sachbearbeiter_id"),
})
@Getter
@Setter
public class Fall extends AbstractEntity {

	@Column(columnDefinition = "int8 DEFAULT nextval('fall_nummer_seq')",
			insertable = false)
	private Long fallNummer;

	@NotNull
	@Column(nullable = false)
	private String mandant = MandantIdentifier.BERN.name();

	@OneToOne(optional = true, fetch = FetchType.LAZY, orphanRemoval = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_fall_gesuchsteller_id"), nullable = true)
	private Benutzer gesuchsteller;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_fall_sachbearbeiter_id"), nullable = true)
	private Benutzer sachbearbeiter;
}
