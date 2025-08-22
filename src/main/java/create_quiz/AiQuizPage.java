package create_quiz;

import Utils.AiHelper;
import Utils.UIUtils;
import dashboard.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AiQuizPage extends JFrame {
    private final String userId;
    private final String subject;
    private List<AiQuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private final List<Integer> userAnswers = new ArrayList<>();

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton nextButton;
    private JButton backButton;

    public AiQuizPage(String userId, String subject) {
        this.userId = userId;
        this.subject = subject;

        setTitle("AI Generated Quiz: " + subject);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout(20, 20));

        initUI();
        generateQuestions();
    }

    private void initUI() {
        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JLabel titleLabel = new JLabel("AI Quiz: " + subject);
        titleLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(contentPanel, BorderLayout.CENTER);

        // Question Label
        questionLabel = new JLabel("Generating questions...", SwingConstants.CENTER);
        questionLabel.setFont(UIUtils.FONT_GENERAL_UI);
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(questionLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        optionsPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(UIUtils.FONT_GENERAL_UI);
            optionButtons[i].setBackground(UIUtils.COLOR_BACKGROUND);
            optionButtons[i].setForeground(Color.WHITE);
            buttonGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        contentPanel.add(optionsPanel);

        // Controls Panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        backButton = new JButton("Back to Dashboard");
        backButton.setFont(UIUtils.FONT_GENERAL_UI);
        backButton.addActionListener(e -> backToDashboard());
        controlsPanel.add(backButton);

        nextButton = new JButton("Next");
        nextButton.setFont(UIUtils.FONT_GENERAL_UI);
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> handleNextButton());
        controlsPanel.add(nextButton);

        add(controlsPanel, BorderLayout.SOUTH);
    }

    private void generateQuestions() {
        SwingWorker<List<AiQuizQuestion>, Void> worker = new SwingWorker<List<AiQuizQuestion>, Void>() {
            @Override
            protected List<AiQuizQuestion> doInBackground() {
                return AiHelper.generateQuiz(subject);
            }

            @Override
            protected void done() {
                try {
                    questions = get();
                    if (questions != null && !questions.isEmpty()) {
                        displayQuestion();
                        nextButton.setEnabled(true);
                    } else {
                        questionLabel.setText("Failed to generate quiz. Please try again.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    questionLabel.setText("An error occurred while generating the quiz.");
                }
            }
        };
        worker.execute();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            AiQuizQuestion q = questions.get(currentQuestionIndex);
            questionLabel.setText("<html><body style='width: 500px;'>" + (currentQuestionIndex + 1) + ". "
                    + q.getQuestion() + "</body></html>");
            List<String> options = q.getOptions();
            for (int i = 0; i < options.size(); i++) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setVisible(true);
            }
            for (int i = options.size(); i < 4; i++) {
                optionButtons[i].setVisible(false);
            }
            buttonGroup.clearSelection();
        } else {
            showFinalScore();
        }
    }

    private void handleNextButton() {
        int selectedOption = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedOption = i;
                break;
            }
        }

        if (selectedOption == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer.", "No Answer Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedOption == questions.get(currentQuestionIndex).getCorrectOptionIndex()) {
            score++;
        }
        userAnswers.add(selectedOption);

        currentQuestionIndex++;
        displayQuestion();
    }

    private void showFinalScore() {
        this.dispose();
        new QuizResultPage(userId, subject, questions, userAnswers, score).setVisible(true);
    }

    private void backToDashboard() {
        this.dispose();
        new Dashboard(userId).setVisible(true);
    }
}
