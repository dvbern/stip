after generate: @XmlRootElement annotation has to be added to Request classes (manually):
e.g. for BusinessPartnerCreateRequest.class:
@XmlRootElement(name = "BusinessPartnerCreate_Request", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER")

