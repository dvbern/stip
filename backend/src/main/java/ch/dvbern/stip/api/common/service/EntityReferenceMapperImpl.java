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

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

@EntityReferenceMapper
@Mapper(config = MappingQualifierConfig.class)
public class EntityReferenceMapperImpl {

    @EntityIdReference
    public <T extends AbstractEntity> T getReference(UUID id, @TargetType Class<T> entityClass)
    throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (id == null) {
            return null;
        }
        T reference = entityClass.getDeclaredConstructor().newInstance();
        reference.setId(id);
        return reference;
    }
}
