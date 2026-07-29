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



@JsonTypeName("Statistik")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class StatistikDto  implements Serializable {
  private @Valid UUID id;
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid Boolean valid;
  private @Valid String userTriggeredCreation;
  private @Valid Integer year;
  private @Valid String error;
  private @Valid String filename;
  private @Valid String filesize;

  protected StatistikDto(StatistikDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.timestampErstellt = b.timestampErstellt;
    this.valid = b.valid;
    this.userTriggeredCreation = b.userTriggeredCreation;
    this.year = b.year;
    this.error = b.error;
    this.filename = b.filename;
    this.filesize = b.filesize;
  }

  public StatistikDto() {
  }

  /**
   **/
  public StatistikDto id(UUID id) {
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
  public StatistikDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public StatistikDto valid(Boolean valid) {
    this.valid = valid;
    return this;
  }

  
  @JsonProperty("valid")
  @NotNull
  public Boolean getValid() {
    return valid;
  }

  @JsonProperty("valid")
  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  /**
   **/
  public StatistikDto userTriggeredCreation(String userTriggeredCreation) {
    this.userTriggeredCreation = userTriggeredCreation;
    return this;
  }

  
  @JsonProperty("userTriggeredCreation")
  @NotNull
  public String getUserTriggeredCreation() {
    return userTriggeredCreation;
  }

  @JsonProperty("userTriggeredCreation")
  public void setUserTriggeredCreation(String userTriggeredCreation) {
    this.userTriggeredCreation = userTriggeredCreation;
  }

  /**
   **/
  public StatistikDto year(Integer year) {
    this.year = year;
    return this;
  }

  
  @JsonProperty("year")
  @NotNull
  public Integer getYear() {
    return year;
  }

  @JsonProperty("year")
  public void setYear(Integer year) {
    this.year = year;
  }

  /**
   **/
  public StatistikDto error(String error) {
    this.error = error;
    return this;
  }

  
  @JsonProperty("error")
  public String getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(String error) {
    this.error = error;
  }

  /**
   **/
  public StatistikDto filename(String filename) {
    this.filename = filename;
    return this;
  }

  
  @JsonProperty("filename")
  public String getFilename() {
    return filename;
  }

  @JsonProperty("filename")
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   **/
  public StatistikDto filesize(String filesize) {
    this.filesize = filesize;
    return this;
  }

  
  @JsonProperty("filesize")
  public String getFilesize() {
    return filesize;
  }

  @JsonProperty("filesize")
  public void setFilesize(String filesize) {
    this.filesize = filesize;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatistikDto statistik = (StatistikDto) o;
    return Objects.equals(this.id, statistik.id) &&
        Objects.equals(this.timestampErstellt, statistik.timestampErstellt) &&
        Objects.equals(this.valid, statistik.valid) &&
        Objects.equals(this.userTriggeredCreation, statistik.userTriggeredCreation) &&
        Objects.equals(this.year, statistik.year) &&
        Objects.equals(this.error, statistik.error) &&
        Objects.equals(this.filename, statistik.filename) &&
        Objects.equals(this.filesize, statistik.filesize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, timestampErstellt, valid, userTriggeredCreation, year, error, filename, filesize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatistikDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    userTriggeredCreation: ").append(toIndentedString(userTriggeredCreation)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    filesize: ").append(toIndentedString(filesize)).append("\n");
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


  public static StatistikDtoBuilder<?, ?> builder() {
    return new StatistikDtoBuilderImpl();
  }

  private static final class StatistikDtoBuilderImpl extends StatistikDtoBuilder<StatistikDto, StatistikDtoBuilderImpl> {

    @Override
    protected StatistikDtoBuilderImpl self() {
      return this;
    }

    @Override
    public StatistikDto build() {
      return new StatistikDto(this);
    }
  }

  public static abstract class StatistikDtoBuilder<C extends StatistikDto, B extends StatistikDtoBuilder<C, B>>  {
    private UUID id;
    private java.time.LocalDateTime timestampErstellt;
    private Boolean valid;
    private String userTriggeredCreation;
    private Integer year;
    private String error;
    private String filename;
    private String filesize;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B timestampErstellt(java.time.LocalDateTime timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
    public B valid(Boolean valid) {
      this.valid = valid;
      return self();
    }
    public B userTriggeredCreation(String userTriggeredCreation) {
      this.userTriggeredCreation = userTriggeredCreation;
      return self();
    }
    public B year(Integer year) {
      this.year = year;
      return self();
    }
    public B error(String error) {
      this.error = error;
      return self();
    }
    public B filename(String filename) {
      this.filename = filename;
      return self();
    }
    public B filesize(String filesize) {
      this.filesize = filesize;
      return self();
    }
  }
}

