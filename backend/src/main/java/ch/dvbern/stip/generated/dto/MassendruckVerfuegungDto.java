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



@JsonTypeName("MassendruckVerfuegung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class MassendruckVerfuegungDto  implements Serializable {
  private @Valid UUID id;
  private @Valid Boolean isVersendet;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String gesuchNummer;
  private @Valid UUID gesuchId;
  private @Valid UUID gesuchTrancheId;

  protected MassendruckVerfuegungDto(MassendruckVerfuegungDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.isVersendet = b.isVersendet;
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.gesuchNummer = b.gesuchNummer;
    this.gesuchId = b.gesuchId;
    this.gesuchTrancheId = b.gesuchTrancheId;
  }

  public MassendruckVerfuegungDto() {
  }

  /**
   **/
  public MassendruckVerfuegungDto id(UUID id) {
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
  public MassendruckVerfuegungDto isVersendet(Boolean isVersendet) {
    this.isVersendet = isVersendet;
    return this;
  }

  
  @JsonProperty("isVersendet")
  @NotNull
  public Boolean getIsVersendet() {
    return isVersendet;
  }

  @JsonProperty("isVersendet")
  public void setIsVersendet(Boolean isVersendet) {
    this.isVersendet = isVersendet;
  }

  /**
   **/
  public MassendruckVerfuegungDto nachname(String nachname) {
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
  public MassendruckVerfuegungDto vorname(String vorname) {
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
  public MassendruckVerfuegungDto gesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
    return this;
  }

  
  @JsonProperty("gesuchNummer")
  @NotNull
  public String getGesuchNummer() {
    return gesuchNummer;
  }

  @JsonProperty("gesuchNummer")
  public void setGesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }

  /**
   **/
  public MassendruckVerfuegungDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty("gesuchId")
  @NotNull
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public MassendruckVerfuegungDto gesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

  
  @JsonProperty("gesuchTrancheId")
  @NotNull
  public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }

  @JsonProperty("gesuchTrancheId")
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MassendruckVerfuegungDto massendruckVerfuegung = (MassendruckVerfuegungDto) o;
    return Objects.equals(this.id, massendruckVerfuegung.id) &&
        Objects.equals(this.isVersendet, massendruckVerfuegung.isVersendet) &&
        Objects.equals(this.nachname, massendruckVerfuegung.nachname) &&
        Objects.equals(this.vorname, massendruckVerfuegung.vorname) &&
        Objects.equals(this.gesuchNummer, massendruckVerfuegung.gesuchNummer) &&
        Objects.equals(this.gesuchId, massendruckVerfuegung.gesuchId) &&
        Objects.equals(this.gesuchTrancheId, massendruckVerfuegung.gesuchTrancheId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, isVersendet, nachname, vorname, gesuchNummer, gesuchId, gesuchTrancheId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MassendruckVerfuegungDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isVersendet: ").append(toIndentedString(isVersendet)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
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


  public static MassendruckVerfuegungDtoBuilder<?, ?> builder() {
    return new MassendruckVerfuegungDtoBuilderImpl();
  }

  private static final class MassendruckVerfuegungDtoBuilderImpl extends MassendruckVerfuegungDtoBuilder<MassendruckVerfuegungDto, MassendruckVerfuegungDtoBuilderImpl> {

    @Override
    protected MassendruckVerfuegungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public MassendruckVerfuegungDto build() {
      return new MassendruckVerfuegungDto(this);
    }
  }

  public static abstract class MassendruckVerfuegungDtoBuilder<C extends MassendruckVerfuegungDto, B extends MassendruckVerfuegungDtoBuilder<C, B>>  {
    private UUID id;
    private Boolean isVersendet;
    private String nachname;
    private String vorname;
    private String gesuchNummer;
    private UUID gesuchId;
    private UUID gesuchTrancheId;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B isVersendet(Boolean isVersendet) {
      this.isVersendet = isVersendet;
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
    public B gesuchNummer(String gesuchNummer) {
      this.gesuchNummer = gesuchNummer;
      return self();
    }
    public B gesuchId(UUID gesuchId) {
      this.gesuchId = gesuchId;
      return self();
    }
    public B gesuchTrancheId(UUID gesuchTrancheId) {
      this.gesuchTrancheId = gesuchTrancheId;
      return self();
    }
  }
}

