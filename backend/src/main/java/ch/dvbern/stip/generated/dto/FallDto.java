package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("Fall")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FallDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String fallNummer;
  private @Valid String mandant;

  /**
   **/
  public FallDto id(UUID id) {
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
  public FallDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }


  @JsonProperty("fallNummer")
  @NotNull
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public FallDto mandant(String mandant) {
    this.mandant = mandant;
    return this;
  }


  @JsonProperty("mandant")
  @NotNull
  public String getMandant() {
    return mandant;
  }

  @JsonProperty("mandant")
  public void setMandant(String mandant) {
    this.mandant = mandant;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallDto fall = (FallDto) o;
    return Objects.equals(this.id, fall.id) &&
        Objects.equals(this.fallNummer, fall.fallNummer) &&
        Objects.equals(this.mandant, fall.mandant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallNummer, mandant);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    mandant: ").append(toIndentedString(mandant)).append("\n");
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

