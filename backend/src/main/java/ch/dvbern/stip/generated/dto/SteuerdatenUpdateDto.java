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



@JsonTypeName("SteuerdatenUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SteuerdatenUpdateDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
  private @Valid Integer steuernStaat;
  private @Valid Integer steuernBund;
  private @Valid Integer fahrkosten;
  private @Valid Integer verpflegung;
  private @Valid Integer steuerjahr;
  private @Valid Integer veranlagungscode;
  private @Valid Integer totalEinkuenfte;
  private @Valid Integer eigenmietwert;
  private @Valid Boolean isArbeitsverhaeltnisSelbstaendig;
  private @Valid Integer kinderalimente;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer vermoegen;
  private @Valid UUID id;
  private @Valid Integer fahrkostenPartner;
  private @Valid Integer verpflegungPartner;
  private @Valid Integer saeule3a;
  private @Valid Integer saeule2;

  /**
   **/
  public SteuerdatenUpdateDto steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
    return this;
  }

  
  @JsonProperty("steuerdatenTyp")
  @NotNull
  public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getSteuerdatenTyp() {
    return steuerdatenTyp;
  }

  @JsonProperty("steuerdatenTyp")
  public void setSteuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
  }

  /**
   **/
  public SteuerdatenUpdateDto steuernStaat(Integer steuernStaat) {
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
  public SteuerdatenUpdateDto steuernBund(Integer steuernBund) {
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
  public SteuerdatenUpdateDto fahrkosten(Integer fahrkosten) {
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
  public SteuerdatenUpdateDto verpflegung(Integer verpflegung) {
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
  public SteuerdatenUpdateDto steuerjahr(Integer steuerjahr) {
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
  public SteuerdatenUpdateDto veranlagungscode(Integer veranlagungscode) {
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
  public SteuerdatenUpdateDto totalEinkuenfte(Integer totalEinkuenfte) {
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
  public SteuerdatenUpdateDto eigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
    return this;
  }

  
  @JsonProperty("eigenmietwert")
  @NotNull
  public Integer getEigenmietwert() {
    return eigenmietwert;
  }

  @JsonProperty("eigenmietwert")
  public void setEigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
  }

  /**
   **/
  public SteuerdatenUpdateDto isArbeitsverhaeltnisSelbstaendig(Boolean isArbeitsverhaeltnisSelbstaendig) {
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
  public SteuerdatenUpdateDto kinderalimente(Integer kinderalimente) {
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
  public SteuerdatenUpdateDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
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
  public SteuerdatenUpdateDto vermoegen(Integer vermoegen) {
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
  public SteuerdatenUpdateDto id(UUID id) {
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
  public SteuerdatenUpdateDto fahrkostenPartner(Integer fahrkostenPartner) {
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
  public SteuerdatenUpdateDto verpflegungPartner(Integer verpflegungPartner) {
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
  public SteuerdatenUpdateDto saeule3a(Integer saeule3a) {
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
  public SteuerdatenUpdateDto saeule2(Integer saeule2) {
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
    SteuerdatenUpdateDto steuerdatenUpdate = (SteuerdatenUpdateDto) o;
    return Objects.equals(this.steuerdatenTyp, steuerdatenUpdate.steuerdatenTyp) &&
        Objects.equals(this.steuernStaat, steuerdatenUpdate.steuernStaat) &&
        Objects.equals(this.steuernBund, steuerdatenUpdate.steuernBund) &&
        Objects.equals(this.fahrkosten, steuerdatenUpdate.fahrkosten) &&
        Objects.equals(this.verpflegung, steuerdatenUpdate.verpflegung) &&
        Objects.equals(this.steuerjahr, steuerdatenUpdate.steuerjahr) &&
        Objects.equals(this.veranlagungscode, steuerdatenUpdate.veranlagungscode) &&
        Objects.equals(this.totalEinkuenfte, steuerdatenUpdate.totalEinkuenfte) &&
        Objects.equals(this.eigenmietwert, steuerdatenUpdate.eigenmietwert) &&
        Objects.equals(this.isArbeitsverhaeltnisSelbstaendig, steuerdatenUpdate.isArbeitsverhaeltnisSelbstaendig) &&
        Objects.equals(this.kinderalimente, steuerdatenUpdate.kinderalimente) &&
        Objects.equals(this.ergaenzungsleistungen, steuerdatenUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.vermoegen, steuerdatenUpdate.vermoegen) &&
        Objects.equals(this.id, steuerdatenUpdate.id) &&
        Objects.equals(this.fahrkostenPartner, steuerdatenUpdate.fahrkostenPartner) &&
        Objects.equals(this.verpflegungPartner, steuerdatenUpdate.verpflegungPartner) &&
        Objects.equals(this.saeule3a, steuerdatenUpdate.saeule3a) &&
        Objects.equals(this.saeule2, steuerdatenUpdate.saeule2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdatenTyp, steuernStaat, steuernBund, fahrkosten, verpflegung, steuerjahr, veranlagungscode, totalEinkuenfte, eigenmietwert, isArbeitsverhaeltnisSelbstaendig, kinderalimente, ergaenzungsleistungen, vermoegen, id, fahrkostenPartner, verpflegungPartner, saeule3a, saeule2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuerdatenUpdateDto {\n");
    
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    steuernStaat: ").append(toIndentedString(steuernStaat)).append("\n");
    sb.append("    steuernBund: ").append(toIndentedString(steuernBund)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    veranlagungscode: ").append(toIndentedString(veranlagungscode)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    isArbeitsverhaeltnisSelbstaendig: ").append(toIndentedString(isArbeitsverhaeltnisSelbstaendig)).append("\n");
    sb.append("    kinderalimente: ").append(toIndentedString(kinderalimente)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
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

