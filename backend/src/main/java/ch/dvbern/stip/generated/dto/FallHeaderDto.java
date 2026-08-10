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



@JsonTypeName("FallHeader")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FallHeaderDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid Integer unreadNotificationsCount;

  protected FallHeaderDto(FallHeaderDtoBuilder<?, ?> b) {
    this.fallId = b.fallId;
    this.unreadNotificationsCount = b.unreadNotificationsCount;
  }

  public FallHeaderDto() {
  }

  /**
   **/
  public FallHeaderDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public FallHeaderDto unreadNotificationsCount(Integer unreadNotificationsCount) {
    this.unreadNotificationsCount = unreadNotificationsCount;
    return this;
  }

  
  @JsonProperty("unreadNotificationsCount")
  @NotNull
  public Integer getUnreadNotificationsCount() {
    return unreadNotificationsCount;
  }

  @JsonProperty("unreadNotificationsCount")
  public void setUnreadNotificationsCount(Integer unreadNotificationsCount) {
    this.unreadNotificationsCount = unreadNotificationsCount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallHeaderDto fallHeader = (FallHeaderDto) o;
    return Objects.equals(this.fallId, fallHeader.fallId) &&
        Objects.equals(this.unreadNotificationsCount, fallHeader.unreadNotificationsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, unreadNotificationsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallHeaderDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    unreadNotificationsCount: ").append(toIndentedString(unreadNotificationsCount)).append("\n");
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


  public static FallHeaderDtoBuilder<?, ?> builder() {
    return new FallHeaderDtoBuilderImpl();
  }

  private static final class FallHeaderDtoBuilderImpl extends FallHeaderDtoBuilder<FallHeaderDto, FallHeaderDtoBuilderImpl> {

    @Override
    protected FallHeaderDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FallHeaderDto build() {
      return new FallHeaderDto(this);
    }
  }

  public static abstract class FallHeaderDtoBuilder<C extends FallHeaderDto, B extends FallHeaderDtoBuilder<C, B>>  {
    private UUID fallId;
    private Integer unreadNotificationsCount;
    protected abstract B self();

    public abstract C build();

    public B fallId(UUID fallId) {
      this.fallId = fallId;
      return self();
    }
    public B unreadNotificationsCount(Integer unreadNotificationsCount) {
      this.unreadNotificationsCount = unreadNotificationsCount;
      return self();
    }
  }
}

