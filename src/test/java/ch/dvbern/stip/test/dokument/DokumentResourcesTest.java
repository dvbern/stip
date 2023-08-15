package ch.dvbern.stip.test.dokument;

import ch.dvbern.oss.stip.contract.test.api.GesuchApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.DokumentDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchCreateDtoSpec;
import ch.dvbern.oss.stip.contract.test.dto.GesuchDtoSpec;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestConstants;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static ch.dvbern.stip.test.util.TestConstants.TEST_FILE_LOCATION;
import static ch.dvbern.stip.test.util.TestConstants.TEST_XML_FILE_LOCATION;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DokumentResourcesTest {

	public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

	private UUID gesuchId;
	private UUID dokumentId;

	@Test
	@Order(1)
	void test_prepare_gesuch_for_dokument() {
		var gesuchDTO = new GesuchCreateDtoSpec();
		gesuchDTO.setFallId(UUID.fromString(TestConstants.FALL_TEST_ID));
		gesuchDTO.setGesuchsperiodeId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
		gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Response.Status.CREATED.getStatusCode());
		var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(GesuchDtoSpec[].class);
		gesuchId = gesuche[0].getId();
		assertThat(gesuche.length, is(1));
	}

	@Test
	@Order(2)
	void test_create_dokument_with_wrong_mime_type() {
		File file = new File(TEST_XML_FILE_LOCATION);
		given()
				.pathParam("gesuchId", gesuchId)
				.pathParam("dokumentTyp", DokumentTyp.ELTERN_DOK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
				.multiPart("fileUpload", file)
				.when()
				.post("/api/v1" + gesuchApiSpec.createDokument().REQ_URI)
				.then()
				.statusCode(Status.BAD_REQUEST.getStatusCode());
	}

	@Test
	@Order(3)
	void test_create_dokument_for_gesuch() {
		File file = new File(TEST_FILE_LOCATION);
		given()
				.pathParam("gesuchId", gesuchId)
				.pathParam("dokumentTyp", DokumentTyp.ELTERN_DOK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
				.multiPart("fileUpload", file)
				.when()
				.post("/api/v1" + gesuchApiSpec.createDokument().REQ_URI)
				.then()
				.statusCode(Status.CREATED.getStatusCode());
	}

	@Test
	@Order(4)
	void test_list_and_read_dokument_for_gesuch() throws IOException {
		var dokumentDtoList = gesuchApiSpec.getDokumenteForTyp()
				.gesuchIdPath(gesuchId)
				.dokumentTypPath(DokumentTyp.ELTERN_DOK)
				.execute(ResponseBody::prettyPeek)
				.then()
				.extract()
				.body()
				.as(DokumentDtoSpec[].class);

		assertThat(dokumentDtoList.length, is(1));

		dokumentId = dokumentDtoList[0].getId();

		given()
				.pathParam("gesuchId", gesuchId)
				.pathParam("dokumentTyp", DokumentTyp.ELTERN_DOK)
				.pathParam("dokumentId", dokumentId)
				.when().get("/api/v1" + GesuchApiSpec.GetDokumentOper.REQ_URI)
				.then()
				.statusCode(200)
				.body(equalTo(readFileData()));
	}

	@Test
	@Order(5)
	void test_delete_dokument() {
		gesuchApiSpec.deleteDokument()
				.gesuchIdPath(gesuchId)
				.dokumentIdPath(dokumentId)
				.dokumentTypPath(DokumentTyp.ELTERN_DOK)
				.execute(ResponseBody::prettyPeek)
				.then()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}

	@Test
	@Order(6)
	void test_delete_gesuch() {
		gesuchApiSpec.deleteGesuch()
				.gesuchIdPath(gesuchId)
				.execute(ResponseBody::prettyPeek)
				.then()
				.assertThat()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}

	private String readFileData() throws IOException {
		return Files.readString(new File(TEST_FILE_LOCATION).toPath());
	}
}
