package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Resultat der Berechnung (gesamtes Gesuch)
 **/

@JsonTypeName("Berechnungsresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BerechnungsresultatDto  implements Serializable {
  private @Valid Integer year;
  private @Valid Integer berechnung;
  private @Valid List<TranchenBerechnungsresultatDto> tranchenBerechnungsresultate = new ArrayList<>();
  private @Valid BigDecimal verminderteBerechnungsFaktor;

  /**
   **/
  public BerechnungsresultatDto year(Integer year) {
    this.year = year;
    return this;
  }

  
  @JsonProperty("year")
  @NotNull
  public Integer getYear() {
    return year;
  }

  @JsonProperty("year")
  public void setYear(Integer year) {
    this.year = year;
  }

  /**
   * Berechneter Stpendiumsanspruch für das Gesuch
   **/
  public BerechnungsresultatDto berechnung(Integer berechnung) {
    this.berechnung = berechnung;
    return this;
  }

  
  @JsonProperty("berechnung")
  @NotNull
  public Integer getBerechnung() {
    return berechnung;
  }

  @JsonProperty("berechnung")
  public void setBerechnung(Integer berechnung) {
    this.berechnung = berechnung;
  }

  /**
   * Resultate der Berechnung für die Tranchen des Gesuchs
   **/
  public BerechnungsresultatDto tranchenBerechnungsresultate(List<TranchenBerechnungsresultatDto> tranchenBerechnungsresultate) {
    this.tranchenBerechnungsresultate = tranchenBerechnungsresultate;
    return this;
  }

  
  @JsonProperty("tranchenBerechnungsresultate")
  @NotNull
  public List<TranchenBerechnungsresultatDto> getTranchenBerechnungsresultate() {
    return tranchenBerechnungsresultate;
  }

  @JsonProperty("tranchenBerechnungsresultate")
  public void setTranchenBerechnungsresultate(List<TranchenBerechnungsresultatDto> tranchenBerechnungsresultate) {
    this.tranchenBerechnungsresultate = tranchenBerechnungsresultate;
  }

  public BerechnungsresultatDto addTranchenBerechnungsresultateItem(TranchenBerechnungsresultatDto tranchenBerechnungsresultateItem) {
    if (this.tranchenBerechnungsresultate == null) {
      this.tranchenBerechnungsresultate = new ArrayList<>();
    }

    this.tranchenBerechnungsresultate.add(tranchenBerechnungsresultateItem);
    return this;
  }

  public BerechnungsresultatDto removeTranchenBerechnungsresultateItem(TranchenBerechnungsresultatDto tranchenBerechnungsresultateItem) {
    if (tranchenBerechnungsresultateItem != null && this.tranchenBerechnungsresultate != null) {
      this.tranchenBerechnungsresultate.remove(tranchenBerechnungsresultateItem);
    }

    return this;
  }
  /**
   * Der Faktor (12 / eigentliche Dauer) falls eine verminderte Berechnung stattfand
   **/
  public BerechnungsresultatDto verminderteBerechnungsFaktor(BigDecimal verminderteBerechnungsFaktor) {
    this.verminderteBerechnungsFaktor = verminderteBerechnungsFaktor;
    return this;
  }

  
  @JsonProperty("verminderteBerechnungsFaktor")
  public BigDecimal getVerminderteBerechnungsFaktor() {
    return verminderteBerechnungsFaktor;
  }

  @JsonProperty("verminderteBerechnungsFaktor")
  public void setVerminderteBerechnungsFaktor(BigDecimal verminderteBerechnungsFaktor) {
    this.verminderteBerechnungsFaktor = verminderteBerechnungsFaktor;
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
    return Objects.equals(this.year, berechnungsresultat.year) &&
        Objects.equals(this.berechnung, berechnungsresultat.berechnung) &&
        Objects.equals(this.tranchenBerechnungsresultate, berechnungsresultat.tranchenBerechnungsresultate) &&
        Objects.equals(this.verminderteBerechnungsFaktor, berechnungsresultat.verminderteBerechnungsFaktor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, berechnung, tranchenBerechnungsresultate, verminderteBerechnungsFaktor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BerechnungsresultatDto {\n");
    
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    berechnung: ").append(toIndentedString(berechnung)).append("\n");
    sb.append("    tranchenBerechnungsresultate: ").append(toIndentedString(tranchenBerechnungsresultate)).append("\n");
    sb.append("    verminderteBerechnungsFaktor: ").append(toIndentedString(verminderteBerechnungsFaktor)).append("\n");
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

