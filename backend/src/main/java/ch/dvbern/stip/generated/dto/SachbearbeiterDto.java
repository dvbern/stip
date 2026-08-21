package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDto;
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



@JsonTypeName("Sachbearbeiter")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SachbearbeiterDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String telefonnummer;
  private @Valid String email;
  private @Valid String funktionDe;
  private @Valid String funktionFr;
  private @Valid List<String> sachbearbeiterRollen = new ArrayList<>();
  private @Valid UUID id;
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;
  private @Valid Boolean nutzungsbedingungenAkzeptiert;

  protected SachbearbeiterDto(SachbearbeiterDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.telefonnummer = b.telefonnummer;
    this.email = b.email;
    this.funktionDe = b.funktionDe;
    this.funktionFr = b.funktionFr;
    this.sachbearbeiterRollen = b.sachbearbeiterRollen;
    this.id = b.id;
    this.benutzereinstellungen = b.benutzereinstellungen;
    this.nutzungsbedingungenAkzeptiert = b.nutzungsbedingungenAkzeptiert;
  }

  public SachbearbeiterDto() {
  }

  /**
   **/
  public SachbearbeiterDto vorname(String vorname) {
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
  public SachbearbeiterDto nachname(String nachname) {
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
  public SachbearbeiterDto telefonnummer(String telefonnummer) {
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
   **/
  public SachbearbeiterDto email(String email) {
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
  public SachbearbeiterDto funktionDe(String funktionDe) {
    this.funktionDe = funktionDe;
    return this;
  }

  
  @JsonProperty("funktionDe")
  @NotNull
  public String getFunktionDe() {
    return funktionDe;
  }

  @JsonProperty("funktionDe")
  public void setFunktionDe(String funktionDe) {
    this.funktionDe = funktionDe;
  }

  /**
   **/
  public SachbearbeiterDto funktionFr(String funktionFr) {
    this.funktionFr = funktionFr;
    return this;
  }

  
  @JsonProperty("funktionFr")
  @NotNull
  public String getFunktionFr() {
    return funktionFr;
  }

  @JsonProperty("funktionFr")
  public void setFunktionFr(String funktionFr) {
    this.funktionFr = funktionFr;
  }

  /**
   **/
  public SachbearbeiterDto sachbearbeiterRollen(List<String> sachbearbeiterRollen) {
    this.sachbearbeiterRollen = sachbearbeiterRollen;
    return this;
  }

  
  @JsonProperty("sachbearbeiterRollen")
  @NotNull
  public List<String> getSachbearbeiterRollen() {
    return sachbearbeiterRollen;
  }

  @JsonProperty("sachbearbeiterRollen")
  public void setSachbearbeiterRollen(List<String> sachbearbeiterRollen) {
    this.sachbearbeiterRollen = sachbearbeiterRollen;
  }

  public SachbearbeiterDto addSachbearbeiterRollenItem(String sachbearbeiterRollenItem) {
    if (this.sachbearbeiterRollen == null) {
      this.sachbearbeiterRollen = new ArrayList<>();
    }

    this.sachbearbeiterRollen.add(sachbearbeiterRollenItem);
    return this;
  }

  public SachbearbeiterDto removeSachbearbeiterRollenItem(String sachbearbeiterRollenItem) {
    if (sachbearbeiterRollenItem != null && this.sachbearbeiterRollen != null) {
      this.sachbearbeiterRollen.remove(sachbearbeiterRollenItem);
    }

    return this;
  }
  /**
   **/
  public SachbearbeiterDto id(UUID id) {
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
  public SachbearbeiterDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
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
  public SachbearbeiterDto nutzungsbedingungenAkzeptiert(Boolean nutzungsbedingungenAkzeptiert) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterDto sachbearbeiter = (SachbearbeiterDto) o;
    return Objects.equals(this.vorname, sachbearbeiter.vorname) &&
        Objects.equals(this.nachname, sachbearbeiter.nachname) &&
        Objects.equals(this.telefonnummer, sachbearbeiter.telefonnummer) &&
        Objects.equals(this.email, sachbearbeiter.email) &&
        Objects.equals(this.funktionDe, sachbearbeiter.funktionDe) &&
        Objects.equals(this.funktionFr, sachbearbeiter.funktionFr) &&
        Objects.equals(this.sachbearbeiterRollen, sachbearbeiter.sachbearbeiterRollen) &&
        Objects.equals(this.id, sachbearbeiter.id) &&
        Objects.equals(this.benutzereinstellungen, sachbearbeiter.benutzereinstellungen) &&
        Objects.equals(this.nutzungsbedingungenAkzeptiert, sachbearbeiter.nutzungsbedingungenAkzeptiert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, telefonnummer, email, funktionDe, funktionFr, sachbearbeiterRollen, id, benutzereinstellungen, nutzungsbedingungenAkzeptiert);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    funktionDe: ").append(toIndentedString(funktionDe)).append("\n");
    sb.append("    funktionFr: ").append(toIndentedString(funktionFr)).append("\n");
    sb.append("    sachbearbeiterRollen: ").append(toIndentedString(sachbearbeiterRollen)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    benutzereinstellungen: ").append(toIndentedString(benutzereinstellungen)).append("\n");
    sb.append("    nutzungsbedingungenAkzeptiert: ").append(toIndentedString(nutzungsbedingungenAkzeptiert)).append("\n");
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


  public static SachbearbeiterDtoBuilder<?, ?> builder() {
    return new SachbearbeiterDtoBuilderImpl();
  }

  private static final class SachbearbeiterDtoBuilderImpl extends SachbearbeiterDtoBuilder<SachbearbeiterDto, SachbearbeiterDtoBuilderImpl> {

    @Override
    protected SachbearbeiterDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SachbearbeiterDto build() {
      return new SachbearbeiterDto(this);
    }
  }

  public static abstract class SachbearbeiterDtoBuilder<C extends SachbearbeiterDto, B extends SachbearbeiterDtoBuilder<C, B>>  {
    private String vorname;
    private String nachname;
    private String telefonnummer;
    private String email;
    private String funktionDe;
    private String funktionFr;
    private List<String> sachbearbeiterRollen = new ArrayList<>();
    private UUID id;
    private BenutzereinstellungenUpdateDto benutzereinstellungen;
    private Boolean nutzungsbedingungenAkzeptiert;
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
    public B telefonnummer(String telefonnummer) {
      this.telefonnummer = telefonnummer;
      return self();
    }
    public B email(String email) {
      this.email = email;
      return self();
    }
    public B funktionDe(String funktionDe) {
      this.funktionDe = funktionDe;
      return self();
    }
    public B funktionFr(String funktionFr) {
      this.funktionFr = funktionFr;
      return self();
    }
    public B sachbearbeiterRollen(List<String> sachbearbeiterRollen) {
      this.sachbearbeiterRollen = sachbearbeiterRollen;
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
  }
}

