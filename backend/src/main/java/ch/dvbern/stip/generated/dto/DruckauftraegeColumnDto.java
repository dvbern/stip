package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DruckauftraegeColumn
 */
public enum DruckauftraegeColumnDto {
  
  BATCH_NAME("BATCH_NAME"),
  
  BEARBEITER("BEARBEITER"),
  
  ERSTELLT_AM("ERSTELLT_AM"),
  
  STATUS("STATUS"),
  
  TYP("TYP");

  private String value;

  DruckauftraegeColumnDto(String value) {
    this.value = value;
  }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static DruckauftraegeColumnDto fromString(String s) {
      for (DruckauftraegeColumnDto b : DruckauftraegeColumnDto.values()) {
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
  public static DruckauftraegeColumnDto fromValue(String value) {
    for (DruckauftraegeColumnDto b : DruckauftraegeColumnDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


