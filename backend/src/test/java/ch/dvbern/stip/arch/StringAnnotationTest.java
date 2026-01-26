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

package ch.dvbern.stip.arch;

import java.util.Set;

import ch.dvbern.stip.arch.util.ArchTestUtil;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_INPUT_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_ISO2CODE_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_ISO3CODE_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MAX_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_MEDIUM_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_SMALL_LENGTH;
import static ch.dvbern.stip.api.common.util.Constants.DB_DEFAULT_STRING_TESTCASE_JSON_DATA_LENGTH;
import static com.tngtech.archunit.lang.SimpleConditionEvent.satisfied;
import static com.tngtech.archunit.lang.SimpleConditionEvent.violated;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

@Execution(ExecutionMode.CONCURRENT)
class StringAnnotationTest {
    @Test
    void entity_string_fields_are_annotated_consistently() {
        final var rule = fields().that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Entity.class)
            .and()
            .haveRawType(String.class)
            .should()
            .beAnnotatedWith(Size.class)
            .andShould()
            .beAnnotatedWith(Column.class);

        rule.check(ArchTestUtil.APP_CLASSES);
    }

    @Test
    void entity_string_fields_size_column_same_length() {
        final var rule = fields().that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Entity.class)
            .and()
            .haveRawType(String.class)
            .and()
            .areAnnotatedWith(Size.class)
            .and()
            .areAnnotatedWith(Column.class)
            .should(new SizeColumnFieldSameLengthCondition());

        rule.check(ArchTestUtil.APP_CLASSES);
    }

    @Test
    void entity_string_fields_size_predefined_length() {
        final var rule = fields().that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Entity.class)
            .and()
            .haveRawType(String.class)
            .and()
            .areAnnotatedWith(Size.class)
            .and()
            .areAnnotatedWith(Column.class)
            .should(new SizeColumnFieldDefinedLengthCondition());

        rule.check(ArchTestUtil.APP_CLASSES);
    }

    abstract static class SizeColumnFieldCondition extends ArchCondition<JavaField> {
        protected SizeColumnFieldCondition(final String description) {
            super(description);
        }

        abstract boolean doCheck(final Column column, final Size size);

        abstract String getText(final boolean success, final String fieldPath);

        @Override
        public void check(JavaField item, ConditionEvents events) {
            Column columnAnnotation = item.getAnnotationOfType(Column.class);
            Size sizeAnnotation = item.getAnnotationOfType(Size.class);

            final String fieldPath = String.format("%s.%s", item.getOwner().getName(), item.getName());
            final var success = doCheck(columnAnnotation, sizeAnnotation);

            if (success) {
                events.add(satisfied(item, getText(success, fieldPath)));
            } else {
                events.add(violated(item, getText(success, fieldPath)));
            }
        }
    }

    private static class SizeColumnFieldSameLengthCondition extends SizeColumnFieldCondition {
        public SizeColumnFieldSameLengthCondition() {
            super("Size and Column Annotation define same length");
        }

        @Override
        public boolean doCheck(final Column column, final Size size) {
            return column.length() == size.max();
        }

        @Override
        String getText(boolean success, String fieldPath) {
            if (success) {
                return String.format("%s: Size and Column Annotation define same length", fieldPath);
            }
            return String.format("%s: Size and Column Annotation don't define same length", fieldPath);
        }
    }

    private static class SizeColumnFieldDefinedLengthCondition extends SizeColumnFieldCondition {
        static final Set<Integer> VALID_STRING_LENGTHS = Set.of(
            DB_DEFAULT_STRING_SMALL_LENGTH,
            DB_DEFAULT_STRING_MEDIUM_LENGTH,
            DB_DEFAULT_STRING_INPUT_MAX_LENGTH,
            DB_DEFAULT_STRING_MAX_LENGTH,
            DB_DEFAULT_STRING_ISO3CODE_LENGTH,
            DB_DEFAULT_STRING_ISO2CODE_LENGTH,
            DB_DEFAULT_STRING_TESTCASE_JSON_DATA_LENGTH
        );

        public SizeColumnFieldDefinedLengthCondition() {
            super("Size and Column Annotation define preset length");
        }

        @Override
        public boolean doCheck(final Column column, final Size size) {
            return VALID_STRING_LENGTHS.contains(column.length()) && VALID_STRING_LENGTHS.contains(size.max());
        }

        @Override
        String getText(boolean success, String fieldPath) {
            if (success) {
                return String.format("%s: Size and Column Annotation define preset length", fieldPath);
            }
            return String.format("%s: Size and Column Annotation don't define preset length", fieldPath);
        }
    }
}
