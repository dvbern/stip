package ch.dvbern.stip.api.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NullDiffUtilTest {
    @Test
    void test() {
        assertFalse(NullDiffUtil.hasNullChanged(new Object(), new Object()));
        assertTrue(NullDiffUtil.hasNullChanged(new Object(), null));
        assertTrue(NullDiffUtil.hasNullChanged(null, new Object()));
        assertFalse(NullDiffUtil.hasNullChanged(null, null));
    }
}
