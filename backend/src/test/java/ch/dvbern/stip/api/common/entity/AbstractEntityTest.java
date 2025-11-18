/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.entity;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
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
