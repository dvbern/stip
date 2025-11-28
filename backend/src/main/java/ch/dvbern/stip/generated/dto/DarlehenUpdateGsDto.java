package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DarlehenGrundDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DarlehenUpdateGs")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenUpdateGsDto  implements Serializable {
  private @Valid Integer darlehenBetragGewuenscht;
  private @Valid Integer schulden;
  private @Valid Integer anzahlBetreibungen;
  private @Valid DarlehenGrundDto grund;

  /**
   * minimum: 0
   **/
  public DarlehenUpdateGsDto darlehenBetragGewuenscht(Integer darlehenBetragGewuenscht) {
    this.darlehenBetragGewuenscht = darlehenBetragGewuenscht;
    return this;
  }

  
  @JsonProperty("darlehenBetragGewuenscht")
 @Min(0)  public Integer getDarlehenBetragGewuenscht() {
    return darlehenBetragGewuenscht;
  }

  @JsonProperty("darlehenBetragGewuenscht")
  public void setDarlehenBetragGewuenscht(Integer darlehenBetragGewuenscht) {
    this.darlehenBetragGewuenscht = darlehenBetragGewuenscht;
  }

  /**
   * minimum: 0
   **/
  public DarlehenUpdateGsDto schulden(Integer schulden) {
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
  public DarlehenUpdateGsDto anzahlBetreibungen(Integer anzahlBetreibungen) {
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
  public DarlehenUpdateGsDto grund(DarlehenGrundDto grund) {
    this.grund = grund;
    return this;
  }

  
  @JsonProperty("grund")
  public DarlehenGrundDto getGrund() {
    return grund;
  }

  @JsonProperty("grund")
  public void setGrund(DarlehenGrundDto grund) {
    this.grund = grund;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DarlehenUpdateGsDto darlehenUpdateGs = (DarlehenUpdateGsDto) o;
    return Objects.equals(this.darlehenBetragGewuenscht, darlehenUpdateGs.darlehenBetragGewuenscht) &&
        Objects.equals(this.schulden, darlehenUpdateGs.schulden) &&
        Objects.equals(this.anzahlBetreibungen, darlehenUpdateGs.anzahlBetreibungen) &&
        Objects.equals(this.grund, darlehenUpdateGs.grund);
  }

  @Override
  public int hashCode() {
    return Objects.hash(darlehenBetragGewuenscht, schulden, anzahlBetreibungen, grund);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenUpdateGsDto {\n");
    
    sb.append("    darlehenBetragGewuenscht: ").append(toIndentedString(darlehenBetragGewuenscht)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    grund: ").append(toIndentedString(grund)).append("\n");
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

