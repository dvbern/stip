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

import java.util.Arrays;

import ch.dvbern.stip.api.common.entity.AbstractMandantEntity;
import ch.dvbern.stip.api.notiz.entity.JuristischeAbklaerungNotiz;
import ch.dvbern.stip.api.sozialdienstadmin.entity.SozialdienstAdmin;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.arch.util.ArchTestUtil.APP_CLASSES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

class JpaTest {

    @Test
    void test_index_on_foreign_keys() {
        var rule = fields().that()
            .areAnnotatedWith(JoinColumn.class)
            .and()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Entity.class)
            .should()
            .beDeclaredInClassesThat()
            .areAnnotatedWith(Table.class)
            .andShould((new ArchCondition<>("have an index") {
                @Override
                public void check(JavaField javaField, ConditionEvents conditionEvents) {
                    final var fieldName = javaField.getName();
                    final var fieldOwner = javaField.getOwner();
                    final var tableAnnotation = fieldOwner.getAnnotationOfType(Table.class);

                    // don't check if it's not a foreign key constraint (e.g. on a inverse join column definition)
                    if (
                        javaField.getAnnotationOfType(JoinColumn.class)
                            .foreignKey()
                            .value() != ConstraintMode.CONSTRAINT
                    ) {
                        return;
                    }

                    if (tableAnnotation != null) {
                        final var hasIndex = tableAnnotation.indexes().length != 0;
                        if (hasIndex) {
                            return;
                        }
                    }

                    final var message = String.format(
                        "Foreign Key column %s on entity %s has no index",
                        fieldName,
                        fieldOwner.getSimpleName()
                    );
                    conditionEvents.add(SimpleConditionEvent.violated(javaField, message));
                }
            }));

        rule.check(APP_CLASSES);
    }

    @Test
    void test_index_on_tenant_field() {
        var rule = classes().that()
            .areAssignableTo(AbstractMandantEntity.class)
            .and()
            .areNotAssignableTo(JuristischeAbklaerungNotiz.class)
            .areNotAssignableTo(SozialdienstAdmin.class)
            .and()
            .areAnnotatedWith(Entity.class)
            .should((new ArchCondition<>("have an index") {
                @Override
                public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                    var tableAnnotation = javaClass.getAnnotationOfType(Table.class);
                    if (tableAnnotation != null) {
                        final var hasIndex = Arrays.stream(tableAnnotation.indexes())
                            .anyMatch(index -> index.columnList().contains("mandant"));

                        if (hasIndex) {
                            return;
                        }
                    }

                    String message = String.format("Mandant column on entity %s has no index", javaClass.getName());
                    conditionEvents.add(SimpleConditionEvent.violated(javaClass, message));
                }
            }));

        rule.check(APP_CLASSES);
    }
}
