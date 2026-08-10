package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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



@JsonTypeName("GeschwisterUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GeschwisterUpdateDto  implements Serializable {
  private @Valid UUID entryId;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz;
  private @Valid ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private @Valid String nachname;
  private @Valid ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp;
  private @Valid UUID id;
  private @Valid BigDecimal wohnsitzAnteilMutter;
  private @Valid BigDecimal wohnsitzAnteilVater;
  private @Valid Boolean hidden;

  protected GeschwisterUpdateDto(GeschwisterUpdateDtoBuilder<?, ?> b) {
    this.entryId = b.entryId;
    this.vorname = b.vorname;
    this.geburtsdatum = b.geburtsdatum;
    this.wohnsitz = b.wohnsitz;
    this.ausbildungssituation = b.ausbildungssituation;
    this.nachname = b.nachname;
    this.geschwisterTyp = b.geschwisterTyp;
    this.id = b.id;
    this.wohnsitzAnteilMutter = b.wohnsitzAnteilMutter;
    this.wohnsitzAnteilVater = b.wohnsitzAnteilVater;
    this.hidden = b.hidden;
  }

  public GeschwisterUpdateDto() {
  }

  /**
   **/
  public GeschwisterUpdateDto entryId(UUID entryId) {
    this.entryId = entryId;
    return this;
  }

  
  @JsonProperty("entryId")
  @NotNull
  public UUID getEntryId() {
    return entryId;
  }

  @JsonProperty("entryId")
  public void setEntryId(UUID entryId) {
    this.entryId = entryId;
  }

  /**
   **/
  public GeschwisterUpdateDto vorname(String vorname) {
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
   * dd.MM.yyyy
   **/
  public GeschwisterUpdateDto geburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public GeschwisterUpdateDto wohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
    this.wohnsitz = wohnsitz;
    return this;
  }

  
  @JsonProperty("wohnsitz")
  @NotNull
  public ch.dvbern.stip.api.common.type.Wohnsitz getWohnsitz() {
    return wohnsitz;
  }

  @JsonProperty("wohnsitz")
  public void setWohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
    this.wohnsitz = wohnsitz;
  }

  /**
   **/
  public GeschwisterUpdateDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
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
  public GeschwisterUpdateDto nachname(String nachname) {
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
  public GeschwisterUpdateDto geschwisterTyp(ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp) {
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
  public GeschwisterUpdateDto id(UUID id) {
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
   * Required wenn Wohnsitz.MUTTER_VATER.
   **/
  public GeschwisterUpdateDto wohnsitzAnteilMutter(BigDecimal wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilMutter")
  public BigDecimal getWohnsitzAnteilMutter() {
    return wohnsitzAnteilMutter;
  }

  @JsonProperty("wohnsitzAnteilMutter")
  public void setWohnsitzAnteilMutter(BigDecimal wohnsitzAnteilMutter) {
    this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
  }

  /**
   * Required wenn Wohnsitz.MUTTER_VATER.
   **/
  public GeschwisterUpdateDto wohnsitzAnteilVater(BigDecimal wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilVater")
  public BigDecimal getWohnsitzAnteilVater() {
    return wohnsitzAnteilVater;
  }

  @JsonProperty("wohnsitzAnteilVater")
  public void setWohnsitzAnteilVater(BigDecimal wohnsitzAnteilVater) {
    this.wohnsitzAnteilVater = wohnsitzAnteilVater;
  }

  /**
   **/
  public GeschwisterUpdateDto hidden(Boolean hidden) {
    this.hidden = hidden;
    return this;
  }

  
  @JsonProperty("hidden")
  public Boolean getHidden() {
    return hidden;
  }

  @JsonProperty("hidden")
  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeschwisterUpdateDto geschwisterUpdate = (GeschwisterUpdateDto) o;
    return Objects.equals(this.entryId, geschwisterUpdate.entryId) &&
        Objects.equals(this.vorname, geschwisterUpdate.vorname) &&
        Objects.equals(this.geburtsdatum, geschwisterUpdate.geburtsdatum) &&
        Objects.equals(this.wohnsitz, geschwisterUpdate.wohnsitz) &&
        Objects.equals(this.ausbildungssituation, geschwisterUpdate.ausbildungssituation) &&
        Objects.equals(this.nachname, geschwisterUpdate.nachname) &&
        Objects.equals(this.geschwisterTyp, geschwisterUpdate.geschwisterTyp) &&
        Objects.equals(this.id, geschwisterUpdate.id) &&
        Objects.equals(this.wohnsitzAnteilMutter, geschwisterUpdate.wohnsitzAnteilMutter) &&
        Objects.equals(this.wohnsitzAnteilVater, geschwisterUpdate.wohnsitzAnteilVater) &&
        Objects.equals(this.hidden, geschwisterUpdate.hidden);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, vorname, geburtsdatum, wohnsitz, ausbildungssituation, nachname, geschwisterTyp, id, wohnsitzAnteilMutter, wohnsitzAnteilVater, hidden);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeschwisterUpdateDto {\n");
    
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    geschwisterTyp: ").append(toIndentedString(geschwisterTyp)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    wohnsitzAnteilMutter: ").append(toIndentedString(wohnsitzAnteilMutter)).append("\n");
    sb.append("    wohnsitzAnteilVater: ").append(toIndentedString(wohnsitzAnteilVater)).append("\n");
    sb.append("    hidden: ").append(toIndentedString(hidden)).append("\n");
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


  public static GeschwisterUpdateDtoBuilder<?, ?> builder() {
    return new GeschwisterUpdateDtoBuilderImpl();
  }

  private static final class GeschwisterUpdateDtoBuilderImpl extends GeschwisterUpdateDtoBuilder<GeschwisterUpdateDto, GeschwisterUpdateDtoBuilderImpl> {

    @Override
    protected GeschwisterUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GeschwisterUpdateDto build() {
      return new GeschwisterUpdateDto(this);
    }
  }

  public static abstract class GeschwisterUpdateDtoBuilder<C extends GeschwisterUpdateDto, B extends GeschwisterUpdateDtoBuilder<C, B>>  {
    private UUID entryId;
    private String vorname;
    private LocalDate geburtsdatum;
    private ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz;
    private ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
    private String nachname;
    private ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp;
    private UUID id;
    private BigDecimal wohnsitzAnteilMutter;
    private BigDecimal wohnsitzAnteilVater;
    private Boolean hidden;
    protected abstract B self();

    public abstract C build();

    public B entryId(UUID entryId) {
      this.entryId = entryId;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B geburtsdatum(LocalDate geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B wohnsitz(ch.dvbern.stip.api.common.type.Wohnsitz wohnsitz) {
      this.wohnsitz = wohnsitz;
      return self();
    }
    public B ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
      this.ausbildungssituation = ausbildungssituation;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B geschwisterTyp(ch.dvbern.stip.api.geschwister.type.GeschwisterTyp geschwisterTyp) {
      this.geschwisterTyp = geschwisterTyp;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B wohnsitzAnteilMutter(BigDecimal wohnsitzAnteilMutter) {
      this.wohnsitzAnteilMutter = wohnsitzAnteilMutter;
      return self();
    }
    public B wohnsitzAnteilVater(BigDecimal wohnsitzAnteilVater) {
      this.wohnsitzAnteilVater = wohnsitzAnteilVater;
      return self();
    }
    public B hidden(Boolean hidden) {
      this.hidden = hidden;
      return self();
    }
  }
}

