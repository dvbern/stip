package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.adresse.repo.AdresseRepository;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungDiffUtil;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class AuszahlungMapper extends EntityUpdateMapper<AuszahlungUpdateDto, Auszahlung> {
    @Inject
    AdresseRepository adresseRepository;

    public abstract Auszahlung toEntity(AuszahlungDto auszahlungDto);

    public abstract AuszahlungDto toDto(Auszahlung auszahlung);

    public abstract Auszahlung partialUpdate(AuszahlungUpdateDto auszahlungUpdateDto, @MappingTarget Auszahlung auszahlung);

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final AuszahlungUpdateDto newAuszahlung,
        @MappingTarget final Auszahlung targetAuszahlung
    ) {
        resetFieldIf(
            () -> AuszahlungDiffUtil.hasAdresseChanged(newAuszahlung, targetAuszahlung),
            "Reset Adresse because ID has changed",
            () -> {
                final var newAdresseId = newAuszahlung.getAdresse().getId();
                if (newAdresseId != null) {
                    targetAuszahlung.setAdresse(adresseRepository.requireById(newAdresseId));
                } else {
                    targetAuszahlung.setAdresse(null);
                }
            }
        );
    }
}
