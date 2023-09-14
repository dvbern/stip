package ch.dvbern.stip.api.common.i18n;

import lombok.Getter;

@Getter
public enum StipEmailMessages {

	 FEHLENDE_DOKUMENTE_SUBJECT("mail_subject_fehlende_dokumente");

	private final String message;

	StipEmailMessages(String message) {
		this.message = message;
	}
}
