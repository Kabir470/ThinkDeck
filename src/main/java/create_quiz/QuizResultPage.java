package create_quiz;

import Utils.UIUtils;
import dashboard.Dashboard;
import db.QuizManager;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizResultPage extends JFrame {
    private final String userId;

    public QuizResultPage(String userId, String subject, List<AiQuizQuestion> questions, List<Integer> userAnswers,
            int score) {
        this.userId = userId;

        // Save the quiz result
        QuizManager.saveQuizResult(new ObjectId(userId), subject, score, questions.size(), "AI Quiz");

        setTitle("Quiz Results: " + subject);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Quiz Results: " + subject, SwingConstants.LEFT);
        titleLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(Color.BLACK);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel scoreLabel = new JLabel("Your Score: " + score + "/" + questions.size(), SwingConstants.RIGHT);
        scoreLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 24f));
        scoreLabel.setForeground(Color.BLACK);
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Results Panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        for (int i = 0; i < questions.size(); i++) {
            AiQuizQuestion q = questions.get(i);
            int userAnswerIndex = userAnswers.get(i);
            int correctAnswerIndex = q.getCorrectOptionIndex();
            boolean isCorrect = userAnswerIndex == correctAnswerIndex;

            JPanel questionResultPanel = new JPanel();
            questionResultPanel.setLayout(new BoxLayout(questionResultPanel, BoxLayout.Y_AXIS));
            questionResultPanel.setBackground(isCorrect ? new Color(40, 80, 40) : new Color(80, 40, 40));
            questionResultPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_OUTLINE),
                    new EmptyBorder(15, 15, 15, 15)));

            JLabel qLabel = new JLabel(
                    "<html><body style='width: 600px;'><b>Q" + (i + 1) + ":</b> " + q.getQuestion() + "</body></html>");
            qLabel.setFont(UIUtils.FONT_GENERAL_UI);
            qLabel.setForeground(Color.WHITE);
            questionResultPanel.add(qLabel);

            questionResultPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            String yourAnswer = "Your answer: " + q.getOptions().get(userAnswerIndex)
                    + (isCorrect ? " (Correct)" : " (Incorrect)");
            JLabel yourAnswerLabel = new JLabel(yourAnswer);
            yourAnswerLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.ITALIC));
            yourAnswerLabel.setForeground(isCorrect ? Color.GREEN : Color.PINK);
            questionResultPanel.add(yourAnswerLabel);

            if (!isCorrect) {
                String correctAnswer = "Correct answer: " + q.getOptions().get(correctAnswerIndex);
                JLabel correctAnswerLabel = new JLabel(correctAnswer);
                correctAnswerLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.ITALIC));
                correctAnswerLabel.setForeground(Color.CYAN);
                questionResultPanel.add(correctAnswerLabel);
            }

            resultsPanel.add(questionResultPanel);
        }

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(UIUtils.FONT_GENERAL_UI);
        backButton.addActionListener(e -> backToDashboard());
        footerPanel.add(backButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void backToDashboard() {
        this.dispose();
        new Dashboard(userId).setVisible(true);
    }
}
