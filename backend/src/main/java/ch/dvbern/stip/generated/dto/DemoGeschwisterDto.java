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



@JsonTypeName("DemoGeschwister")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoGeschwisterDto  implements Serializable {
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String geburtsdatum;
  private @Valid Integer alter;
  private @Valid ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei;
  private @Valid ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private @Valid ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp;
  private @Valid Integer wohnsitzAnteilVater;
  private @Valid Integer wohnsitzAnteilMutter;
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp elternteilPiaOfStiefHalbGeschwister;

  protected DemoGeschwisterDto(DemoGeschwisterDtoBuilder<?, ?> b) {
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.geburtsdatum = b.geburtsdatum;
    this.alter = b.alter;
    this.wohnsitzBei = b.wohnsitzBei;
    this.ausbildungssituation = b.ausbildungssituation;
    this.geschwisterTyp = b.geschwisterTyp;
    this.wohnsitzAnteilVater = b.wohnsitzAnteilVater;
    this.wohnsitzAnteilMutter = b.wohnsitzAnteilMutter;
    this.elternteilPiaOfStiefHalbGeschwister = b.elternteilPiaOfStiefHalbGeschwister;
  }

  public DemoGeschwisterDto() {
  }

  /**
   **/
  public DemoGeschwisterDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  @NotNull
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public DemoGeschwisterDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public DemoGeschwisterDto geburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
 @Pattern(regexp="^\\d{2}.\\d{2}$")  public String getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DemoGeschwisterDto alter(Integer alter) {
    this.alter = alter;
    return this;
  }

  
  @JsonProperty("alter")
  @NotNull
  public Integer getAlter() {
    return alter;
  }

  @JsonProperty("alter")
  public void setAlter(Integer alter) {
    this.alter = alter;
  }

  /**
   **/
  public DemoGeschwisterDto wohnsitzBei(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei) {
    this.wohnsitzBei = wohnsitzBei;
    return this;
  }

  
  @JsonProperty("wohnsitzBei")
  @NotNull
  public ch.dvbern.stip.api.common.type.Wohnsitz getWohnsitzBei() {
    return wohnsitzBei;
  }

  @JsonProperty("wohnsitzBei")
  public void setWohnsitzBei(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei) {
    this.wohnsitzBei = wohnsitzBei;
  }

  /**
   **/
  public DemoGeschwisterDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
    this.ausbildungssituation = ausbildungssituation;
    return this;
  }

  
  @JsonProperty("ausbildungssituation")
  @NotNull
  public ch.dvbern.stip.api.common.type.Ausbildungssituation getAusbildungssituation() {
    return ausbildungssituation;
  }

  @JsonProperty("ausbildungssituation")
  public void setAusbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
    this.ausbildungssituation = ausbildungssituation;
  }

  /**
   **/
  public DemoGeschwisterDto geschwisterTyp(ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp) {
    this.geschwisterTyp = geschwisterTyp;
    return this;
  }

  
  @JsonProperty("geschwisterTyp")
  @NotNull
  public ch.dvbern.stip.api.geschwister.type.GeschwisterTyp getGeschwisterTyp() {
    return geschwisterTyp;
  }

  @JsonProperty("geschwisterTyp")
  public void setGeschwisterTyp(ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp) {
    this.geschwisterTyp = geschwisterTyp;
  }

  /**
   **/
  public DemoGeschwisterDto wohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilVater")
  public Integer getWohnsitzAnteilVater() {
    return wohnsitzAnteilVater;
  }

  @JsonProperty("wohnsitzAnteilVater")
  public void setWohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
  }

  /**
   **/
  public DemoGeschwisterDto wohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilMutter")
  public Integer getWohnsitzAnteilMutter() {
    return wohnsitzAnteilMutter;
  }

  @JsonProperty("wohnsitzAnteilMutter")
  public void setWohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
  }

  /**
   **/
  public DemoGeschwisterDto elternteilPiaOfStiefHalbGeschwister(ch.dvbern.stip.api.eltern.type.ElternTyp elternteilPiaOfStiefHalbGeschwister) {
    this.elternteilPiaOfStiefHalbGeschwister = elternteilPiaOfStiefHalbGeschwister;
    return this;
  }

  
  @JsonProperty("elternteilPiaOfStiefHalbGeschwister")
  public ch.dvbern.stip.api.eltern.type.ElternTyp getElternteilPiaOfStiefHalbGeschwister() {
    return elternteilPiaOfStiefHalbGeschwister;
  }

  @JsonProperty("elternteilPiaOfStiefHalbGeschwister")
  public void setElternteilPiaOfStiefHalbGeschwister(ch.dvbern.stip.api.eltern.type.ElternTyp elternteilPiaOfStiefHalbGeschwister) {
    this.elternteilPiaOfStiefHalbGeschwister = elternteilPiaOfStiefHalbGeschwister;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoGeschwisterDto demoGeschwister = (DemoGeschwisterDto) o;
    return Objects.equals(this.nachname, demoGeschwister.nachname) &&
        Objects.equals(this.vorname, demoGeschwister.vorname) &&
        Objects.equals(this.geburtsdatum, demoGeschwister.geburtsdatum) &&
        Objects.equals(this.alter, demoGeschwister.alter) &&
        Objects.equals(this.wohnsitzBei, demoGeschwister.wohnsitzBei) &&
        Objects.equals(this.ausbildungssituation, demoGeschwister.ausbildungssituation) &&
        Objects.equals(this.geschwisterTyp, demoGeschwister.geschwisterTyp) &&
        Objects.equals(this.wohnsitzAnteilVater, demoGeschwister.wohnsitzAnteilVater) &&
        Objects.equals(this.wohnsitzAnteilMutter, demoGeschwister.wohnsitzAnteilMutter) &&
        Objects.equals(this.elternteilPiaOfStiefHalbGeschwister, demoGeschwister.elternteilPiaOfStiefHalbGeschwister);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nachname, vorname, geburtsdatum, alter, wohnsitzBei, ausbildungssituation, geschwisterTyp, wohnsitzAnteilVater, wohnsitzAnteilMutter, elternteilPiaOfStiefHalbGeschwister);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoGeschwisterDto {\n");
    
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    alter: ").append(toIndentedString(alter)).append("\n");
    sb.append("    wohnsitzBei: ").append(toIndentedString(wohnsitzBei)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    geschwisterTyp: ").append(toIndentedString(geschwisterTyp)).append("\n");
    sb.append("    wohnsitzAnteilVater: ").append(toIndentedString(wohnsitzAnteilVater)).append("\n");
    sb.append("    wohnsitzAnteilMutter: ").append(toIndentedString(wohnsitzAnteilMutter)).append("\n");
    sb.append("    elternteilPiaOfStiefHalbGeschwister: ").append(toIndentedString(elternteilPiaOfStiefHalbGeschwister)).append("\n");
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


  public static DemoGeschwisterDtoBuilder<?, ?> builder() {
    return new DemoGeschwisterDtoBuilderImpl();
  }

  private static final class DemoGeschwisterDtoBuilderImpl extends DemoGeschwisterDtoBuilder<DemoGeschwisterDto, DemoGeschwisterDtoBuilderImpl> {

    @Override
    protected DemoGeschwisterDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoGeschwisterDto build() {
      return new DemoGeschwisterDto(this);
    }
  }

  public static abstract class DemoGeschwisterDtoBuilder<C extends DemoGeschwisterDto, B extends DemoGeschwisterDtoBuilder<C, B>>  {
    private String nachname;
    private String vorname;
    private String geburtsdatum;
    private Integer alter;
    private ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei;
    private ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
    private ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp;
    private Integer wohnsitzAnteilVater;
    private Integer wohnsitzAnteilMutter;
    private ch.dvbern.stip.api.eltern.type.ElternTyp elternteilPiaOfStiefHalbGeschwister;
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
    public B wohnsitzBei(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitzBei) {
      this.wohnsitzBei = wohnsitzBei;
      return self();
    }
    public B ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
      this.ausbildungssituation = ausbildungssituation;
      return self();
    }
    public B geschwisterTyp(ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp) {
      this.geschwisterTyp = geschwisterTyp;
      return self();
    }
    public B wohnsitzAnteilVater(Integer wohnsitzAnteilVater) {
      this.wohnsitzAnteilVater = wohnsitzAnteilVater;
      return self();
    }
    public B wohnsitzAnteilMutter(Integer wohnsitzAnteilMutter) {
      this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
      return self();
    }
    public B elternteilPiaOfStiefHalbGeschwister(ch.dvbern.stip.api.eltern.type.ElternTyp elternteilPiaOfStiefHalbGeschwister) {
      this.elternteilPiaOfStiefHalbGeschwister = elternteilPiaOfStiefHalbGeschwister;
      return self();
    }
  }
}

