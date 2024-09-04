package ch.dvbern.stip.api.auszahlung.sap.businesspartner.create;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named
public class BusinessPartnerCreatePersDataMapper {
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA toBusinessPartnerPERSDATA(Auszahlung auszahlung){
        BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA persdata = new BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA();
        persdata.setFIRSTNAME(auszahlung.getVorname());
        persdata.setLASTNAME(auszahlung.getNachname());
        return persdata;
    }


}
