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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class NotificationDto  implements Serializable {
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.notification.type.NotificationType notificationType;
  private @Valid UUID fallId;
  private @Valid String userErstellt;
  private @Valid LocalDate timestampErstellt;
  private @Valid String absender;
  private @Valid Boolean read;
  private @Valid String notificationText;
  private @Valid UUID contextId;

  protected NotificationDto(NotificationDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.notificationType = b.notificationType;
    this.fallId = b.fallId;
    this.userErstellt = b.userErstellt;
    this.timestampErstellt = b.timestampErstellt;
    this.absender = b.absender;
    this.read = b.read;
    this.notificationText = b.notificationText;
    this.contextId = b.contextId;
  }

  public NotificationDto() {
  }

  /**
   **/
  public NotificationDto id(UUID id) {
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
  public NotificationDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
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
  @NotNull
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public NotificationDto absender(String absender) {
    this.absender = absender;
    return this;
  }

  
  @JsonProperty("absender")
  @NotNull
  public String getAbsender() {
    return absender;
  }

  @JsonProperty("absender")
  public void setAbsender(String absender) {
    this.absender = absender;
  }

  /**
   **/
  public NotificationDto read(Boolean read) {
    this.read = read;
    return this;
  }

  
  @JsonProperty("read")
  @NotNull
  public Boolean getRead() {
    return read;
  }

  @JsonProperty("read")
  public void setRead(Boolean read) {
    this.read = read;
  }

  /**
   **/
  public NotificationDto notificationText(String notificationText) {
    this.notificationText = notificationText;
    return this;
  }

  
  @JsonProperty("notificationText")
  public String getNotificationText() {
    return notificationText;
  }

  @JsonProperty("notificationText")
  public void setNotificationText(String notificationText) {
    this.notificationText = notificationText;
  }

  /**
   **/
  public NotificationDto contextId(UUID contextId) {
    this.contextId = contextId;
    return this;
  }

  
  @JsonProperty("contextId")
  public UUID getContextId() {
    return contextId;
  }

  @JsonProperty("contextId")
  public void setContextId(UUID contextId) {
    this.contextId = contextId;
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
    return Objects.equals(this.id, notification.id) &&
        Objects.equals(this.notificationType, notification.notificationType) &&
        Objects.equals(this.fallId, notification.fallId) &&
        Objects.equals(this.userErstellt, notification.userErstellt) &&
        Objects.equals(this.timestampErstellt, notification.timestampErstellt) &&
        Objects.equals(this.absender, notification.absender) &&
        Objects.equals(this.read, notification.read) &&
        Objects.equals(this.notificationText, notification.notificationText) &&
        Objects.equals(this.contextId, notification.contextId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, notificationType, fallId, userErstellt, timestampErstellt, absender, read, notificationText, contextId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    notificationType: ").append(toIndentedString(notificationType)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    absender: ").append(toIndentedString(absender)).append("\n");
    sb.append("    read: ").append(toIndentedString(read)).append("\n");
    sb.append("    notificationText: ").append(toIndentedString(notificationText)).append("\n");
    sb.append("    contextId: ").append(toIndentedString(contextId)).append("\n");
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


  public static NotificationDtoBuilder<?, ?> builder() {
    return new NotificationDtoBuilderImpl();
  }

  private static final class NotificationDtoBuilderImpl extends NotificationDtoBuilder<NotificationDto, NotificationDtoBuilderImpl> {

    @Override
    protected NotificationDtoBuilderImpl self() {
      return this;
    }

    @Override
    public NotificationDto build() {
      return new NotificationDto(this);
    }
  }

  public static abstract class NotificationDtoBuilder<C extends NotificationDto, B extends NotificationDtoBuilder<C, B>>  {
    private UUID id;
    private ch.dvbern.stip.api.notification.type.NotificationType notificationType;
    private UUID fallId;
    private String userErstellt;
    private LocalDate timestampErstellt;
    private String absender;
    private Boolean read;
    private String notificationText;
    private UUID contextId;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B notificationType(ch.dvbern.stip.api.notification.type.NotificationType notificationType) {
      this.notificationType = notificationType;
      return self();
    }
    public B fallId(UUID fallId) {
      this.fallId = fallId;
      return self();
    }
    public B userErstellt(String userErstellt) {
      this.userErstellt = userErstellt;
      return self();
    }
    public B timestampErstellt(LocalDate timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
    public B absender(String absender) {
      this.absender = absender;
      return self();
    }
    public B read(Boolean read) {
      this.read = read;
      return self();
    }
    public B notificationText(String notificationText) {
      this.notificationText = notificationText;
      return self();
    }
    public B contextId(UUID contextId) {
      this.contextId = contextId;
      return self();
    }
  }
}

