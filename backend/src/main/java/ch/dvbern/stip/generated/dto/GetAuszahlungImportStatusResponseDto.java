package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AuszahlungImportStatusLogDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GetAuszahlungImportStatusResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GetAuszahlungImportStatusResponseDto  implements Serializable {
  private @Valid String status;
  private @Valid List<AuszahlungImportStatusLogDto> logs;

  /**
   **/
  public GetAuszahlungImportStatusResponseDto status(String status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   **/
  public GetAuszahlungImportStatusResponseDto logs(List<AuszahlungImportStatusLogDto> logs) {
    this.logs = logs;
    return this;
  }

  
  @JsonProperty("logs")
  public List<AuszahlungImportStatusLogDto> getLogs() {
    return logs;
  }

  @JsonProperty("logs")
  public void setLogs(List<AuszahlungImportStatusLogDto> logs) {
    this.logs = logs;
  }

  public GetAuszahlungImportStatusResponseDto addLogsItem(AuszahlungImportStatusLogDto logsItem) {
    if (this.logs == null) {
      this.logs = new ArrayList<>();
    }

    this.logs.add(logsItem);
    return this;
  }

  public GetAuszahlungImportStatusResponseDto removeLogsItem(AuszahlungImportStatusLogDto logsItem) {
    if (logsItem != null && this.logs != null) {
      this.logs.remove(logsItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAuszahlungImportStatusResponseDto getAuszahlungImportStatusResponse = (GetAuszahlungImportStatusResponseDto) o;
    return Objects.equals(this.status, getAuszahlungImportStatusResponse.status) &&
        Objects.equals(this.logs, getAuszahlungImportStatusResponse.logs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, logs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAuszahlungImportStatusResponseDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

