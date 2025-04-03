package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("BeschwerdeVerlaufEntryCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BeschwerdeVerlaufEntryCreateDto  implements Serializable {
  private @Valid String kommentar;
  private @Valid Boolean beschwerdeSetTo;

  /**
   **/
  public BeschwerdeVerlaufEntryCreateDto kommentar(String kommentar) {
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
  public BeschwerdeVerlaufEntryCreateDto beschwerdeSetTo(Boolean beschwerdeSetTo) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeschwerdeVerlaufEntryCreateDto beschwerdeVerlaufEntryCreate = (BeschwerdeVerlaufEntryCreateDto) o;
    return Objects.equals(this.kommentar, beschwerdeVerlaufEntryCreate.kommentar) &&
        Objects.equals(this.beschwerdeSetTo, beschwerdeVerlaufEntryCreate.beschwerdeSetTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, beschwerdeSetTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeVerlaufEntryCreateDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    beschwerdeSetTo: ").append(toIndentedString(beschwerdeSetTo)).append("\n");
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

