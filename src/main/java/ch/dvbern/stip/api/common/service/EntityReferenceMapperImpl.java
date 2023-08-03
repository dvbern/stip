package ch.dvbern.stip.api.common.service;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@EntityReferenceMapper
@Mapper(config = MappingConfig.class)
public class EntityReferenceMapperImpl {

    @EntityIdReference
    public <T extends AbstractEntity> T getReference(UUID id, @TargetType Class<T> entityClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T reference = entityClass.getDeclaredConstructor().newInstance();
        reference.setId(id);
        return reference;
    }
}
