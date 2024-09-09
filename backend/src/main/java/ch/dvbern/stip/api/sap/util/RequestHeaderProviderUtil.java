package ch.dvbern.stip.api.sap.util;

import org.eclipse.microprofile.config.ConfigProvider;

public interface RequestHeaderProviderUtil {
    default String getAuthHeader(){
        return ConfigProvider.getConfig().getValue("kstip.sap.auth-header-value",String.class);
    }
}
