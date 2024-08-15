package ch.dvbern.stip.api.auszahlung.util;

import org.eclipse.microprofile.config.ConfigProvider;

public interface RequestHeaderProviderUtil {
    default String getAuthHeader(){
        return ConfigProvider.getConfig().getValue("kstip.stip_sap_auth_header_value",String.class);
    }
}
