package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;

public class BusinessPartnerCreateAdresseMapper {
    BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS toBusinessPartnerAdresse(Auszahlung auszahlung) {
        BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS address = new BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS();
        Adresse adresse = auszahlung.getAdresse();
        if(adresse != null) {
            address.setCITY(adresse.getOrt());
            address.setCOUNTRY(adresse.getLand().getLandDE());
            address.setCONAME(adresse.getCoAdresse());
            address.setSTREET(adresse.getStrasse());
            address.setHOUSENO(adresse.getHausnummer());
            address.setPOSTLCOD1(adresse.getPlz());
        }
        return address;
    }
}
