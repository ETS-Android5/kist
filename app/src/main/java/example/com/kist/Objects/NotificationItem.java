package example.com.kist.Objects;

/**
 * Created by pr0 on 9/8/17.
 */

public class NotificationItem {
    String msgID, userId, senderId, message, type, image, name, time;

    public NotificationItem() {

    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }

    public String getMsgID() {
        return msgID;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
