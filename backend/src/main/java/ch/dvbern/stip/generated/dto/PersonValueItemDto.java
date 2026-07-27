package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("PersonValueItem")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PersonValueItemDto  implements Serializable {
  private @Valid String vorname;
  private @Valid Integer value;

  protected PersonValueItemDto(PersonValueItemDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.value = b.value;
  }

  public PersonValueItemDto() {
  }

  /**
   **/
  public PersonValueItemDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public PersonValueItemDto value(Integer value) {
    this.value = value;
    return this;
  }

  
  @JsonProperty("value")
  public Integer getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(Integer value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonValueItemDto personValueItem = (PersonValueItemDto) o;
    return Objects.equals(this.vorname, personValueItem.vorname) &&
        Objects.equals(this.value, personValueItem.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonValueItemDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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


  public static PersonValueItemDtoBuilder<?, ?> builder() {
    return new PersonValueItemDtoBuilderImpl();
  }

  private static final class PersonValueItemDtoBuilderImpl extends PersonValueItemDtoBuilder<PersonValueItemDto, PersonValueItemDtoBuilderImpl> {

    @Override
    protected PersonValueItemDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PersonValueItemDto build() {
      return new PersonValueItemDto(this);
    }
  }

  public static abstract class PersonValueItemDtoBuilder<C extends PersonValueItemDto, B extends PersonValueItemDtoBuilder<C, B>>  {
    private String vorname;
    private Integer value;
    protected abstract B self();

    public abstract C build();

    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B value(Integer value) {
      this.value = value;
      return self();
    }
  }
}

