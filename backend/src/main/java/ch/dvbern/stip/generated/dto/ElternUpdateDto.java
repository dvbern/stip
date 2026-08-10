package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
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



@JsonTypeName("ElternUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class ElternUpdateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid AdresseDto adresse;
  private @Valid Boolean identischerZivilrechtlicherWohnsitz;
  private @Valid String telefonnummer;
  private @Valid LocalDate geburtsdatum;
  private @Valid Boolean ausweisbFluechtling;
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp;
  private @Valid String nachname;
  private @Valid Boolean sozialhilfebeitraege;
  private @Valid String identischerZivilrechtlicherWohnsitzOrt;
  private @Valid String identischerZivilrechtlicherWohnsitzPLZ;
  private @Valid String sozialversicherungsnummer;
  private @Valid UUID id;
  private @Valid Integer wohnkosten;
  private @Valid Boolean wiederverheiratet;

  protected ElternUpdateDto(ElternUpdateDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.adresse = b.adresse;
    this.identischerZivilrechtlicherWohnsitz = b.identischerZivilrechtlicherWohnsitz;
    this.telefonnummer = b.telefonnummer;
    this.geburtsdatum = b.geburtsdatum;
    this.ausweisbFluechtling = b.ausweisbFluechtling;
    this.elternTyp = b.elternTyp;
    this.nachname = b.nachname;
    this.sozialhilfebeitraege = b.sozialhilfebeitraege;
    this.identischerZivilrechtlicherWohnsitzOrt = b.identischerZivilrechtlicherWohnsitzOrt;
    this.identischerZivilrechtlicherWohnsitzPLZ = b.identischerZivilrechtlicherWohnsitzPLZ;
    this.sozialversicherungsnummer = b.sozialversicherungsnummer;
    this.id = b.id;
    this.wohnkosten = b.wohnkosten;
    this.wiederverheiratet = b.wiederverheiratet;
  }

  public ElternUpdateDto() {
  }

  /**
   **/
  public ElternUpdateDto vorname(String vorname) {
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
  public ElternUpdateDto adresse(AdresseDto adresse) {
    this.adresse = adresse;
    return this;
  }

  
  @JsonProperty("adresse")
  @NotNull
  public AdresseDto getAdresse() {
    return adresse;
  }

  @JsonProperty("adresse")
  public void setAdresse(AdresseDto adresse) {
    this.adresse = adresse;
  }

  /**
   **/
  public ElternUpdateDto identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
    this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
    return this;
  }

  
  @JsonProperty("identischerZivilrechtlicherWohnsitz")
  @NotNull
  public Boolean getIdentischerZivilrechtlicherWohnsitz() {
    return identischerZivilrechtlicherWohnsitz;
  }

  @JsonProperty("identischerZivilrechtlicherWohnsitz")
  public void setIdentischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
    this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
  }

  /**
   **/
  public ElternUpdateDto telefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
    return this;
  }

  
  @JsonProperty("telefonnummer")
  @NotNull
  public String getTelefonnummer() {
    return telefonnummer;
  }

  @JsonProperty("telefonnummer")
  public void setTelefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
  }

  /**
   * dd.MM.yyyy
   **/
  public ElternUpdateDto geburtsdatum(LocalDate geburtsdatum) {
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
  public ElternUpdateDto ausweisbFluechtling(Boolean ausweisbFluechtling) {
    this.ausweisbFluechtling = ausweisbFluechtling;
    return this;
  }

  
  @JsonProperty("ausweisbFluechtling")
  @NotNull
  public Boolean getAusweisbFluechtling() {
    return ausweisbFluechtling;
  }

  @JsonProperty("ausweisbFluechtling")
  public void setAusweisbFluechtling(Boolean ausweisbFluechtling) {
    this.ausweisbFluechtling = ausweisbFluechtling;
  }

  /**
   **/
  public ElternUpdateDto elternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
    this.elternTyp = elternTyp;
    return this;
  }

  
  @JsonProperty("elternTyp")
  @NotNull
  public ch.dvbern.stip.api.eltern.type.ElternTyp getElternTyp() {
    return elternTyp;
  }

  @JsonProperty("elternTyp")
  public void setElternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
    this.elternTyp = elternTyp;
  }

  /**
   **/
  public ElternUpdateDto nachname(String nachname) {
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
  public ElternUpdateDto sozialhilfebeitraege(Boolean sozialhilfebeitraege) {
    this.sozialhilfebeitraege = sozialhilfebeitraege;
    return this;
  }

  
  @JsonProperty("sozialhilfebeitraege")
  @NotNull
  public Boolean getSozialhilfebeitraege() {
    return sozialhilfebeitraege;
  }

  @JsonProperty("sozialhilfebeitraege")
  public void setSozialhilfebeitraege(Boolean sozialhilfebeitraege) {
    this.sozialhilfebeitraege = sozialhilfebeitraege;
  }

  /**
   * Required wenn identischerZivilrechtlicherWohnsitz &#x3D; false
   **/
  public ElternUpdateDto identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
    this.identischerZivilrechtlicherWohnsitzOrt = identischerZivilrechtlicherWohnsitzOrt;
    return this;
  }

  
  @JsonProperty("identischerZivilrechtlicherWohnsitzOrt")
  public String getIdentischerZivilrechtlicherWohnsitzOrt() {
    return identischerZivilrechtlicherWohnsitzOrt;
  }

  @JsonProperty("identischerZivilrechtlicherWohnsitzOrt")
  public void setIdentischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
    this.identischerZivilrechtlicherWohnsitzOrt = identischerZivilrechtlicherWohnsitzOrt;
  }

  /**
   * Required wenn identischerZivilrechtlicherWohnsitz &#x3D; false
   **/
  public ElternUpdateDto identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
    this.identischerZivilrechtlicherWohnsitzPLZ = identischerZivilrechtlicherWohnsitzPLZ;
    return this;
  }

  
  @JsonProperty("identischerZivilrechtlicherWohnsitzPLZ")
  public String getIdentischerZivilrechtlicherWohnsitzPLZ() {
    return identischerZivilrechtlicherWohnsitzPLZ;
  }

  @JsonProperty("identischerZivilrechtlicherWohnsitzPLZ")
  public void setIdentischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
    this.identischerZivilrechtlicherWohnsitzPLZ = identischerZivilrechtlicherWohnsitzPLZ;
  }

  /**
   **/
  public ElternUpdateDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }

  
  @JsonProperty("sozialversicherungsnummer")
  public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty("sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   **/
  public ElternUpdateDto id(UUID id) {
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
   * Die Wohnkosten vom Elternteil, werden gespiegelt auf den anderen falls Sie zusammen wohnen
   **/
  public ElternUpdateDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   **/
  public ElternUpdateDto wiederverheiratet(Boolean wiederverheiratet) {
    this.wiederverheiratet = wiederverheiratet;
    return this;
  }

  
  @JsonProperty("wiederverheiratet")
  public Boolean getWiederverheiratet() {
    return wiederverheiratet;
  }

  @JsonProperty("wiederverheiratet")
  public void setWiederverheiratet(Boolean wiederverheiratet) {
    this.wiederverheiratet = wiederverheiratet;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElternUpdateDto elternUpdate = (ElternUpdateDto) o;
    return Objects.equals(this.vorname, elternUpdate.vorname) &&
        Objects.equals(this.adresse, elternUpdate.adresse) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitz, elternUpdate.identischerZivilrechtlicherWohnsitz) &&
        Objects.equals(this.telefonnummer, elternUpdate.telefonnummer) &&
        Objects.equals(this.geburtsdatum, elternUpdate.geburtsdatum) &&
        Objects.equals(this.ausweisbFluechtling, elternUpdate.ausweisbFluechtling) &&
        Objects.equals(this.elternTyp, elternUpdate.elternTyp) &&
        Objects.equals(this.nachname, elternUpdate.nachname) &&
        Objects.equals(this.sozialhilfebeitraege, elternUpdate.sozialhilfebeitraege) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzOrt, elternUpdate.identischerZivilrechtlicherWohnsitzOrt) &&
        Objects.equals(this.identischerZivilrechtlicherWohnsitzPLZ, elternUpdate.identischerZivilrechtlicherWohnsitzPLZ) &&
        Objects.equals(this.sozialversicherungsnummer, elternUpdate.sozialversicherungsnummer) &&
        Objects.equals(this.id, elternUpdate.id) &&
        Objects.equals(this.wohnkosten, elternUpdate.wohnkosten) &&
        Objects.equals(this.wiederverheiratet, elternUpdate.wiederverheiratet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, adresse, identischerZivilrechtlicherWohnsitz, telefonnummer, geburtsdatum, ausweisbFluechtling, elternTyp, nachname, sozialhilfebeitraege, identischerZivilrechtlicherWohnsitzOrt, identischerZivilrechtlicherWohnsitzPLZ, sozialversicherungsnummer, id, wohnkosten, wiederverheiratet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElternUpdateDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitz: ").append(toIndentedString(identischerZivilrechtlicherWohnsitz)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    ausweisbFluechtling: ").append(toIndentedString(ausweisbFluechtling)).append("\n");
    sb.append("    elternTyp: ").append(toIndentedString(elternTyp)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    sozialhilfebeitraege: ").append(toIndentedString(sozialhilfebeitraege)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzOrt: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzOrt)).append("\n");
    sb.append("    identischerZivilrechtlicherWohnsitzPLZ: ").append(toIndentedString(identischerZivilrechtlicherWohnsitzPLZ)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    wiederverheiratet: ").append(toIndentedString(wiederverheiratet)).append("\n");
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


  public static ElternUpdateDtoBuilder<?, ?> builder() {
    return new ElternUpdateDtoBuilderImpl();
  }

  private static final class ElternUpdateDtoBuilderImpl extends ElternUpdateDtoBuilder<ElternUpdateDto, ElternUpdateDtoBuilderImpl> {

    @Override
    protected ElternUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public ElternUpdateDto build() {
      return new ElternUpdateDto(this);
    }
  }

  public static abstract class ElternUpdateDtoBuilder<C extends ElternUpdateDto, B extends ElternUpdateDtoBuilder<C, B>>  {
    private String vorname;
    private AdresseDto adresse;
    private Boolean identischerZivilrechtlicherWohnsitz;
    private String telefonnummer;
    private LocalDate geburtsdatum;
    private Boolean ausweisbFluechtling;
    private ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp;
    private String nachname;
    private Boolean sozialhilfebeitraege;
    private String identischerZivilrechtlicherWohnsitzOrt;
    private String identischerZivilrechtlicherWohnsitzPLZ;
    private String sozialversicherungsnummer;
    private UUID id;
    private Integer wohnkosten;
    private Boolean wiederverheiratet;
    protected abstract B self();

    public abstract C build();

    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B adresse(AdresseDto adresse) {
      this.adresse = adresse;
      return self();
    }
    public B identischerZivilrechtlicherWohnsitz(Boolean identischerZivilrechtlicherWohnsitz) {
      this.identischerZivilrechtlicherWohnsitz = identischerZivilrechtlicherWohnsitz;
      return self();
    }
    public B telefonnummer(String telefonnummer) {
      this.telefonnummer = telefonnummer;
      return self();
    }
    public B geburtsdatum(LocalDate geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B ausweisbFluechtling(Boolean ausweisbFluechtling) {
      this.ausweisbFluechtling = ausweisbFluechtling;
      return self();
    }
    public B elternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
      this.elternTyp = elternTyp;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B sozialhilfebeitraege(Boolean sozialhilfebeitraege) {
      this.sozialhilfebeitraege = sozialhilfebeitraege;
      return self();
    }
    public B identischerZivilrechtlicherWohnsitzOrt(String identischerZivilrechtlicherWohnsitzOrt) {
      this.identischerZivilrechtlicherWohnsitzOrt = identischerZivilrechtlicherWohnsitzOrt;
      return self();
    }
    public B identischerZivilrechtlicherWohnsitzPLZ(String identischerZivilrechtlicherWohnsitzPLZ) {
      this.identischerZivilrechtlicherWohnsitzPLZ = identischerZivilrechtlicherWohnsitzPLZ;
      return self();
    }
    public B sozialversicherungsnummer(String sozialversicherungsnummer) {
      this.sozialversicherungsnummer = sozialversicherungsnummer;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B wohnkosten(Integer wohnkosten) {
      this.wohnkosten = wohnkosten;
      return self();
    }
    public B wiederverheiratet(Boolean wiederverheiratet) {
      this.wiederverheiratet = wiederverheiratet;
      return self();
    }
  }
}

