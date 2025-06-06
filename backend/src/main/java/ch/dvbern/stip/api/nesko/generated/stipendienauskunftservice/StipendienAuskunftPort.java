package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 4.0.3
 * 2025-03-05T16:19:52.850+01:00
 * Generated source version: 4.0.3
 *
 */
@WebService(targetNamespace = "http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService", name = "StipendienAuskunftPort")
@XmlSeeAlso({ ch.dvbern.stip.api.nesko.generated.exceptioninfo.ObjectFactory.class, ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface StipendienAuskunftPort {

    /**
     * Eine Stipendien-Anfrage wird mit der Lieferung von Steuerdaten beantwortet. Die Stipendien-Anfrage findet für jede Person einzeln statt.
     *
     */
    @WebMethod(operationName = "GetSteuerdaten")
    @WebResult(name = "GetSteuerdatenResponse", targetNamespace = "http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService", partName = "parameters")
    public GetSteuerdatenResponse getSteuerdaten(

        @WebParam(partName = "parameters", name = "GetSteuerdaten", targetNamespace = "http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService")
        GetSteuerdaten parameters
    ) throws InvalidArgumentsFault, PermissionDeniedFault, InfrastructureFault, BusinessFault;
}
