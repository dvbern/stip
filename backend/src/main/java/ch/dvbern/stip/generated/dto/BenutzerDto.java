package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Benutzer")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BenutzerDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid UUID id;
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;
  private @Valid Boolean nutzungsbedingungenAkzeptiert;
  private @Valid SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten;

  protected BenutzerDto(BenutzerDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.id = b.id;
    this.benutzereinstellungen = b.benutzereinstellungen;
    this.nutzungsbedingungenAkzeptiert = b.nutzungsbedingungenAkzeptiert;
    this.sachbearbeiterZuordnungStammdaten = b.sachbearbeiterZuordnungStammdaten;
  }

  public BenutzerDto() {
  }

  /**
   **/
  public BenutzerDto vorname(String vorname) {
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
  public BenutzerDto nachname(String nachname) {
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
  public BenutzerDto id(UUID id) {
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
  public BenutzerDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
    this.benutzereinstellungen = benutzereinstellungen;
    return this;
  }

  
  @JsonProperty("benutzereinstellungen")
  public BenutzereinstellungenUpdateDto getBenutzereinstellungen() {
    return benutzereinstellungen;
  }

  @JsonProperty("benutzereinstellungen")
  public void setBenutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
    this.benutzereinstellungen = benutzereinstellungen;
  }

  /**
   **/
  public BenutzerDto nutzungsbedingungenAkzeptiert(Boolean nutzungsbedingungenAkzeptiert) {
    this.nutzungsbedingungenAkzeptiert = nutzungsbedingungenAkzeptiert;
    return this;
  }

  
  @JsonProperty("nutzungsbedingungenAkzeptiert")
  public Boolean getNutzungsbedingungenAkzeptiert() {
    return nutzungsbedingungenAkzeptiert;
  }

  @JsonProperty("nutzungsbedingungenAkzeptiert")
  public void setNutzungsbedingungenAkzeptiert(Boolean nutzungsbedingungenAkzeptiert) {
    this.nutzungsbedingungenAkzeptiert = nutzungsbedingungenAkzeptiert;
  }

  /**
   **/
  public BenutzerDto sachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
    this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
    return this;
  }

  
  @JsonProperty("sachbearbeiterZuordnungStammdaten")
  public SachbearbeiterZuordnungStammdatenDto getSachbearbeiterZuordnungStammdaten() {
    return sachbearbeiterZuordnungStammdaten;
  }

  @JsonProperty("sachbearbeiterZuordnungStammdaten")
  public void setSachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
    this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BenutzerDto benutzer = (BenutzerDto) o;
    return Objects.equals(this.vorname, benutzer.vorname) &&
        Objects.equals(this.nachname, benutzer.nachname) &&
        Objects.equals(this.id, benutzer.id) &&
        Objects.equals(this.benutzereinstellungen, benutzer.benutzereinstellungen) &&
        Objects.equals(this.nutzungsbedingungenAkzeptiert, benutzer.nutzungsbedingungenAkzeptiert) &&
        Objects.equals(this.sachbearbeiterZuordnungStammdaten, benutzer.sachbearbeiterZuordnungStammdaten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, id, benutzereinstellungen, nutzungsbedingungenAkzeptiert, sachbearbeiterZuordnungStammdaten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzerDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    benutzereinstellungen: ").append(toIndentedString(benutzereinstellungen)).append("\n");
    sb.append("    nutzungsbedingungenAkzeptiert: ").append(toIndentedString(nutzungsbedingungenAkzeptiert)).append("\n");
    sb.append("    sachbearbeiterZuordnungStammdaten: ").append(toIndentedString(sachbearbeiterZuordnungStammdaten)).append("\n");
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


  public static BenutzerDtoBuilder<?, ?> builder() {
    return new BenutzerDtoBuilderImpl();
  }

  private static final class BenutzerDtoBuilderImpl extends BenutzerDtoBuilder<BenutzerDto, BenutzerDtoBuilderImpl> {

    @Override
    protected BenutzerDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BenutzerDto build() {
      return new BenutzerDto(this);
    }
  }

  public static abstract class BenutzerDtoBuilder<C extends BenutzerDto, B extends BenutzerDtoBuilder<C, B>>  {
    private String vorname;
    private String nachname;
    private UUID id;
    private BenutzereinstellungenUpdateDto benutzereinstellungen;
    private Boolean nutzungsbedingungenAkzeptiert;
    private SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten;
    protected abstract B self();

    public abstract C build();

    public B vorname(String vorname) {
      this.vorname = vorname;
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
    public B benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
      this.benutzereinstellungen = benutzereinstellungen;
      return self();
    }
    public B nutzungsbedingungenAkzeptiert(Boolean nutzungsbedingungenAkzeptiert) {
      this.nutzungsbedingungenAkzeptiert = nutzungsbedingungenAkzeptiert;
      return self();
    }
    public B sachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
      this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
      return self();
    }
  }
}

