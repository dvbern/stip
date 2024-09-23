package ch.dvbern.stip.api.common.validation;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

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
        assertThat(checkEmailPattern(""), is(false));
        assertThat(checkEmailPattern("hallo@welt"), is(false));
        assertThat(checkEmailPattern("hallo@welt."), is(false));
        assertThat(checkEmailPattern("welt.ch"), is(false));
        assertThat(checkEmailPattern("@welt.ch"), is(false));
        assertThat(checkEmailPattern("hallo@welt.c"), is(true));
        assertThat(checkEmailPattern("_hallo@welt.ch"), is(true));
        assertThat(checkEmailPattern("hallo_@welt.ch"), is(true));
        assertThat(checkEmailPattern(".hallo@welt.ch"), is(false));
        assertThat(checkEmailPattern("hallo.@welt.ch"), is(false));
        assertThat(checkEmailPattern("hans..muster@welt.ch"), is(false));
        assertThat(checkEmailPattern("heinz.mueller@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("heinz-mueller@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("heinz_mueller@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("aaa|asdsads@sdsdsd.com"), is(true));
        assertThat(checkEmailPattern("heinz-mueller+001@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("aaa%asdsads@sdsdsd.com"), is(true));
        assertThat(checkEmailPattern("aaa&asdsads@sdsdsd.com"), is(true));
        assertThat(checkEmailPattern("h@dvbern.ch"), is(true));
        assertThat(checkEmailPattern("h@b.ch"), is(true));
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
        assertThat(checkEmailPattern("aaaçasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaa asdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaäasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaéasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaüasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaÄasdsads@sdsdsd.com"), is(false));
        assertThat(checkEmailPattern("aaaôasdsads@sdsdsd.com"), is(false));
    }
}
