package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;
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
