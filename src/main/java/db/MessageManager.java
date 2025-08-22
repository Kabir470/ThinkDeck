package db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageManager {

    public static void sendMessage(ObjectId senderId, ObjectId receiverId, String content) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> messages = db.getCollection("messages");

        Document newMessage = new Document("senderId", senderId)
                .append("receiverId", receiverId)
                .append("content", content)
                .append("timestamp", new Date());
        messages.insertOne(newMessage);
    }

    public static List<Message> getMessagesForUser(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> messages = db.getCollection("messages");
        List<Message> userMessages = new ArrayList<>();

        messages.find(Filters.eq("receiverId", userId))
                .sort(Sorts.descending("timestamp"))
                .forEach(doc -> {
                    Message msg = new Message(
                            doc.getObjectId("senderId"),
                            doc.getObjectId("receiverId"),
                            doc.getString("content"));
                    userMessages.add(msg);
                });
        return userMessages;
    }

    public static List<Message> getConversation(ObjectId user1Id, ObjectId user2Id) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> messages = db.getCollection("messages");
        List<Message> conversation = new ArrayList<>();

        messages.find(
                Filters.or(
                        Filters.and(Filters.eq("senderId", user1Id), Filters.eq("receiverId", user2Id)),
                        Filters.and(Filters.eq("senderId", user2Id), Filters.eq("receiverId", user1Id))))
                .sort(Sorts.ascending("timestamp"))
                .forEach(doc -> {
                    Message msg = new Message(
                            doc.getObjectId("senderId"),
                            doc.getObjectId("receiverId"),
                            doc.getString("content"));
                    conversation.add(msg);
                });
        return conversation;
    }

    public static void deleteAllMessagesForUser(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> messages = db.getCollection("messages");
        messages.deleteMany(Filters.or(
                Filters.eq("senderId", userId),
                Filters.eq("receiverId", userId)));
    }
}
