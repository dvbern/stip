package ch.dvbern.stip.api.auszahlung.sap.businesspartner.change;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
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
