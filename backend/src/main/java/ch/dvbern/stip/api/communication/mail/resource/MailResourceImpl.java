package ch.dvbern.stip.api.communication.mail.resource;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.generated.api.MailResource;
import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class MailResourceImpl implements MailResource {

    private final MailService mailService;

    @Override
    public Response sendWelcomeEmail(WelcomeMailDto welcomeMailDto) {
        mailService.sendBenutzerWelcomeEmail(welcomeMailDto);
        return Response.ok().build();
    }
}
