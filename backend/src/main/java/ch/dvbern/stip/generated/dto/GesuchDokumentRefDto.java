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



@JsonTypeName("GesuchDokumentRef")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchDokumentRefDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
  private @Valid UUID entryId;

  protected GesuchDokumentRefDto(GesuchDokumentRefDtoBuilder<?, ?> b) {
    this.dokumentTyp = b.dokumentTyp;
    this.entryId = b.entryId;
  }

  public GesuchDokumentRefDto() {
  }

  /**
   **/
  public GesuchDokumentRefDto dokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
    return this;
  }

  
  @JsonProperty("dokumentTyp")
  @NotNull
  public ch.dvbern.stip.api.dokument.type.DokumentTyp getDokumentTyp() {
    return dokumentTyp;
  }

  @JsonProperty("dokumentTyp")
  public void setDokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
  }

  /**
   **/
  public GesuchDokumentRefDto entryId(UUID entryId) {
    this.entryId = entryId;
    return this;
  }

  
  @JsonProperty("entryId")
  public UUID getEntryId() {
    return entryId;
  }

  @JsonProperty("entryId")
  public void setEntryId(UUID entryId) {
    this.entryId = entryId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDokumentRefDto gesuchDokumentRef = (GesuchDokumentRefDto) o;
    return Objects.equals(this.dokumentTyp, gesuchDokumentRef.dokumentTyp) &&
        Objects.equals(this.entryId, gesuchDokumentRef.entryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dokumentTyp, entryId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentRefDto {\n");
    
    sb.append("    dokumentTyp: ").append(toIndentedString(dokumentTyp)).append("\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
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


  public static GesuchDokumentRefDtoBuilder<?, ?> builder() {
    return new GesuchDokumentRefDtoBuilderImpl();
  }

  private static final class GesuchDokumentRefDtoBuilderImpl extends GesuchDokumentRefDtoBuilder<GesuchDokumentRefDto, GesuchDokumentRefDtoBuilderImpl> {

    @Override
    protected GesuchDokumentRefDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchDokumentRefDto build() {
      return new GesuchDokumentRefDto(this);
    }
  }

  public static abstract class GesuchDokumentRefDtoBuilder<C extends GesuchDokumentRefDto, B extends GesuchDokumentRefDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp;
    private UUID entryId;
    protected abstract B self();

    public abstract C build();

    public B dokumentTyp(ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp) {
      this.dokumentTyp = dokumentTyp;
      return self();
    }
    public B entryId(UUID entryId) {
      this.entryId = entryId;
      return self();
    }
  }
}

