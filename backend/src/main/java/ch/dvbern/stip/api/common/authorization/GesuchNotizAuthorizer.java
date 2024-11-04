package ch.dvbern.stip.api.common.authorization;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class GesuchNotizAuthorizer extends BaseAuthorizer {
}
