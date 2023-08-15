package ch.dvbern.stip.test.common.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.entity.ReferenceEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class AbstractEntityTest {

    @Test
    void test_equals_self() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        assertThat(a, is(a));
    }

    @Test
    void test_not_equals_null() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        assertThat(null, not(a));
    }

    @Test
    void test_class_not_equal() {
        ReferenceEntity a = new ReferenceEntity(UUID.randomUUID());
        AbstractEntity b = new ReferenceEntity(UUID.randomUUID());

        assertThat(a, not(b));
    }

    @Test
    void test_equals_with_same_id() {
        var id = UUID.randomUUID();
        AbstractEntity a = new ReferenceEntity(id);
        AbstractEntity b = new ReferenceEntity(id);

        assertThat(a, is(b));
    }

    @Test
    void test_not_equals_with_different_id() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        AbstractEntity b = new ReferenceEntity(UUID.randomUUID());

        assertThat(a, not(b));
    }
}
