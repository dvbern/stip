package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Auszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AuszahlungDto  implements Serializable {
  private @Valid Boolean auszahlungAnSozialdienst;
  private @Valid ZahlungsverbindungDto zahlungsverbindung;

  /**
   **/
  public AuszahlungDto auszahlungAnSozialdienst(Boolean auszahlungAnSozialdienst) {
    this.auszahlungAnSozialdienst = auszahlungAnSozialdienst;
    return this;
  }

  
  @JsonProperty("auszahlungAnSozialdienst")
  @NotNull
  public Boolean getAuszahlungAnSozialdienst() {
    return auszahlungAnSozialdienst;
  }

  @JsonProperty("auszahlungAnSozialdienst")
  public void setAuszahlungAnSozialdienst(Boolean auszahlungAnSozialdienst) {
    this.auszahlungAnSozialdienst = auszahlungAnSozialdienst;
  }

  /**
   **/
  public AuszahlungDto zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
    return this;
  }

  
  @JsonProperty("zahlungsverbindung")
  public ZahlungsverbindungDto getZahlungsverbindung() {
    return zahlungsverbindung;
  }

  @JsonProperty("zahlungsverbindung")
  public void setZahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuszahlungDto auszahlung = (AuszahlungDto) o;
    return Objects.equals(this.auszahlungAnSozialdienst, auszahlung.auszahlungAnSozialdienst) &&
        Objects.equals(this.zahlungsverbindung, auszahlung.zahlungsverbindung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auszahlungAnSozialdienst, zahlungsverbindung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuszahlungDto {\n");
    
    sb.append("    auszahlungAnSozialdienst: ").append(toIndentedString(auszahlungAnSozialdienst)).append("\n");
    sb.append("    zahlungsverbindung: ").append(toIndentedString(zahlungsverbindung)).append("\n");
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

