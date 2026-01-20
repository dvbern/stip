package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("Darlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenDto  implements Serializable {
  private @Valid UUID id;
  private @Valid UUID fallId;
  private @Valid UUID relatedGesuchId;
  private @Valid ch.dvbern.stip.api.darlehen.type.DarlehenStatus status;
  private @Valid Boolean gewaehren;
  private @Valid Integer betrag;
  private @Valid String kommentar;
  private @Valid Integer betragGewuenscht;
  private @Valid Integer schulden;
  private @Valid Integer anzahlBetreibungen;
  private @Valid List<ch.dvbern.stip.api.darlehen.type.DarlehenGrund> gruende;
  private @Valid Boolean isDelegiert;
  private @Valid String timestampErstellt;

  /**
   **/
  public DarlehenDto id(UUID id) {
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
  public DarlehenDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public DarlehenDto relatedGesuchId(UUID relatedGesuchId) {
    this.relatedGesuchId = relatedGesuchId;
    return this;
  }

  
  @JsonProperty("relatedGesuchId")
  public UUID getRelatedGesuchId() {
    return relatedGesuchId;
  }

  @JsonProperty("relatedGesuchId")
  public void setRelatedGesuchId(UUID relatedGesuchId) {
    this.relatedGesuchId = relatedGesuchId;
  }

  /**
   **/
  public DarlehenDto status(ch.dvbern.stip.api.darlehen.type.DarlehenStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public ch.dvbern.stip.api.darlehen.type.DarlehenStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.darlehen.type.DarlehenStatus status) {
    this.status = status;
  }

  /**
   **/
  public DarlehenDto gewaehren(Boolean gewaehren) {
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
  public DarlehenDto betrag(Integer betrag) {
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
  public DarlehenDto kommentar(String kommentar) {
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

  /**
   * minimum: 0
   **/
  public DarlehenDto betragGewuenscht(Integer betragGewuenscht) {
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
  public DarlehenDto schulden(Integer schulden) {
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
  public DarlehenDto anzahlBetreibungen(Integer anzahlBetreibungen) {
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
  public DarlehenDto gruende(List<ch.dvbern.stip.api.darlehen.type.DarlehenGrund> gruende) {
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

  public DarlehenDto addGruendeItem(ch.dvbern.stip.api.darlehen.type.DarlehenGrund gruendeItem) {
    if (this.gruende == null) {
      this.gruende = new ArrayList<>();
    }

    this.gruende.add(gruendeItem);
    return this;
  }

  public DarlehenDto removeGruendeItem(ch.dvbern.stip.api.darlehen.type.DarlehenGrund gruendeItem) {
    if (gruendeItem != null && this.gruende != null) {
      this.gruende.remove(gruendeItem);
    }

    return this;
  }
  /**
   **/
  public DarlehenDto isDelegiert(Boolean isDelegiert) {
    this.isDelegiert = isDelegiert;
    return this;
  }

  
  @JsonProperty("isDelegiert")
  public Boolean getIsDelegiert() {
    return isDelegiert;
  }

  @JsonProperty("isDelegiert")
  public void setIsDelegiert(Boolean isDelegiert) {
    this.isDelegiert = isDelegiert;
  }

  /**
   **/
  public DarlehenDto timestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  public String getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DarlehenDto darlehen = (DarlehenDto) o;
    return Objects.equals(this.id, darlehen.id) &&
        Objects.equals(this.fallId, darlehen.fallId) &&
        Objects.equals(this.relatedGesuchId, darlehen.relatedGesuchId) &&
        Objects.equals(this.status, darlehen.status) &&
        Objects.equals(this.gewaehren, darlehen.gewaehren) &&
        Objects.equals(this.betrag, darlehen.betrag) &&
        Objects.equals(this.kommentar, darlehen.kommentar) &&
        Objects.equals(this.betragGewuenscht, darlehen.betragGewuenscht) &&
        Objects.equals(this.schulden, darlehen.schulden) &&
        Objects.equals(this.anzahlBetreibungen, darlehen.anzahlBetreibungen) &&
        Objects.equals(this.gruende, darlehen.gruende) &&
        Objects.equals(this.isDelegiert, darlehen.isDelegiert) &&
        Objects.equals(this.timestampErstellt, darlehen.timestampErstellt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallId, relatedGesuchId, status, gewaehren, betrag, kommentar, betragGewuenscht, schulden, anzahlBetreibungen, gruende, isDelegiert, timestampErstellt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    relatedGesuchId: ").append(toIndentedString(relatedGesuchId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    gewaehren: ").append(toIndentedString(gewaehren)).append("\n");
    sb.append("    betrag: ").append(toIndentedString(betrag)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    betragGewuenscht: ").append(toIndentedString(betragGewuenscht)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    gruende: ").append(toIndentedString(gruende)).append("\n");
    sb.append("    isDelegiert: ").append(toIndentedString(isDelegiert)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
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

