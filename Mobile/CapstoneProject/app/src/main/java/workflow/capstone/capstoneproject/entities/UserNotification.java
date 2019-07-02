package workflow.capstone.capstoneproject.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserNotification implements Serializable {
    @SerializedName("eventID")
    @Expose
    private String eventID;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("actorName")
    @Expose
    private String actorName;
    @SerializedName("notificationType")
    @Expose
    private Integer notificationType;
    @SerializedName("notificationTypeName")
    @Expose
    private String notificationTypeName;
    @SerializedName("isHandled")
    @Expose
    private Boolean isHandled;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationTypeName() {
        return notificationTypeName;
    }

    public void setNotificationTypeName(String notificationTypeName) {
        this.notificationTypeName = notificationTypeName;
    }

    public Boolean getIsHandled() {
        return isHandled;
    }

    public void setIsHandled(Boolean isHandled) {
        this.isHandled = isHandled;
    }
}
