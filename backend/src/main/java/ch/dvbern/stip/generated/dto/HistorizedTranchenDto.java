package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AenderungenWithVerfuegungDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.TranchenWithVerfuegungDto;
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



@JsonTypeName("HistorizedTranchen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class HistorizedTranchenDto  implements Serializable {
  private @Valid List<AenderungenWithVerfuegungDto> akzeptierteAenderungen = new ArrayList<>();
  private @Valid List<GesuchTrancheSlimDto> abgelehnteAenderungen = new ArrayList<>();
  private @Valid TranchenWithVerfuegungDto initial;
  private @Valid GesuchTrancheSlimDto offeneAenderung;

  /**
   **/
  public HistorizedTranchenDto akzeptierteAenderungen(List<AenderungenWithVerfuegungDto> akzeptierteAenderungen) {
    this.akzeptierteAenderungen = akzeptierteAenderungen;
    return this;
  }

  
  @JsonProperty("akzeptierteAenderungen")
  @NotNull
  public List<AenderungenWithVerfuegungDto> getAkzeptierteAenderungen() {
    return akzeptierteAenderungen;
  }

  @JsonProperty("akzeptierteAenderungen")
  public void setAkzeptierteAenderungen(List<AenderungenWithVerfuegungDto> akzeptierteAenderungen) {
    this.akzeptierteAenderungen = akzeptierteAenderungen;
  }

  public HistorizedTranchenDto addAkzeptierteAenderungenItem(AenderungenWithVerfuegungDto akzeptierteAenderungenItem) {
    if (this.akzeptierteAenderungen == null) {
      this.akzeptierteAenderungen = new ArrayList<>();
    }

    this.akzeptierteAenderungen.add(akzeptierteAenderungenItem);
    return this;
  }

  public HistorizedTranchenDto removeAkzeptierteAenderungenItem(AenderungenWithVerfuegungDto akzeptierteAenderungenItem) {
    if (akzeptierteAenderungenItem != null && this.akzeptierteAenderungen != null) {
      this.akzeptierteAenderungen.remove(akzeptierteAenderungenItem);
    }

    return this;
  }
  /**
   **/
  public HistorizedTranchenDto abgelehnteAenderungen(List<GesuchTrancheSlimDto> abgelehnteAenderungen) {
    this.abgelehnteAenderungen = abgelehnteAenderungen;
    return this;
  }

  
  @JsonProperty("abgelehnteAenderungen")
  @NotNull
  public List<GesuchTrancheSlimDto> getAbgelehnteAenderungen() {
    return abgelehnteAenderungen;
  }

  @JsonProperty("abgelehnteAenderungen")
  public void setAbgelehnteAenderungen(List<GesuchTrancheSlimDto> abgelehnteAenderungen) {
    this.abgelehnteAenderungen = abgelehnteAenderungen;
  }

  public HistorizedTranchenDto addAbgelehnteAenderungenItem(GesuchTrancheSlimDto abgelehnteAenderungenItem) {
    if (this.abgelehnteAenderungen == null) {
      this.abgelehnteAenderungen = new ArrayList<>();
    }

    this.abgelehnteAenderungen.add(abgelehnteAenderungenItem);
    return this;
  }

  public HistorizedTranchenDto removeAbgelehnteAenderungenItem(GesuchTrancheSlimDto abgelehnteAenderungenItem) {
    if (abgelehnteAenderungenItem != null && this.abgelehnteAenderungen != null) {
      this.abgelehnteAenderungen.remove(abgelehnteAenderungenItem);
    }

    return this;
  }
  /**
   **/
  public HistorizedTranchenDto initial(TranchenWithVerfuegungDto initial) {
    this.initial = initial;
    return this;
  }

  
  @JsonProperty("initial")
  public TranchenWithVerfuegungDto getInitial() {
    return initial;
  }

  @JsonProperty("initial")
  public void setInitial(TranchenWithVerfuegungDto initial) {
    this.initial = initial;
  }

  /**
   **/
  public HistorizedTranchenDto offeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
    this.offeneAenderung = offeneAenderung;
    return this;
  }

  
  @JsonProperty("offeneAenderung")
  public GesuchTrancheSlimDto getOffeneAenderung() {
    return offeneAenderung;
  }

  @JsonProperty("offeneAenderung")
  public void setOffeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
    this.offeneAenderung = offeneAenderung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistorizedTranchenDto historizedTranchen = (HistorizedTranchenDto) o;
    return Objects.equals(this.akzeptierteAenderungen, historizedTranchen.akzeptierteAenderungen) &&
        Objects.equals(this.abgelehnteAenderungen, historizedTranchen.abgelehnteAenderungen) &&
        Objects.equals(this.initial, historizedTranchen.initial) &&
        Objects.equals(this.offeneAenderung, historizedTranchen.offeneAenderung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(akzeptierteAenderungen, abgelehnteAenderungen, initial, offeneAenderung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistorizedTranchenDto {\n");
    
    sb.append("    akzeptierteAenderungen: ").append(toIndentedString(akzeptierteAenderungen)).append("\n");
    sb.append("    abgelehnteAenderungen: ").append(toIndentedString(abgelehnteAenderungen)).append("\n");
    sb.append("    initial: ").append(toIndentedString(initial)).append("\n");
    sb.append("    offeneAenderung: ").append(toIndentedString(offeneAenderung)).append("\n");
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

