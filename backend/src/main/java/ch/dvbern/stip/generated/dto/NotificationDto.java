package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Notification for a Gesuch
 **/

@JsonTypeName("Notification")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class NotificationDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.notification.type.NotificationType notificationType;
  private @Valid UUID gesuchId;
  private @Valid String userErstellt;
  private @Valid LocalDate timestampErstellt;

  /**
   **/
  public NotificationDto notificationType(ch.dvbern.stip.api.notification.type.NotificationType notificationType) {
    this.notificationType = notificationType;
    return this;
  }

  
  @JsonProperty("notificationType")
  @NotNull
  public ch.dvbern.stip.api.notification.type.NotificationType getNotificationType() {
    return notificationType;
  }

  @JsonProperty("notificationType")
  public void setNotificationType(ch.dvbern.stip.api.notification.type.NotificationType notificationType) {
    this.notificationType = notificationType;
  }

  /**
   **/
  public NotificationDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty("gesuchId")
  @NotNull
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public NotificationDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  @NotNull
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public NotificationDto timestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationDto notification = (NotificationDto) o;
    return Objects.equals(this.notificationType, notification.notificationType) &&
        Objects.equals(this.gesuchId, notification.gesuchId) &&
        Objects.equals(this.userErstellt, notification.userErstellt) &&
        Objects.equals(this.timestampErstellt, notification.timestampErstellt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificationType, gesuchId, userErstellt, timestampErstellt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDto {\n");
    
    sb.append("    notificationType: ").append(toIndentedString(notificationType)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
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
