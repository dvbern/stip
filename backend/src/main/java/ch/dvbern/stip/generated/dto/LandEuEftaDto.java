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



@JsonTypeName("LandEuEfta")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class LandEuEftaDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.stammdaten.type.Land land;
  private @Valid String laendercodeBfs;
  private @Valid Boolean isEuEfta;
  private @Valid Boolean eintragGueltig;
  private @Valid String deKurzform;
  private @Valid String frKurzform;
  private @Valid String itKurzform;
  private @Valid UUID id;
  private @Valid String iso3code;
  private @Valid String enKurzform;

  /**
   **/
  public LandEuEftaDto land(ch.dvbern.stip.api.stammdaten.type.Land land) {
    this.land = land;
    return this;
  }

  
  @JsonProperty("land")
  @NotNull
  public ch.dvbern.stip.api.stammdaten.type.Land getLand() {
    return land;
  }

  @JsonProperty("land")
  public void setLand(ch.dvbern.stip.api.stammdaten.type.Land land) {
    this.land = land;
  }

  /**
   **/
  public LandEuEftaDto laendercodeBfs(String laendercodeBfs) {
    this.laendercodeBfs = laendercodeBfs;
    return this;
  }

  
  @JsonProperty("laendercodeBfs")
  @NotNull
  public String getLaendercodeBfs() {
    return laendercodeBfs;
  }

  @JsonProperty("laendercodeBfs")
  public void setLaendercodeBfs(String laendercodeBfs) {
    this.laendercodeBfs = laendercodeBfs;
  }

  /**
   **/
  public LandEuEftaDto isEuEfta(Boolean isEuEfta) {
    this.isEuEfta = isEuEfta;
    return this;
  }

  
  @JsonProperty("isEuEfta")
  @NotNull
  public Boolean getIsEuEfta() {
    return isEuEfta;
  }

  @JsonProperty("isEuEfta")
  public void setIsEuEfta(Boolean isEuEfta) {
    this.isEuEfta = isEuEfta;
  }

  /**
   **/
  public LandEuEftaDto eintragGueltig(Boolean eintragGueltig) {
    this.eintragGueltig = eintragGueltig;
    return this;
  }

  
  @JsonProperty("eintragGueltig")
  @NotNull
  public Boolean getEintragGueltig() {
    return eintragGueltig;
  }

  @JsonProperty("eintragGueltig")
  public void setEintragGueltig(Boolean eintragGueltig) {
    this.eintragGueltig = eintragGueltig;
  }

  /**
   **/
  public LandEuEftaDto deKurzform(String deKurzform) {
    this.deKurzform = deKurzform;
    return this;
  }

  
  @JsonProperty("deKurzform")
  @NotNull
  public String getDeKurzform() {
    return deKurzform;
  }

  @JsonProperty("deKurzform")
  public void setDeKurzform(String deKurzform) {
    this.deKurzform = deKurzform;
  }

  /**
   **/
  public LandEuEftaDto frKurzform(String frKurzform) {
    this.frKurzform = frKurzform;
    return this;
  }

  
  @JsonProperty("frKurzform")
  @NotNull
  public String getFrKurzform() {
    return frKurzform;
  }

  @JsonProperty("frKurzform")
  public void setFrKurzform(String frKurzform) {
    this.frKurzform = frKurzform;
  }

  /**
   **/
  public LandEuEftaDto itKurzform(String itKurzform) {
    this.itKurzform = itKurzform;
    return this;
  }

  
  @JsonProperty("itKurzform")
  @NotNull
  public String getItKurzform() {
    return itKurzform;
  }

  @JsonProperty("itKurzform")
  public void setItKurzform(String itKurzform) {
    this.itKurzform = itKurzform;
  }

  /**
   **/
  public LandEuEftaDto id(UUID id) {
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
  public LandEuEftaDto iso3code(String iso3code) {
    this.iso3code = iso3code;
    return this;
  }

  
  @JsonProperty("iso3code")
  public String getIso3code() {
    return iso3code;
  }

  @JsonProperty("iso3code")
  public void setIso3code(String iso3code) {
    this.iso3code = iso3code;
  }

  /**
   **/
  public LandEuEftaDto enKurzform(String enKurzform) {
    this.enKurzform = enKurzform;
    return this;
  }

  
  @JsonProperty("enKurzform")
  public String getEnKurzform() {
    return enKurzform;
  }

  @JsonProperty("enKurzform")
  public void setEnKurzform(String enKurzform) {
    this.enKurzform = enKurzform;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandEuEftaDto landEuEfta = (LandEuEftaDto) o;
    return Objects.equals(this.land, landEuEfta.land) &&
        Objects.equals(this.laendercodeBfs, landEuEfta.laendercodeBfs) &&
        Objects.equals(this.isEuEfta, landEuEfta.isEuEfta) &&
        Objects.equals(this.eintragGueltig, landEuEfta.eintragGueltig) &&
        Objects.equals(this.deKurzform, landEuEfta.deKurzform) &&
        Objects.equals(this.frKurzform, landEuEfta.frKurzform) &&
        Objects.equals(this.itKurzform, landEuEfta.itKurzform) &&
        Objects.equals(this.id, landEuEfta.id) &&
        Objects.equals(this.iso3code, landEuEfta.iso3code) &&
        Objects.equals(this.enKurzform, landEuEfta.enKurzform);
  }

  @Override
  public int hashCode() {
    return Objects.hash(land, laendercodeBfs, isEuEfta, eintragGueltig, deKurzform, frKurzform, itKurzform, id, iso3code, enKurzform);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandEuEftaDto {\n");
    
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    laendercodeBfs: ").append(toIndentedString(laendercodeBfs)).append("\n");
    sb.append("    isEuEfta: ").append(toIndentedString(isEuEfta)).append("\n");
    sb.append("    eintragGueltig: ").append(toIndentedString(eintragGueltig)).append("\n");
    sb.append("    deKurzform: ").append(toIndentedString(deKurzform)).append("\n");
    sb.append("    frKurzform: ").append(toIndentedString(frKurzform)).append("\n");
    sb.append("    itKurzform: ").append(toIndentedString(itKurzform)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    iso3code: ").append(toIndentedString(iso3code)).append("\n");
    sb.append("    enKurzform: ").append(toIndentedString(enKurzform)).append("\n");
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

