package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("AuszahlungImportStatusLog")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AuszahlungImportStatusLogDto  implements Serializable {
  private @Valid String datetime;
  private @Valid String message;

  /**
   **/
  public AuszahlungImportStatusLogDto datetime(String datetime) {
    this.datetime = datetime;
    return this;
  }


  @JsonProperty("datetime")
  public String getDatetime() {
    return datetime;
  }

  @JsonProperty("datetime")
  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  /**
   **/
  public AuszahlungImportStatusLogDto message(String message) {
    this.message = message;
    return this;
  }


  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuszahlungImportStatusLogDto auszahlungImportStatusLog = (AuszahlungImportStatusLogDto) o;
    return Objects.equals(this.datetime, auszahlungImportStatusLog.datetime) &&
        Objects.equals(this.message, auszahlungImportStatusLog.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datetime, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuszahlungImportStatusLogDto {\n");

    sb.append("    datetime: ").append(toIndentedString(datetime)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

