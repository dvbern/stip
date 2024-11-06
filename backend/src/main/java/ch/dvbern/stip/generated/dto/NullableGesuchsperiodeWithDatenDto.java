package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;



@JsonTypeName("NullableGesuchsperiodeWithDaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class NullableGesuchsperiodeWithDatenDto  implements Serializable {
  private @Valid GesuchsperiodeWithDatenDto value;

  /**
   **/
  public NullableGesuchsperiodeWithDatenDto value(GesuchsperiodeWithDatenDto value) {
    this.value = value;
    return this;
  }


  @JsonProperty("value")
  public GesuchsperiodeWithDatenDto getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(GesuchsperiodeWithDatenDto value) {
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
    NullableGesuchsperiodeWithDatenDto nullableGesuchsperiodeWithDaten = (NullableGesuchsperiodeWithDatenDto) o;
    return Objects.equals(this.value, nullableGesuchsperiodeWithDaten.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NullableGesuchsperiodeWithDatenDto {\n");

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

