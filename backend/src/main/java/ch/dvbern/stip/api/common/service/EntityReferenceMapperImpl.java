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
