package ch.dvbern.stip.api.auszahlung.service.sap;

public enum SapMessageType {
    S("success"), E("no action");

    public String getDescription() {
        return description;
    }

    private String description;
    SapMessageType(String descritpion){
        this.description = descritpion;
    }
}
