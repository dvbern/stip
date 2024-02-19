package ch.dvbern.stip.api.common.validation;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.EMAIL_VALIDATION_PATTERN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EmailPatternTest {

    public static boolean checkEmailPattern(String emailAddress) {
        return Pattern.compile(EMAIL_VALIDATION_PATTERN)
            .matcher(emailAddress)
            .matches();
    }

    @Test
    void testEmailPattern() {
        // Unvalid Email
        assertThat(checkEmailPattern(""), is(false));
        assertThat(checkEmailPattern(""), is(false));
        assertThat(checkEmailPattern("hallo@welt"), is(false));
        assertThat(checkEmailPattern("hallo@welt."), is(false));
        assertThat(checkEmailPattern("welt.ch"), is(false));
        assertThat(checkEmailPattern("@welt.ch"), is(false));
        assertThat(checkEmailPattern("_hallo@welt.ch"), is(false));
        assertThat(checkEmailPattern("_hallo@welt.ch"), is(false));
        assertThat(checkEmailPattern("hallo@welt.c"), is(false));

        // Email valid
        assertThat(checkEmailPattern("heinz.mueller@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("heinz_mueller@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("heinz-mueller@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("h@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("h@b.ch"), is(true));

        // Valid Email mit ungueltige Karaktern
        assertThat(checkEmailPattern("()<>,;:\"[]|ç%&@adsad.com"), is(false));
        assertThat(checkEmailPattern("aaa(asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa)asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa<asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa>asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa@asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa,asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa;asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa:asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa\"asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa[asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa]asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa|asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaçasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa%asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa&asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaäasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaéasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaüasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaÄasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaôasdsads@sdsdsd.com"), is(false));
    }
}
