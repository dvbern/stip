package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DarlehenStatus
 */
public enum DarlehenStatusDto {
  
  IN_BEARBEITUNG_GS("IN_BEARBEITUNG_GS"),
  
  EINGEGEBEN("EINGEGEBEN"),
  
  IN_FREIGABE("IN_FREIGABE"),
  
  AKZEPTIERT("AKZEPTIERT"),
  
  ABGELEHNT("ABGELEHNT");

  private String value;

  DarlehenStatusDto(String value) {
    this.value = value;
  }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static DarlehenStatusDto fromString(String s) {
      for (DarlehenStatusDto b : DarlehenStatusDto.values()) {
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
  public static DarlehenStatusDto fromValue(String value) {
    for (DarlehenStatusDto b : DarlehenStatusDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


