package ch.dvbern.stip.test.common.i18n;

import ch.dvbern.stip.api.common.i18n.StipEmailMessages;
import ch.dvbern.stip.api.common.i18n.StipMessagesResourceBundle;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StipMessagesResourceBundleTest {
    @Test
    void stipMessagesResourceBundleTest() {
        String a = StipMessagesResourceBundle.getMessage(StipEmailMessages.FEHLENDE_DOKUMENTE_SUBJECT.getMessage(), Locale.GERMAN);
        String b = StipMessagesResourceBundle.getMessage(StipEmailMessages.FEHLENDE_DOKUMENTE_SUBJECT.getMessage(), Locale.FRENCH);
        assertThat(a).isNotEqualTo(b);
    }
}
