/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.auszahlung.resource;

// import java.io.IOException;
// import java.math.BigDecimal;
// import java.util.UUID;
//
// import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
// import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
// import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
// import ch.dvbern.stip.api.generator.entities.service.AuszahlungGenerator;
// import ch.dvbern.stip.api.sapold.generated.businesspartner.create.BusinessPartnerCreateResponse;
// import ch.dvbern.stip.api.sapold.generated.importstatus.ImportStatusReadResponse;
// import ch.dvbern.stip.api.sapold.service.SapEndpointService;
// import ch.dvbern.stip.api.sapold.service.endpoints.clients.BusinessPartnerChangeClient;
// import ch.dvbern.stip.api.sapold.service.endpoints.clients.BusinessPartnerCreateClient;
// import ch.dvbern.stip.api.sapold.service.endpoints.clients.ImportStatusReadClient;
// import ch.dvbern.stip.api.sapold.service.endpoints.clients.VendorPostingCreateClient;
// import io.quarkus.test.InjectMock;
// import io.quarkus.test.junit.QuarkusTest;
// import jakarta.inject.Inject;
// import jakarta.validation.ConstraintViolationException;
// import jakarta.ws.rs.WebApplicationException;
// import org.apache.http.HttpStatus;
// import org.eclipse.microprofile.rest.client.inject.RestClient;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;
// import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
//
// @QuarkusTest
class SapEndpointServiceTest {
    // private static final BigDecimal EXAMPLE_DELIVERY_ID = BigDecimal.valueOf(2761);
    // private static final Integer EXAMPLE_BUSINESS_PARTNER_ID = 2762;
    // private static final String EXAMPLE_EXTERNAL_ID = UUID.randomUUID().toString();
    // @RestClient
    // @InjectMock
    // ImportStatusReadClient importStatusReadClient;
    //
    // @RestClient
    // @InjectMock
    // BusinessPartnerCreateClient businessPartnerCreateClient;
    //
    // @RestClient
    // @InjectMock
    // BusinessPartnerChangeClient businessPartnerChangeClient;
    //
    // @RestClient
    // @InjectMock
    // VendorPostingCreateClient vendorPostingCreateClient;
    //
    // @Inject
    // SapEndpointService auszahlungSapService;
    //
    // @Inject
    // AuszahlungMapper auszahlungMapper;
    //
    // private static final BigDecimal DELIVERY_ID = EXAMPLE_DELIVERY_ID;
    //
    // @TestAsSachbearbeiter
    // @Test
    // void getImportStatusTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
    // "UTF-8"
    // );
    // when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
    //
    // // act
    // final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
    // final var responseDto = (ImportStatusReadResponse) response.getEntity();
    //
    // // assert
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // assertEquals("S", responseDto.getRETURNCODE().get(0).getTYPE());
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // }
    //
    // @TestAsSachbearbeiter
    // @Test
    // void getImportStatusInvalidDeliveryIdTest(){
    // //arrange
    // when(importStatusReadClient.getImportStatus(any())).thenThrow(WebApplicationException.class);
    // // act & assert
    // final var deliveryId1 = BigDecimal.valueOf(-1);
    // assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(deliveryId1));
    // final var deliveryId2 = BigDecimal.valueOf(0);
    // assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(deliveryId2));
    // assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(null));
    // }
    //
    // @TestAsSachbearbeiter
    // @Test
    // void getImportStatusDeliveryIdNotFoundTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream("/auszahlung/getImportStatusDeliveryIdNotFoundExampleResponse.xml"),
    // "UTF-8"
    // );
    // when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
    //
    // // act
    // final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
    // final var responseDto = (ImportStatusReadResponse) response.getEntity();
    //
    // // assert
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // assertEquals("E", responseDto.getRETURNCODE().get(0).getTYPE());
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // }
    //
    // @Test
    // void createBusinessPartnerAlreadyExistingParnterTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
    // "UTF-8"
    // );
    // when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
    // // act
    // final var response = auszahlungSapService
    // .createBusinessPartner(AuszahlungGenerator.initAuszahlung(), EXAMPLE_EXTERNAL_ID, DELIVERY_ID);
    // final var businessPartnerId =
    // ((BusinessPartnerCreateResponse) response.getEntity()).getBUSINESSPARTNER().getHEADER().getBPARTNER();
    //
    // // assert
    // assertNotNull(businessPartnerId);
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // }
    //
    // @ParameterizedTest
    // @ValueSource(
    // strings = { "/auszahlung/createBusinessPartnerSuccessResponse.xml",
    // "/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml" }
    // )
    // void createBusinessPartnerTest(String xmlFilePath) throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream(xmlFilePath),
    // "UTF-8"
    // );
    // when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
    // // act
    // final var response = auszahlungSapService
    // .createBusinessPartner(AuszahlungGenerator.initAuszahlung(), EXAMPLE_EXTERNAL_ID, DELIVERY_ID);
    // // assert
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // }
    //
    // @Test
    // void changeBusinessPartnerAlreadyExistingDeliveryIdTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass()
    // .getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
    // "UTF-8"
    // );
    // final Auszahlung requestDto1 = AuszahlungGenerator.initAuszahlung();
    // requestDto1.setAdresse(null);
    // when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
    //
    // // act
    // final var response = auszahlungSapService
    // .changeBusinessPartner(AuszahlungGenerator.initAuszahlung(), EXAMPLE_BUSINESS_PARTNER_ID, DELIVERY_ID);
    //
    // // assert
    // assertThrows(
    // ConstraintViolationException.class,
    // () -> auszahlungSapService.changeBusinessPartner(requestDto1, EXAMPLE_BUSINESS_PARTNER_ID, DELIVERY_ID)
    // );
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    //
    // }
    //
    // @Test
    // void changeBusinessPartnerSuccessTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerSuccessResponse.xml"),
    // "UTF-8"
    // );
    // when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
    // Auszahlung requestDto1 = AuszahlungGenerator.initAuszahlung();
    //
    // // act
    // final var response =
    // auszahlungSapService.changeBusinessPartner(requestDto1, EXAMPLE_BUSINESS_PARTNER_ID, DELIVERY_ID);
    //
    // // assert
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    //
    // }
    //
    // @Test
    // void createAuszahlungInvalidDtoTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
    // "UTF-8"
    // );
    // final Auszahlung requestDto1 = AuszahlungGenerator.initAuszahlung();
    // requestDto1.setAdresse(null);
    // when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
    //
    // // act
    // final var response = auszahlungSapService
    // .createVendorPosting(AuszahlungGenerator.initAuszahlung(), EXAMPLE_BUSINESS_PARTNER_ID, DELIVERY_ID);
    //
    // // assert
    // assertThrows(
    // ConstraintViolationException.class,
    // () -> auszahlungSapService.createVendorPosting(requestDto1, EXAMPLE_BUSINESS_PARTNER_ID, DELIVERY_ID)
    // );
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // }
    //
    // @Test
    // void createAuszahlungTest() throws IOException {
    // // arrange
    // String xml = IOUtils.toString(
    // this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
    // "UTF-8"
    // );
    // when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
    //
    // // act
    // final var response = auszahlungSapService
    // .createVendorPosting(AuszahlungGenerator.initAuszahlung(), EXAMPLE_BUSINESS_PARTNER_ID, DELIVERY_ID);
    //
    // // assert
    // assertEquals(HttpStatus.SC_OK, response.getStatus());
    // }
    //
}
