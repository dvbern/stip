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



@JsonTypeName("Land")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class LandDto  implements Serializable {
  private @Valid String laendercodeBfs;
  private @Valid Boolean isEuEfta;
  private @Valid Boolean eintragGueltig;
  private @Valid String deKurzform;
  private @Valid String frKurzform;
  private @Valid String itKurzform;
  private @Valid String enKurzform;
  private @Valid UUID id;
  private @Valid String iso2code;
  private @Valid String iso3code;

  /**
   **/
  public LandDto laendercodeBfs(String laendercodeBfs) {
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
  public LandDto isEuEfta(Boolean isEuEfta) {
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
  public LandDto eintragGueltig(Boolean eintragGueltig) {
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
  public LandDto deKurzform(String deKurzform) {
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
  public LandDto frKurzform(String frKurzform) {
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
  public LandDto itKurzform(String itKurzform) {
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
  public LandDto enKurzform(String enKurzform) {
    this.enKurzform = enKurzform;
    return this;
  }

  
  @JsonProperty("enKurzform")
  @NotNull
  public String getEnKurzform() {
    return enKurzform;
  }

  @JsonProperty("enKurzform")
  public void setEnKurzform(String enKurzform) {
    this.enKurzform = enKurzform;
  }

  /**
   **/
  public LandDto id(UUID id) {
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
  public LandDto iso2code(String iso2code) {
    this.iso2code = iso2code;
    return this;
  }

  
  @JsonProperty("iso2code")
  public String getIso2code() {
    return iso2code;
  }

  @JsonProperty("iso2code")
  public void setIso2code(String iso2code) {
    this.iso2code = iso2code;
  }

  /**
   **/
  public LandDto iso3code(String iso3code) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandDto land = (LandDto) o;
    return Objects.equals(this.laendercodeBfs, land.laendercodeBfs) &&
        Objects.equals(this.isEuEfta, land.isEuEfta) &&
        Objects.equals(this.eintragGueltig, land.eintragGueltig) &&
        Objects.equals(this.deKurzform, land.deKurzform) &&
        Objects.equals(this.frKurzform, land.frKurzform) &&
        Objects.equals(this.itKurzform, land.itKurzform) &&
        Objects.equals(this.enKurzform, land.enKurzform) &&
        Objects.equals(this.id, land.id) &&
        Objects.equals(this.iso2code, land.iso2code) &&
        Objects.equals(this.iso3code, land.iso3code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(laendercodeBfs, isEuEfta, eintragGueltig, deKurzform, frKurzform, itKurzform, enKurzform, id, iso2code, iso3code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandDto {\n");
    
    sb.append("    laendercodeBfs: ").append(toIndentedString(laendercodeBfs)).append("\n");
    sb.append("    isEuEfta: ").append(toIndentedString(isEuEfta)).append("\n");
    sb.append("    eintragGueltig: ").append(toIndentedString(eintragGueltig)).append("\n");
    sb.append("    deKurzform: ").append(toIndentedString(deKurzform)).append("\n");
    sb.append("    frKurzform: ").append(toIndentedString(frKurzform)).append("\n");
    sb.append("    itKurzform: ").append(toIndentedString(itKurzform)).append("\n");
    sb.append("    enKurzform: ").append(toIndentedString(enKurzform)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    iso2code: ").append(toIndentedString(iso2code)).append("\n");
    sb.append("    iso3code: ").append(toIndentedString(iso3code)).append("\n");
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

