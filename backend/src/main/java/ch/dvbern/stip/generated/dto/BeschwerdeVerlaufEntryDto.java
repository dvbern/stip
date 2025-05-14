package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BeschwerdeEntscheidDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("BeschwerdeVerlaufEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BeschwerdeVerlaufEntryDto  implements Serializable {
  private @Valid String kommentar;
  private @Valid Boolean beschwerdeSetTo;
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid String userErstellt;
  private @Valid BeschwerdeEntscheidDto beschwerdeEntscheid;

  /**
   **/
  public BeschwerdeVerlaufEntryDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  @NotNull
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto beschwerdeSetTo(Boolean beschwerdeSetTo) {
    this.beschwerdeSetTo = beschwerdeSetTo;
    return this;
  }

  
  @JsonProperty("beschwerdeSetTo")
  @NotNull
  public Boolean getBeschwerdeSetTo() {
    return beschwerdeSetTo;
  }

  @JsonProperty("beschwerdeSetTo")
  public void setBeschwerdeSetTo(Boolean beschwerdeSetTo) {
    this.beschwerdeSetTo = beschwerdeSetTo;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  @NotNull
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto beschwerdeEntscheid(BeschwerdeEntscheidDto beschwerdeEntscheid) {
    this.beschwerdeEntscheid = beschwerdeEntscheid;
    return this;
  }

  
  @JsonProperty("beschwerdeEntscheid")
  public BeschwerdeEntscheidDto getBeschwerdeEntscheid() {
    return beschwerdeEntscheid;
  }

  @JsonProperty("beschwerdeEntscheid")
  public void setBeschwerdeEntscheid(BeschwerdeEntscheidDto beschwerdeEntscheid) {
    this.beschwerdeEntscheid = beschwerdeEntscheid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeschwerdeVerlaufEntryDto beschwerdeVerlaufEntry = (BeschwerdeVerlaufEntryDto) o;
    return Objects.equals(this.kommentar, beschwerdeVerlaufEntry.kommentar) &&
        Objects.equals(this.beschwerdeSetTo, beschwerdeVerlaufEntry.beschwerdeSetTo) &&
        Objects.equals(this.timestampErstellt, beschwerdeVerlaufEntry.timestampErstellt) &&
        Objects.equals(this.userErstellt, beschwerdeVerlaufEntry.userErstellt) &&
        Objects.equals(this.beschwerdeEntscheid, beschwerdeVerlaufEntry.beschwerdeEntscheid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, beschwerdeSetTo, timestampErstellt, userErstellt, beschwerdeEntscheid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeVerlaufEntryDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    beschwerdeSetTo: ").append(toIndentedString(beschwerdeSetTo)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    beschwerdeEntscheid: ").append(toIndentedString(beschwerdeEntscheid)).append("\n");
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

