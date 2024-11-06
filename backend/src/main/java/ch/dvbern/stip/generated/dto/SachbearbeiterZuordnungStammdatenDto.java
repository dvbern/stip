package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;



@JsonTypeName("SachbearbeiterZuordnungStammdaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SachbearbeiterZuordnungStammdatenDto  implements Serializable {
  private @Valid String buchstabenDe;
  private @Valid String buchstabenFr;

  /**
   **/
  public SachbearbeiterZuordnungStammdatenDto buchstabenDe(String buchstabenDe) {
    this.buchstabenDe = buchstabenDe;
    return this;
  }


  @JsonProperty("buchstabenDe")
  public String getBuchstabenDe() {
    return buchstabenDe;
  }

  @JsonProperty("buchstabenDe")
  public void setBuchstabenDe(String buchstabenDe) {
    this.buchstabenDe = buchstabenDe;
  }

  /**
   **/
  public SachbearbeiterZuordnungStammdatenDto buchstabenFr(String buchstabenFr) {
    this.buchstabenFr = buchstabenFr;
    return this;
  }


  @JsonProperty("buchstabenFr")
  public String getBuchstabenFr() {
    return buchstabenFr;
  }

  @JsonProperty("buchstabenFr")
  public void setBuchstabenFr(String buchstabenFr) {
    this.buchstabenFr = buchstabenFr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten = (SachbearbeiterZuordnungStammdatenDto) o;
    return Objects.equals(this.buchstabenDe, sachbearbeiterZuordnungStammdaten.buchstabenDe) &&
        Objects.equals(this.buchstabenFr, sachbearbeiterZuordnungStammdaten.buchstabenFr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(buchstabenDe, buchstabenFr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterZuordnungStammdatenDto {\n");

    sb.append("    buchstabenDe: ").append(toIndentedString(buchstabenDe)).append("\n");
    sb.append("    buchstabenFr: ").append(toIndentedString(buchstabenFr)).append("\n");
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

