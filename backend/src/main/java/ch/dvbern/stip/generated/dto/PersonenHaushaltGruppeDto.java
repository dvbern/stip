package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("PersonenHaushaltGruppe")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PersonenHaushaltGruppeDto  implements Serializable {
  private @Valid ch.dvbern.stip.berechnung.domain.type.PersonenHaushalt typ;
  private @Valid List<String> names = new ArrayList<>();

  /**
   **/
  public PersonenHaushaltGruppeDto typ(ch.dvbern.stip.berechnung.domain.type.PersonenHaushalt typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  @NotNull
  public ch.dvbern.stip.berechnung.domain.type.PersonenHaushalt getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(ch.dvbern.stip.berechnung.domain.type.PersonenHaushalt typ) {
    this.typ = typ;
  }

  /**
   **/
  public PersonenHaushaltGruppeDto names(List<String> names) {
    this.names = names;
    return this;
  }

  
  @JsonProperty("names")
  @NotNull
  public List<String> getNames() {
    return names;
  }

  @JsonProperty("names")
  public void setNames(List<String> names) {
    this.names = names;
  }

  public PersonenHaushaltGruppeDto addNamesItem(String namesItem) {
    if (this.names == null) {
      this.names = new ArrayList<>();
    }

    this.names.add(namesItem);
    return this;
  }

  public PersonenHaushaltGruppeDto removeNamesItem(String namesItem) {
    if (namesItem != null && this.names != null) {
      this.names.remove(namesItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonenHaushaltGruppeDto personenHaushaltGruppe = (PersonenHaushaltGruppeDto) o;
    return Objects.equals(this.typ, personenHaushaltGruppe.typ) &&
        Objects.equals(this.names, personenHaushaltGruppe.names);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typ, names);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonenHaushaltGruppeDto {\n");
    
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    names: ").append(toIndentedString(names)).append("\n");
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

