package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.vendorposting.SenderParmsDelivery;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

public class VendorPostingCreateRequestMapper {
    VendorPostingCreateMapper vendorPostingCreateMapper = new VendorPostingCreateMapper();
    public VendorPostingCreateRequest toVendorPostingCreateRequest(Auszahlung auszahlung, BigInteger sysid, BigDecimal deliveryId) {
        VendorPostingCreateRequest vendorPostingCreateRequest = new VendorPostingCreateRequest();
        VendorPostingCreateRequest.VENDORPOSTING vendorposting = vendorPostingCreateMapper.toVendorPosting(auszahlung);
        vendorPostingCreateRequest.getVENDORPOSTING().add(vendorposting);
        SenderParmsDelivery senderParmsDelivery = new SenderParmsDelivery();
        senderParmsDelivery.setDELIVERYID(deliveryId);
        senderParmsDelivery.setSYSID(sysid);
        vendorPostingCreateRequest.setSENDER(senderParmsDelivery);
        return vendorPostingCreateRequest;
    }
}
