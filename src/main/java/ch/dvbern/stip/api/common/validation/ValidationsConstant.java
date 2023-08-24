package ch.dvbern.stip.api.common.validation;

public final class ValidationsConstant {
	private ValidationsConstant() {
		throw new IllegalStateException("Validations Constant class");
	}
	public static final String EMAIL_VALIDATION_PATTERN = "^[a-z0-9]+[a-z0-9._-]*@[a-z0-9.-]+\\.[a-z]{2,4}$";

	public static final String VALIDATION_NACHNAME_NOTBLANK_MESSAGE =
			"{jakarta.validation.constraints.nachname.NotBlank.message}";
	public static final String VALIDATION_VORNAME_NOTBLANK_MESSAGE =
			"{jakarta.validation.constraints.vorname.NotBlank.message}";
	public static final String VALIDATION_IZW_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.izw.required.message}";
	public static final String VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.izw.required.null.message}";
	public static final String VALIDATION_HEIMATORT_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.heimatort.required.message}";
	public static final String VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.heimatort.required.null.message}";
	public static final String VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.niederlassungsstatus.required.message}";
	public static final String VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.niederlassungsstatus.required.null.message}";
	public static final String VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.wohnsitzanteil.required.message}";
	public static final String VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.wohnsitzanteil.required.null.message}";
	public static final String VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.alternativeAusbildung.required.message}";
	public static final String VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.alternativeAusbildung.required.null.message}";
	public static final String VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.ausbildung.required.message}";
	public static final String VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.ausbildung.required.null.message}";
	public static final String VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.werZahltAlimente.required.message}";
	public static final String VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.werZahltAlimente.required.null.message}";
	public static final String VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.obhut.required.message}";
	public static final String VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.obhut.required.null.message}";
	public static final String VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE =
			"{jakarta.validation.constraints.wohnsitzanteil.berechnung.message}";
	public static final String VALIDATION_OBHUT_GEMEINSAM_BERECHNUNG_MESSAGE =
			"{jakarta.validation.constraints.obhut.berechnung.message}";
	public static final String VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.lebenslaufitem.art.required.message}";
	public static final String VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE =
			"{jakarta.validation.constraints.lebenslaufitem.art.required.null.message}";
	public static final String VALIDATION_AHV_MESSAGE = "{jakarta.validation.constraints.ahv.message}";
	public static final String VALIDATION_EMAIL_MESSAGE = "{jakarta.validation.constraints.email.message}";
	public static final String VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE =
			"{jakarta.validation.constraints.familiensituation.eltern.entity.message}";
	public static final String VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE = "{jakarta.validation.constraints.lebenslauf.luckenlos.message}";
	public static final String VALIDATION_EINNAHMEN_KOSTEN_ALIMENTE_REQUIRED_MESSAGE = "{jakarta.validation.constraints.einnahmen.kosten.alimente.required.message}";
	public static final String VALIDATION_EINNAHMEN_KOSTEN_RENTEN_REQUIRED_MESSAGE = "{jakarta.validation.constraints.einnahmen.kosten.renten.required.message}";
	public static final String VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE = "{jakarta.validation.constraints.einnahmen.kosten.zulagen.required.message}";
	public static final String VALIDATION_EINNAHMEN_KOSTEN_DARLEHEN_REQUIRED_MESSAGE = "{jakarta.validation.constraints.einnahmen.kosten.darlehen.required.message}";
	public static final String VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE2_REQUIRED_MESSAGE = "{jakarta.validation.constraints.einnahmen.kosten.ausbildungskosten.stufe2.required.message}";
	public static final String VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE = "{jakarta.validation.constraints.einnahmen.kosten.ausbildungskosten.stufe3.required.message}";
	public static final String VALIDATION_LEBENSLAUF_AUSBILDUNG_UEBERSCHNEIDEN_MESSAGE = "{jakarta.validation.constraints.lebenslauf.ausbildung.ueberschneiden.message}";
}