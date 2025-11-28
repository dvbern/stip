package ch.dvbern.stip.api.demo.resource;

import ch.dvbern.stip.api.common.authorization.DemoDataAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.OidcPermissions;
import ch.dvbern.stip.generated.api.DemoDataResource;
import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataSlimDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@Validated
@RequestScoped
@RequiredArgsConstructor
public class DemoDataResourceImpl implements DemoDataResource {
    private final DemoDataAuthorizer demoDataAuthorizer;

    @Override
    @RolesAllowed(OidcPermissions.SB_GESUCH_UPDATE)
    public ApplyDemoDataResponseDto applyDemoData(String id) {
        demoDataAuthorizer.canRead();
        return null;
    }

    @Override
    public DemoDataSlimDto getAllDemoData() {
        return null;
    }
}
