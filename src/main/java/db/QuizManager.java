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

public class QuizManager {

    public static void saveQuizResult(ObjectId userId, String subject, int score, int totalQuestions, String quizType) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> quizResults = db.getCollection("quizResults");

        Document result = new Document("userId", userId)
                .append("subject", subject)
                .append("score", score)
                .append("totalQuestions", totalQuestions)
                .append("quizType", quizType)
                .append("date", new Date());

        quizResults.insertOne(result);
    }

    public static List<Document> getQuizHistory(ObjectId userId) {
        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> quizResults = db.getCollection("quizResults");

        return quizResults.find(Filters.eq("userId", userId))
                .sort(Sorts.descending("date"))
                .into(new ArrayList<>());
    }
}
