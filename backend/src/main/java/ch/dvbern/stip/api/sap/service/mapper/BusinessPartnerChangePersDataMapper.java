package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named
public class BusinessPartnerChangePersDataMapper {
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA toBusinessPartnerPERSDATA(Auszahlung auszahlung){
        BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA persdata = new BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA();
        persdata.setFIRSTNAME(auszahlung.getVorname());
        persdata.setLASTNAME(auszahlung.getNachname());
        return persdata;
    }


}
