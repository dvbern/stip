package ch.dvbern.stip.api.common.entity;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.entity.ReferenceEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AbstractEntityTest {

    @Test
    void test_equals_self() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        assertThat(a.equals(a), is(true));
    }

    @Test
    void test_not_equals_null() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        assertThat(a.equals(null), is(false));
    }

    @Test
    void test_class_not_equal() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        Object b = new Object();

        assertThat(a.equals(b), is(false));
    }

    @Test
    void test_not_equals_with_null_id() {
        AbstractEntity a = new ReferenceEntity(null);
        AbstractEntity b = new ReferenceEntity(UUID.randomUUID());

        assertThat(a.equals(b), is(false));
    }

    @Test
    void test_equals_with_same_id() {
        var id = UUID.randomUUID();
        AbstractEntity a = new ReferenceEntity(id);
        AbstractEntity b = new ReferenceEntity(id);

        assertThat(a.equals(b), is(true));
    }

    @Test
    void test_not_equals_with_different_id() {
        AbstractEntity a = new ReferenceEntity(UUID.randomUUID());
        AbstractEntity b = new ReferenceEntity(UUID.randomUUID());

        assertThat(a.equals(b), is(false));
    }
}
