package db;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {

    public static void addFlashcard(ObjectId userId, String question, String answer) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");

        Document card = new Document("userId", userId)
                .append("question", question)
                .append("answer", answer);
        flashcards.insertOne(card);
        System.out.println("Flashcard added.");
    }

    public static List<Document> getFlashcards(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");

        FindIterable<Document> docs = flashcards.find(Filters.eq("userId", userId));
        List<Document> result = new ArrayList<>();
        for (Document d : docs)
            result.add(d);
        return result;
    }
}
