package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("AenderungenWithVerfuegung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AenderungenWithVerfuegungDto  implements Serializable {
  private @Valid List<GesuchTrancheSlimDto> tranchen = new ArrayList<>();
  private @Valid UUID berechnungId;
  private @Valid GesuchTrancheSlimDto aenderung;

  /**
   **/
  public AenderungenWithVerfuegungDto tranchen(List<GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
    return this;
  }

  
  @JsonProperty("tranchen")
  @NotNull
  public List<GesuchTrancheSlimDto> getTranchen() {
    return tranchen;
  }

  @JsonProperty("tranchen")
  public void setTranchen(List<GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
  }

  public AenderungenWithVerfuegungDto addTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (this.tranchen == null) {
      this.tranchen = new ArrayList<>();
    }

    this.tranchen.add(tranchenItem);
    return this;
  }

  public AenderungenWithVerfuegungDto removeTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (tranchenItem != null && this.tranchen != null) {
      this.tranchen.remove(tranchenItem);
    }

    return this;
  }
  /**
   **/
  public AenderungenWithVerfuegungDto berechnungId(UUID berechnungId) {
    this.berechnungId = berechnungId;
    return this;
  }

  
  @JsonProperty("berechnungId")
  @NotNull
  public UUID getBerechnungId() {
    return berechnungId;
  }

  @JsonProperty("berechnungId")
  public void setBerechnungId(UUID berechnungId) {
    this.berechnungId = berechnungId;
  }

  /**
   **/
  public AenderungenWithVerfuegungDto aenderung(GesuchTrancheSlimDto aenderung) {
    this.aenderung = aenderung;
    return this;
  }

  
  @JsonProperty("aenderung")
  @NotNull
  public GesuchTrancheSlimDto getAenderung() {
    return aenderung;
  }

  @JsonProperty("aenderung")
  public void setAenderung(GesuchTrancheSlimDto aenderung) {
    this.aenderung = aenderung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AenderungenWithVerfuegungDto aenderungenWithVerfuegung = (AenderungenWithVerfuegungDto) o;
    return Objects.equals(this.tranchen, aenderungenWithVerfuegung.tranchen) &&
        Objects.equals(this.berechnungId, aenderungenWithVerfuegung.berechnungId) &&
        Objects.equals(this.aenderung, aenderungenWithVerfuegung.aenderung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tranchen, berechnungId, aenderung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AenderungenWithVerfuegungDto {\n");
    
    sb.append("    tranchen: ").append(toIndentedString(tranchen)).append("\n");
    sb.append("    berechnungId: ").append(toIndentedString(berechnungId)).append("\n");
    sb.append("    aenderung: ").append(toIndentedString(aenderung)).append("\n");
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

