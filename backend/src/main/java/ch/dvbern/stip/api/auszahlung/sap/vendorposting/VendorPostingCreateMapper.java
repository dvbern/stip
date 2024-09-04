package ch.dvbern.stip.api.auszahlung.sap.vendorposting;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;

public class VendorPostingCreateMapper {
    PaymentDetailCreateMapper paymentDetailCreateMapper = new PaymentDetailCreateMapper();
    public VendorPostingCreateRequest.VENDORPOSTING toVendorPosting(Auszahlung auszahlung){
        VendorPostingCreateRequest.VENDORPOSTING vendorPosting = new VendorPostingCreateRequest.VENDORPOSTING();
        vendorPosting.setVENDOR(new VendorPostingCreateRequest.VENDORPOSTING.VENDOR());
        vendorPosting.setPAYMENTDETAIL(paymentDetailCreateMapper.toPaymentDetail(auszahlung));
        return vendorPosting;
    }
}
