package create_flashcard;

import java.util.*;

public class FlashcardStorage {
    private static final Map<String, List<Flashcard>> flashcardStore = new HashMap<>();

    // Initialize with default subjects if empty

    static {
        initializeDefaultSubjects();
    }

    private static void initializeDefaultSubjects() {
        if (flashcardStore.isEmpty()) {
            String[] defaultSubjects = {
                    "Machine Learning",
                    "C++",
                    "English",
                    "OOP",
                    "Algorithms",
                    "Database"
            };

            for (String subject : defaultSubjects) {
                flashcardStore.put(subject, new ArrayList<>());
            }
        }
    }

    public static void addCard(String subject, Flashcard card) {
        flashcardStore.putIfAbsent(subject, new ArrayList<>());
        flashcardStore.get(subject).add(card);
    }

    public static void addSubject(String subject) {
        flashcardStore.putIfAbsent(subject, new ArrayList<>());
    }

    public static List<Flashcard> getCards(String subject) {
        return flashcardStore.getOrDefault(subject, new ArrayList<>());
    }

    public static boolean hasEnough(String subject, int min) {
        return getCards(subject).size() >= min;
    }

    public static Set<String> getAllSubjects() {
        return flashcardStore.keySet();
    }

    public static boolean subjectExists(String subject) {
        return flashcardStore.containsKey(subject);
    }
}