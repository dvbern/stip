package ch.dvbern.stip.api.sap.util;

public enum SapMessageType {
    S("success"), E("no action | failure");

    public String getDescription() {
        return description;
    }

    private String description;
    SapMessageType(String descritpion){
        this.description = descritpion;
    }
}
