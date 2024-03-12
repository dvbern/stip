package ch.dvbern.stip.api.common.i18n.translations;

import ch.dvbern.oss.commons.i18nl10n.I18nMessage;
import ch.dvbern.oss.commons.i18nl10n.Translator;

import java.io.Serializable;

/**
 * Pure convenience wrapper for {@link Translator} to allow for easier usage in day-to-day usage.
 */
@SuppressWarnings("unused")
public interface TL extends Translator {

	default String translate(
			String key
	) {
		return translate(I18nMessage.of(key));
	}

	default String translate(
			String key,
			String a1,
			Serializable v1
	) {
		return translate(I18nMessage.of(key, a1, v1));
	}

	default String translate(
			String key,
			String a1,
			Serializable v1,
			String a2,
			Serializable v2
	) {
		return translate(I18nMessage.of(key, a1, v1, a2, v2));
	}

	default String translate(
			String key,
			String a1,
			Serializable v1,
			String a2,
			Serializable v2,
			String a3,
			Serializable v3
	) {
		return translate(I18nMessage.of(key, a1, v1, a2, v2, a3, v3));
	}

    @SuppressWarnings("java:S107")
	default String translate(
			String key,
			String a1,
			Serializable v1,
			String a2,
			Serializable v2,
			String a3,
			Serializable v3,
			String a4,
			Serializable v4
	) {
		return translate(I18nMessage.of(key, a1, v1, a2, v2, a3, v3, a4, v4));
	}

    @SuppressWarnings("java:S107")
	default String translate(
			String key,
			String a1,
			Serializable v1,
			String a2,
			Serializable v2,
			String a3,
			Serializable v3,
			String a4,
			Serializable v4,
			String a5,
			Serializable v5
	) {
		return translate(I18nMessage.of(key, a1, v1, a2, v2, a3, v3, a4, v4, a5, v5));
	}
}
