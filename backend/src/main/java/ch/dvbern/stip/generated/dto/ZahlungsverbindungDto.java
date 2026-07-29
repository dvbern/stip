package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Zahlungsverbindung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class ZahlungsverbindungDto  implements Serializable {
  private @Valid AdresseDto adresse;
  private @Valid String iban;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String institution;

  protected ZahlungsverbindungDto(ZahlungsverbindungDtoBuilder<?, ?> b) {
    this.adresse = b.adresse;
    this.iban = b.iban;
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.institution = b.institution;
  }

  public ZahlungsverbindungDto() {
  }

  /**
   **/
  public ZahlungsverbindungDto adresse(AdresseDto adresse) {
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
  public ZahlungsverbindungDto iban(String iban) {
    this.iban = iban;
    return this;
  }

  
  @JsonProperty("iban")
  @NotNull
  public String getIban() {
    return iban;
  }

  @JsonProperty("iban")
  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   **/
  public ZahlungsverbindungDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public ZahlungsverbindungDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public ZahlungsverbindungDto institution(String institution) {
    this.institution = institution;
    return this;
  }

  
  @JsonProperty("institution")
  public String getInstitution() {
    return institution;
  }

  @JsonProperty("institution")
  public void setInstitution(String institution) {
    this.institution = institution;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZahlungsverbindungDto zahlungsverbindung = (ZahlungsverbindungDto) o;
    return Objects.equals(this.adresse, zahlungsverbindung.adresse) &&
        Objects.equals(this.iban, zahlungsverbindung.iban) &&
        Objects.equals(this.vorname, zahlungsverbindung.vorname) &&
        Objects.equals(this.nachname, zahlungsverbindung.nachname) &&
        Objects.equals(this.institution, zahlungsverbindung.institution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, iban, vorname, nachname, institution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZahlungsverbindungDto {\n");
    
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    institution: ").append(toIndentedString(institution)).append("\n");
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


  public static ZahlungsverbindungDtoBuilder<?, ?> builder() {
    return new ZahlungsverbindungDtoBuilderImpl();
  }

  private static final class ZahlungsverbindungDtoBuilderImpl extends ZahlungsverbindungDtoBuilder<ZahlungsverbindungDto, ZahlungsverbindungDtoBuilderImpl> {

    @Override
    protected ZahlungsverbindungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public ZahlungsverbindungDto build() {
      return new ZahlungsverbindungDto(this);
    }
  }

  public static abstract class ZahlungsverbindungDtoBuilder<C extends ZahlungsverbindungDto, B extends ZahlungsverbindungDtoBuilder<C, B>>  {
    private AdresseDto adresse;
    private String iban;
    private String vorname;
    private String nachname;
    private String institution;
    protected abstract B self();

    public abstract C build();

    public B adresse(AdresseDto adresse) {
      this.adresse = adresse;
      return self();
    }
    public B iban(String iban) {
      this.iban = iban;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B institution(String institution) {
      this.institution = institution;
      return self();
    }
  }
}

