@XmlSchema(
    namespace = "http://www.bfs.admin.ch/xmlns/STIP/2",
    elementFormDefault = QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "", namespaceURI = "http://www.bfs.admin.ch/xmlns/STIP/2"),
        @XmlNs(prefix = "eCH-0044", namespaceURI = "http://www.ech.ch/xmlns/eCH-0044/1"),
        @XmlNs(prefix = "eCH-0006", namespaceURI = "http://www.ech.ch/xmlns/eCH-0006/2"),
        @XmlNs(prefix = "eCH-0007", namespaceURI = "http://www.ech.ch/xmlns/eCH-0007/3"),
        @XmlNs(prefix = "eCH-0008", namespaceURI = "http://www.ech.ch/xmlns/eCH-0008/2")
    }
)
package ch.dvbern.stip.api.statistik.dto;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlSchema;

import static jakarta.xml.bind.annotation.XmlNsForm.QUALIFIED;
