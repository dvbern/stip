package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GueltigkeitStatusDto;
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



@JsonTypeName("GesuchsjahrCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsjahrCreateDto  implements Serializable {
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid LocalDate ausbildungsjahrStart;
  private @Valid LocalDate ausbildungsjahrEnde;
  private @Valid Integer technischesJahr;
  private @Valid GueltigkeitStatusDto gueltigkeitStatus;

  /**
   **/
  public GesuchsjahrCreateDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public GesuchsjahrCreateDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public GesuchsjahrCreateDto ausbildungsjahrStart(LocalDate ausbildungsjahrStart) {
    this.ausbildungsjahrStart = ausbildungsjahrStart;
    return this;
  }

  
  @JsonProperty("ausbildungsjahrStart")
  @NotNull
  public LocalDate getAusbildungsjahrStart() {
    return ausbildungsjahrStart;
  }

  @JsonProperty("ausbildungsjahrStart")
  public void setAusbildungsjahrStart(LocalDate ausbildungsjahrStart) {
    this.ausbildungsjahrStart = ausbildungsjahrStart;
  }

  /**
   **/
  public GesuchsjahrCreateDto ausbildungsjahrEnde(LocalDate ausbildungsjahrEnde) {
    this.ausbildungsjahrEnde = ausbildungsjahrEnde;
    return this;
  }

  
  @JsonProperty("ausbildungsjahrEnde")
  @NotNull
  public LocalDate getAusbildungsjahrEnde() {
    return ausbildungsjahrEnde;
  }

  @JsonProperty("ausbildungsjahrEnde")
  public void setAusbildungsjahrEnde(LocalDate ausbildungsjahrEnde) {
    this.ausbildungsjahrEnde = ausbildungsjahrEnde;
  }

  /**
   **/
  public GesuchsjahrCreateDto technischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
    return this;
  }

  
  @JsonProperty("technischesJahr")
  @NotNull
  public Integer getTechnischesJahr() {
    return technischesJahr;
  }

  @JsonProperty("technischesJahr")
  public void setTechnischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
  }

  /**
   **/
  public GesuchsjahrCreateDto gueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
    return this;
  }

  
  @JsonProperty("gueltigkeitStatus")
  @NotNull
  public GueltigkeitStatusDto getGueltigkeitStatus() {
    return gueltigkeitStatus;
  }

  @JsonProperty("gueltigkeitStatus")
  public void setGueltigkeitStatus(GueltigkeitStatusDto gueltigkeitStatus) {
    this.gueltigkeitStatus = gueltigkeitStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsjahrCreateDto gesuchsjahrCreate = (GesuchsjahrCreateDto) o;
    return Objects.equals(this.bezeichnungDe, gesuchsjahrCreate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsjahrCreate.bezeichnungFr) &&
        Objects.equals(this.ausbildungsjahrStart, gesuchsjahrCreate.ausbildungsjahrStart) &&
        Objects.equals(this.ausbildungsjahrEnde, gesuchsjahrCreate.ausbildungsjahrEnde) &&
        Objects.equals(this.technischesJahr, gesuchsjahrCreate.technischesJahr) &&
        Objects.equals(this.gueltigkeitStatus, gesuchsjahrCreate.gueltigkeitStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, ausbildungsjahrStart, ausbildungsjahrEnde, technischesJahr, gueltigkeitStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsjahrCreateDto {\n");
    
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    ausbildungsjahrStart: ").append(toIndentedString(ausbildungsjahrStart)).append("\n");
    sb.append("    ausbildungsjahrEnde: ").append(toIndentedString(ausbildungsjahrEnde)).append("\n");
    sb.append("    technischesJahr: ").append(toIndentedString(technischesJahr)).append("\n");
    sb.append("    gueltigkeitStatus: ").append(toIndentedString(gueltigkeitStatus)).append("\n");
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

