package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ElternUpdate_adresse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ElternUpdateAdresseDto  implements Serializable {
  private @Valid String coAdresse;
  private @Valid String strasse;
  private @Valid String hausnummer;
  private @Valid String plz;
  private @Valid String ort;
  private @Valid String land;
  private @Valid String id;

  /**
   **/
  public ElternUpdateAdresseDto coAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
    return this;
  }

  
  @JsonProperty("coAdresse")
  public String getCoAdresse() {
    return coAdresse;
  }

  @JsonProperty("coAdresse")
  public void setCoAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
  }

  /**
   **/
  public ElternUpdateAdresseDto strasse(String strasse) {
    this.strasse = strasse;
    return this;
  }

  
  @JsonProperty("strasse")
  public String getStrasse() {
    return strasse;
  }

  @JsonProperty("strasse")
  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  /**
   **/
  public ElternUpdateAdresseDto hausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
    return this;
  }

  
  @JsonProperty("hausnummer")
  public String getHausnummer() {
    return hausnummer;
  }

  @JsonProperty("hausnummer")
  public void setHausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
  }

  /**
   **/
  public ElternUpdateAdresseDto plz(String plz) {
    this.plz = plz;
    return this;
  }

  
  @JsonProperty("plz")
  public String getPlz() {
    return plz;
  }

  @JsonProperty("plz")
  public void setPlz(String plz) {
    this.plz = plz;
  }

  /**
   **/
  public ElternUpdateAdresseDto ort(String ort) {
    this.ort = ort;
    return this;
  }

  
  @JsonProperty("ort")
  public String getOrt() {
    return ort;
  }

  @JsonProperty("ort")
  public void setOrt(String ort) {
    this.ort = ort;
  }

  /**
   **/
  public ElternUpdateAdresseDto land(String land) {
    this.land = land;
    return this;
  }

  
  @JsonProperty("land")
  public String getLand() {
    return land;
  }

  @JsonProperty("land")
  public void setLand(String land) {
    this.land = land;
  }

  /**
   **/
  public ElternUpdateAdresseDto id(String id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElternUpdateAdresseDto elternUpdateAdresse = (ElternUpdateAdresseDto) o;
    return Objects.equals(this.coAdresse, elternUpdateAdresse.coAdresse) &&
        Objects.equals(this.strasse, elternUpdateAdresse.strasse) &&
        Objects.equals(this.hausnummer, elternUpdateAdresse.hausnummer) &&
        Objects.equals(this.plz, elternUpdateAdresse.plz) &&
        Objects.equals(this.ort, elternUpdateAdresse.ort) &&
        Objects.equals(this.land, elternUpdateAdresse.land) &&
        Objects.equals(this.id, elternUpdateAdresse.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coAdresse, strasse, hausnummer, plz, ort, land, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElternUpdateAdresseDto {\n");
    
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

