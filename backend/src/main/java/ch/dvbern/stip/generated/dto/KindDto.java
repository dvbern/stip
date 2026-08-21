package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("Kind")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class KindDto  implements Serializable {
  private @Valid UUID entryId;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
  private @Valid Integer wohnsitzAnteilPia;
  private @Valid String nachname;
  private @Valid UUID id;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer kinderUndAusbildungszulagen;
  private @Valid Integer renten;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer betreuungskosten;
  private @Valid Integer andereEinnahmen;

  protected KindDto(KindDtoBuilder<?, ?> b) {
    this.entryId = b.entryId;
    this.vorname = b.vorname;
    this.geburtsdatum = b.geburtsdatum;
    this.ausbildungssituation = b.ausbildungssituation;
    this.wohnsitzAnteilPia = b.wohnsitzAnteilPia;
    this.nachname = b.nachname;
    this.id = b.id;
    this.unterhaltsbeitraege = b.unterhaltsbeitraege;
    this.kinderUndAusbildungszulagen = b.kinderUndAusbildungszulagen;
    this.renten = b.renten;
    this.ergaenzungsleistungen = b.ergaenzungsleistungen;
    this.betreuungskosten = b.betreuungskosten;
    this.andereEinnahmen = b.andereEinnahmen;
  }

  public KindDto() {
  }

  /**
   **/
  public KindDto entryId(UUID entryId) {
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
  public KindDto vorname(String vorname) {
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
  public KindDto geburtsdatum(LocalDate geburtsdatum) {
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
  public KindDto ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
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
  public KindDto wohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
    this.wohnsitzAnteilPia = wohnsitzAnteilPia;
    return this;
  }

  
  @JsonProperty("wohnsitzAnteilPia")
  @NotNull
  public Integer getWohnsitzAnteilPia() {
    return wohnsitzAnteilPia;
  }

  @JsonProperty("wohnsitzAnteilPia")
  public void setWohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
    this.wohnsitzAnteilPia = wohnsitzAnteilPia;
  }

  /**
   **/
  public KindDto nachname(String nachname) {
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
  public KindDto id(UUID id) {
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
  public KindDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public KindDto kinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
    this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
    return this;
  }

  
  @JsonProperty("kinderUndAusbildungszulagen")
  public Integer getKinderUndAusbildungszulagen() {
    return kinderUndAusbildungszulagen;
  }

  @JsonProperty("kinderUndAusbildungszulagen")
  public void setKinderUndAusbildungszulagen(Integer kinderUndAusbildungszulagen) {
    this.kinderUndAusbildungszulagen = kinderUndAusbildungszulagen;
  }

  /**
   **/
  public KindDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public KindDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public KindDto betreuungskosten(Integer betreuungskosten) {
    this.betreuungskosten = betreuungskosten;
    return this;
  }

  
  @JsonProperty("betreuungskosten")
  public Integer getBetreuungskosten() {
    return betreuungskosten;
  }

  @JsonProperty("betreuungskosten")
  public void setBetreuungskosten(Integer betreuungskosten) {
    this.betreuungskosten = betreuungskosten;
  }

  /**
   **/
  public KindDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
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
    KindDto kind = (KindDto) o;
    return Objects.equals(this.entryId, kind.entryId) &&
        Objects.equals(this.vorname, kind.vorname) &&
        Objects.equals(this.geburtsdatum, kind.geburtsdatum) &&
        Objects.equals(this.ausbildungssituation, kind.ausbildungssituation) &&
        Objects.equals(this.wohnsitzAnteilPia, kind.wohnsitzAnteilPia) &&
        Objects.equals(this.nachname, kind.nachname) &&
        Objects.equals(this.id, kind.id) &&
        Objects.equals(this.unterhaltsbeitraege, kind.unterhaltsbeitraege) &&
        Objects.equals(this.kinderUndAusbildungszulagen, kind.kinderUndAusbildungszulagen) &&
        Objects.equals(this.renten, kind.renten) &&
        Objects.equals(this.ergaenzungsleistungen, kind.ergaenzungsleistungen) &&
        Objects.equals(this.betreuungskosten, kind.betreuungskosten) &&
        Objects.equals(this.andereEinnahmen, kind.andereEinnahmen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, vorname, geburtsdatum, ausbildungssituation, wohnsitzAnteilPia, nachname, id, unterhaltsbeitraege, kinderUndAusbildungszulagen, renten, ergaenzungsleistungen, betreuungskosten, andereEinnahmen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KindDto {\n");
    
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    ausbildungssituation: ").append(toIndentedString(ausbildungssituation)).append("\n");
    sb.append("    wohnsitzAnteilPia: ").append(toIndentedString(wohnsitzAnteilPia)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static KindDtoBuilder<?, ?> builder() {
    return new KindDtoBuilderImpl();
  }

  private static final class KindDtoBuilderImpl extends KindDtoBuilder<KindDto, KindDtoBuilderImpl> {

    @Override
    protected KindDtoBuilderImpl self() {
      return this;
    }

    @Override
    public KindDto build() {
      return new KindDto(this);
    }
  }

  public static abstract class KindDtoBuilder<C extends KindDto, B extends KindDtoBuilder<C, B>>  {
    private UUID entryId;
    private String vorname;
    private LocalDate geburtsdatum;
    private ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation;
    private Integer wohnsitzAnteilPia;
    private String nachname;
    private UUID id;
    private Integer unterhaltsbeitraege;
    private Integer kinderUndAusbildungszulagen;
    private Integer renten;
    private Integer ergaenzungsleistungen;
    private Integer betreuungskosten;
    private Integer andereEinnahmen;
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
    public B ausbildungssituation(ch.dvbern.stip.api.common.type.Ausbildungssituation ausbildungssituation) {
      this.ausbildungssituation = ausbildungssituation;
      return self();
    }
    public B wohnsitzAnteilPia(Integer wohnsitzAnteilPia) {
      this.wohnsitzAnteilPia = wohnsitzAnteilPia;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
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

