package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Kommentar zu einem (abgelehnten) GesuchDokument
 **/

@JsonTypeName("GesuchDokumentKommentar")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchDokumentKommentarDto  implements Serializable {
  private @Valid UUID gesuchTrancheId;
  private @Valid UUID gesuchDokumentId;
  private @Valid String kommentar;
  private @Valid String userErstellt;
  private @Valid LocalDate timestampErstellt;

  protected GesuchDokumentKommentarDto(GesuchDokumentKommentarDtoBuilder<?, ?> b) {
    this.gesuchTrancheId = b.gesuchTrancheId;
    this.gesuchDokumentId = b.gesuchDokumentId;
    this.kommentar = b.kommentar;
    this.userErstellt = b.userErstellt;
    this.timestampErstellt = b.timestampErstellt;
  }

  public GesuchDokumentKommentarDto() {
  }

  /**
   **/
  public GesuchDokumentKommentarDto gesuchTrancheId(UUID gesuchTrancheId) {
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

  /**
   **/
  public GesuchDokumentKommentarDto gesuchDokumentId(UUID gesuchDokumentId) {
    this.gesuchDokumentId = gesuchDokumentId;
    return this;
  }

  
  @JsonProperty("gesuchDokumentId")
  @NotNull
  public UUID getGesuchDokumentId() {
    return gesuchDokumentId;
  }

  @JsonProperty("gesuchDokumentId")
  public void setGesuchDokumentId(UUID gesuchDokumentId) {
    this.gesuchDokumentId = gesuchDokumentId;
  }

  /**
   **/
  public GesuchDokumentKommentarDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public GesuchDokumentKommentarDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("user_erstellt")
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("user_erstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public GesuchDokumentKommentarDto timestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDokumentKommentarDto gesuchDokumentKommentar = (GesuchDokumentKommentarDto) o;
    return Objects.equals(this.gesuchTrancheId, gesuchDokumentKommentar.gesuchTrancheId) &&
        Objects.equals(this.gesuchDokumentId, gesuchDokumentKommentar.gesuchDokumentId) &&
        Objects.equals(this.kommentar, gesuchDokumentKommentar.kommentar) &&
        Objects.equals(this.userErstellt, gesuchDokumentKommentar.userErstellt) &&
        Objects.equals(this.timestampErstellt, gesuchDokumentKommentar.timestampErstellt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchTrancheId, gesuchDokumentId, kommentar, userErstellt, timestampErstellt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentKommentarDto {\n");
    
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    gesuchDokumentId: ").append(toIndentedString(gesuchDokumentId)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
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


  public static GesuchDokumentKommentarDtoBuilder<?, ?> builder() {
    return new GesuchDokumentKommentarDtoBuilderImpl();
  }

  private static final class GesuchDokumentKommentarDtoBuilderImpl extends GesuchDokumentKommentarDtoBuilder<GesuchDokumentKommentarDto, GesuchDokumentKommentarDtoBuilderImpl> {

    @Override
    protected GesuchDokumentKommentarDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchDokumentKommentarDto build() {
      return new GesuchDokumentKommentarDto(this);
    }
  }

  public static abstract class GesuchDokumentKommentarDtoBuilder<C extends GesuchDokumentKommentarDto, B extends GesuchDokumentKommentarDtoBuilder<C, B>>  {
    private UUID gesuchTrancheId;
    private UUID gesuchDokumentId;
    private String kommentar;
    private String userErstellt;
    private LocalDate timestampErstellt;
    protected abstract B self();

    public abstract C build();

    public B gesuchTrancheId(UUID gesuchTrancheId) {
      this.gesuchTrancheId = gesuchTrancheId;
      return self();
    }
    public B gesuchDokumentId(UUID gesuchDokumentId) {
      this.gesuchDokumentId = gesuchDokumentId;
      return self();
    }
    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
    public B userErstellt(String userErstellt) {
      this.userErstellt = userErstellt;
      return self();
    }
    public B timestampErstellt(LocalDate timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
  }
}

