package ch.dvbern.stip.api.common.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

@Qualifier
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Mapper(config = MappingConfig.class)
public @interface DateMapper {
}
