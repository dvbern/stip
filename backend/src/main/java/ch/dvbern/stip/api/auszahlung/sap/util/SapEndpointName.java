package ch.dvbern.stip.api.auszahlung.sap.util;

import lombok.Getter;

@Getter
public enum SapEndpointName {
    IMPORT_STATUS("IMPORT_STATUS");

    private final String name;
    SapEndpointName(String name){
        this.name = name;
    }
}
