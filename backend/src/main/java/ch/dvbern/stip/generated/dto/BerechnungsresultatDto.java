package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
  private @Valid Integer berechnungVorKuerzungUndTeilung;
  private @Valid Integer berechnungStipendium;
  private @Valid List<TranchenBerechnungsresultatDto> tranchenBerechnungsresultate = new ArrayList<>();
  private @Valid Integer totalNachKuerzungNachEinreichefrist;
  private @Valid Integer anzahlMonateEinreichefrist;
  private @Valid Integer totalNachKuerzungUnterbruch;
  private @Valid Integer anzahlMonateUnterbruch;
  private @Valid Integer berechnungDarlehen;

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
   * Die Summe der berechneten Stpendiumansprüche für das Gesuch vor Kürzungen und Teilung des Gesetzlichen Darlehens
   **/
  public BerechnungsresultatDto berechnungVorKuerzungUndTeilung(Integer berechnungVorKuerzungUndTeilung) {
    this.berechnungVorKuerzungUndTeilung = berechnungVorKuerzungUndTeilung;
    return this;
  }

  
  @JsonProperty("berechnungVorKuerzungUndTeilung")
  @NotNull
  public Integer getBerechnungVorKuerzungUndTeilung() {
    return berechnungVorKuerzungUndTeilung;
  }

  @JsonProperty("berechnungVorKuerzungUndTeilung")
  public void setBerechnungVorKuerzungUndTeilung(Integer berechnungVorKuerzungUndTeilung) {
    this.berechnungVorKuerzungUndTeilung = berechnungVorKuerzungUndTeilung;
  }

  /**
   * Berechneter Stpendiumsanspruch für das Gesuch
   **/
  public BerechnungsresultatDto berechnungStipendium(Integer berechnungStipendium) {
    this.berechnungStipendium = berechnungStipendium;
    return this;
  }

  
  @JsonProperty("berechnungStipendium")
  @NotNull
  public Integer getBerechnungStipendium() {
    return berechnungStipendium;
  }

  @JsonProperty("berechnungStipendium")
  public void setBerechnungStipendium(Integer berechnungStipendium) {
    this.berechnungStipendium = berechnungStipendium;
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
   * Die Summe nach der Kürzung der verspäteten Eingabe
   **/
  public BerechnungsresultatDto totalNachKuerzungNachEinreichefrist(Integer totalNachKuerzungNachEinreichefrist) {
    this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
    return this;
  }

  
  @JsonProperty("totalNachKuerzungNachEinreichefrist")
  public Integer getTotalNachKuerzungNachEinreichefrist() {
    return totalNachKuerzungNachEinreichefrist;
  }

  @JsonProperty("totalNachKuerzungNachEinreichefrist")
  public void setTotalNachKuerzungNachEinreichefrist(Integer totalNachKuerzungNachEinreichefrist) {
    this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
  }

  /**
   * Die Anzahl Monate der verspäteten Eingabe
   **/
  public BerechnungsresultatDto anzahlMonateEinreichefrist(Integer anzahlMonateEinreichefrist) {
    this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
    return this;
  }

  
  @JsonProperty("anzahlMonateEinreichefrist")
  public Integer getAnzahlMonateEinreichefrist() {
    return anzahlMonateEinreichefrist;
  }

  @JsonProperty("anzahlMonateEinreichefrist")
  public void setAnzahlMonateEinreichefrist(Integer anzahlMonateEinreichefrist) {
    this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
  }

  /**
   * Die Summe nach der Kürzung des Unterbruchs der Ausbildung
   **/
  public BerechnungsresultatDto totalNachKuerzungUnterbruch(Integer totalNachKuerzungUnterbruch) {
    this.totalNachKuerzungUnterbruch = totalNachKuerzungUnterbruch;
    return this;
  }

  
  @JsonProperty("totalNachKuerzungUnterbruch")
  public Integer getTotalNachKuerzungUnterbruch() {
    return totalNachKuerzungUnterbruch;
  }

  @JsonProperty("totalNachKuerzungUnterbruch")
  public void setTotalNachKuerzungUnterbruch(Integer totalNachKuerzungUnterbruch) {
    this.totalNachKuerzungUnterbruch = totalNachKuerzungUnterbruch;
  }

  /**
   * Die Anzahl Monate von dem Unterbruch der Ausbildung
   **/
  public BerechnungsresultatDto anzahlMonateUnterbruch(Integer anzahlMonateUnterbruch) {
    this.anzahlMonateUnterbruch = anzahlMonateUnterbruch;
    return this;
  }

  
  @JsonProperty("anzahlMonateUnterbruch")
  public Integer getAnzahlMonateUnterbruch() {
    return anzahlMonateUnterbruch;
  }

  @JsonProperty("anzahlMonateUnterbruch")
  public void setAnzahlMonateUnterbruch(Integer anzahlMonateUnterbruch) {
    this.anzahlMonateUnterbruch = anzahlMonateUnterbruch;
  }

  /**
   * Berechneter Darlehensanspruch für das Gesuch
   **/
  public BerechnungsresultatDto berechnungDarlehen(Integer berechnungDarlehen) {
    this.berechnungDarlehen = berechnungDarlehen;
    return this;
  }

  
  @JsonProperty("berechnungDarlehen")
  public Integer getBerechnungDarlehen() {
    return berechnungDarlehen;
  }

  @JsonProperty("berechnungDarlehen")
  public void setBerechnungDarlehen(Integer berechnungDarlehen) {
    this.berechnungDarlehen = berechnungDarlehen;
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
        Objects.equals(this.berechnungVorKuerzungUndTeilung, berechnungsresultat.berechnungVorKuerzungUndTeilung) &&
        Objects.equals(this.berechnungStipendium, berechnungsresultat.berechnungStipendium) &&
        Objects.equals(this.tranchenBerechnungsresultate, berechnungsresultat.tranchenBerechnungsresultate) &&
        Objects.equals(this.totalNachKuerzungNachEinreichefrist, berechnungsresultat.totalNachKuerzungNachEinreichefrist) &&
        Objects.equals(this.anzahlMonateEinreichefrist, berechnungsresultat.anzahlMonateEinreichefrist) &&
        Objects.equals(this.totalNachKuerzungUnterbruch, berechnungsresultat.totalNachKuerzungUnterbruch) &&
        Objects.equals(this.anzahlMonateUnterbruch, berechnungsresultat.anzahlMonateUnterbruch) &&
        Objects.equals(this.berechnungDarlehen, berechnungsresultat.berechnungDarlehen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, berechnungVorKuerzungUndTeilung, berechnungStipendium, tranchenBerechnungsresultate, totalNachKuerzungNachEinreichefrist, anzahlMonateEinreichefrist, totalNachKuerzungUnterbruch, anzahlMonateUnterbruch, berechnungDarlehen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BerechnungsresultatDto {\n");
    
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    berechnungVorKuerzungUndTeilung: ").append(toIndentedString(berechnungVorKuerzungUndTeilung)).append("\n");
    sb.append("    berechnungStipendium: ").append(toIndentedString(berechnungStipendium)).append("\n");
    sb.append("    tranchenBerechnungsresultate: ").append(toIndentedString(tranchenBerechnungsresultate)).append("\n");
    sb.append("    totalNachKuerzungNachEinreichefrist: ").append(toIndentedString(totalNachKuerzungNachEinreichefrist)).append("\n");
    sb.append("    anzahlMonateEinreichefrist: ").append(toIndentedString(anzahlMonateEinreichefrist)).append("\n");
    sb.append("    totalNachKuerzungUnterbruch: ").append(toIndentedString(totalNachKuerzungUnterbruch)).append("\n");
    sb.append("    anzahlMonateUnterbruch: ").append(toIndentedString(anzahlMonateUnterbruch)).append("\n");
    sb.append("    berechnungDarlehen: ").append(toIndentedString(berechnungDarlehen)).append("\n");
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

