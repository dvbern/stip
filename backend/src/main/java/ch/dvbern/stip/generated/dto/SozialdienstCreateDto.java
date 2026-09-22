package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SozialdienstCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SozialdienstCreateDto  implements Serializable {
  private String name;
  private ZahlungsverbindungDto zahlungsverbindung;
  private SozialdienstAdminDto sozialdienstAdmin;

  protected SozialdienstCreateDto(SozialdienstCreateDtoBuilder<?, ?> b) {
    this.name = b.name;
    this.zahlungsverbindung = b.zahlungsverbindung;
    this.sozialdienstAdmin = b.sozialdienstAdmin;
  }

  public SozialdienstCreateDto() {
  }

  /**
   **/
  public SozialdienstCreateDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty(required = true, value = "name")
  @NotNull public String getName() {
    return name;
  }

  @JsonProperty(required = true, value = "name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public SozialdienstCreateDto zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
    return this;
  }

  
  @JsonProperty(required = true, value = "zahlungsverbindung")
  @NotNull @Valid public ZahlungsverbindungDto getZahlungsverbindung() {
    return zahlungsverbindung;
  }

  @JsonProperty(required = true, value = "zahlungsverbindung")
  public void setZahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
  }

  /**
   **/
  public SozialdienstCreateDto sozialdienstAdmin(SozialdienstAdminDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
    return this;
  }

  
  @JsonProperty(required = true, value = "sozialdienstAdmin")
  @NotNull @Valid public SozialdienstAdminDto getSozialdienstAdmin() {
    return sozialdienstAdmin;
  }

  @JsonProperty(required = true, value = "sozialdienstAdmin")
  public void setSozialdienstAdmin(SozialdienstAdminDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstCreateDto sozialdienstCreate = (SozialdienstCreateDto) o;
    return Objects.equals(this.name, sozialdienstCreate.name) &&
        Objects.equals(this.zahlungsverbindung, sozialdienstCreate.zahlungsverbindung) &&
        Objects.equals(this.sozialdienstAdmin, sozialdienstCreate.sozialdienstAdmin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, zahlungsverbindung, sozialdienstAdmin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstCreateDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    zahlungsverbindung: ").append(toIndentedString(zahlungsverbindung)).append("\n");
    sb.append("    sozialdienstAdmin: ").append(toIndentedString(sozialdienstAdmin)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static SozialdienstCreateDtoBuilder<?, ?> builder() {
    return new SozialdienstCreateDtoBuilderImpl();
  }

  private static final class SozialdienstCreateDtoBuilderImpl extends SozialdienstCreateDtoBuilder<SozialdienstCreateDto, SozialdienstCreateDtoBuilderImpl> {

    @Override
    protected SozialdienstCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SozialdienstCreateDto build() {
      return new SozialdienstCreateDto(this);
    }
  }

  public static abstract class SozialdienstCreateDtoBuilder<C extends SozialdienstCreateDto, B extends SozialdienstCreateDtoBuilder<C, B>>  {
    private String name;
    private ZahlungsverbindungDto zahlungsverbindung;
    private SozialdienstAdminDto sozialdienstAdmin;
    protected abstract B self();

    public abstract C build();

    public B name(String name) {
      this.name = name;
      return self();
    }
    public B zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
      this.zahlungsverbindung = zahlungsverbindung;
      return self();
    }
    public B sozialdienstAdmin(SozialdienstAdminDto sozialdienstAdmin) {
      this.sozialdienstAdmin = sozialdienstAdmin;
      return self();
    }
  }
}
