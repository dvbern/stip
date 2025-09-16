package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DruckauftragStatus
 */
public enum DruckauftragStatusDto {
  
  IN_GENERIERUNG("IN_GENERIERUNG"),
  
  VERSANDBEREIT("VERSANDBEREIT"),
  
  FEHLERHAFTE_GENERIERUNG("FEHLERHAFTE_GENERIERUNG"),
  
  ARCHIVIERT("ARCHIVIERT");

  private String value;

  DruckauftragStatusDto(String value) {
    this.value = value;
  }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static DruckauftragStatusDto fromString(String s) {
      for (DruckauftragStatusDto b : DruckauftragStatusDto.values()) {
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
  public static DruckauftragStatusDto fromValue(String value) {
    for (DruckauftragStatusDto b : DruckauftragStatusDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


