package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BeschwerdeEntscheidDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("BeschwerdeVerlaufEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BeschwerdeVerlaufEntryDto  implements Serializable {
  private String kommentar;
  private Boolean beschwerdeSetTo;
  private java.time.LocalDateTime timestampErstellt;
  private String userErstellt;
  private BeschwerdeEntscheidDto beschwerdeEntscheid;

  protected BeschwerdeVerlaufEntryDto(BeschwerdeVerlaufEntryDtoBuilder<?, ?> b) {
    this.kommentar = b.kommentar;
    this.beschwerdeSetTo = b.beschwerdeSetTo;
    this.timestampErstellt = b.timestampErstellt;
    this.userErstellt = b.userErstellt;
    this.beschwerdeEntscheid = b.beschwerdeEntscheid;
  }

  public BeschwerdeVerlaufEntryDto() {
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty(required = true, value = "kommentar")
  @NotNull public String getKommentar() {
    return kommentar;
  }

  @JsonProperty(required = true, value = "kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto beschwerdeSetTo(Boolean beschwerdeSetTo) {
    this.beschwerdeSetTo = beschwerdeSetTo;
    return this;
  }

  
  @JsonProperty(required = true, value = "beschwerdeSetTo")
  @NotNull public Boolean getBeschwerdeSetTo() {
    return beschwerdeSetTo;
  }

  @JsonProperty(required = true, value = "beschwerdeSetTo")
  public void setBeschwerdeSetTo(Boolean beschwerdeSetTo) {
    this.beschwerdeSetTo = beschwerdeSetTo;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty(required = true, value = "timestampErstellt")
  @NotNull public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty(required = true, value = "timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty(required = true, value = "userErstellt")
  @NotNull public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty(required = true, value = "userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public BeschwerdeVerlaufEntryDto beschwerdeEntscheid(BeschwerdeEntscheidDto beschwerdeEntscheid) {
    this.beschwerdeEntscheid = beschwerdeEntscheid;
    return this;
  }

  
  @JsonProperty("beschwerdeEntscheid")
  @Valid public BeschwerdeEntscheidDto getBeschwerdeEntscheid() {
    return beschwerdeEntscheid;
  }

  @JsonProperty("beschwerdeEntscheid")
  public void setBeschwerdeEntscheid(BeschwerdeEntscheidDto beschwerdeEntscheid) {
    this.beschwerdeEntscheid = beschwerdeEntscheid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeschwerdeVerlaufEntryDto beschwerdeVerlaufEntry = (BeschwerdeVerlaufEntryDto) o;
    return Objects.equals(this.kommentar, beschwerdeVerlaufEntry.kommentar) &&
        Objects.equals(this.beschwerdeSetTo, beschwerdeVerlaufEntry.beschwerdeSetTo) &&
        Objects.equals(this.timestampErstellt, beschwerdeVerlaufEntry.timestampErstellt) &&
        Objects.equals(this.userErstellt, beschwerdeVerlaufEntry.userErstellt) &&
        Objects.equals(this.beschwerdeEntscheid, beschwerdeVerlaufEntry.beschwerdeEntscheid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, beschwerdeSetTo, timestampErstellt, userErstellt, beschwerdeEntscheid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeVerlaufEntryDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    beschwerdeSetTo: ").append(toIndentedString(beschwerdeSetTo)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    beschwerdeEntscheid: ").append(toIndentedString(beschwerdeEntscheid)).append("\n");
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


  public static BeschwerdeVerlaufEntryDtoBuilder<?, ?> builder() {
    return new BeschwerdeVerlaufEntryDtoBuilderImpl();
  }

  private static final class BeschwerdeVerlaufEntryDtoBuilderImpl extends BeschwerdeVerlaufEntryDtoBuilder<BeschwerdeVerlaufEntryDto, BeschwerdeVerlaufEntryDtoBuilderImpl> {

    @Override
    protected BeschwerdeVerlaufEntryDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BeschwerdeVerlaufEntryDto build() {
      return new BeschwerdeVerlaufEntryDto(this);
    }
  }

  public static abstract class BeschwerdeVerlaufEntryDtoBuilder<C extends BeschwerdeVerlaufEntryDto, B extends BeschwerdeVerlaufEntryDtoBuilder<C, B>>  {
    private String kommentar;
    private Boolean beschwerdeSetTo;
    private java.time.LocalDateTime timestampErstellt;
    private String userErstellt;
    private BeschwerdeEntscheidDto beschwerdeEntscheid;
    protected abstract B self();

    public abstract C build();

    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
    public B beschwerdeSetTo(Boolean beschwerdeSetTo) {
      this.beschwerdeSetTo = beschwerdeSetTo;
      return self();
    }
    public B timestampErstellt(java.time.LocalDateTime timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
    public B userErstellt(String userErstellt) {
      this.userErstellt = userErstellt;
      return self();
    }
    public B beschwerdeEntscheid(BeschwerdeEntscheidDto beschwerdeEntscheid) {
      this.beschwerdeEntscheid = beschwerdeEntscheid;
      return self();
    }
  }
}
