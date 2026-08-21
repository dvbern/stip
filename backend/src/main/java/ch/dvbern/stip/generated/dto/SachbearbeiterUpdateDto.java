package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SachbearbeiterUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SachbearbeiterUpdateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String telefonnummer;
  private @Valid String email;
  private @Valid String funktionDe;
  private @Valid String funktionFr;
  private @Valid List<String> sachbearbeiterRollen = new ArrayList<>();
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;
  private @Valid Boolean nutzungsbedingungenAkzeptiert;

  protected SachbearbeiterUpdateDto(SachbearbeiterUpdateDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.telefonnummer = b.telefonnummer;
    this.email = b.email;
    this.funktionDe = b.funktionDe;
    this.funktionFr = b.funktionFr;
    this.sachbearbeiterRollen = b.sachbearbeiterRollen;
    this.benutzereinstellungen = b.benutzereinstellungen;
    this.nutzungsbedingungenAkzeptiert = b.nutzungsbedingungenAkzeptiert;
  }

  public SachbearbeiterUpdateDto() {
  }

  /**
   **/
  public SachbearbeiterUpdateDto vorname(String vorname) {
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
  public SachbearbeiterUpdateDto nachname(String nachname) {
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
  public SachbearbeiterUpdateDto telefonnummer(String telefonnummer) {
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
  public SachbearbeiterUpdateDto email(String email) {
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
  public SachbearbeiterUpdateDto funktionDe(String funktionDe) {
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
  public SachbearbeiterUpdateDto funktionFr(String funktionFr) {
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
  public SachbearbeiterUpdateDto sachbearbeiterRollen(List<String> sachbearbeiterRollen) {
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

  public SachbearbeiterUpdateDto addSachbearbeiterRollenItem(String sachbearbeiterRollenItem) {
    if (this.sachbearbeiterRollen == null) {
      this.sachbearbeiterRollen = new ArrayList<>();
    }

    this.sachbearbeiterRollen.add(sachbearbeiterRollenItem);
    return this;
  }

  public SachbearbeiterUpdateDto removeSachbearbeiterRollenItem(String sachbearbeiterRollenItem) {
    if (sachbearbeiterRollenItem != null && this.sachbearbeiterRollen != null) {
      this.sachbearbeiterRollen.remove(sachbearbeiterRollenItem);
    }

    return this;
  }
  /**
   **/
  public SachbearbeiterUpdateDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
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
  public SachbearbeiterUpdateDto nutzungsbedingungenAkzeptiert(Boolean nutzungsbedingungenAkzeptiert) {
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
    SachbearbeiterUpdateDto sachbearbeiterUpdate = (SachbearbeiterUpdateDto) o;
    return Objects.equals(this.vorname, sachbearbeiterUpdate.vorname) &&
        Objects.equals(this.nachname, sachbearbeiterUpdate.nachname) &&
        Objects.equals(this.telefonnummer, sachbearbeiterUpdate.telefonnummer) &&
        Objects.equals(this.email, sachbearbeiterUpdate.email) &&
        Objects.equals(this.funktionDe, sachbearbeiterUpdate.funktionDe) &&
        Objects.equals(this.funktionFr, sachbearbeiterUpdate.funktionFr) &&
        Objects.equals(this.sachbearbeiterRollen, sachbearbeiterUpdate.sachbearbeiterRollen) &&
        Objects.equals(this.benutzereinstellungen, sachbearbeiterUpdate.benutzereinstellungen) &&
        Objects.equals(this.nutzungsbedingungenAkzeptiert, sachbearbeiterUpdate.nutzungsbedingungenAkzeptiert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, telefonnummer, email, funktionDe, funktionFr, sachbearbeiterRollen, benutzereinstellungen, nutzungsbedingungenAkzeptiert);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterUpdateDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    funktionDe: ").append(toIndentedString(funktionDe)).append("\n");
    sb.append("    funktionFr: ").append(toIndentedString(funktionFr)).append("\n");
    sb.append("    sachbearbeiterRollen: ").append(toIndentedString(sachbearbeiterRollen)).append("\n");
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


  public static SachbearbeiterUpdateDtoBuilder<?, ?> builder() {
    return new SachbearbeiterUpdateDtoBuilderImpl();
  }

  private static final class SachbearbeiterUpdateDtoBuilderImpl extends SachbearbeiterUpdateDtoBuilder<SachbearbeiterUpdateDto, SachbearbeiterUpdateDtoBuilderImpl> {

    @Override
    protected SachbearbeiterUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SachbearbeiterUpdateDto build() {
      return new SachbearbeiterUpdateDto(this);
    }
  }

  public static abstract class SachbearbeiterUpdateDtoBuilder<C extends SachbearbeiterUpdateDto, B extends SachbearbeiterUpdateDtoBuilder<C, B>>  {
    private String vorname;
    private String nachname;
    private String telefonnummer;
    private String email;
    private String funktionDe;
    private String funktionFr;
    private List<String> sachbearbeiterRollen = new ArrayList<>();
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

