package ch.dvbern.stip.api.lebenslauf.type;

import lombok.Getter;

@Getter
public enum LebenslaufAusbildungsArt {
	BERUFSVORBEREITENDES_SCHULJAHR(false),
	VORLEHRE(false),
	GESTALTERISCHE_VORKURSE(false),
	EIDGENOESSISCHES_BERUFSATTEST(false),
	EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS(true),
	BERUFSMATURITAET_NACH_LEHRE(false),
	FACHMATURITAET(false),
	GYMNASIALE_MATURITAETSSCHULEN(false),
	EIDGENOESSISCHES_DIPLOM(false),
	EIDGENOESSISCHER_FACHAUSWEIS(false),
	DIPLOM_HOEHERE_FACHSCHULE(false),
	BACHELOR_HOCHSCHULE_UNI(false),
	BACHELOR_FACHHOCHSCHULE(true),
	MASTER(true),
	ANDERER_BILDUNGSABSCHLUSS(false);

	private final boolean isBerufsbefaehigenderAbschluss;

	LebenslaufAusbildungsArt(boolean isBerufsbefaehigenderAbschluss) {
		this.isBerufsbefaehigenderAbschluss = isBerufsbefaehigenderAbschluss;
	}
}
