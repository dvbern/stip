package ch.dvbern.stip.api.familiensituation.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.Objects;

@Audited
@Entity
@Getter
@Setter
public class Familiensituation extends AbstractEntity {

	@NotNull
	@Column(nullable = false)
	private Boolean elternVerheiratetZusammen;

	@Column(nullable = true)
	private Boolean elternteilVerstorben;

	@Column(nullable = true)
	private Boolean elternteilUnbekanntVerstorben;

	@Column(nullable = true)
	private Boolean gerichtlicheAlimentenregelung;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ElternAbwesenheitsGrund mutterUnbekanntVerstorben;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ElternUnbekanntheitsGrund mutterUnbekanntGrund;

	@Column(nullable = true)
	private Boolean mutterWiederverheiratet;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ElternAbwesenheitsGrund vaterUnbekanntVerstorben;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ElternUnbekanntheitsGrund vaterUnbekanntGrund;

	@Column(nullable = true)
	private Boolean vaterWiederverheiratet;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Elternschaftsteilung sorgerecht;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Elternschaftsteilung obhut;

	@Column(nullable = true)
	private BigDecimal obhutMutter;

	@Column(nullable = true)
	private BigDecimal obhutVater;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Elternschaftsteilung werZahltAlimente;

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
		Familiensituation that = (Familiensituation) o;
		return Objects.equals(getElternVerheiratetZusammen(), that.getElternVerheiratetZusammen())
				&& Objects.equals(getElternteilVerstorben(), that.getElternteilVerstorben())
				&& Objects.equals(getElternteilUnbekanntVerstorben(), that.getElternteilUnbekanntVerstorben())
				&& Objects.equals(getGerichtlicheAlimentenregelung(), that.getGerichtlicheAlimentenregelung())
				&& getMutterUnbekanntVerstorben() == that.getMutterUnbekanntVerstorben()
				&& getMutterUnbekanntGrund() == that.getMutterUnbekanntGrund()
				&& Objects.equals(getMutterWiederverheiratet(), that.getMutterWiederverheiratet())
				&& getVaterUnbekanntVerstorben() == that.getVaterUnbekanntVerstorben()
				&& getVaterUnbekanntGrund() == that.getVaterUnbekanntGrund()
				&& Objects.equals(getVaterWiederverheiratet(), that.getVaterWiederverheiratet())
				&& getSorgerecht() == that.getSorgerecht()
				&& getObhut() == that.getObhut()
				&& Objects.equals(getObhutMutter(), that.getObhutMutter())
				&& Objects.equals(getObhutVater(), that.getObhutVater())
				&& getWerZahltAlimente() == that.getWerZahltAlimente();
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				super.hashCode(),
				getElternVerheiratetZusammen(),
				getElternteilVerstorben(),
				getElternteilUnbekanntVerstorben(),
				getGerichtlicheAlimentenregelung(),
				getMutterUnbekanntVerstorben(),
				getMutterUnbekanntGrund(),
				getMutterWiederverheiratet(),
				getVaterUnbekanntVerstorben(),
				getVaterUnbekanntGrund(),
				getVaterWiederverheiratet(),
				getSorgerecht(),
				getObhut(),
				getObhutMutter(),
				getObhutVater(),
				getWerZahltAlimente());
	}
}
