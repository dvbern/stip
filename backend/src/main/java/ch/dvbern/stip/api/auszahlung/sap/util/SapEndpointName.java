package ch.dvbern.stip.api.auszahlung.sap.util;

import lombok.Getter;

@Getter
public enum SapEndpointName {
    IMPORT_STATUS("KTBE_ERP_FI:IMPORT_STATUS"),
    BUSINESPARTNER("KTBE_MDG:BUSINESS_PARTNER"),
    VENDORPOSTING("KTBE_ERP_FI:VENDOR_POSTING");

    private final String name;
    SapEndpointName(String name){
        this.name = name;
    }
}
