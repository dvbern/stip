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

import ch.dvbern.stip.arch.util.ArchTestUtil;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.SimpleConditionEvent.satisfied;
import static com.tngtech.archunit.lang.SimpleConditionEvent.violated;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

public class StringAnnotationTest {
    @Test
    void entity_string_fields_are_annotated_consistently() {
        final var rule = fields().that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Entity.class)
            .and()
            .haveRawType(String.class)
            .should()
            .beAnnotatedWith(
                Size.class
            )
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
            .should(new SizeColumnFieldCondition());

        rule.check(ArchTestUtil.APP_CLASSES);
    }

    private static class SizeColumnFieldCondition extends ArchCondition<JavaField> {
        public SizeColumnFieldCondition() {
            super("Size and Column Annotation define same length");
        }

        @Override
        public void check(JavaField item, ConditionEvents events) {
            Column columnAnnotation = item.getAnnotationOfType(Column.class);
            Size sizeAnnotation = item.getAnnotationOfType(Size.class);
            var className = item.getOwner().getName();
            var fieldName = item.getName();

            if (columnAnnotation.length() == sizeAnnotation.max()) {
                events.add(
                    satisfied(
                        item,
                        String.format("%s.%s: Size and Column Annotation define same length", className, fieldName)
                    )
                );
            } else {
                events.add(
                    violated(
                        item,
                        String.format("%s.%s: Size and Column don't define same length", className, fieldName)
                    )
                );
            }
        }
    }
}
