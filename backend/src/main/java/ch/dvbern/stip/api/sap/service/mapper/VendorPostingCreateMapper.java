package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateRequest;

public class VendorPostingCreateMapper {
    PaymentDetailCreateMapper paymentDetailCreateMapper = new PaymentDetailCreateMapper();
    public VendorPostingCreateRequest.VENDORPOSTING toVendorPosting(Auszahlung auszahlung){
        VendorPostingCreateRequest.VENDORPOSTING vendorPosting = new VendorPostingCreateRequest.VENDORPOSTING();
        vendorPosting.setVENDOR(new VendorPostingCreateRequest.VENDORPOSTING.VENDOR());
        vendorPosting.setPAYMENTDETAIL(paymentDetailCreateMapper.toPaymentDetail(auszahlung));
        return vendorPosting;
    }
}
