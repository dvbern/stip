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



@JsonTypeName("ZahlungsverbindungSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class ZahlungsverbindungSlimDto  implements Serializable {
  private @Valid AdresseDto adresse;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String institution;

  protected ZahlungsverbindungSlimDto(ZahlungsverbindungSlimDtoBuilder<?, ?> b) {
    this.adresse = b.adresse;
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.institution = b.institution;
  }

  public ZahlungsverbindungSlimDto() {
  }

  /**
   **/
  public ZahlungsverbindungSlimDto adresse(AdresseDto adresse) {
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
  public ZahlungsverbindungSlimDto vorname(String vorname) {
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
  public ZahlungsverbindungSlimDto nachname(String nachname) {
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
  public ZahlungsverbindungSlimDto institution(String institution) {
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
    ZahlungsverbindungSlimDto zahlungsverbindungSlim = (ZahlungsverbindungSlimDto) o;
    return Objects.equals(this.adresse, zahlungsverbindungSlim.adresse) &&
        Objects.equals(this.vorname, zahlungsverbindungSlim.vorname) &&
        Objects.equals(this.nachname, zahlungsverbindungSlim.nachname) &&
        Objects.equals(this.institution, zahlungsverbindungSlim.institution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, vorname, nachname, institution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZahlungsverbindungSlimDto {\n");
    
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
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


  public static ZahlungsverbindungSlimDtoBuilder<?, ?> builder() {
    return new ZahlungsverbindungSlimDtoBuilderImpl();
  }

  private static final class ZahlungsverbindungSlimDtoBuilderImpl extends ZahlungsverbindungSlimDtoBuilder<ZahlungsverbindungSlimDto, ZahlungsverbindungSlimDtoBuilderImpl> {

    @Override
    protected ZahlungsverbindungSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public ZahlungsverbindungSlimDto build() {
      return new ZahlungsverbindungSlimDto(this);
    }
  }

  public static abstract class ZahlungsverbindungSlimDtoBuilder<C extends ZahlungsverbindungSlimDto, B extends ZahlungsverbindungSlimDtoBuilder<C, B>>  {
    private AdresseDto adresse;
    private String vorname;
    private String nachname;
    private String institution;
    protected abstract B self();

    public abstract C build();

    public B adresse(AdresseDto adresse) {
      this.adresse = adresse;
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

