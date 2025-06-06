package ch.dvbern.stip.api.sap.generated.business_partner;

import java.net.URL;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 4.0.3
 * 2025-04-24T16:07:19.212+02:00
 * Generated source version: 4.0.3
 *
 */
@WebServiceClient(name = "os_BusinessPartnerChangeService",
                  wsdlLocation = "wsdl/businesspartner/SST-077-BusinessPartnerChange.wsdl",
                  targetNamespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER")
public class OsBusinessPartnerChangeService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "os_BusinessPartnerChangeService");
    public final static QName HTTPSPort = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "HTTPS_Port");
    public final static QName HTTPPort = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "HTTP_Port");
    static {
        URL url = OsBusinessPartnerChangeService.class.getResource("wsdl/businesspartner/SST-077-BusinessPartnerChange.wsdl");
        if (url == null) {
            url = OsBusinessPartnerChangeService.class.getClassLoader().getResource("wsdl/businesspartner/SST-077-BusinessPartnerChange.wsdl");
        }
        if (url == null) {
            java.util.logging.Logger.getLogger(OsBusinessPartnerChangeService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "wsdl/businesspartner/SST-077-BusinessPartnerChange.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public OsBusinessPartnerChangeService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public OsBusinessPartnerChangeService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OsBusinessPartnerChangeService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public OsBusinessPartnerChangeService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public OsBusinessPartnerChangeService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public OsBusinessPartnerChangeService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns OsBusinessPartnerChange
     */
    @WebEndpoint(name = "HTTPS_Port")
    public OsBusinessPartnerChange getHTTPSPort() {
        return super.getPort(HTTPSPort, OsBusinessPartnerChange.class);
    }

    /**
     *
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OsBusinessPartnerChange
     */
    @WebEndpoint(name = "HTTPS_Port")
    public OsBusinessPartnerChange getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(HTTPSPort, OsBusinessPartnerChange.class, features);
    }


    /**
     *
     * @return
     *     returns OsBusinessPartnerChange
     */
    @WebEndpoint(name = "HTTP_Port")
    public OsBusinessPartnerChange getHTTPPort() {
        return super.getPort(HTTPPort, OsBusinessPartnerChange.class);
    }

    /**
     *
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OsBusinessPartnerChange
     */
    @WebEndpoint(name = "HTTP_Port")
    public OsBusinessPartnerChange getHTTPPort(WebServiceFeature... features) {
        return super.getPort(HTTPPort, OsBusinessPartnerChange.class, features);
    }

}
