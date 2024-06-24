package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.SteuerdatenTypDto;
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

/**
 * 
 **/

@JsonTypeName("Steuerdaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SteuerdatenDto  implements Serializable {
  private @Valid Integer steuernStaat;
  private @Valid Integer steuernBund;
  private @Valid Integer fahrkosten;
  private @Valid Integer verpflegung;
  private @Valid Integer steuerjahr;
  private @Valid Integer veranlagungscode;
  private @Valid Integer totalEinkuenfte;
  private @Valid Boolean isArbeitsverhaeltnisSelbstaendig;
  private @Valid Integer kinderalimente;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer vermoegen;
  private @Valid UUID id;
  private @Valid SteuerdatenTypDto typ;
  private @Valid Integer fahrkostenPartner;
  private @Valid Integer verpflegungPartner;
  private @Valid Integer eingenmietwert;
  private @Valid Integer saeule3a;
  private @Valid Integer saeule2;

  /**
   **/
  public SteuerdatenDto steuernStaat(Integer steuernStaat) {
    this.steuernStaat = steuernStaat;
    return this;
  }

  
  @JsonProperty("steuernStaat")
  @NotNull
  public Integer getSteuernStaat() {
    return steuernStaat;
  }

  @JsonProperty("steuernStaat")
  public void setSteuernStaat(Integer steuernStaat) {
    this.steuernStaat = steuernStaat;
  }

  /**
   **/
  public SteuerdatenDto steuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
    return this;
  }

  
  @JsonProperty("steuernBund")
  @NotNull
  public Integer getSteuernBund() {
    return steuernBund;
  }

  @JsonProperty("steuernBund")
  public void setSteuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
  }

  /**
   **/
  public SteuerdatenDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public SteuerdatenDto verpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
    return this;
  }

  
  @JsonProperty("verpflegung")
  @NotNull
  public Integer getVerpflegung() {
    return verpflegung;
  }

  @JsonProperty("verpflegung")
  public void setVerpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
  }

  /**
   **/
  public SteuerdatenDto steuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
    return this;
  }

  
  @JsonProperty("steuerjahr")
  @NotNull
  public Integer getSteuerjahr() {
    return steuerjahr;
  }

  @JsonProperty("steuerjahr")
  public void setSteuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
  }

  /**
   **/
  public SteuerdatenDto veranlagungscode(Integer veranlagungscode) {
    this.veranlagungscode = veranlagungscode;
    return this;
  }

  
  @JsonProperty("veranlagungscode")
  @NotNull
  public Integer getVeranlagungscode() {
    return veranlagungscode;
  }

  @JsonProperty("veranlagungscode")
  public void setVeranlagungscode(Integer veranlagungscode) {
    this.veranlagungscode = veranlagungscode;
  }

  /**
   **/
  public SteuerdatenDto totalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
    return this;
  }

  
  @JsonProperty("totalEinkuenfte")
  @NotNull
  public Integer getTotalEinkuenfte() {
    return totalEinkuenfte;
  }

  @JsonProperty("totalEinkuenfte")
  public void setTotalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
  }

  /**
   **/
  public SteuerdatenDto isArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
    this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
    return this;
  }

  
  @JsonProperty("isArbeitsverhaeltnisSelbstaendig")
  @NotNull
  public Boolean getIsArbeitsverhaeltnisSelbstaendig() {
    return isArbeitsverhaeltnisSelbstaendig;
  }

  @JsonProperty("isArbeitsverhaeltnisSelbstaendig")
  public void setIsArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
    this.isArbeitsverhaeltnisSelbstaendig = isArbeitsverhaeltnisSelbstaendig;
  }

  /**
   **/
  public SteuerdatenDto kinderalimente(Integer kinderalimente) {
    this.kinderalimente = kinderalimente;
    return this;
  }

  
  @JsonProperty("kinderalimente")
  @NotNull
  public Integer getKinderalimente() {
    return kinderalimente;
  }

  @JsonProperty("kinderalimente")
  public void setKinderalimente(Integer kinderalimente) {
    this.kinderalimente = kinderalimente;
  }

  /**
   **/
  public SteuerdatenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public SteuerdatenDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty("vermoegen")
  @NotNull
  public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty("vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   **/
  public SteuerdatenDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public SteuerdatenDto typ(SteuerdatenTypDto typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  public SteuerdatenTypDto getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(SteuerdatenTypDto typ) {
    this.typ = typ;
  }

  /**
   **/
  public SteuerdatenDto fahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

  
  @JsonProperty("fahrkostenPartner")
  public Integer getFahrkostenPartner() {
    return fahrkostenPartner;
  }

  @JsonProperty("fahrkostenPartner")
  public void setFahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
  }

  /**
   **/
  public SteuerdatenDto verpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
    return this;
  }

  
  @JsonProperty("verpflegungPartner")
  public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }

  @JsonProperty("verpflegungPartner")
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
  }

  /**
   **/
  public SteuerdatenDto eingenmietwert(Integer eingenmietwert) {
    this.eingenmietwert = eingenmietwert;
    return this;
  }

  
  @JsonProperty("eingenmietwert")
  public Integer getEingenmietwert() {
    return eingenmietwert;
  }

  @JsonProperty("eingenmietwert")
  public void setEingenmietwert(Integer eingenmietwert) {
    this.eingenmietwert = eingenmietwert;
  }

  /**
   **/
  public SteuerdatenDto saeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
    return this;
  }

  
  @JsonProperty("saeule3a")
  public Integer getSaeule3a() {
    return saeule3a;
  }

  @JsonProperty("saeule3a")
  public void setSaeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
  }

  /**
   **/
  public SteuerdatenDto saeule2(Integer saeule2) {
    this.saeule2 = saeule2;
    return this;
  }

  
  @JsonProperty("saeule2")
  public Integer getSaeule2() {
    return saeule2;
  }

  @JsonProperty("saeule2")
  public void setSaeule2(Integer saeule2) {
    this.saeule2 = saeule2;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteuerdatenDto steuerdaten = (SteuerdatenDto) o;
    return Objects.equals(this.steuernStaat, steuerdaten.steuernStaat) &&
        Objects.equals(this.steuernBund, steuerdaten.steuernBund) &&
        Objects.equals(this.fahrkosten, steuerdaten.fahrkosten) &&
        Objects.equals(this.verpflegung, steuerdaten.verpflegung) &&
        Objects.equals(this.steuerjahr, steuerdaten.steuerjahr) &&
        Objects.equals(this.veranlagungscode, steuerdaten.veranlagungscode) &&
        Objects.equals(this.totalEinkuenfte, steuerdaten.totalEinkuenfte) &&
        Objects.equals(this.isArbeitsverhaeltnisSelbstaendig, steuerdaten.isArbeitsverhaeltnisSelbstaendig) &&
        Objects.equals(this.kinderalimente, steuerdaten.kinderalimente) &&
        Objects.equals(this.ergaenzungsleistungen, steuerdaten.ergaenzungsleistungen) &&
        Objects.equals(this.vermoegen, steuerdaten.vermoegen) &&
        Objects.equals(this.id, steuerdaten.id) &&
        Objects.equals(this.typ, steuerdaten.typ) &&
        Objects.equals(this.fahrkostenPartner, steuerdaten.fahrkostenPartner) &&
        Objects.equals(this.verpflegungPartner, steuerdaten.verpflegungPartner) &&
        Objects.equals(this.eingenmietwert, steuerdaten.eingenmietwert) &&
        Objects.equals(this.saeule3a, steuerdaten.saeule3a) &&
        Objects.equals(this.saeule2, steuerdaten.saeule2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuernStaat, steuernBund, fahrkosten, verpflegung, steuerjahr, veranlagungscode, totalEinkuenfte, isArbeitsverhaeltnisSelbstaendig, kinderalimente, ergaenzungsleistungen, vermoegen, id, typ, fahrkostenPartner, verpflegungPartner, eingenmietwert, saeule3a, saeule2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuerdatenDto {\n");
    
    sb.append("    steuernStaat: ").append(toIndentedString(steuernStaat)).append("\n");
    sb.append("    steuernBund: ").append(toIndentedString(steuernBund)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    veranlagungscode: ").append(toIndentedString(veranlagungscode)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    isArbeitsverhaeltnisSelbstaendig: ").append(toIndentedString(isArbeitsverhaeltnisSelbstaendig)).append("\n");
    sb.append("    kinderalimente: ").append(toIndentedString(kinderalimente)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    eingenmietwert: ").append(toIndentedString(eingenmietwert)).append("\n");
    sb.append("    saeule3a: ").append(toIndentedString(saeule3a)).append("\n");
    sb.append("    saeule2: ").append(toIndentedString(saeule2)).append("\n");
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

