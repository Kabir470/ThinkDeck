package db;

import org.bson.types.ObjectId;
import java.util.Date;

public class Message {
    private final ObjectId id;
    private final ObjectId senderId;
    private final ObjectId receiverId;
    private final String content;
    private final Date timestamp;

    public Message(ObjectId senderId, ObjectId receiverId, String content) {
        this.id = new ObjectId();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = new Date();
    }

    // Getters
    public ObjectId getId() {
        return id;
    }

    public ObjectId getSenderId() {
        return senderId;
    }

    public ObjectId getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
