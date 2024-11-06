package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("NullableGesuchDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class NullableGesuchDokumentDto  implements Serializable {
  private @Valid GesuchDokumentDto value;

  /**
   **/
  public NullableGesuchDokumentDto value(GesuchDokumentDto value) {
    this.value = value;
    return this;
  }

  
  @JsonProperty("value")
  public GesuchDokumentDto getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(GesuchDokumentDto value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NullableGesuchDokumentDto nullableGesuchDokument = (NullableGesuchDokumentDto) o;
    return Objects.equals(this.value, nullableGesuchDokument.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NullableGesuchDokumentDto {\n");
    
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

