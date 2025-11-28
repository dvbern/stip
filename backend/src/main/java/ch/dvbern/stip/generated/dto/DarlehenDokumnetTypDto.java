package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DarlehenDokumnetTyp
 */
public enum DarlehenDokumnetTypDto {
  
  BETREIBUNGS_AUSZUG("BETREIBUNGS_AUSZUG"),
  
  AUFSTELLUNG_KOSTEN_ELTERN("AUFSTELLUNG_KOSTEN_ELTERN"),
  
  KOPIE_SCHULGELDRECHNUNG("KOPIE_SCHULGELDRECHNUNG"),
  
  BELEGE_ANSCHAFFUNGEN("BELEGE_ANSCHAFFUNGEN");

  private String value;

  DarlehenDokumnetTypDto(String value) {
    this.value = value;
  }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static DarlehenDokumnetTypDto fromString(String s) {
      for (DarlehenDokumnetTypDto b : DarlehenDokumnetTypDto.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
	}
	
  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DarlehenDokumnetTypDto fromValue(String value) {
    for (DarlehenDokumnetTypDto b : DarlehenDokumnetTypDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


