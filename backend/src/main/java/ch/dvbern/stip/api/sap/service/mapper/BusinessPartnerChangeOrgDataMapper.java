package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;

public class BusinessPartnerChangeOrgDataMapper {

    BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA toBusinessPartnerORGDATA(Auszahlung auszahlung){
        BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA orgdata = new BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA();
        orgdata.setNAME1(String.format("%s %s", auszahlung.getVorname(), auszahlung.getNachname()));
        return orgdata;
    }
}
