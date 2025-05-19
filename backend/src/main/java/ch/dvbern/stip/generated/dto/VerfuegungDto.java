package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Verfuegung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class VerfuegungDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String timestampErstellt;
  private @Valid String filename;
  private @Valid ch.dvbern.stip.api.common.type.StipDecision stipDecision;

  /**
   **/
  public VerfuegungDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public VerfuegungDto timestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public String getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public VerfuegungDto filename(String filename) {
    this.filename = filename;
    return this;
  }

  
  @JsonProperty("filename")
  @NotNull
  public String getFilename() {
    return filename;
  }

  @JsonProperty("filename")
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   **/
  public VerfuegungDto stipDecision(ch.dvbern.stip.api.common.type.StipDecision stipDecision) {
    this.stipDecision = stipDecision;
    return this;
  }

  
  @JsonProperty("stipDecision")
  @NotNull
  public ch.dvbern.stip.api.common.type.StipDecision getStipDecision() {
    return stipDecision;
  }

  @JsonProperty("stipDecision")
  public void setStipDecision(ch.dvbern.stip.api.common.type.StipDecision stipDecision) {
    this.stipDecision = stipDecision;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerfuegungDto verfuegung = (VerfuegungDto) o;
    return Objects.equals(this.id, verfuegung.id) &&
        Objects.equals(this.timestampErstellt, verfuegung.timestampErstellt) &&
        Objects.equals(this.filename, verfuegung.filename) &&
        Objects.equals(this.stipDecision, verfuegung.stipDecision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, timestampErstellt, filename, stipDecision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerfuegungDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    stipDecision: ").append(toIndentedString(stipDecision)).append("\n");
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

