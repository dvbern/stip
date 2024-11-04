package ch.dvbern.stip.api.personinausbildung.service;

import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.PersonInAusbildungDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class PersonInAusbildungMapper extends EntityUpdateMapper<PersonInAusbildungUpdateDto, PersonInAusbildung> {
    public abstract PersonInAusbildung toEntity(PersonInAusbildungDto personInAusbildungDto);

    public abstract PersonInAusbildungDto toDto(PersonInAusbildung personInAusbildung);

    public abstract PersonInAusbildung partialUpdate(
        PersonInAusbildungUpdateDto personInAusbildungUpdateDto,
        @MappingTarget PersonInAusbildung personInAusbildung);

    public abstract PersonInAusbildungUpdateDto toUpdateDto(PersonInAusbildung personInAusbildung);

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final PersonInAusbildungUpdateDto newFormular,
        final @MappingTarget PersonInAusbildung targetFormular
    ) {
        resetFieldIf(
            () -> Boolean.TRUE.equals(newFormular.getIdentischerZivilrechtlicherWohnsitz()),
            "Reset IzW PLZ and Ort because IzW is true",
            () -> {
                newFormular.setIdentischerZivilrechtlicherWohnsitzOrt(null);
                newFormular.setIdentischerZivilrechtlicherWohnsitzPLZ(null);
            }
        );
    }
}
