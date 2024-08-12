package ch.dvbern.stip.api.partner.service;

import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.partner.repo.PartnerRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;

    public void deletePartner(final Partner partner) {
        partnerRepository.delete(partner);
    }
}
