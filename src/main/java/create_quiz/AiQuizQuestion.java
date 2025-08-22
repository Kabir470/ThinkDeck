package create_quiz;

import java.util.List;

public class AiQuizQuestion {
    private final String question;
    private final List<String> options;
    private final int correctOptionIndex;

    public AiQuizQuestion(String question, List<String> options, int correctOptionIndex) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}
