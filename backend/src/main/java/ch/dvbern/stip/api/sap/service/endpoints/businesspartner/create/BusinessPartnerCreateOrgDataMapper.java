package ch.dvbern.stip.api.sap.service.endpoints.businesspartner.create;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;

public class BusinessPartnerCreateOrgDataMapper {

    BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA toBusinessPartnerORGDATA(Auszahlung auszahlung){
        BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA orgdata = new BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA();
        orgdata.setNAME1(String.format("%s %s", auszahlung.getVorname(), auszahlung.getNachname()));
        return orgdata;
    }
}
