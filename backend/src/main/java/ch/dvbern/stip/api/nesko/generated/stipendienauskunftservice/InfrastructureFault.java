
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 4.0.3
 * 2025-03-05T16:19:52.843+01:00
 * Generated source version: 4.0.3
 */

@WebFault(name = "InfrastructureFault", targetNamespace = "http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo")
public class InfrastructureFault extends Exception {

    private ch.dvbern.stip.api.nesko.generated.exceptioninfo.InfrastructureFault faultInfo;

    public InfrastructureFault() {
        super();
    }

    public InfrastructureFault(String message) {
        super(message);
    }

    public InfrastructureFault(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public InfrastructureFault(String message, ch.dvbern.stip.api.nesko.generated.exceptioninfo.InfrastructureFault infrastructureFault) {
        super(message);
        this.faultInfo = infrastructureFault;
    }

    public InfrastructureFault(String message, ch.dvbern.stip.api.nesko.generated.exceptioninfo.InfrastructureFault infrastructureFault, java.lang.Throwable cause) {
        super(message, cause);
        this.faultInfo = infrastructureFault;
    }

    public ch.dvbern.stip.api.nesko.generated.exceptioninfo.InfrastructureFault getFaultInfo() {
        return this.faultInfo;
    }
}
