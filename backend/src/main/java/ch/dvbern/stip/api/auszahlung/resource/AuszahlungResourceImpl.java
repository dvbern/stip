package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.service.AuszahlungSapService;
import ch.dvbern.stip.generated.api.AuszahlungResource;
import ch.dvbern.stip.generated.dto.ChangeAuszahlungKreditorDto;
import ch.dvbern.stip.generated.dto.CreateAuszahlungDto;
import ch.dvbern.stip.generated.dto.CreateAuszahlungKreditorDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class AuszahlungResourceImpl implements AuszahlungResource {
    private final AuszahlungSapService auszahlungSapService;
    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response changeKreditor(ChangeAuszahlungKreditorDto changeAuszahlungKreditorDto) {
        return auszahlungSapService.changeBusinessPartner(changeAuszahlungKreditorDto);
    }

    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response createAuszahlung(CreateAuszahlungDto createAuszahlungDto) {
        return auszahlungSapService.createAuszahlung(createAuszahlungDto);
    }

    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response createKreditor(CreateAuszahlungKreditorDto createAuszahlungKreditorDto) {
        return auszahlungSapService.createBusinessPartner(createAuszahlungKreditorDto);
    }

    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response getImportStatus(Integer deliveryId) {
        return auszahlungSapService.getImportStatus(deliveryId);
    }

}
