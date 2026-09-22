package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("AbschlussSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AbschlussSlimDto  implements Serializable {
  private UUID id;
  private String bezeichnungDe;
  private String bezeichnungFr;
  private ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie;
  private ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung;
  private Boolean aktiv;
  private ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage;

  protected AbschlussSlimDto(AbschlussSlimDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.bezeichnungDe = b.bezeichnungDe;
    this.bezeichnungFr = b.bezeichnungFr;
    this.ausbildungskategorie = b.ausbildungskategorie;
    this.bildungsrichtung = b.bildungsrichtung;
    this.aktiv = b.aktiv;
    this.zusatzfrage = b.zusatzfrage;
  }

  public AbschlussSlimDto() {
  }

  /**
   **/
  public AbschlussSlimDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty(required = true, value = "id")
  @NotNull public UUID getId() {
    return id;
  }

  @JsonProperty(required = true, value = "id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public AbschlussSlimDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty(required = true, value = "bezeichnungDe")
  @NotNull public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty(required = true, value = "bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public AbschlussSlimDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty(required = true, value = "bezeichnungFr")
  @NotNull public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty(required = true, value = "bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public AbschlussSlimDto ausbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungskategorie")
  @NotNull public ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie getAusbildungskategorie() {
    return ausbildungskategorie;
  }

  @JsonProperty(required = true, value = "ausbildungskategorie")
  public void setAusbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
  }

  /**
   **/
  public AbschlussSlimDto bildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
    return this;
  }

  
  @JsonProperty(required = true, value = "bildungsrichtung")
  @NotNull public ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung getBildungsrichtung() {
    return bildungsrichtung;
  }

  @JsonProperty(required = true, value = "bildungsrichtung")
  public void setBildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
  }

  /**
   **/
  public AbschlussSlimDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty(required = true, value = "aktiv")
  @NotNull public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty(required = true, value = "aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  /**
   **/
  public AbschlussSlimDto zusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
    this.zusatzfrage = zusatzfrage;
    return this;
  }

  
  @JsonProperty("zusatzfrage")
  public ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage getZusatzfrage() {
    return zusatzfrage;
  }

  @JsonProperty("zusatzfrage")
  public void setZusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
    this.zusatzfrage = zusatzfrage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbschlussSlimDto abschlussSlim = (AbschlussSlimDto) o;
    return Objects.equals(this.id, abschlussSlim.id) &&
        Objects.equals(this.bezeichnungDe, abschlussSlim.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, abschlussSlim.bezeichnungFr) &&
        Objects.equals(this.ausbildungskategorie, abschlussSlim.ausbildungskategorie) &&
        Objects.equals(this.bildungsrichtung, abschlussSlim.bildungsrichtung) &&
        Objects.equals(this.aktiv, abschlussSlim.aktiv) &&
        Objects.equals(this.zusatzfrage, abschlussSlim.zusatzfrage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, ausbildungskategorie, bildungsrichtung, aktiv, zusatzfrage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbschlussSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    ausbildungskategorie: ").append(toIndentedString(ausbildungskategorie)).append("\n");
    sb.append("    bildungsrichtung: ").append(toIndentedString(bildungsrichtung)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
    sb.append("    zusatzfrage: ").append(toIndentedString(zusatzfrage)).append("\n");
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


  public static AbschlussSlimDtoBuilder<?, ?> builder() {
    return new AbschlussSlimDtoBuilderImpl();
  }

  private static final class AbschlussSlimDtoBuilderImpl extends AbschlussSlimDtoBuilder<AbschlussSlimDto, AbschlussSlimDtoBuilderImpl> {

    @Override
    protected AbschlussSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AbschlussSlimDto build() {
      return new AbschlussSlimDto(this);
    }
  }

  public static abstract class AbschlussSlimDtoBuilder<C extends AbschlussSlimDto, B extends AbschlussSlimDtoBuilder<C, B>>  {
    private UUID id;
    private String bezeichnungDe;
    private String bezeichnungFr;
    private ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie;
    private ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung;
    private Boolean aktiv;
    private ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B bezeichnungDe(String bezeichnungDe) {
      this.bezeichnungDe = bezeichnungDe;
      return self();
    }
    public B bezeichnungFr(String bezeichnungFr) {
      this.bezeichnungFr = bezeichnungFr;
      return self();
    }
    public B ausbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
      this.ausbildungskategorie = ausbildungskategorie;
      return self();
    }
    public B bildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
      this.bildungsrichtung = bildungsrichtung;
      return self();
    }
    public B aktiv(Boolean aktiv) {
      this.aktiv = aktiv;
      return self();
    }
    public B zusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
      this.zusatzfrage = zusatzfrage;
      return self();
    }
  }
}
