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
     * The implementing method must be annotated with {@code @AfterMapping} and is called after mapping source to
     * target.
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
     * @param checkCondition the condition to check
     * @param message the log message to generate if the condition is met
     * @param resetFieldAction the action to perform if the condition is met
     */
    protected static void resetFieldIf(BooleanSupplier checkCondition, String message, Runnable resetFieldAction) {
        if (checkCondition.getAsBoolean()) {
            LOG.debug(message);
            resetFieldAction.run();
        }
    }
}
