package ch.dvbern.stip.test.config;

import ch.dvbern.stip.generated.test.api.ConfigurationApiSpec;
import ch.dvbern.stip.generated.test.dto.DeploymentConfigDtoSpec;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigResourceTest {

    public final ConfigurationApiSpec configurationApiSpec =
        ConfigurationApiSpec._configuration(RequestSpecUtil.quarkusSpec());

    @Test
    void testGetDeploymentConfig() {
        var response = configurationApiSpec.getDeploymentConfig().execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(DeploymentConfigDtoSpec.class);

        assertThat(response.getVersion()).isNotBlank();
        assertThat(response.getEnvironment()).isNotBlank();
        assertThat(response.getAllowedMimeTypes()).isNotEmpty();
    }
}
