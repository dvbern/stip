package ch.dvbern.stip.api.ausbildung.resource;

import ch.dvbern.stip.api.ausbildung.service.AusbildungsgangService;
import ch.dvbern.stip.generated.api.AusbildungsgangResource;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungsgangResourceImpl implements AusbildungsgangResource {

	private final UriInfo uriInfo;
	private final AusbildungsgangService ausbildungsgangService;

	@Override
	@RolesAllowed(ROLE_SACHBEARBEITER)
	public Response createAusbildungsgang(AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
		AusbildungsgangDto created = ausbildungsgangService.createAusbildungsgang(ausbildungsgangUpdateDto);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build()).build();
	}

	@Override
	@RolesAllowed(ROLE_SACHBEARBEITER)
	public Response deleteAusbildungsgang(UUID ausbildungsgangId) {
		 ausbildungsgangService.deleteAusbildungsgang(ausbildungsgangId);
		 return Response.noContent().build();
	}

	@Override
	@RolesAllowed({ROLE_SACHBEARBEITER, ROLE_GESUCHSTELLER})
	public Response getAusbildungsgang(UUID ausbildungsgangId) {
		return Response.ok(ausbildungsgangService.findById(ausbildungsgangId)).build();
	}

	@Override
	@RolesAllowed(ROLE_SACHBEARBEITER)
	public Response updateAusbildungsgang(UUID ausbildungsgangId, AusbildungsgangUpdateDto ausbildungsgangUpdateDto) {
		AusbildungsgangDto updated = ausbildungsgangService.updateAusbildungsgang(ausbildungsgangId, ausbildungsgangUpdateDto);
		return Response.accepted().build();
	}
}
