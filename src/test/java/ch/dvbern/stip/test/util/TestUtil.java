package ch.dvbern.stip.test.util;

import io.restassured.response.ValidatableResponse;
import jakarta.ws.rs.core.HttpHeaders;

import java.util.UUID;

public class TestUtil {

    public static UUID extractIdFromResponse(ValidatableResponse response) {
        var locationString = response.extract().header(HttpHeaders.LOCATION).split("/");
        var idString = locationString[locationString.length-1];
        return UUID.fromString(idString);
    }
}
