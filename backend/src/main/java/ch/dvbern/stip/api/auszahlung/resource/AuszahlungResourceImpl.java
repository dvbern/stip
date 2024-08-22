package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.service.AuszahlungSapService;
import ch.dvbern.stip.generated.api.AuszahlungResource;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
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
    public Response changeKreditor(AuszahlungDto auszahlungDto) {
        return auszahlungSapService.changeBusinessPartner(auszahlungDto);
    }

    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response createAuszahlung(AuszahlungDto auszahlungDto) {
        return auszahlungSapService.createAuszahlung(auszahlungDto);
    }

    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response createKreditor(AuszahlungDto auszahlungDto) {
        return auszahlungSapService.createBusinessPartner(auszahlungDto);
    }

    @RolesAllowed({ROLE_SACHBEARBEITER})
    @Override
    public Response getImportStatus(Integer deliveryId) {
        return auszahlungSapService.getImportStatus(deliveryId);
    }

}
