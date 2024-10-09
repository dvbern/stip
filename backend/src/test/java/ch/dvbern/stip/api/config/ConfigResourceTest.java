package ch.dvbern.stip.api.config;

import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.ConfigurationApiSpec;
import ch.dvbern.stip.generated.dto.DeploymentConfigDtoSpec;
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
@QuarkusTestResource(TestClamAVEnvironment.class)
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
