package ch.dvbern.stip.api.common.i18n;

import java.util.Locale;

import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StipMessagesResourceBundleTest {
    @Test
    void stipMessagesResourceBundleTest() {
        String a = StipMessagesResourceBundle.getMessage(
            StipEmailMessages.EINGEREICHT.getMessage(),
            Locale.GERMAN);
        String b = StipMessagesResourceBundle.getMessage(
            StipEmailMessages.EINGEREICHT.getMessage(),
            Locale.FRENCH);
        assertThat(a).isNotEqualTo(b);
    }
}
