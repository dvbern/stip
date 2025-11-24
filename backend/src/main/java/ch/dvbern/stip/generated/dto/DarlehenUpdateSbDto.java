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



@JsonTypeName("DarlehenUpdateSb")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenUpdateSbDto  implements Serializable {
  private @Valid Boolean darlehenGewaehren;
  private @Valid Integer darlehenBetrag;
  private @Valid String kommentar;

  /**
   **/
  public DarlehenUpdateSbDto darlehenGewaehren(Boolean darlehenGewaehren) {
    this.darlehenGewaehren = darlehenGewaehren;
    return this;
  }

  
  @JsonProperty("darlehenGewaehren")
  public Boolean getDarlehenGewaehren() {
    return darlehenGewaehren;
  }

  @JsonProperty("darlehenGewaehren")
  public void setDarlehenGewaehren(Boolean darlehenGewaehren) {
    this.darlehenGewaehren = darlehenGewaehren;
  }

  /**
   * minimum: 0
   **/
  public DarlehenUpdateSbDto darlehenBetrag(Integer darlehenBetrag) {
    this.darlehenBetrag = darlehenBetrag;
    return this;
  }

  
  @JsonProperty("darlehenBetrag")
 @Min(0)  public Integer getDarlehenBetrag() {
    return darlehenBetrag;
  }

  @JsonProperty("darlehenBetrag")
  public void setDarlehenBetrag(Integer darlehenBetrag) {
    this.darlehenBetrag = darlehenBetrag;
  }

  /**
   **/
  public DarlehenUpdateSbDto kommentar(String kommentar) {
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
    DarlehenUpdateSbDto darlehenUpdateSb = (DarlehenUpdateSbDto) o;
    return Objects.equals(this.darlehenGewaehren, darlehenUpdateSb.darlehenGewaehren) &&
        Objects.equals(this.darlehenBetrag, darlehenUpdateSb.darlehenBetrag) &&
        Objects.equals(this.kommentar, darlehenUpdateSb.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(darlehenGewaehren, darlehenBetrag, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenUpdateSbDto {\n");
    
    sb.append("    darlehenGewaehren: ").append(toIndentedString(darlehenGewaehren)).append("\n");
    sb.append("    darlehenBetrag: ").append(toIndentedString(darlehenBetrag)).append("\n");
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

