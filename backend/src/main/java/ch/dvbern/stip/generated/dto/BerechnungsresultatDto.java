package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;

/**
 * Resultat der Berechnung (eine Tranche)
 **/

@JsonTypeName("Berechnungsresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BerechnungsresultatDto  implements Serializable {
  private @Valid Integer berechnung;
  private @Valid JsonObject berechnungsdaten;

  /**
   * Berechneter Stpendiumsanspruch für diese Tranche
   **/
  public BerechnungsresultatDto berechnung(Integer berechnung) {
    this.berechnung = berechnung;
    return this;
  }


  @JsonProperty("berechnung")
  public Integer getBerechnung() {
    return berechnung;
  }

  @JsonProperty("berechnung")
  public void setBerechnung(Integer berechnung) {
    this.berechnung = berechnung;
  }

  /**
   * Resultat der Berechnung als JSON objekt, transparent
   **/
  public BerechnungsresultatDto berechnungsdaten(JsonObject berechnungsdaten) {
    this.berechnungsdaten = berechnungsdaten;
    return this;
  }


  @JsonProperty("berechnungsdaten")
  public Object getBerechnungsdaten() {
    return berechnungsdaten;
  }

  @JsonProperty("berechnungsdaten")
  public void setBerechnungsdaten(JsonObject berechnungsdaten) {
    this.berechnungsdaten = berechnungsdaten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BerechnungsresultatDto berechnungsresultat = (BerechnungsresultatDto) o;
    return Objects.equals(this.berechnung, berechnungsresultat.berechnung) &&
        Objects.equals(this.berechnungsdaten, berechnungsresultat.berechnungsdaten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(berechnung, berechnungsdaten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BerechnungsresultatDto {\n");

    sb.append("    berechnung: ").append(toIndentedString(berechnung)).append("\n");
    sb.append("    berechnungsdaten: ").append(toIndentedString(berechnungsdaten)).append("\n");
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

