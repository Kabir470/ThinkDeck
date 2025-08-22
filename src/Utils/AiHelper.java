package Utils;

import create_flashcard.Flashcard;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AiHelper {

    private static final Map<String, List<Flashcard>> knowledgeBase = new HashMap<>();

    static {
        // Populate with some data for different subjects
        List<Flashcard> oopCards = new ArrayList<>();
        oopCards.add(new Flashcard("What is Encapsulation?",
                "Bundling data and methods that operate on the data into a single unit."));
        oopCards.add(new Flashcard("What is Polymorphism?", "The ability of an object to take on many forms."));
        oopCards.add(
                new Flashcard("What is Inheritance?", "A mechanism where a new class derives from an existing class."));
        knowledgeBase.put("OOP", oopCards);

        List<Flashcard> cppCards = new ArrayList<>();
        cppCards.add(new Flashcard("What is a pointer in C++?",
                "A variable that stores the memory address of another variable."));
        cppCards.add(new Flashcard("What is RAII?", "Resource Acquisition Is Initialization. A programming idiom."));
        knowledgeBase.put("C++", cppCards);

        List<Flashcard> dbCards = new ArrayList<>();
        dbCards.add(new Flashcard("What is a Primary Key?",
                "A constraint that uniquely identifies each record in a table."));
        dbCards.add(new Flashcard("What is a Foreign Key?", "A key used to link two tables together."));
        knowledgeBase.put("Database", dbCards);

        List<Flashcard> mlCards = new ArrayList<>();
        mlCards.add(new Flashcard("What is Supervised Learning?",
                "A type of machine learning where the model learns from labeled data."));
        mlCards.add(new Flashcard("What is Unsupervised Learning?",
                "A type of machine learning where the model learns from unlabeled data."));
        knowledgeBase.put("Machine Learning", mlCards);
    }

    public static List<Flashcard> generateFlashcards(String topic) {
        // Simulate network delay
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return knowledgeBase.getOrDefault(topic, new ArrayList<>());
    }

    public static String getAnswer(String question) {
        // Simulate network delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simple keyword matching for simulation
        String qLower = question.toLowerCase();
        for (List<Flashcard> cards : knowledgeBase.values()) {
            for (Flashcard card : cards) {
                if (card.getQuestion().toLowerCase().contains(qLower)
                        || qLower.contains(card.getQuestion().toLowerCase())) {
                    return card.getAnswer();
                }
            }
        }

        return "I'm sorry, I don't have an answer for that. I am a simulated AI with a limited knowledge base.";
    }
}
