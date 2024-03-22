package ch.dvbern.stip.api.gesuchsjahr.resource;

import java.util.UUID;

import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrService;
import ch.dvbern.stip.generated.api.GesuchsjahrResource;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@RequestScoped
@AllArgsConstructor
public class GesuchsjahrResourceImpl implements GesuchsjahrResource {

	private final GesuchsjahrService gesuchsjahrService;

	@RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
	@Override
	public Response getGesuchsjahr(UUID gesuchsjahrId) {
		var gesuchsjahr = gesuchsjahrService.getGesuchsjahr(gesuchsjahrId);
		return Response.ok(gesuchsjahr).build();
	}

	@RolesAllowed({ ROLE_GESUCHSTELLER, ROLE_SACHBEARBEITER })
	@Override
	public Response getGesuchsjahre() {
		return Response.ok(gesuchsjahrService.getGesuchsjahre()).build();
	}

	@RolesAllowed(ROLE_ADMIN)
	@Override
	public Response createGesuchsjahr(GesuchsjahrCreateDto gesuchsjahrCreateDto) {
		var gesuchsjahr = gesuchsjahrService.createGesuchsjahr(gesuchsjahrCreateDto);
		return Response.ok(gesuchsjahr).build();
	}

	@RolesAllowed(ROLE_ADMIN)
	@Override
	public Response updateGesuchsjahr(UUID gesuchsjahrId, GesuchsjahrUpdateDto gesuchsjahrUpdateDto) {
		var gesuchsjahr = gesuchsjahrService.updateGesuchsjahr(gesuchsjahrId, gesuchsjahrUpdateDto);
    	return Response.ok(gesuchsjahr).build();
	}

	@RolesAllowed(ROLE_ADMIN)
	@Override
	public Response publishGesuchsjahr(UUID gesuchsjahrId) {
		gesuchsjahrService.publishGesuchsjahr(gesuchsjahrId);
    	return Response.ok().build();
	}

	@RolesAllowed(ROLE_ADMIN)
	@Override
	public Response deleteGesuchsjahr(UUID gesuchsjahrId) {
		gesuchsjahrService.deleteGesuchsjahr(gesuchsjahrId);
    	return Response.noContent().build();
	}
}
