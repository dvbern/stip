package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("StipDecisionText")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class StipDecisionTextDto  implements Serializable {
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.common.type.StipDecision stipDecision;
  private @Valid String titleDe;
  private @Valid String textDe;
  private @Valid String textFr;

  /**
   **/
  public StipDecisionTextDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public StipDecisionTextDto stipDecision(ch.dvbern.stip.api.common.type.StipDecision stipDecision) {
    this.stipDecision = stipDecision;
    return this;
  }

  
  @JsonProperty("stipDecision")
  @NotNull
  public ch.dvbern.stip.api.common.type.StipDecision getStipDecision() {
    return stipDecision;
  }

  @JsonProperty("stipDecision")
  public void setStipDecision(ch.dvbern.stip.api.common.type.StipDecision stipDecision) {
    this.stipDecision = stipDecision;
  }

  /**
   **/
  public StipDecisionTextDto titleDe(String titleDe) {
    this.titleDe = titleDe;
    return this;
  }

  
  @JsonProperty("titleDe")
  @NotNull
  public String getTitleDe() {
    return titleDe;
  }

  @JsonProperty("titleDe")
  public void setTitleDe(String titleDe) {
    this.titleDe = titleDe;
  }

  /**
   **/
  public StipDecisionTextDto textDe(String textDe) {
    this.textDe = textDe;
    return this;
  }

  
  @JsonProperty("textDe")
  @NotNull
  public String getTextDe() {
    return textDe;
  }

  @JsonProperty("textDe")
  public void setTextDe(String textDe) {
    this.textDe = textDe;
  }

  /**
   **/
  public StipDecisionTextDto textFr(String textFr) {
    this.textFr = textFr;
    return this;
  }

  
  @JsonProperty("textFr")
  @NotNull
  public String getTextFr() {
    return textFr;
  }

  @JsonProperty("textFr")
  public void setTextFr(String textFr) {
    this.textFr = textFr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StipDecisionTextDto stipDecisionText = (StipDecisionTextDto) o;
    return Objects.equals(this.id, stipDecisionText.id) &&
        Objects.equals(this.stipDecision, stipDecisionText.stipDecision) &&
        Objects.equals(this.titleDe, stipDecisionText.titleDe) &&
        Objects.equals(this.textDe, stipDecisionText.textDe) &&
        Objects.equals(this.textFr, stipDecisionText.textFr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, stipDecision, titleDe, textDe, textFr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StipDecisionTextDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    stipDecision: ").append(toIndentedString(stipDecision)).append("\n");
    sb.append("    titleDe: ").append(toIndentedString(titleDe)).append("\n");
    sb.append("    textDe: ").append(toIndentedString(textDe)).append("\n");
    sb.append("    textFr: ").append(toIndentedString(textFr)).append("\n");
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

