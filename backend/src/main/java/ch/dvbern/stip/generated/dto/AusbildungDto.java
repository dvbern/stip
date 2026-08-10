package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Ausbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid String ausbildungBegin;
  private @Valid String ausbildungEnd;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum;
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
  private @Valid LocalDate earliestActiveGesuchPeriodeStart;

  protected AusbildungDto(AusbildungDtoBuilder<?, ?> b) {
    this.fallId = b.fallId;
    this.ausbildungBegin = b.ausbildungBegin;
    this.ausbildungEnd = b.ausbildungEnd;
    this.pensum = b.pensum;
    this.status = b.status;
    this.editable = b.editable;
    this.id = b.id;
    this.fachrichtungBerufsbezeichnung = b.fachrichtungBerufsbezeichnung;
    this.ausbildungNichtGefunden = b.ausbildungNichtGefunden;
    this.besuchtBMS = b.besuchtBMS;
    this.alternativeAusbildungsstaette = b.alternativeAusbildungsstaette;
    this.alternativeAusbildungsgang = b.alternativeAusbildungsgang;
    this.ausbildungsortPLZ = b.ausbildungsortPLZ;
    this.ausbildungsort = b.ausbildungsort;
    this.isAusbildungAusland = b.isAusbildungAusland;
    this.landId = b.landId;
    this.ausbildungsgang = b.ausbildungsgang;
    this.earliestActiveGesuchPeriodeStart = b.earliestActiveGesuchPeriodeStart;
  }

  public AusbildungDto() {
  }

  /**
   **/
  public AusbildungDto fallId(UUID fallId) {
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
  public AusbildungDto ausbildungBegin(String ausbildungBegin) {
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
  public AusbildungDto ausbildungEnd(String ausbildungEnd) {
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
  public AusbildungDto pensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
    return this;
  }

  
  @JsonProperty("pensum")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum getPensum() {
    return pensum;
  }

  @JsonProperty("pensum")
  public void setPensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
  }

  /**
   **/
  public AusbildungDto status(ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus status) {
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
  public AusbildungDto editable(Boolean editable) {
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
  public AusbildungDto id(UUID id) {
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
  public AusbildungDto fachrichtungBerufsbezeichnung(String fachrichtungBerufsbezeichnung) {
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
  public AusbildungDto ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
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
  public AusbildungDto besuchtBMS(Boolean besuchtBMS) {
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
  public AusbildungDto alternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
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
  public AusbildungDto alternativeAusbildungsgang(String alternativeAusbildungsgang) {
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
  public AusbildungDto ausbildungsortPLZ(String ausbildungsortPLZ) {
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
  public AusbildungDto ausbildungsort(String ausbildungsort) {
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
  public AusbildungDto isAusbildungAusland(Boolean isAusbildungAusland) {
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
  public AusbildungDto landId(UUID landId) {
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
  public AusbildungDto ausbildungsgang(AusbildungsgangDto ausbildungsgang) {
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
  public AusbildungDto earliestActiveGesuchPeriodeStart(LocalDate earliestActiveGesuchPeriodeStart) {
    this.earliestActiveGesuchPeriodeStart = earliestActiveGesuchPeriodeStart;
    return this;
  }

  
  @JsonProperty("earliestActiveGesuchPeriodeStart")
  public LocalDate getEarliestActiveGesuchPeriodeStart() {
    return earliestActiveGesuchPeriodeStart;
  }

  @JsonProperty("earliestActiveGesuchPeriodeStart")
  public void setEarliestActiveGesuchPeriodeStart(LocalDate earliestActiveGesuchPeriodeStart) {
    this.earliestActiveGesuchPeriodeStart = earliestActiveGesuchPeriodeStart;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungDto ausbildung = (AusbildungDto) o;
    return Objects.equals(this.fallId, ausbildung.fallId) &&
        Objects.equals(this.ausbildungBegin, ausbildung.ausbildungBegin) &&
        Objects.equals(this.ausbildungEnd, ausbildung.ausbildungEnd) &&
        Objects.equals(this.pensum, ausbildung.pensum) &&
        Objects.equals(this.status, ausbildung.status) &&
        Objects.equals(this.editable, ausbildung.editable) &&
        Objects.equals(this.id, ausbildung.id) &&
        Objects.equals(this.fachrichtungBerufsbezeichnung, ausbildung.fachrichtungBerufsbezeichnung) &&
        Objects.equals(this.ausbildungNichtGefunden, ausbildung.ausbildungNichtGefunden) &&
        Objects.equals(this.besuchtBMS, ausbildung.besuchtBMS) &&
        Objects.equals(this.alternativeAusbildungsstaette, ausbildung.alternativeAusbildungsstaette) &&
        Objects.equals(this.alternativeAusbildungsgang, ausbildung.alternativeAusbildungsgang) &&
        Objects.equals(this.ausbildungsortPLZ, ausbildung.ausbildungsortPLZ) &&
        Objects.equals(this.ausbildungsort, ausbildung.ausbildungsort) &&
        Objects.equals(this.isAusbildungAusland, ausbildung.isAusbildungAusland) &&
        Objects.equals(this.landId, ausbildung.landId) &&
        Objects.equals(this.ausbildungsgang, ausbildung.ausbildungsgang) &&
        Objects.equals(this.earliestActiveGesuchPeriodeStart, ausbildung.earliestActiveGesuchPeriodeStart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, ausbildungBegin, ausbildungEnd, pensum, status, editable, id, fachrichtungBerufsbezeichnung, ausbildungNichtGefunden, besuchtBMS, alternativeAusbildungsstaette, alternativeAusbildungsgang, ausbildungsortPLZ, ausbildungsort, isAusbildungAusland, landId, ausbildungsgang, earliestActiveGesuchPeriodeStart);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungDto {\n");
    
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
    sb.append("    earliestActiveGesuchPeriodeStart: ").append(toIndentedString(earliestActiveGesuchPeriodeStart)).append("\n");
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


  public static AusbildungDtoBuilder<?, ?> builder() {
    return new AusbildungDtoBuilderImpl();
  }

  private static final class AusbildungDtoBuilderImpl extends AusbildungDtoBuilder<AusbildungDto, AusbildungDtoBuilderImpl> {

    @Override
    protected AusbildungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungDto build() {
      return new AusbildungDto(this);
    }
  }

  public static abstract class AusbildungDtoBuilder<C extends AusbildungDto, B extends AusbildungDtoBuilder<C, B>>  {
    private UUID fallId;
    private String ausbildungBegin;
    private String ausbildungEnd;
    private ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum;
    private ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus status;
    private Boolean editable;
    private UUID id;
    private String fachrichtungBerufsbezeichnung;
    private Boolean ausbildungNichtGefunden;
    private Boolean besuchtBMS;
    private String alternativeAusbildungsstaette;
    private String alternativeAusbildungsgang;
    private String ausbildungsortPLZ;
    private String ausbildungsort;
    private Boolean isAusbildungAusland;
    private UUID landId;
    private AusbildungsgangDto ausbildungsgang;
    private LocalDate earliestActiveGesuchPeriodeStart;
    protected abstract B self();

    public abstract C build();

    public B fallId(UUID fallId) {
      this.fallId = fallId;
      return self();
    }
    public B ausbildungBegin(String ausbildungBegin) {
      this.ausbildungBegin = ausbildungBegin;
      return self();
    }
    public B ausbildungEnd(String ausbildungEnd) {
      this.ausbildungEnd = ausbildungEnd;
      return self();
    }
    public B pensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
      this.pensum = pensum;
      return self();
    }
    public B status(ch.dvbern.stip.api.ausbildung.type.AusbildungsStatus status) {
      this.status = status;
      return self();
    }
    public B editable(Boolean editable) {
      this.editable = editable;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B fachrichtungBerufsbezeichnung(String fachrichtungBerufsbezeichnung) {
      this.fachrichtungBerufsbezeichnung = fachrichtungBerufsbezeichnung;
      return self();
    }
    public B ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
      this.ausbildungNichtGefunden = ausbildungNichtGefunden;
      return self();
    }
    public B besuchtBMS(Boolean besuchtBMS) {
      this.besuchtBMS = besuchtBMS;
      return self();
    }
    public B alternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
      this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
      return self();
    }
    public B alternativeAusbildungsgang(String alternativeAusbildungsgang) {
      this.alternativeAusbildungsgang = alternativeAusbildungsgang;
      return self();
    }
    public B ausbildungsortPLZ(String ausbildungsortPLZ) {
      this.ausbildungsortPLZ = ausbildungsortPLZ;
      return self();
    }
    public B ausbildungsort(String ausbildungsort) {
      this.ausbildungsort = ausbildungsort;
      return self();
    }
    public B isAusbildungAusland(Boolean isAusbildungAusland) {
      this.isAusbildungAusland = isAusbildungAusland;
      return self();
    }
    public B landId(UUID landId) {
      this.landId = landId;
      return self();
    }
    public B ausbildungsgang(AusbildungsgangDto ausbildungsgang) {
      this.ausbildungsgang = ausbildungsgang;
      return self();
    }
    public B earliestActiveGesuchPeriodeStart(LocalDate earliestActiveGesuchPeriodeStart) {
      this.earliestActiveGesuchPeriodeStart = earliestActiveGesuchPeriodeStart;
      return self();
    }
  }
}

