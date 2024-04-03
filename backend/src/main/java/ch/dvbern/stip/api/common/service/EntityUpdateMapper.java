package ch.dvbern.stip.api.common.service;

import java.util.function.BooleanSupplier;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.MappingTarget;

/**
 * This abstract class serves as a base class for entity update mappers. It provides methods that can be overridden
 * to perform actions before and after mapping.
 *
 * @param <S> the source type
 * @param <T> the target type, which must extend AbstractEntity
 */
@Slf4j
public abstract class EntityUpdateMapper<S, T extends AbstractEntity> {

    /**
     * Resets dependent data after updating the target entity during mapping.
     * The implementing method must be annotated with {@code @AfterMapping} and is called after mapping source to target.
     * It is called by entity update mappers to perform any necessary actions after updating the target entity.
     *
     * @param source the source object that was mapped
     * @param target the target object that was updated
     */
    protected abstract void resetDependentDataBeforeUpdate(final S source, @MappingTarget final T target);

    /**
     * Resets a field if a condition is met. If the condition specified by the {@code checkCondition}
     * evaluates to {@code true}, the {@code resetFieldAction} is executed, and a debug log message
     * is generated using the {@code logMessage} parameter.
     *
     * @param checkCondition   the condition to check
     * @param message          the log message to generate if the condition is met
     * @param resetFieldAction the action to perform if the condition is met
     */
    protected static void resetFieldIf(BooleanSupplier checkCondition, String message, Runnable resetFieldAction) {
        if (checkCondition.getAsBoolean()) {
            LOG.debug(message);
            resetFieldAction.run();
        }
    }
}
