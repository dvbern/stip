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



@JsonTypeName("EinreichedatumAendernRequest")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class EinreichedatumAendernRequestDto  implements Serializable {
  private @Valid java.time.LocalDateTime newEinreichedatum;

  /**
   **/
  public EinreichedatumAendernRequestDto newEinreichedatum(java.time.LocalDateTime newEinreichedatum) {
    this.newEinreichedatum = newEinreichedatum;
    return this;
  }

  
  @JsonProperty("newEinreichedatum")
  @NotNull
  public java.time.LocalDateTime getNewEinreichedatum() {
    return newEinreichedatum;
  }

  @JsonProperty("newEinreichedatum")
  public void setNewEinreichedatum(java.time.LocalDateTime newEinreichedatum) {
    this.newEinreichedatum = newEinreichedatum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EinreichedatumAendernRequestDto einreichedatumAendernRequest = (EinreichedatumAendernRequestDto) o;
    return Objects.equals(this.newEinreichedatum, einreichedatumAendernRequest.newEinreichedatum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newEinreichedatum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinreichedatumAendernRequestDto {\n");
    
    sb.append("    newEinreichedatum: ").append(toIndentedString(newEinreichedatum)).append("\n");
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

