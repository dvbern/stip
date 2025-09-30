package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungsPensumDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemDto;
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



@JsonTypeName("AusbildungDashboardItem")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungDashboardItemDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid String ausbildungBegin;
  private @Valid String ausbildungEnd;
  private @Valid AusbildungsPensumDto pensum;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus status;
  private @Valid Boolean editable;
  private @Valid UUID id;
  private @Valid String fachrichtungBerufsbezeichnung;
  private @Valid Boolean ausbildungNichtGefunden;
  private @Valid Boolean besuchtBMS;
  private @Valid String alternativeAusbildungsstaette;
  private @Valid String alternativeAusbildungsgang;
  private @Valid String ausbildungsortPLZ;
  private @Valid String ausbildungsort;
  private @Valid Boolean isAusbildungAusland;
  private @Valid UUID landId;
  private @Valid AusbildungsgangDto ausbildungsgang;
  private @Valid List<GesuchDashboardItemDto> gesuchs;

  /**
   **/
  public AusbildungDashboardItemDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   * Datum im Format mm.YYYY
   **/
  public AusbildungDashboardItemDto ausbildungBegin(String ausbildungBegin) {
    this.ausbildungBegin = ausbildungBegin;
    return this;
  }

  
  @JsonProperty("ausbildungBegin")
  @NotNull
  public String getAusbildungBegin() {
    return ausbildungBegin;
  }

  @JsonProperty("ausbildungBegin")
  public void setAusbildungBegin(String ausbildungBegin) {
    this.ausbildungBegin = ausbildungBegin;
  }

  /**
   * Datum im Format mm.YYYY
   **/
  public AusbildungDashboardItemDto ausbildungEnd(String ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
    return this;
  }

  
  @JsonProperty("ausbildungEnd")
  @NotNull
  public String getAusbildungEnd() {
    return ausbildungEnd;
  }

  @JsonProperty("ausbildungEnd")
  public void setAusbildungEnd(String ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
  }

  /**
   **/
  public AusbildungDashboardItemDto pensum(AusbildungsPensumDto pensum) {
    this.pensum = pensum;
    return this;
  }

  
  @JsonProperty("pensum")
  @NotNull
  public AusbildungsPensumDto getPensum() {
    return pensum;
  }

  @JsonProperty("pensum")
  public void setPensum(AusbildungsPensumDto pensum) {
    this.pensum = pensum;
  }

  /**
   **/
  public AusbildungDashboardItemDto status(ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus status) {
    this.status = status;
  }

  /**
   **/
  public AusbildungDashboardItemDto editable(Boolean editable) {
    this.editable = editable;
    return this;
  }

  
  @JsonProperty("editable")
  @NotNull
  public Boolean getEditable() {
    return editable;
  }

  @JsonProperty("editable")
  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  /**
   **/
  public AusbildungDashboardItemDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public AusbildungDashboardItemDto fachrichtungBerufsbezeichnung(String fachrichtungBerufsbezeichnung) {
    this.fachrichtungBerufsbezeichnung = fachrichtungBerufsbezeichnung;
    return this;
  }

  
  @JsonProperty("fachrichtungBerufsbezeichnung")
  public String getFachrichtungBerufsbezeichnung() {
    return fachrichtungBerufsbezeichnung;
  }

  @JsonProperty("fachrichtungBerufsbezeichnung")
  public void setFachrichtungBerufsbezeichnung(String fachrichtungBerufsbezeichnung) {
    this.fachrichtungBerufsbezeichnung = fachrichtungBerufsbezeichnung;
  }

  /**
   **/
  public AusbildungDashboardItemDto ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
    this.ausbildungNichtGefunden = ausbildungNichtGefunden;
    return this;
  }

  
  @JsonProperty("ausbildungNichtGefunden")
  public Boolean getAusbildungNichtGefunden() {
    return ausbildungNichtGefunden;
  }

  @JsonProperty("ausbildungNichtGefunden")
  public void setAusbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
    this.ausbildungNichtGefunden = ausbildungNichtGefunden;
  }

  /**
   * Required wenn Abschluss.askForBerufsmaturitaet &#x3D; true
   **/
  public AusbildungDashboardItemDto besuchtBMS(Boolean besuchtBMS) {
    this.besuchtBMS = besuchtBMS;
    return this;
  }

  
  @JsonProperty("besuchtBMS")
  public Boolean getBesuchtBMS() {
    return besuchtBMS;
  }

  @JsonProperty("besuchtBMS")
  public void setBesuchtBMS(Boolean besuchtBMS) {
    this.besuchtBMS = besuchtBMS;
  }

  /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   **/
  public AusbildungDashboardItemDto alternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
    this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
    return this;
  }

  
  @JsonProperty("alternativeAusbildungsstaette")
  public String getAlternativeAusbildungsstaette() {
    return alternativeAusbildungsstaette;
  }

  @JsonProperty("alternativeAusbildungsstaette")
  public void setAlternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
    this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
  }

  /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   **/
  public AusbildungDashboardItemDto alternativeAusbildungsgang(String alternativeAusbildungsgang) {
    this.alternativeAusbildungsgang = alternativeAusbildungsgang;
    return this;
  }

  
  @JsonProperty("alternativeAusbildungsgang")
  public String getAlternativeAusbildungsgang() {
    return alternativeAusbildungsgang;
  }

  @JsonProperty("alternativeAusbildungsgang")
  public void setAlternativeAusbildungsgang(String alternativeAusbildungsgang) {
    this.alternativeAusbildungsgang = alternativeAusbildungsgang;
  }

  /**
   * Not required if isAusbildungAusland &#x3D; true
   **/
  public AusbildungDashboardItemDto ausbildungsortPLZ(String ausbildungsortPLZ) {
    this.ausbildungsortPLZ = ausbildungsortPLZ;
    return this;
  }

  
  @JsonProperty("ausbildungsortPLZ")
  public String getAusbildungsortPLZ() {
    return ausbildungsortPLZ;
  }

  @JsonProperty("ausbildungsortPLZ")
  public void setAusbildungsortPLZ(String ausbildungsortPLZ) {
    this.ausbildungsortPLZ = ausbildungsortPLZ;
  }

  /**
   * Not required if isAusbildungAusland &#x3D; true
   **/
  public AusbildungDashboardItemDto ausbildungsort(String ausbildungsort) {
    this.ausbildungsort = ausbildungsort;
    return this;
  }

  
  @JsonProperty("ausbildungsort")
  public String getAusbildungsort() {
    return ausbildungsort;
  }

  @JsonProperty("ausbildungsort")
  public void setAusbildungsort(String ausbildungsort) {
    this.ausbildungsort = ausbildungsort;
  }

  /**
   **/
  public AusbildungDashboardItemDto isAusbildungAusland(Boolean isAusbildungAusland) {
    this.isAusbildungAusland = isAusbildungAusland;
    return this;
  }

  
  @JsonProperty("isAusbildungAusland")
  public Boolean getIsAusbildungAusland() {
    return isAusbildungAusland;
  }

  @JsonProperty("isAusbildungAusland")
  public void setIsAusbildungAusland(Boolean isAusbildungAusland) {
    this.isAusbildungAusland = isAusbildungAusland;
  }

  /**
   **/
  public AusbildungDashboardItemDto landId(UUID landId) {
    this.landId = landId;
    return this;
  }

  
  @JsonProperty("landId")
  public UUID getLandId() {
    return landId;
  }

  @JsonProperty("landId")
  public void setLandId(UUID landId) {
    this.landId = landId;
  }

  /**
   **/
  public AusbildungDashboardItemDto ausbildungsgang(AusbildungsgangDto ausbildungsgang) {
    this.ausbildungsgang = ausbildungsgang;
    return this;
  }

  
  @JsonProperty("ausbildungsgang")
  public AusbildungsgangDto getAusbildungsgang() {
    return ausbildungsgang;
  }

  @JsonProperty("ausbildungsgang")
  public void setAusbildungsgang(AusbildungsgangDto ausbildungsgang) {
    this.ausbildungsgang = ausbildungsgang;
  }

  /**
   **/
  public AusbildungDashboardItemDto gesuchs(List<GesuchDashboardItemDto> gesuchs) {
    this.gesuchs = gesuchs;
    return this;
  }

  
  @JsonProperty("gesuchs")
  public List<GesuchDashboardItemDto> getGesuchs() {
    return gesuchs;
  }

  @JsonProperty("gesuchs")
  public void setGesuchs(List<GesuchDashboardItemDto> gesuchs) {
    this.gesuchs = gesuchs;
  }

  public AusbildungDashboardItemDto addGesuchsItem(GesuchDashboardItemDto gesuchsItem) {
    if (this.gesuchs == null) {
      this.gesuchs = new ArrayList<>();
    }

    this.gesuchs.add(gesuchsItem);
    return this;
  }

  public AusbildungDashboardItemDto removeGesuchsItem(GesuchDashboardItemDto gesuchsItem) {
    if (gesuchsItem != null && this.gesuchs != null) {
      this.gesuchs.remove(gesuchsItem);
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
    AusbildungDashboardItemDto ausbildungDashboardItem = (AusbildungDashboardItemDto) o;
    return Objects.equals(this.fallId, ausbildungDashboardItem.fallId) &&
        Objects.equals(this.ausbildungBegin, ausbildungDashboardItem.ausbildungBegin) &&
        Objects.equals(this.ausbildungEnd, ausbildungDashboardItem.ausbildungEnd) &&
        Objects.equals(this.pensum, ausbildungDashboardItem.pensum) &&
        Objects.equals(this.status, ausbildungDashboardItem.status) &&
        Objects.equals(this.editable, ausbildungDashboardItem.editable) &&
        Objects.equals(this.id, ausbildungDashboardItem.id) &&
        Objects.equals(this.fachrichtungBerufsbezeichnung, ausbildungDashboardItem.fachrichtungBerufsbezeichnung) &&
        Objects.equals(this.ausbildungNichtGefunden, ausbildungDashboardItem.ausbildungNichtGefunden) &&
        Objects.equals(this.besuchtBMS, ausbildungDashboardItem.besuchtBMS) &&
        Objects.equals(this.alternativeAusbildungsstaette, ausbildungDashboardItem.alternativeAusbildungsstaette) &&
        Objects.equals(this.alternativeAusbildungsgang, ausbildungDashboardItem.alternativeAusbildungsgang) &&
        Objects.equals(this.ausbildungsortPLZ, ausbildungDashboardItem.ausbildungsortPLZ) &&
        Objects.equals(this.ausbildungsort, ausbildungDashboardItem.ausbildungsort) &&
        Objects.equals(this.isAusbildungAusland, ausbildungDashboardItem.isAusbildungAusland) &&
        Objects.equals(this.landId, ausbildungDashboardItem.landId) &&
        Objects.equals(this.ausbildungsgang, ausbildungDashboardItem.ausbildungsgang) &&
        Objects.equals(this.gesuchs, ausbildungDashboardItem.gesuchs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, ausbildungBegin, ausbildungEnd, pensum, status, editable, id, fachrichtungBerufsbezeichnung, ausbildungNichtGefunden, besuchtBMS, alternativeAusbildungsstaette, alternativeAusbildungsgang, ausbildungsortPLZ, ausbildungsort, isAusbildungAusland, landId, ausbildungsgang, gesuchs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungDashboardItemDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    ausbildungBegin: ").append(toIndentedString(ausbildungBegin)).append("\n");
    sb.append("    ausbildungEnd: ").append(toIndentedString(ausbildungEnd)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fachrichtungBerufsbezeichnung: ").append(toIndentedString(fachrichtungBerufsbezeichnung)).append("\n");
    sb.append("    ausbildungNichtGefunden: ").append(toIndentedString(ausbildungNichtGefunden)).append("\n");
    sb.append("    besuchtBMS: ").append(toIndentedString(besuchtBMS)).append("\n");
    sb.append("    alternativeAusbildungsstaette: ").append(toIndentedString(alternativeAusbildungsstaette)).append("\n");
    sb.append("    alternativeAusbildungsgang: ").append(toIndentedString(alternativeAusbildungsgang)).append("\n");
    sb.append("    ausbildungsortPLZ: ").append(toIndentedString(ausbildungsortPLZ)).append("\n");
    sb.append("    ausbildungsort: ").append(toIndentedString(ausbildungsort)).append("\n");
    sb.append("    isAusbildungAusland: ").append(toIndentedString(isAusbildungAusland)).append("\n");
    sb.append("    landId: ").append(toIndentedString(landId)).append("\n");
    sb.append("    ausbildungsgang: ").append(toIndentedString(ausbildungsgang)).append("\n");
    sb.append("    gesuchs: ").append(toIndentedString(gesuchs)).append("\n");
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

