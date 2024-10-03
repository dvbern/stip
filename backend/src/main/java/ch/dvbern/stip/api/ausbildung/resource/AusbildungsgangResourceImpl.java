package ch.dvbern.stip.api.ausbildung.resource;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsgangService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.generated.api.AusbildungsgangResource;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangResourceImpl implements AusbildungsgangResource {
    private final AusbildungsgangService ausbildungsgangService;

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    @AllowAll
    public Response createAusbildungsgang(AusbildungsgangCreateDto ausbildungsgangCreateDto) {
        AusbildungsgangDto created = ausbildungsgangService.createAusbildungsgang(ausbildungsgangCreateDto);
        return Response.ok(created).build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_DELETE)
    @AllowAll
    public Response deleteAusbildungsgang(UUID ausbildungsgangId) {
        ausbildungsgangService.deleteAusbildungsgang(ausbildungsgangId);
        return Response.noContent().build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    @AllowAll
    public Response getAusbildungsgang(UUID ausbildungsgangId) {
        return Response.ok(ausbildungsgangService.findById(ausbildungsgangId)).build();
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    @AllowAll
    public Response updateAusbildungsgang(UUID ausbildungsgangId, AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
        AusbildungsgangDto updated = ausbildungsgangService.updateAusbildungsgang(ausbildungsgangId, ausbildungsgangUpdateDto);
        return Response.ok(updated).build();
    }
}
