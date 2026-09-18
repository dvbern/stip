package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoLebenslaufAusbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoLebenslaufAusbildungDto  implements Serializable {
  private String abschluss;
  private LocalDate von;
  private LocalDate bis;
  private ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz;
  private Boolean ausbildungAbgeschlossen;
  private String berufsbezeichnungFachrichtung;

  protected DemoLebenslaufAusbildungDto(DemoLebenslaufAusbildungDtoBuilder<?, ?> b) {
    this.abschluss = b.abschluss;
    this.von = b.von;
    this.bis = b.bis;
    this.wohnsitz = b.wohnsitz;
    this.ausbildungAbgeschlossen = b.ausbildungAbgeschlossen;
    this.berufsbezeichnungFachrichtung = b.berufsbezeichnungFachrichtung;
  }

  public DemoLebenslaufAusbildungDto() {
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto abschluss(String abschluss) {
    this.abschluss = abschluss;
    return this;
  }

  
  @JsonProperty(required = true, value = "abschluss")
  @NotNull public String getAbschluss() {
    return abschluss;
  }

  @JsonProperty(required = true, value = "abschluss")
  public void setAbschluss(String abschluss) {
    this.abschluss = abschluss;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto von(LocalDate von) {
    this.von = von;
    return this;
  }

  
  @JsonProperty(required = true, value = "von")
  @NotNull public LocalDate getVon() {
    return von;
  }

  @JsonProperty(required = true, value = "von")
  public void setVon(LocalDate von) {
    this.von = von;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto bis(LocalDate bis) {
    this.bis = bis;
    return this;
  }

  
  @JsonProperty(required = true, value = "bis")
  @NotNull public LocalDate getBis() {
    return bis;
  }

  @JsonProperty(required = true, value = "bis")
  public void setBis(LocalDate bis) {
    this.bis = bis;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto wohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
    this.wohnsitz = wohnsitz;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnsitz")
  @NotNull public ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton getWohnsitz() {
    return wohnsitz;
  }

  @JsonProperty(required = true, value = "wohnsitz")
  public void setWohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
    this.wohnsitz = wohnsitz;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto ausbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
    this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungAbgeschlossen")
  @NotNull public Boolean getAusbildungAbgeschlossen() {
    return ausbildungAbgeschlossen;
  }

  @JsonProperty(required = true, value = "ausbildungAbgeschlossen")
  public void setAusbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
    this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto berufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
    this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
    return this;
  }

  
  @JsonProperty("berufsbezeichnungFachrichtung")
  public String getBerufsbezeichnungFachrichtung() {
    return berufsbezeichnungFachrichtung;
  }

  @JsonProperty("berufsbezeichnungFachrichtung")
  public void setBerufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
    this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoLebenslaufAusbildungDto demoLebenslaufAusbildung = (DemoLebenslaufAusbildungDto) o;
    return Objects.equals(this.abschluss, demoLebenslaufAusbildung.abschluss) &&
        Objects.equals(this.von, demoLebenslaufAusbildung.von) &&
        Objects.equals(this.bis, demoLebenslaufAusbildung.bis) &&
        Objects.equals(this.wohnsitz, demoLebenslaufAusbildung.wohnsitz) &&
        Objects.equals(this.ausbildungAbgeschlossen, demoLebenslaufAusbildung.ausbildungAbgeschlossen) &&
        Objects.equals(this.berufsbezeichnungFachrichtung, demoLebenslaufAusbildung.berufsbezeichnungFachrichtung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abschluss, von, bis, wohnsitz, ausbildungAbgeschlossen, berufsbezeichnungFachrichtung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoLebenslaufAusbildungDto {\n");
    
    sb.append("    abschluss: ").append(toIndentedString(abschluss)).append("\n");
    sb.append("    von: ").append(toIndentedString(von)).append("\n");
    sb.append("    bis: ").append(toIndentedString(bis)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
    sb.append("    ausbildungAbgeschlossen: ").append(toIndentedString(ausbildungAbgeschlossen)).append("\n");
    sb.append("    berufsbezeichnungFachrichtung: ").append(toIndentedString(berufsbezeichnungFachrichtung)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static DemoLebenslaufAusbildungDtoBuilder<?, ?> builder() {
    return new DemoLebenslaufAusbildungDtoBuilderImpl();
  }

  private static final class DemoLebenslaufAusbildungDtoBuilderImpl extends DemoLebenslaufAusbildungDtoBuilder<DemoLebenslaufAusbildungDto, DemoLebenslaufAusbildungDtoBuilderImpl> {

    @Override
    protected DemoLebenslaufAusbildungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoLebenslaufAusbildungDto build() {
      return new DemoLebenslaufAusbildungDto(this);
    }
  }

  public static abstract class DemoLebenslaufAusbildungDtoBuilder<C extends DemoLebenslaufAusbildungDto, B extends DemoLebenslaufAusbildungDtoBuilder<C, B>>  {
    private String abschluss;
    private LocalDate von;
    private LocalDate bis;
    private ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz;
    private Boolean ausbildungAbgeschlossen;
    private String berufsbezeichnungFachrichtung;
    protected abstract B self();

    public abstract C build();

    public B abschluss(String abschluss) {
      this.abschluss = abschluss;
      return self();
    }
    public B von(LocalDate von) {
      this.von = von;
      return self();
    }
    public B bis(LocalDate bis) {
      this.bis = bis;
      return self();
    }
    public B wohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
      this.wohnsitz = wohnsitz;
      return self();
    }
    public B ausbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
      this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
      return self();
    }
    public B berufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
      this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
      return self();
    }
  }
}
