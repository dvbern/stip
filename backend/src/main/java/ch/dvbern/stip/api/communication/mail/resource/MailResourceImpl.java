package ch.dvbern.stip.api.communication.mail.resource;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.generated.api.MailResource;
import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.SEND_EMAIL;

@RequestScoped
@RequiredArgsConstructor
public class MailResourceImpl implements MailResource {

    private final MailService mailService;

    @Override
    @AllowAll
    @RolesAllowed(SEND_EMAIL)
    public Response sendWelcomeEmail(WelcomeMailDto welcomeMailDto) {
        mailService.sendBenutzerWelcomeEmail(welcomeMailDto);
        return Response.ok().build();
    }
}
