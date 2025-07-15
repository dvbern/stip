package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsgangDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid UUID abschlussId;
  private @Valid String abschlussBezeichnungDe;
  private @Valid String abschlussBezeichnungFr;
  private @Valid UUID ausbildungsstaetteId;
  private @Valid String ausbildungsstaetteNameDe;
  private @Valid String ausbildungsstaetteNameFr;
  private @Valid Boolean aktiv;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung;

  /**
   **/
  public AusbildungsgangDto id(UUID id) {
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
  public AusbildungsgangDto bezeichnungDe(String bezeichnungDe) {
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
  public AusbildungsgangDto bezeichnungFr(String bezeichnungFr) {
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
  public AusbildungsgangDto abschlussId(UUID abschlussId) {
    this.abschlussId = abschlussId;
    return this;
  }

  
  @JsonProperty("abschlussId")
  @NotNull
  public UUID getAbschlussId() {
    return abschlussId;
  }

  @JsonProperty("abschlussId")
  public void setAbschlussId(UUID abschlussId) {
    this.abschlussId = abschlussId;
  }

  /**
   **/
  public AusbildungsgangDto abschlussBezeichnungDe(String abschlussBezeichnungDe) {
    this.abschlussBezeichnungDe = abschlussBezeichnungDe;
    return this;
  }

  
  @JsonProperty("abschlussBezeichnungDe")
  @NotNull
  public String getAbschlussBezeichnungDe() {
    return abschlussBezeichnungDe;
  }

  @JsonProperty("abschlussBezeichnungDe")
  public void setAbschlussBezeichnungDe(String abschlussBezeichnungDe) {
    this.abschlussBezeichnungDe = abschlussBezeichnungDe;
  }

  /**
   **/
  public AusbildungsgangDto abschlussBezeichnungFr(String abschlussBezeichnungFr) {
    this.abschlussBezeichnungFr = abschlussBezeichnungFr;
    return this;
  }

  
  @JsonProperty("abschlussBezeichnungFr")
  @NotNull
  public String getAbschlussBezeichnungFr() {
    return abschlussBezeichnungFr;
  }

  @JsonProperty("abschlussBezeichnungFr")
  public void setAbschlussBezeichnungFr(String abschlussBezeichnungFr) {
    this.abschlussBezeichnungFr = abschlussBezeichnungFr;
  }

  /**
   **/
  public AusbildungsgangDto ausbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
    return this;
  }

  
  @JsonProperty("ausbildungsstaetteId")
  @NotNull
  public UUID getAusbildungsstaetteId() {
    return ausbildungsstaetteId;
  }

  @JsonProperty("ausbildungsstaetteId")
  public void setAusbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
  }

  /**
   **/
  public AusbildungsgangDto ausbildungsstaetteNameDe(String ausbildungsstaetteNameDe) {
    this.ausbildungsstaetteNameDe = ausbildungsstaetteNameDe;
    return this;
  }

  
  @JsonProperty("ausbildungsstaetteNameDe")
  @NotNull
  public String getAusbildungsstaetteNameDe() {
    return ausbildungsstaetteNameDe;
  }

  @JsonProperty("ausbildungsstaetteNameDe")
  public void setAusbildungsstaetteNameDe(String ausbildungsstaetteNameDe) {
    this.ausbildungsstaetteNameDe = ausbildungsstaetteNameDe;
  }

  /**
   **/
  public AusbildungsgangDto ausbildungsstaetteNameFr(String ausbildungsstaetteNameFr) {
    this.ausbildungsstaetteNameFr = ausbildungsstaetteNameFr;
    return this;
  }

  
  @JsonProperty("ausbildungsstaetteNameFr")
  @NotNull
  public String getAusbildungsstaetteNameFr() {
    return ausbildungsstaetteNameFr;
  }

  @JsonProperty("ausbildungsstaetteNameFr")
  public void setAusbildungsstaetteNameFr(String ausbildungsstaetteNameFr) {
    this.ausbildungsstaetteNameFr = ausbildungsstaetteNameFr;
  }

  /**
   **/
  public AusbildungsgangDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty("aktiv")
  @NotNull
  public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty("aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  /**
   **/
  public AusbildungsgangDto ausbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
    return this;
  }

  
  @JsonProperty("ausbildungskategorie")
  public ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie getAusbildungskategorie() {
    return ausbildungskategorie;
  }

  @JsonProperty("ausbildungskategorie")
  public void setAusbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
  }

  /**
   **/
  public AusbildungsgangDto bildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
    return this;
  }

  
  @JsonProperty("bildungsrichtung")
  public ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung getBildungsrichtung() {
    return bildungsrichtung;
  }

  @JsonProperty("bildungsrichtung")
  public void setBildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsgangDto ausbildungsgang = (AusbildungsgangDto) o;
    return Objects.equals(this.id, ausbildungsgang.id) &&
        Objects.equals(this.bezeichnungDe, ausbildungsgang.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, ausbildungsgang.bezeichnungFr) &&
        Objects.equals(this.abschlussId, ausbildungsgang.abschlussId) &&
        Objects.equals(this.abschlussBezeichnungDe, ausbildungsgang.abschlussBezeichnungDe) &&
        Objects.equals(this.abschlussBezeichnungFr, ausbildungsgang.abschlussBezeichnungFr) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgang.ausbildungsstaetteId) &&
        Objects.equals(this.ausbildungsstaetteNameDe, ausbildungsgang.ausbildungsstaetteNameDe) &&
        Objects.equals(this.ausbildungsstaetteNameFr, ausbildungsgang.ausbildungsstaetteNameFr) &&
        Objects.equals(this.aktiv, ausbildungsgang.aktiv) &&
        Objects.equals(this.ausbildungskategorie, ausbildungsgang.ausbildungskategorie) &&
        Objects.equals(this.bildungsrichtung, ausbildungsgang.bildungsrichtung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, abschlussId, abschlussBezeichnungDe, abschlussBezeichnungFr, ausbildungsstaetteId, ausbildungsstaetteNameDe, ausbildungsstaetteNameFr, aktiv, ausbildungskategorie, bildungsrichtung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    abschlussId: ").append(toIndentedString(abschlussId)).append("\n");
    sb.append("    abschlussBezeichnungDe: ").append(toIndentedString(abschlussBezeichnungDe)).append("\n");
    sb.append("    abschlussBezeichnungFr: ").append(toIndentedString(abschlussBezeichnungFr)).append("\n");
    sb.append("    ausbildungsstaetteId: ").append(toIndentedString(ausbildungsstaetteId)).append("\n");
    sb.append("    ausbildungsstaetteNameDe: ").append(toIndentedString(ausbildungsstaetteNameDe)).append("\n");
    sb.append("    ausbildungsstaetteNameFr: ").append(toIndentedString(ausbildungsstaetteNameFr)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
    sb.append("    ausbildungskategorie: ").append(toIndentedString(ausbildungskategorie)).append("\n");
    sb.append("    bildungsrichtung: ").append(toIndentedString(bildungsrichtung)).append("\n");
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

