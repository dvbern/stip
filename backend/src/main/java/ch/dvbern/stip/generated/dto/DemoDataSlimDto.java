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



@JsonTypeName("DemoDataSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataSlimDto  implements Serializable {
  private @Valid String id;
  private @Valid String testFall;
  private @Valid String name;
  private @Valid String description;

  /**
   **/
  public DemoDataSlimDto id(String id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  public DemoDataSlimDto testFall(String testFall) {
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
  public DemoDataSlimDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public DemoDataSlimDto description(String description) {
    this.description = description;
    return this;
  }

  
  @JsonProperty("description")
  @NotNull
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataSlimDto demoDataSlim = (DemoDataSlimDto) o;
    return Objects.equals(this.id, demoDataSlim.id) &&
        Objects.equals(this.testFall, demoDataSlim.testFall) &&
        Objects.equals(this.name, demoDataSlim.name) &&
        Objects.equals(this.description, demoDataSlim.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, testFall, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    testFall: ").append(toIndentedString(testFall)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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


}

