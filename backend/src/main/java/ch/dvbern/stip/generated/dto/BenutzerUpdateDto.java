package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("BenutzerUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BenutzerUpdateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;
  private @Valid Boolean nutzungsbedingungenAkzeptiert;

  protected BenutzerUpdateDto(BenutzerUpdateDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.benutzereinstellungen = b.benutzereinstellungen;
    this.nutzungsbedingungenAkzeptiert = b.nutzungsbedingungenAkzeptiert;
  }

  public BenutzerUpdateDto() {
  }

  /**
   **/
  public BenutzerUpdateDto vorname(String vorname) {
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
  public BenutzerUpdateDto nachname(String nachname) {
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
  public BenutzerUpdateDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
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
  public BenutzerUpdateDto nutzungsbedingungenAkzeptiert(Boolean nutzungsbedingungenAkzeptiert) {
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
    BenutzerUpdateDto benutzerUpdate = (BenutzerUpdateDto) o;
    return Objects.equals(this.vorname, benutzerUpdate.vorname) &&
        Objects.equals(this.nachname, benutzerUpdate.nachname) &&
        Objects.equals(this.benutzereinstellungen, benutzerUpdate.benutzereinstellungen) &&
        Objects.equals(this.nutzungsbedingungenAkzeptiert, benutzerUpdate.nutzungsbedingungenAkzeptiert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, benutzereinstellungen, nutzungsbedingungenAkzeptiert);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzerUpdateDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
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


  public static BenutzerUpdateDtoBuilder<?, ?> builder() {
    return new BenutzerUpdateDtoBuilderImpl();
  }

  private static final class BenutzerUpdateDtoBuilderImpl extends BenutzerUpdateDtoBuilder<BenutzerUpdateDto, BenutzerUpdateDtoBuilderImpl> {

    @Override
    protected BenutzerUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BenutzerUpdateDto build() {
      return new BenutzerUpdateDto(this);
    }
  }

  public static abstract class BenutzerUpdateDtoBuilder<C extends BenutzerUpdateDto, B extends BenutzerUpdateDtoBuilder<C, B>>  {
    private String vorname;
    private String nachname;
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

