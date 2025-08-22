package create_quiz;

import db.FlashcardManager;
import org.bson.Document;
import org.bson.types.ObjectId;
import create_flashcard.Flashcard;
import component.Toaster;
import Utils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class QuizPage extends JFrame {

    private final String subject;
    private final String userId;
    private final ObjectId userObjectId;
    private final List<Flashcard> questions;
    private final JPanel panel;
    private final Toaster toaster;
    private Flashcard currentQuestion;
    private int currentIndex = 0;
    private int score = 0;

    public QuizPage(String subject, String userId) {
        this.subject = subject;
        this.userId = userId;
        this.userObjectId = new ObjectId(userId);
        this.questions = new ArrayList<>();

        // Load flashcards for this user and subject from MongoDB
        List<Document> loadedDocs = FlashcardManager.getFlashcards(userObjectId, subject);
        if (loadedDocs != null) {
            for (Document doc : loadedDocs) {
                String question = doc.getString("question");
                String answer = doc.getString("answer");
                questions.add(new Flashcard(question, answer));
            }
        }
        Collections.shuffle(questions);

        setTitle("Quiz - " + subject);
        setSize(800, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 800, 500);
        panel.setBackground(UIUtils.COLOR_BACKGROUND);
        add(panel);

        toaster = new Toaster(panel);
        addWindowControls();

        if (questions.isEmpty()) {
            showNoCardsMessage();
            return;
        }

        loadNextQuestion();
    }

    private void addWindowControls() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);
        controlPanel.setBounds(0, 0, 800, 50);

        // Left panel for back button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        JButton backButton = createControlButton("←", new Color(70, 130, 180));
        backButton.setToolTipText("Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
            new dashboard.Dashboard(userId).setVisible(true);
        });
        leftPanel.add(backButton);

        // Right panel for min/close
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        JButton minimizeButton = createControlButton("—", new Color(100, 100, 100));
        minimizeButton.addActionListener(unused -> setState(Frame.ICONIFIED));

        JButton closeButton = createControlButton("×", new Color(200, 70, 70));
        closeButton.addActionListener(unused -> dispose());

        rightPanel.add(minimizeButton);
        rightPanel.add(closeButton);

        controlPanel.add(leftPanel, BorderLayout.WEST);
        controlPanel.add(rightPanel, BorderLayout.EAST);
        panel.add(controlPanel);
    }

    private JButton createControlButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(text)) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(text, x, y);
            }
        };

        button.setPreferredSize(new Dimension(30, 30));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);

        return button;
    }

    private void showNoCardsMessage() {
        panel.removeAll();

        JLabel message = new JLabel("No flashcards available for " + subject, SwingConstants.CENTER);
        message.setFont(UIUtils.FONT_GENERAL_UI);
        message.setForeground(Color.WHITE);
        message.setBounds(0, 150, 800, 40);
        panel.add(message);

        panel.add(createActionButton("Back to Dashboard", 250, () -> {
            dispose();
            new dashboard.Dashboard(userId); // Pass userId to Dashboard
        }));

        panel.repaint();
    }

    private void loadNextQuestion() {
        panel.removeAll();
        addWindowControls();

        if (currentIndex >= questions.size()) {
            showQuizCompleted();
            return;
        }

        currentQuestion = questions.get(currentIndex);
        createQuestionUI();
        panel.repaint();
    }

    private void createQuestionUI() {
        // Question label
        JLabel qLabel = new JLabel("<html><div style='text-align:center;'>Q" + (currentIndex + 1) + ": "
                + currentQuestion.getQuestion() + "</div></html>", SwingConstants.CENTER);
        qLabel.setFont(UIUtils.FONT_GENERAL_UI);
        qLabel.setForeground(Color.WHITE);
        qLabel.setBounds(50, 80, 700, 60);
        panel.add(qLabel);

        // Score display
        JLabel scoreLabel = new JLabel("Score: " + score + "/" + questions.size(), SwingConstants.RIGHT);
        scoreLabel.setFont(UIUtils.FONT_GENERAL_UI);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(600, 20, 150, 30);
        panel.add(scoreLabel);

        // Generate and display options
        List<String> options = generateOptions(currentQuestion.getAnswer());
        int y = 160;
        for (String opt : options) {
            panel.add(createOptionButton(opt, y));
            y += 60;
        }
    }

    private JLabel createOptionButton(String optionText, int yPos) {
        JLabel option = new JLabel("<html><div style='text-align:center;padding:10px;'>" + optionText + "</div></html>",
                SwingConstants.CENTER);
        option.setFont(UIUtils.FONT_GENERAL_UI);
        option.setOpaque(true);
        option.setBackground(new Color(66, 133, 244));
        option.setForeground(Color.WHITE);
        option.setBounds(250, yPos, 300, 50);
        option.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        option.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        option.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent unused) {
                handleAnswerSelection(optionText);
            }

            @Override
            public void mouseEntered(MouseEvent unused) {
                option.setBackground(new Color(51, 103, 214));
            }

            @Override
            public void mouseExited(MouseEvent unused) {
                option.setBackground(new Color(66, 133, 244));
            }
        });

        return option;
    }

    private void handleAnswerSelection(String selectedAnswer) {
        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            score++;
            toaster.success("Correct!");
        } else {
            toaster.error("Wrong! Correct: " + currentQuestion.getAnswer());
        }
        currentIndex++;

        Timer timer = new Timer(1000, unused -> loadNextQuestion());
        timer.setRepeats(false);
        timer.start();
    }

    private List<String> generateOptions(String correct) {
        Set<String> allAnswers = new HashSet<>();
        // Get all flashcards for this user and subject from DB
        List<Document> allDocs = FlashcardManager.getFlashcards(userObjectId, subject);
        if (allDocs != null) {
            for (Document doc : allDocs) {
                String ans = doc.getString("answer");
                if (!ans.equals(correct) && allAnswers.size() < 3) {
                    allAnswers.add(ans);
                }
            }
        }

        List<String> options = new ArrayList<>(allAnswers);
        options.add(correct);

        while (options.size() < 4) {
            options.add("Sample Answer " + (options.size() + 1));
        }

        Collections.shuffle(options);
        return options;
    }

    private void showQuizCompleted() {
        JLabel done = new JLabel(
                "<html><div style='text-align:center;'>Quiz Completed!<br>Final Score: " + score + "/"
                        + questions.size() + "</div></html>",
                SwingConstants.CENTER);
        done.setFont(new Font("Segoe UI", Font.BOLD, 24));
        done.setForeground(Color.WHITE);
        done.setBounds(0, 150, 800, 100);
        panel.add(done);

        panel.add(createActionButton("Restart Quiz", 250, () -> {
            currentIndex = 0;
            score = 0;
            Collections.shuffle(questions);
            loadNextQuestion();
        }));

        panel.add(createActionButton("Back to Dashboard", 350, () -> {
            dispose();
            new dashboard.Dashboard(userId); // Pass userId to Dashboard
        }));

        panel.repaint();
    }

    private JButton createActionButton(String text, int y, Runnable action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_INTERACTIVE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(text)) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(text, x, y);
            }
        };

        button.setBounds(300, y, 200, 44);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(unused -> action.run());

        return button;
    }
}