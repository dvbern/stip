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



@JsonTypeName("DemoKind")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoKindDto  implements Serializable {
  private String nachname;
  private String vorname;
  private String geburtsdatum;
  private Integer alter;
  private Integer wohnsitzAnteilPia;
  private ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private Integer unterhaltsbeitraege;
  private Integer kinderUndAusbildungszulagen;
  private Integer renten;
  private Integer ergaenzungsleistungen;
  private Integer betreuungskosten;
  private Integer andereEinnahmen;

  protected DemoKindDto(DemoKindDtoBuilder<?, ?> b) {
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.geburtsdatum = b.geburtsdatum;
    this.alter = b.alter;
    this.wohnsitzAnteilPia = b.wohnsitzAnteilPia;
    this.ausbildungssituation = b.ausbildungssituation;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.kinderUndAusbildungszulagen = b.kinderUndAusbildungszulagen;
    this.renten = b.renten;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.betreuungskosten = b.betreuungskosten;
    this.andereEinnahmen = b.andereEinnahmen;
  }

  public DemoKindDto() {
  }

  /**
   **/
  public DemoKindDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty(required = true, value = "nachname")
  @NotNull public String getNachname() {
    return nachname;
  }

  @JsonProperty(required = true, value = "nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public DemoKindDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty(required = true, value = "vorname")
  @NotNull public String getVorname() {
    return vorname;
  }

  @JsonProperty(required = true, value = "vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public DemoKindDto geburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty(required = true, value = "geburtsdatum")
  @NotNull  @Pattern(regexp="^\\d{2}.\\d{2}$")public String getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty(required = true, value = "geburtsdatum")
  public void setGeburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DemoKindDto alter(Integer alter) {
    this.alter = alter;
    return this;
  }

  
  @JsonProperty(required = true, value = "alter")
  @NotNull public Integer getAlter() {
    return alter;
  }

  @JsonProperty(required = true, value = "alter")
  public void setAlter(Integer alter) {
    this.alter = alter;
  }

  /**
   **/
  public DemoKindDto wohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
    this.wohnsitzAnteilPia = wohnsitzAnteilPia;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnsitzAnteilPia")
  @NotNull public Integer getWohnsitzAnteilPia() {
    return wohnsitzAnteilPia;
  }

  @JsonProperty(required = true, value = "wohnsitzAnteilPia")
  public void setWohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
    this.wohnsitzAnteilPia = wohnsitzAnteilPia;
  }

  /**
   **/
  public DemoKindDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
    this.ausbildungssituation = ausbildungssituation;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungssituation")
  @NotNull public ch.dvbern.stip.api.common.type.Ausbildungssituation getAusbildungssituation() {
    return ausbildungssituation;
  }

  @JsonProperty(required = true, value = "ausbildungssituation")
  public void setAusbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
    this.ausbildungssituation = ausbildungssituation;
  }

  /**
   **/
  public DemoKindDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty(required = true, value = "unterhaltsbeitraege")
  @NotNull public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty(required = true, value = "unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public DemoKindDto kinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
    this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
    return this;
  }

  
  @JsonProperty(required = true, value = "kinderUndAusbildungszulagen")
  @NotNull public Integer getKinderUndAusbildungszulagen() {
    return kinderUndAusbildungszulagen;
  }

  @JsonProperty(required = true, value = "kinderUndAusbildungszulagen")
  public void setKinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
    this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
  }

  /**
   **/
  public DemoKindDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty(required = true, value = "renten")
  @NotNull public Integer getRenten() {
    return renten;
  }

  @JsonProperty(required = true, value = "renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public DemoKindDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty(required = true, value = "ergaenzungsleistungen")
  @NotNull public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty(required = true, value = "ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public DemoKindDto betreuungskosten(Integer betreuungskosten) {
    this.betreuungskosten = betreuungskosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "betreuungskosten")
  @NotNull public Integer getBetreuungskosten() {
    return betreuungskosten;
  }

  @JsonProperty(required = true, value = "betreuungskosten")
  public void setBetreuungskosten(Integer betreuungskosten) {
    this.betreuungskosten = betreuungskosten;
  }

  /**
   **/
  public DemoKindDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty(required = true, value = "andereEinnahmen")
  @NotNull public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty(required = true, value = "andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoKindDto demoKind = (DemoKindDto) o;
    return Objects.equals(this.nachname, demoKind.nachname) &&
        Objects.equals(this.vorname, demoKind.vorname) &&
        Objects.equals(this.geburtsdatum, demoKind.geburtsdatum) &&
        Objects.equals(this.alter, demoKind.alter) &&
        Objects.equals(this.wohnsitzAnteilPia, demoKind.wohnsitzAnteilPia) &&
        Objects.equals(this.ausbildungssituation, demoKind.ausbildungssituation) &&
        Objects.equals(this.unterhaltsbeitraege, demoKind.unterhaltsbeitraege) &&
        Objects.equals(this.kinderUndAusbildungszulagen, demoKind.kinderUndAusbildungszulagen) &&
        Objects.equals(this.renten, demoKind.renten) &&
        Objects.equals(this.ergaenzungsleistungen, demoKind.ergaenzungsleistungen) &&
        Objects.equals(this.betreuungskosten, demoKind.betreuungskosten) &&
        Objects.equals(this.andereEinnahmen, demoKind.andereEinnahmen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nachname, vorname, geburtsdatum, alter, wohnsitzAnteilPia, ausbildungssituation, unterhaltsbeitraege, kinderUndAusbildungszulagen, renten, ergaenzungsleistungen, betreuungskosten, andereEinnahmen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoKindDto {\n");
    
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    alter: ").append(toIndentedString(alter)).append("\n");
    sb.append("    wohnsitzAnteilPia: ").append(toIndentedString(wohnsitzAnteilPia)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    kinderUndAusbildungszulagen: ").append(toIndentedString(kinderUndAusbildungszulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    betreuungskosten: ").append(toIndentedString(betreuungskosten)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
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


  public static DemoKindDtoBuilder<?, ?> builder() {
    return new DemoKindDtoBuilderImpl();
  }

  private static final class DemoKindDtoBuilderImpl extends DemoKindDtoBuilder<DemoKindDto, DemoKindDtoBuilderImpl> {

    @Override
    protected DemoKindDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoKindDto build() {
      return new DemoKindDto(this);
    }
  }

  public static abstract class DemoKindDtoBuilder<C extends DemoKindDto, B extends DemoKindDtoBuilder<C, B>>  {
    private String nachname;
    private String vorname;
    private String geburtsdatum;
    private Integer alter;
    private Integer wohnsitzAnteilPia;
    private ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
    private Integer unterhaltsbeitraege;
    private Integer kinderUndAusbildungszulagen;
    private Integer renten;
    private Integer ergaenzungsleistungen;
    private Integer betreuungskosten;
    private Integer andereEinnahmen;
    protected abstract B self();

    public abstract C build();

    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B geburtsdatum(String geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B alter(Integer alter) {
      this.alter = alter;
      return self();
    }
    public B wohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
      this.wohnsitzAnteilPia = wohnsitzAnteilPia;
      return self();
    }
    public B ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
      this.ausbildungssituation = ausbildungssituation;
      return self();
    }
    public B unterhaltsbeitraege(Integer unterhaltsbeitraege) {
      this.unterhaltsbeitraege = unterhaltsbeitraege;
      return self();
    }
    public B kinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
      this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
      return self();
    }
    public B renten(Integer renten) {
      this.renten = renten;
      return self();
    }
    public B ergaenzungsleistungen(Integer ergaenzungsleistungen) {
      this.ergaenzungsleistungen = ergaenzungsleistungen;
      return self();
    }
    public B betreuungskosten(Integer betreuungskosten) {
      this.betreuungskosten = betreuungskosten;
      return self();
    }
    public B andereEinnahmen(Integer andereEinnahmen) {
      this.andereEinnahmen = andereEinnahmen;
      return self();
    }
  }
}
