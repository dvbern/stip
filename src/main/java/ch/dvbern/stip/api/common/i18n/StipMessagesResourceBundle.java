package ch.dvbern.stip.api.common.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.checkerframework.checker.nullness.qual.NonNull;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;

public final class StipMessagesResourceBundle {

	private StipMessagesResourceBundle() {
	}

	private static final String MESSAGE_BUNDLE_NAME = "messages";
	private static final ResourceBundle BUNDLE_DE = ResourceBundle.getBundle(
			MESSAGE_BUNDLE_NAME,
			Locale.GERMAN,
			requireNonNull(currentThread().getContextClassLoader()));

	private static final ResourceBundle BUNDLE_FR = ResourceBundle.getBundle(
			MESSAGE_BUNDLE_NAME,
			Locale.FRENCH,
			requireNonNull(currentThread().getContextClassLoader()));

	@NonNull
	public static String getMessage(
			@NonNull String key,
			@NonNull Locale locale,
			Object... args
	) {
		ResourceBundle bundle = getBundle(locale);
		try {
			String template = bundle.getString(key);
			return MessageFormat.format(template, args);
		} catch (MissingResourceException ignore) {
			return "????" + key + "????";
		}
	}

	@NonNull
	private static ResourceBundle getBundle(@NonNull Locale locale) {
		if (locale.getLanguage().equalsIgnoreCase("FR")) {
			return BUNDLE_FR;
		}
		return BUNDLE_DE;
	}
}
