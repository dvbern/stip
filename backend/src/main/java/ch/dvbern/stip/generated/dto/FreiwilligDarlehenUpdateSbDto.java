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



@JsonTypeName("FreiwilligDarlehenUpdateSb")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FreiwilligDarlehenUpdateSbDto  implements Serializable {
  private @Valid Boolean gewaehren;
  private @Valid Integer betrag;
  private @Valid String kommentar;

  /**
   **/
  public FreiwilligDarlehenUpdateSbDto gewaehren(Boolean gewaehren) {
    this.gewaehren = gewaehren;
    return this;
  }

  
  @JsonProperty("gewaehren")
  public Boolean getGewaehren() {
    return gewaehren;
  }

  @JsonProperty("gewaehren")
  public void setGewaehren(Boolean gewaehren) {
    this.gewaehren = gewaehren;
  }

  /**
   * minimum: 0
   **/
  public FreiwilligDarlehenUpdateSbDto betrag(Integer betrag) {
    this.betrag = betrag;
    return this;
  }

  
  @JsonProperty("betrag")
 @Min(0)  public Integer getBetrag() {
    return betrag;
  }

  @JsonProperty("betrag")
  public void setBetrag(Integer betrag) {
    this.betrag = betrag;
  }

  /**
   **/
  public FreiwilligDarlehenUpdateSbDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FreiwilligDarlehenUpdateSbDto freiwilligDarlehenUpdateSb = (FreiwilligDarlehenUpdateSbDto) o;
    return Objects.equals(this.gewaehren, freiwilligDarlehenUpdateSb.gewaehren) &&
        Objects.equals(this.betrag, freiwilligDarlehenUpdateSb.betrag) &&
        Objects.equals(this.kommentar, freiwilligDarlehenUpdateSb.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gewaehren, betrag, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FreiwilligDarlehenUpdateSbDto {\n");
    
    sb.append("    gewaehren: ").append(toIndentedString(gewaehren)).append("\n");
    sb.append("    betrag: ").append(toIndentedString(betrag)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
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

