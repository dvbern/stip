package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("JuristischeAbklaerungNotizAntwort")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class JuristischeAbklaerungNotizAntwortDto  implements Serializable {
  private @Valid String antwort;

  /**
   **/
  public JuristischeAbklaerungNotizAntwortDto antwort(String antwort) {
    this.antwort = antwort;
    return this;
  }


  @JsonProperty("antwort")
  @NotNull
  public String getAntwort() {
    return antwort;
  }

  @JsonProperty("antwort")
  public void setAntwort(String antwort) {
    this.antwort = antwort;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JuristischeAbklaerungNotizAntwortDto juristischeAbklaerungNotizAntwort = (JuristischeAbklaerungNotizAntwortDto) o;
    return Objects.equals(this.antwort, juristischeAbklaerungNotizAntwort.antwort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(antwort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JuristischeAbklaerungNotizAntwortDto {\n");

    sb.append("    antwort: ").append(toIndentedString(antwort)).append("\n");
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

