package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.ZahlungsverbindungSlimDto;
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



@JsonTypeName("SozialdienstSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SozialdienstSlimDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String name;
  private @Valid Boolean aktiv;
  private @Valid ZahlungsverbindungSlimDto zahlungsverbindung;

  protected SozialdienstSlimDto(SozialdienstSlimDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.name = b.name;
    this.aktiv = b.aktiv;
    this.zahlungsverbindung = b.zahlungsverbindung;
  }

  public SozialdienstSlimDto() {
  }

  /**
   **/
  public SozialdienstSlimDto id(UUID id) {
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
  public SozialdienstSlimDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public SozialdienstSlimDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty("aktiv")
  @NotNull
  public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty("aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  /**
   **/
  public SozialdienstSlimDto zahlungsverbindung(ZahlungsverbindungSlimDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
    return this;
  }

  
  @JsonProperty("zahlungsverbindung")
  @NotNull
  public ZahlungsverbindungSlimDto getZahlungsverbindung() {
    return zahlungsverbindung;
  }

  @JsonProperty("zahlungsverbindung")
  public void setZahlungsverbindung(ZahlungsverbindungSlimDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstSlimDto sozialdienstSlim = (SozialdienstSlimDto) o;
    return Objects.equals(this.id, sozialdienstSlim.id) &&
        Objects.equals(this.name, sozialdienstSlim.name) &&
        Objects.equals(this.aktiv, sozialdienstSlim.aktiv) &&
        Objects.equals(this.zahlungsverbindung, sozialdienstSlim.zahlungsverbindung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, aktiv, zahlungsverbindung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
    sb.append("    zahlungsverbindung: ").append(toIndentedString(zahlungsverbindung)).append("\n");
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


  public static SozialdienstSlimDtoBuilder<?, ?> builder() {
    return new SozialdienstSlimDtoBuilderImpl();
  }

  private static final class SozialdienstSlimDtoBuilderImpl extends SozialdienstSlimDtoBuilder<SozialdienstSlimDto, SozialdienstSlimDtoBuilderImpl> {

    @Override
    protected SozialdienstSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SozialdienstSlimDto build() {
      return new SozialdienstSlimDto(this);
    }
  }

  public static abstract class SozialdienstSlimDtoBuilder<C extends SozialdienstSlimDto, B extends SozialdienstSlimDtoBuilder<C, B>>  {
    private UUID id;
    private String name;
    private Boolean aktiv;
    private ZahlungsverbindungSlimDto zahlungsverbindung;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B name(String name) {
      this.name = name;
      return self();
    }
    public B aktiv(Boolean aktiv) {
      this.aktiv = aktiv;
      return self();
    }
    public B zahlungsverbindung(ZahlungsverbindungSlimDto zahlungsverbindung) {
      this.zahlungsverbindung = zahlungsverbindung;
      return self();
    }
  }
}

