package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("FreiwilligDarlehenUpdateGs")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FreiwilligDarlehenUpdateGsDto  implements Serializable {
  private @Valid Integer betragGewuenscht;
  private @Valid Integer schulden;
  private @Valid Integer anzahlBetreibungen;
  private @Valid List<ch.dvbern.stip.api.darlehen.type.DarlehenGrund> gruende;

  /**
   * minimum: 0
   **/
  public FreiwilligDarlehenUpdateGsDto betragGewuenscht(Integer betragGewuenscht) {
    this.betragGewuenscht = betragGewuenscht;
    return this;
  }

  
  @JsonProperty("betragGewuenscht")
 @Min(0)  public Integer getBetragGewuenscht() {
    return betragGewuenscht;
  }

  @JsonProperty("betragGewuenscht")
  public void setBetragGewuenscht(Integer betragGewuenscht) {
    this.betragGewuenscht = betragGewuenscht;
  }

  /**
   * minimum: 0
   **/
  public FreiwilligDarlehenUpdateGsDto schulden(Integer schulden) {
    this.schulden = schulden;
    return this;
  }

  
  @JsonProperty("schulden")
 @Min(0)  public Integer getSchulden() {
    return schulden;
  }

  @JsonProperty("schulden")
  public void setSchulden(Integer schulden) {
    this.schulden = schulden;
  }

  /**
   * minimum: 0
   **/
  public FreiwilligDarlehenUpdateGsDto anzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
    return this;
  }

  
  @JsonProperty("anzahlBetreibungen")
 @Min(0)  public Integer getAnzahlBetreibungen() {
    return anzahlBetreibungen;
  }

  @JsonProperty("anzahlBetreibungen")
  public void setAnzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
  }

  /**
   **/
  public FreiwilligDarlehenUpdateGsDto gruende(List<ch.dvbern.stip.api.darlehen.type.DarlehenGrund> gruende) {
    this.gruende = gruende;
    return this;
  }

  
  @JsonProperty("gruende")
  public List<ch.dvbern.stip.api.darlehen.type.DarlehenGrund> getGruende() {
    return gruende;
  }

  @JsonProperty("gruende")
  public void setGruende(List<ch.dvbern.stip.api.darlehen.type.DarlehenGrund> gruende) {
    this.gruende = gruende;
  }

  public FreiwilligDarlehenUpdateGsDto addGruendeItem(ch.dvbern.stip.api.darlehen.type.DarlehenGrund gruendeItem) {
    if (this.gruende == null) {
      this.gruende = new ArrayList<>();
    }

    this.gruende.add(gruendeItem);
    return this;
  }

  public FreiwilligDarlehenUpdateGsDto removeGruendeItem(ch.dvbern.stip.api.darlehen.type.DarlehenGrund gruendeItem) {
    if (gruendeItem != null && this.gruende != null) {
      this.gruende.remove(gruendeItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FreiwilligDarlehenUpdateGsDto freiwilligDarlehenUpdateGs = (FreiwilligDarlehenUpdateGsDto) o;
    return Objects.equals(this.betragGewuenscht, freiwilligDarlehenUpdateGs.betragGewuenscht) &&
        Objects.equals(this.schulden, freiwilligDarlehenUpdateGs.schulden) &&
        Objects.equals(this.anzahlBetreibungen, freiwilligDarlehenUpdateGs.anzahlBetreibungen) &&
        Objects.equals(this.gruende, freiwilligDarlehenUpdateGs.gruende);
  }

  @Override
  public int hashCode() {
    return Objects.hash(betragGewuenscht, schulden, anzahlBetreibungen, gruende);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FreiwilligDarlehenUpdateGsDto {\n");
    
    sb.append("    betragGewuenscht: ").append(toIndentedString(betragGewuenscht)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    gruende: ").append(toIndentedString(gruende)).append("\n");
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

