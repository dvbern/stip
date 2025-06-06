package ch.dvbern.stip.api.sap.generated.vendor_posting;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 4.0.3
 * 2025-04-24T16:07:19.054+02:00
 * Generated source version: 4.0.3
 *
 */
@WebService(targetNamespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", name = "os_VendorPostingCreate")
@XmlSeeAlso({ ch.dvbern.stip.api.sap.generated.general.ObjectFactory.class, ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface OsVendorPostingCreate {

    @WebMethod(operationName = "os_VendorPostingCreate", action = "http://sap.com/xi/WebService/soap1.1")
    @WebResult(name = "VendorPostingCreate_Response", targetNamespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", partName = "VendorPostingCreate_Response")
    public VendorPostingCreateResponse osVendorPostingCreate(

        @WebParam(partName = "VendorPostingCreate_Request", name = "VendorPostingCreate_Request", targetNamespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING")
		VendorPostingCreateRequest vendorPostingCreateRequest
    );
}
