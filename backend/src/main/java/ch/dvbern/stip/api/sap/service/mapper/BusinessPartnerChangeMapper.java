package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;

public class BusinessPartnerChangeMapper {
    BusinessPartnerChangeAdresseMapper adresseMapper = new BusinessPartnerChangeAdresseMapper();
    BusinessPartnerChangePersDataMapper persDataMapper = new BusinessPartnerChangePersDataMapper();
    BusinessPartnerChangeOrgDataMapper orgDataMapper = new BusinessPartnerChangeOrgDataMapper();

    BusinessPartnerChangeRequest.BUSINESSPARTNER toBusniessPartner(Auszahlung auszahlung){
        BusinessPartnerChangeRequest.BUSINESSPARTNER businesspartner = new BusinessPartnerChangeRequest.BUSINESSPARTNER();
        BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA persdata = persDataMapper.toBusinessPartnerPERSDATA(auszahlung);
        BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA orgdata = orgDataMapper.toBusinessPartnerORGDATA(auszahlung);
        BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS address = adresseMapper.toBusinessPartnerAdresse(auszahlung);
        businesspartner.setPERSDATA(persdata);
        businesspartner.setORGDATA(orgdata);
        businesspartner.getADDRESS().add(address);
        return businesspartner;
    }
}
