package ch.dvbern.stip.api.plz.resource;

import ch.dvbern.stip.api.plz.repo.PlzRepository;
import ch.dvbern.stip.api.plz.service.PlzMapper;
import ch.dvbern.stip.api.plz.service.PlzService;
import ch.dvbern.stip.generated.api.PlzResource;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class PlzResourceImpl implements PlzResource {
    private final PlzService plzService;

    @RolesAllowed({ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER})
    @Override
    public Response getPlz() {
        return Response.ok(plzService.getAllPlz()).build();
    }
}
