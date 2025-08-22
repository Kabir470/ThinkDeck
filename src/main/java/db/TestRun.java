package db;

import db.UserManager;
import db.FlashcardManager;

import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.List;

public class TestRun {
    public static void main(String[] args) {
        UserManager.insertUser("Farhad", "rixonahmed@gmail.com", "1234", "Dhaka mirpur 12");

        Document user = UserManager.loginUser("Farhad", "1234");

        if (user != null) {
            System.out.println("Login success: " + user.getString("username"));
            ObjectId userId = user.getObjectId("_id");

            List<Document> cards = FlashcardManager.getFlashcards(userId);
            for (Document card : cards) {
                System.out.println("Q: " + card.getString("question"));
                System.out.println("A: " + card.getString("answer"));
                System.out.println("---");
            }
        } else {
            System.out.println("Login failed.");
        }
    }
}
