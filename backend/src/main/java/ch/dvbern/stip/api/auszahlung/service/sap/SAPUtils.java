package ch.dvbern.stip.api.auszahlung.service.sap;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SAPUtils {
    int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public String generateBusinessPartnerId(){
        return String.valueOf(generateRandomNumber(0,Integer.MAX_VALUE));
    }
    public String generateDeliveryId(){
        return String.valueOf(generateRandomNumber(0,Integer.MAX_VALUE));
    }

    public SapMessageType parseSapMessageType(String sapXmlResponse){
        if(sapXmlResponse.contains("<TYPE>S</TYPE>")){
            return SapMessageType.S;
        }
        else{
            return SapMessageType.E;
        }
    }

    public String parseBusinessPartnerId(String sapXmlResponse){
        String startTag = "<BPARTNER>";
        String endTag = "</BPARTNER>";
        if(sapXmlResponse.contains("<BPARTNER>")){
            return sapXmlResponse.substring(sapXmlResponse.indexOf(startTag) + startTag.length(),sapXmlResponse.indexOf(endTag) );
        }else{return null;}
    }
}
