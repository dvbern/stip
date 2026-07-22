package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValidDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValuesDto;
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



@JsonTypeName("DemoDataTestBerechnungResultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataTestBerechnungResultatDto  implements Serializable {
  private @Valid UUID demoDataId;
  private @Valid String testFall;
  private @Valid DemoDataTestBerechnungValidDto valid;
  private @Valid String message;
  private @Valid DemoDataTestBerechnungValuesDto soll;
  private @Valid DemoDataTestBerechnungValuesDto ist;

  protected DemoDataTestBerechnungResultatDto(DemoDataTestBerechnungResultatDtoBuilder<?, ?> b) {
    this.demoDataId = b.demoDataId;
    this.testFall = b.testFall;
    this.valid = b.valid;
    this.message = b.message;
    this.soll = b.soll;
    this.ist = b.ist;
  }

  public DemoDataTestBerechnungResultatDto() {
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto demoDataId(UUID demoDataId) {
    this.demoDataId = demoDataId;
    return this;
  }

  
  @JsonProperty("demoDataId")
  @NotNull
  public UUID getDemoDataId() {
    return demoDataId;
  }

  @JsonProperty("demoDataId")
  public void setDemoDataId(UUID demoDataId) {
    this.demoDataId = demoDataId;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto testFall(String testFall) {
    this.testFall = testFall;
    return this;
  }

  
  @JsonProperty("testFall")
  @NotNull
  public String getTestFall() {
    return testFall;
  }

  @JsonProperty("testFall")
  public void setTestFall(String testFall) {
    this.testFall = testFall;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto valid(DemoDataTestBerechnungValidDto valid) {
    this.valid = valid;
    return this;
  }

  
  @JsonProperty("valid")
  public DemoDataTestBerechnungValidDto getValid() {
    return valid;
  }

  @JsonProperty("valid")
  public void setValid(DemoDataTestBerechnungValidDto valid) {
    this.valid = valid;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto message(String message) {
    this.message = message;
    return this;
  }

  
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto soll(DemoDataTestBerechnungValuesDto soll) {
    this.soll = soll;
    return this;
  }

  
  @JsonProperty("soll")
  public DemoDataTestBerechnungValuesDto getSoll() {
    return soll;
  }

  @JsonProperty("soll")
  public void setSoll(DemoDataTestBerechnungValuesDto soll) {
    this.soll = soll;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto ist(DemoDataTestBerechnungValuesDto ist) {
    this.ist = ist;
    return this;
  }

  
  @JsonProperty("ist")
  public DemoDataTestBerechnungValuesDto getIst() {
    return ist;
  }

  @JsonProperty("ist")
  public void setIst(DemoDataTestBerechnungValuesDto ist) {
    this.ist = ist;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataTestBerechnungResultatDto demoDataTestBerechnungResultat = (DemoDataTestBerechnungResultatDto) o;
    return Objects.equals(this.demoDataId, demoDataTestBerechnungResultat.demoDataId) &&
        Objects.equals(this.testFall, demoDataTestBerechnungResultat.testFall) &&
        Objects.equals(this.valid, demoDataTestBerechnungResultat.valid) &&
        Objects.equals(this.message, demoDataTestBerechnungResultat.message) &&
        Objects.equals(this.soll, demoDataTestBerechnungResultat.soll) &&
        Objects.equals(this.ist, demoDataTestBerechnungResultat.ist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(demoDataId, testFall, valid, message, soll, ist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungResultatDto {\n");
    
    sb.append("    demoDataId: ").append(toIndentedString(demoDataId)).append("\n");
    sb.append("    testFall: ").append(toIndentedString(testFall)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    soll: ").append(toIndentedString(soll)).append("\n");
    sb.append("    ist: ").append(toIndentedString(ist)).append("\n");
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


  public static DemoDataTestBerechnungResultatDtoBuilder<?, ?> builder() {
    return new DemoDataTestBerechnungResultatDtoBuilderImpl();
  }

  private static final class DemoDataTestBerechnungResultatDtoBuilderImpl extends DemoDataTestBerechnungResultatDtoBuilder<DemoDataTestBerechnungResultatDto, DemoDataTestBerechnungResultatDtoBuilderImpl> {

    @Override
    protected DemoDataTestBerechnungResultatDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataTestBerechnungResultatDto build() {
      return new DemoDataTestBerechnungResultatDto(this);
    }
  }

  public static abstract class DemoDataTestBerechnungResultatDtoBuilder<C extends DemoDataTestBerechnungResultatDto, B extends DemoDataTestBerechnungResultatDtoBuilder<C, B>>  {
    private UUID demoDataId;
    private String testFall;
    private DemoDataTestBerechnungValidDto valid;
    private String message;
    private DemoDataTestBerechnungValuesDto soll;
    private DemoDataTestBerechnungValuesDto ist;
    protected abstract B self();

    public abstract C build();

    public B demoDataId(UUID demoDataId) {
      this.demoDataId = demoDataId;
      return self();
    }
    public B testFall(String testFall) {
      this.testFall = testFall;
      return self();
    }
    public B valid(DemoDataTestBerechnungValidDto valid) {
      this.valid = valid;
      return self();
    }
    public B message(String message) {
      this.message = message;
      return self();
    }
    public B soll(DemoDataTestBerechnungValuesDto soll) {
      this.soll = soll;
      return self();
    }
    public B ist(DemoDataTestBerechnungValuesDto ist) {
      this.ist = ist;
      return self();
    }
  }
}

