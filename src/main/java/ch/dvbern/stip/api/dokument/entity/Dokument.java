package ch.dvbern.stip.api.dokument.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Objects;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_MAX_LENGTH;

@Audited
@Entity
@Getter
@Setter
public class Dokument extends AbstractEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_dokument_dokument_typ_id"), nullable = false)
	private GesuchDokument gesuchDokument;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String filename;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String filepfad;

	@NotNull
	@Size(max = DB_DEFAULT_MAX_LENGTH)
	@Column(nullable = false)
	private String filesize;

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
		Dokument dokument = (Dokument) o;
		return Objects.equals(getGesuchDokument(), dokument.getGesuchDokument())
				&& Objects.equals(getFilename(), dokument.getFilename())
				&& Objects.equals(getFilepfad(), dokument.getFilepfad())
				&& Objects.equals(getFilesize(), dokument.getFilesize());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getGesuchDokument(), getFilename(), getFilepfad(), getFilesize());
	}
}
