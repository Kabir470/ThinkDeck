package db;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class UserManager {

    public static void insertUser(String username, String email, String password, String location) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");

        Document newUser = new Document("username", username)
                .append("email", email)
                .append("password", password)
                .append("location", location);
        users.insertOne(newUser);
        System.out.println("User added: " + username);
    }

    public static Document loginUser(String username, String password) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");

        return users.find(Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", password))).first();
    }

    public static Document getUserById(org.bson.types.ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");
        return users.find(Filters.eq("_id", userId)).first();
    }

    public static void setUserHiddenStatus(org.bson.types.ObjectId userId, boolean isHidden) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");
        Document update = new Document("$set", new Document("isHidden", isHidden));
        users.updateOne(Filters.eq("_id", userId), update);
    }

    public static void setUserMessagePreference(org.bson.types.ObjectId userId, boolean canReceive) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");
        Document update = new Document("$set", new Document("canReceiveMessages", canReceive));
        users.updateOne(Filters.eq("_id", userId), update);
    }

    public static void deleteUser(org.bson.types.ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");
        users.deleteOne(Filters.eq("_id", userId));
    }

    public static void updateUser(org.bson.types.ObjectId userId, String username, String email, String location) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");

        Document update = new Document("$set", new Document("username", username)
                .append("email", email)
                .append("location", location));

        users.updateOne(Filters.eq("_id", userId), update);
    }
}
