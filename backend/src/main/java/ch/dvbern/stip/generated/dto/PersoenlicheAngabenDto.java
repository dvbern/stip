package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
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



@JsonTypeName("PersoenlicheAngaben")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PersoenlicheAngabenDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.common.type.Anrede anrede;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid String email;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Sprache sprache;
  private @Valid AdresseDto adresse;

  protected PersoenlicheAngabenDto(PersoenlicheAngabenDtoBuilder<?, ?> b) {
    this.anrede = b.anrede;
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.geburtsdatum = b.geburtsdatum;
    this.email = b.email;
    this.sprache = b.sprache;
    this.adresse = b.adresse;
  }

  public PersoenlicheAngabenDto() {
  }

  /**
   **/
  public PersoenlicheAngabenDto anrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
    this.anrede = anrede;
    return this;
  }

  
  @JsonProperty("anrede")
  @NotNull
  public ch.dvbern.stip.api.common.type.Anrede getAnrede() {
    return anrede;
  }

  @JsonProperty("anrede")
  public void setAnrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
    this.anrede = anrede;
  }

  /**
   **/
  public PersoenlicheAngabenDto nachname(String nachname) {
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
  public PersoenlicheAngabenDto vorname(String vorname) {
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
  public PersoenlicheAngabenDto geburtsdatum(LocalDate geburtsdatum) {
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
  public PersoenlicheAngabenDto email(String email) {
    this.email = email;
    return this;
  }

  
  @JsonProperty("email")
  @NotNull
  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   **/
  public PersoenlicheAngabenDto sprache(ch.dvbern.stip.api.personinausbildung.type.Sprache sprache) {
    this.sprache = sprache;
    return this;
  }

  
  @JsonProperty("sprache")
  @NotNull
  public ch.dvbern.stip.api.personinausbildung.type.Sprache getSprache() {
    return sprache;
  }

  @JsonProperty("sprache")
  public void setSprache(ch.dvbern.stip.api.personinausbildung.type.Sprache sprache) {
    this.sprache = sprache;
  }

  /**
   **/
  public PersoenlicheAngabenDto adresse(AdresseDto adresse) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersoenlicheAngabenDto persoenlicheAngaben = (PersoenlicheAngabenDto) o;
    return Objects.equals(this.anrede, persoenlicheAngaben.anrede) &&
        Objects.equals(this.nachname, persoenlicheAngaben.nachname) &&
        Objects.equals(this.vorname, persoenlicheAngaben.vorname) &&
        Objects.equals(this.geburtsdatum, persoenlicheAngaben.geburtsdatum) &&
        Objects.equals(this.email, persoenlicheAngaben.email) &&
        Objects.equals(this.sprache, persoenlicheAngaben.sprache) &&
        Objects.equals(this.adresse, persoenlicheAngaben.adresse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anrede, nachname, vorname, geburtsdatum, email, sprache, adresse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlicheAngabenDto {\n");
    
    sb.append("    anrede: ").append(toIndentedString(anrede)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    sprache: ").append(toIndentedString(sprache)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
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


  public static PersoenlicheAngabenDtoBuilder<?, ?> builder() {
    return new PersoenlicheAngabenDtoBuilderImpl();
  }

  private static final class PersoenlicheAngabenDtoBuilderImpl extends PersoenlicheAngabenDtoBuilder<PersoenlicheAngabenDto, PersoenlicheAngabenDtoBuilderImpl> {

    @Override
    protected PersoenlicheAngabenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PersoenlicheAngabenDto build() {
      return new PersoenlicheAngabenDto(this);
    }
  }

  public static abstract class PersoenlicheAngabenDtoBuilder<C extends PersoenlicheAngabenDto, B extends PersoenlicheAngabenDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.common.type.Anrede anrede;
    private String nachname;
    private String vorname;
    private LocalDate geburtsdatum;
    private String email;
    private ch.dvbern.stip.api.personinausbildung.type.Sprache sprache;
    private AdresseDto adresse;
    protected abstract B self();

    public abstract C build();

    public B anrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
      this.anrede = anrede;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
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
    public B email(String email) {
      this.email = email;
      return self();
    }
    public B sprache(ch.dvbern.stip.api.personinausbildung.type.Sprache sprache) {
      this.sprache = sprache;
      return self();
    }
    public B adresse(AdresseDto adresse) {
      this.adresse = adresse;
      return self();
    }
  }
}

