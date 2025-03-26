package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("NachfristAendernRequest")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class NachfristAendernRequestDto  implements Serializable {
  private @Valid LocalDate newNachfrist;

  /**
   **/
  public NachfristAendernRequestDto newNachfrist(LocalDate newNachfrist) {
    this.newNachfrist = newNachfrist;
    return this;
  }

  
  @JsonProperty("newNachfrist")
  @NotNull
  public LocalDate getNewNachfrist() {
    return newNachfrist;
  }

  @JsonProperty("newNachfrist")
  public void setNewNachfrist(LocalDate newNachfrist) {
    this.newNachfrist = newNachfrist;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NachfristAendernRequestDto nachfristAendernRequest = (NachfristAendernRequestDto) o;
    return Objects.equals(this.newNachfrist, nachfristAendernRequest.newNachfrist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newNachfrist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NachfristAendernRequestDto {\n");
    
    sb.append("    newNachfrist: ").append(toIndentedString(newNachfrist)).append("\n");
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

