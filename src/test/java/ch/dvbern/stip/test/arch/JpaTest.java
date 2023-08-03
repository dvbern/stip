package ch.dvbern.stip.test.arch;

import ch.dvbern.stip.api.common.entity.StipPhysicalNamingStrategy;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static ch.dvbern.stip.test.arch.util.ArchTestUtil.APP_CLASSES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

public class JpaTest {

    @Test
    public void test_index_on_foreign_keys() {
        var rule = fields().that().areAnnotatedWith(JoinColumn.class)
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
                        if (javaField.getAnnotationOfType(JoinColumn.class).foreignKey().value() != ConstraintMode.CONSTRAINT) {
                            return;
                        }

                        if (tableAnnotation != null) {
                            final var hasIndex = Arrays.stream(tableAnnotation.indexes())
                                    .anyMatch(index -> index.columnList().contains(StipPhysicalNamingStrategy.toSnakeCase(fieldName)));

                            if (hasIndex) {
                                return;
                            }
                        }

                        String message = String.format("Foreign Key column %s on entity %s has no index", fieldName, fieldOwner.getSimpleName());
                        conditionEvents.add(SimpleConditionEvent.violated(javaField, message));

                    }
                }));

        rule.check(APP_CLASSES);
    }
}
