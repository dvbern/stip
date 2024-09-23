package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;

public class BusinessPartnerChangeAdresseMapper {
    BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS toBusinessPartnerAdresse(Auszahlung auszahlung) {
        BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS address = new BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS();
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
