package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DelegierungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("FallWithDelegierung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FallWithDelegierungDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String fallNummer;
  private @Valid String mandant;
  private @Valid DelegierungDto delegierung;
  private @Valid LocalDate letzteAktivitaet;

  /**
   **/
  public FallWithDelegierungDto id(UUID id) {
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
  public FallWithDelegierungDto fallNummer(String fallNummer) {
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
  public FallWithDelegierungDto mandant(String mandant) {
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

  /**
   **/
  public FallWithDelegierungDto delegierung(DelegierungDto delegierung) {
    this.delegierung = delegierung;
    return this;
  }

  
  @JsonProperty("delegierung")
  @NotNull
  public DelegierungDto getDelegierung() {
    return delegierung;
  }

  @JsonProperty("delegierung")
  public void setDelegierung(DelegierungDto delegierung) {
    this.delegierung = delegierung;
  }

  /**
   **/
  public FallWithDelegierungDto letzteAktivitaet(LocalDate letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
    return this;
  }

  
  @JsonProperty("letzteAktivitaet")
  @NotNull
  public LocalDate getLetzteAktivitaet() {
    return letzteAktivitaet;
  }

  @JsonProperty("letzteAktivitaet")
  public void setLetzteAktivitaet(LocalDate letzteAktivitaet) {
    this.letzteAktivitaet = letzteAktivitaet;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallWithDelegierungDto fallWithDelegierung = (FallWithDelegierungDto) o;
    return Objects.equals(this.id, fallWithDelegierung.id) &&
        Objects.equals(this.fallNummer, fallWithDelegierung.fallNummer) &&
        Objects.equals(this.mandant, fallWithDelegierung.mandant) &&
        Objects.equals(this.delegierung, fallWithDelegierung.delegierung) &&
        Objects.equals(this.letzteAktivitaet, fallWithDelegierung.letzteAktivitaet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallNummer, mandant, delegierung, letzteAktivitaet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallWithDelegierungDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    mandant: ").append(toIndentedString(mandant)).append("\n");
    sb.append("    delegierung: ").append(toIndentedString(delegierung)).append("\n");
    sb.append("    letzteAktivitaet: ").append(toIndentedString(letzteAktivitaet)).append("\n");
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

