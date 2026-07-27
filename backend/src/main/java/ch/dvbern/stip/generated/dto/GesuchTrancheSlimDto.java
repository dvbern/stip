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



@JsonTypeName("GesuchTrancheSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchTrancheSlimDto  implements Serializable {
  private @Valid UUID id;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status;
  private @Valid ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ;
  private @Valid String comment;
  private @Valid Integer revision;

  protected GesuchTrancheSlimDto(GesuchTrancheSlimDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.gueltigAb = b.gueltigAb;
    this.gueltigBis = b.gueltigBis;
    this.status = b.status;
    this.typ = b.typ;
    this.comment = b.comment;
    this.revision = b.revision;
  }

  public GesuchTrancheSlimDto() {
  }

  /**
   **/
  public GesuchTrancheSlimDto id(UUID id) {
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
  public GesuchTrancheSlimDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  @NotNull
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public GesuchTrancheSlimDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  @NotNull
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   **/
  public GesuchTrancheSlimDto status(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status) {
    this.status = status;
  }

  /**
   **/
  public GesuchTrancheSlimDto typ(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  @NotNull
  public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
    this.typ = typ;
  }

  /**
   **/
  public GesuchTrancheSlimDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  
  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   **/
  public GesuchTrancheSlimDto revision(Integer revision) {
    this.revision = revision;
    return this;
  }

  
  @JsonProperty("revision")
  public Integer getRevision() {
    return revision;
  }

  @JsonProperty("revision")
  public void setRevision(Integer revision) {
    this.revision = revision;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheSlimDto gesuchTrancheSlim = (GesuchTrancheSlimDto) o;
    return Objects.equals(this.id, gesuchTrancheSlim.id) &&
        Objects.equals(this.gueltigAb, gesuchTrancheSlim.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchTrancheSlim.gueltigBis) &&
        Objects.equals(this.status, gesuchTrancheSlim.status) &&
        Objects.equals(this.typ, gesuchTrancheSlim.typ) &&
        Objects.equals(this.comment, gesuchTrancheSlim.comment) &&
        Objects.equals(this.revision, gesuchTrancheSlim.revision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gueltigAb, gueltigBis, status, typ, comment, revision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
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


  public static GesuchTrancheSlimDtoBuilder<?, ?> builder() {
    return new GesuchTrancheSlimDtoBuilderImpl();
  }

  private static final class GesuchTrancheSlimDtoBuilderImpl extends GesuchTrancheSlimDtoBuilder<GesuchTrancheSlimDto, GesuchTrancheSlimDtoBuilderImpl> {

    @Override
    protected GesuchTrancheSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchTrancheSlimDto build() {
      return new GesuchTrancheSlimDto(this);
    }
  }

  public static abstract class GesuchTrancheSlimDtoBuilder<C extends GesuchTrancheSlimDto, B extends GesuchTrancheSlimDtoBuilder<C, B>>  {
    private UUID id;
    private LocalDate gueltigAb;
    private LocalDate gueltigBis;
    private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status;
    private ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ;
    private String comment;
    private Integer revision;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B gueltigAb(LocalDate gueltigAb) {
      this.gueltigAb = gueltigAb;
      return self();
    }
    public B gueltigBis(LocalDate gueltigBis) {
      this.gueltigBis = gueltigBis;
      return self();
    }
    public B status(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status) {
      this.status = status;
      return self();
    }
    public B typ(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
      this.typ = typ;
      return self();
    }
    public B comment(String comment) {
      this.comment = comment;
      return self();
    }
    public B revision(Integer revision) {
      this.revision = revision;
      return self();
    }
  }
}

