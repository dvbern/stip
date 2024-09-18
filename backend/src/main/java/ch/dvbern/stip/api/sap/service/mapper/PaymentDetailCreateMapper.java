package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateRequest;

public class PaymentDetailCreateMapper {
    public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL toPaymentDetail(Auszahlung auszahlung) {
        VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL paymentdetail = new VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL();
        VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN iban = new VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN();
        iban.setIBAN(auszahlung.getIban());
        paymentdetail.setIBAN(iban);
        return paymentdetail;
    }
}
