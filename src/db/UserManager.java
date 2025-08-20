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
}
