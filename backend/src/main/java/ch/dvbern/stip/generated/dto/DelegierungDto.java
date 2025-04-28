package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.PersoenlicheAngabenDto;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Delegierung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DelegierungDto  implements Serializable {
  private @Valid SozialdienstSlimDto sozialdienst;
  private @Valid Boolean delegierungAngenommen;
  private @Valid PersoenlicheAngabenDto persoenlicheAngaben;

  /**
   **/
  public DelegierungDto sozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
    return this;
  }

  
  @JsonProperty("sozialdienst")
  public SozialdienstSlimDto getSozialdienst() {
    return sozialdienst;
  }

  @JsonProperty("sozialdienst")
  public void setSozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
  }

  /**
   **/
  public DelegierungDto delegierungAngenommen(Boolean delegierungAngenommen) {
    this.delegierungAngenommen = delegierungAngenommen;
    return this;
  }

  
  @JsonProperty("delegierungAngenommen")
  public Boolean getDelegierungAngenommen() {
    return delegierungAngenommen;
  }

  @JsonProperty("delegierungAngenommen")
  public void setDelegierungAngenommen(Boolean delegierungAngenommen) {
    this.delegierungAngenommen = delegierungAngenommen;
  }

  /**
   **/
  public DelegierungDto persoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
    this.persoenlicheAngaben = persoenlicheAngaben;
    return this;
  }

  
  @JsonProperty("persoenlicheAngaben")
  public PersoenlicheAngabenDto getPersoenlicheAngaben() {
    return persoenlicheAngaben;
  }

  @JsonProperty("persoenlicheAngaben")
  public void setPersoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
    this.persoenlicheAngaben = persoenlicheAngaben;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierungDto delegierung = (DelegierungDto) o;
    return Objects.equals(this.sozialdienst, delegierung.sozialdienst) &&
        Objects.equals(this.delegierungAngenommen, delegierung.delegierungAngenommen) &&
        Objects.equals(this.persoenlicheAngaben, delegierung.persoenlicheAngaben);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sozialdienst, delegierungAngenommen, persoenlicheAngaben);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierungDto {\n");
    
    sb.append("    sozialdienst: ").append(toIndentedString(sozialdienst)).append("\n");
    sb.append("    delegierungAngenommen: ").append(toIndentedString(delegierungAngenommen)).append("\n");
    sb.append("    persoenlicheAngaben: ").append(toIndentedString(persoenlicheAngaben)).append("\n");
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

