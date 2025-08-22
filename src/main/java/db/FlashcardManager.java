package db;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.HashSet;
import java.util.Set;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {

    public static void addFlashcard(ObjectId userId, String subject, String question, String answer) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");

        Document card = new Document("userId", userId)
                .append("subject", subject)
                .append("question", question)
                .append("answer", answer);
        flashcards.insertOne(card);
        System.out.println("Flashcard added.");
    }

    public static boolean hasEnough(ObjectId userId, String subject, int minCount) {
        List<Document> flashcards = getFlashcards(userId, subject);
        return flashcards != null && flashcards.size() >= minCount;
    }

    // Helper method to get flashcards by subject is no longer needed,
    // as getFlashcards(userId, subject) is more specific.

    public static List<Document> getFlashcards(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");

        FindIterable<Document> docs = flashcards.find(Filters.eq("userId", userId));
        List<Document> result = new ArrayList<>();
        for (Document d : docs)
            result.add(d);
        return result;
    }

    // ...

    public static Set<String> getAllSubjects(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");
        DistinctIterable<String> subjects = flashcards.distinct("subject", Filters.eq("userId", userId), String.class);
        Set<String> result = new HashSet<>();
        for (String s : subjects) {
            result.add(s);
        }
        return result;
    }

    // New: Get flashcards for a user and subject
    public static List<Document> getFlashcards(ObjectId userId, String subject) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");
        FindIterable<Document> docs = flashcards.find(Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("subject", subject)));
        List<Document> result = new ArrayList<>();
        for (Document d : docs)
            result.add(d);
        return result;
    }

    public static void deleteAllFlashcardsForUser(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> flashcards = db.getCollection("flashcards");
        flashcards.deleteMany(Filters.eq("userId", userId));
    }
}
