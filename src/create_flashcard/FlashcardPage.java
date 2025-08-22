package create_flashcard;

import create_flashcard.Flashcard;
import db.FlashcardManager;
import org.bson.Document;
import org.bson.types.ObjectId;
import Utils.*;
import component.Toaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class FlashcardPage extends JFrame {
    // private final Map<String, List<Flashcard>> flashcardStore = new HashMap<>();
    private final JPanel displayPanel;
    private final Toaster toaster;
    private final String subject;
    private final String userId;
    private final ObjectId userObjectId;

    public FlashcardPage(String subject, String userId) {
        this.subject = subject;
        this.userId = userId;
        this.userObjectId = new ObjectId(userId);

        setTitle("Flashcards - " + subject);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(UIUtils.COLOR_BACKGROUND);
        panel.setBounds(0, 0, 800, 500);
        add(panel);

        // marked

        JLabel backBtn = new JLabel("< Back", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(new Color(70, 130, 180)); // Steel blue
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
                FontMetrics metrics = g2.getFontMetrics(getFont());
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), x, y);
            }
        };

        backBtn.setFont(UIUtils.FONT_GENERAL_UI);
        backBtn.setBounds(20, 440, 80, 30);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setOpaque(false);

        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new dashboard.Dashboard(userId);
                dispose();
            }
        });

        panel.add(backBtn);

        toaster = new Toaster(panel);

        addTitle(panel);
        addForm(panel);

        displayPanel = new JPanel();
        displayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        displayPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        displayPanel.setBounds(50, 220, 700, 220);
        panel.add(displayPanel);

        addCloseButton(panel);
        setVisible(true);
    }

    private void addTitle(JPanel panel) {
        JLabel title = new JLabel("Flashcards: " + subject, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 20, 800, 40);
        panel.add(title);
    }

    private void addForm(JPanel panel) {

        // question field

        JTextField questionField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
                super.paintComponent(g2);

            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_OUTLINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
            }
        };

        questionField.setFont(UIUtils.FONT_GENERAL_UI);
        questionField.setForeground(Color.WHITE);
        questionField.setBackground(UIUtils.COLOR_BACKGROUND);
        questionField.setCaretColor(Color.WHITE);
        questionField.setBounds(100, 80, 600, 40);
        questionField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(questionField);

        // answer field

        JTextField answerField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
                super.paintComponent(g2);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_OUTLINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
            }
        };
        answerField.setFont(UIUtils.FONT_GENERAL_UI);
        answerField.setForeground(Color.WHITE);
        answerField.setBackground(UIUtils.COLOR_BACKGROUND);
        answerField.setCaretColor(Color.WHITE);
        answerField.setBounds(100, 130, 600, 40);
        answerField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(answerField);

        // add flashcard button field

        JLabel addBtn = new JLabel("Add Flashcard", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                g2.setColor(UIUtils.COLOR_INTERACTIVE);
                g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
                FontMetrics metrics = g2.getFontMetrics(UIUtils.FONT_GENERAL_UI);
                int x2 = (getWidth() - metrics.stringWidth("Add Flashcard")) / 2;
                int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                g2.setColor(Color.WHITE);
                g2.drawString("Add Flashcard", x2, y2);
            }
        };

        addBtn.setFont(UIUtils.FONT_GENERAL_UI);
        addBtn.setBounds(300, 180, 200, 44);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String question = questionField.getText().trim();
                String answer = answerField.getText().trim();
                if (question.isEmpty() || answer.isEmpty()) {
                    toaster.warn("Fill both fields");
                    return;
                }

                // Save flashcard to MongoDB
                FlashcardManager.addFlashcard(userObjectId, subject, question, answer);
                toaster.success("Flashcard added!");
                questionField.setText("");
                answerField.setText("");
                renderCards();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                addBtn.setForeground(UIUtils.OFFWHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addBtn.setForeground(Color.WHITE);
            }
        });

        panel.add(addBtn);

    }

    private void renderCards() {
        displayPanel.removeAll();
        // Load flashcards from MongoDB for this user and subject
        List<Document> docs = FlashcardManager.getFlashcards(userObjectId, subject);
        for (Document doc : docs) {
            String question = doc.getString("question");
            String answer = doc.getString("answer");
            JPanel cardPanel = createFlashcardUI(new Flashcard(question, answer));
            displayPanel.add(cardPanel);
        }
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private JPanel createFlashcardUI(Flashcard card) {

        // this is flashcard view box ;

        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(new Color(45, 60, 75));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
                super.paintComponent(g2);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_OUTLINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
            }
        };

        cardPanel.setPreferredSize(new Dimension(250, 100));
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel content = new JLabel("<html><div style='text-align:center;'>" + card.getQuestion() + "</div></html>",
                SwingConstants.CENTER);
        content.setFont(UIUtils.FONT_GENERAL_UI);
        content.setForeground(Color.WHITE);
        cardPanel.add(content, BorderLayout.CENTER);

        final boolean[] showingQuestion = { true };

        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showingQuestion[0] = !showingQuestion[0];
                if (showingQuestion[0]) {
                    content.setText("<html><div style='text-align:center;'>" + card.getQuestion() + "</div></html>");
                } else {
                    content.setText("<html><div style='text-align:center;'>" + card.getAnswer() + "</div></html>");
                }
            }
        });

        return cardPanel;
    }

    private void addCloseButton(JPanel panel) {

        JLabel closeBtn = new JLabel("X", SwingConstants.CENTER);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(200, 70, 70));
        closeBtn.setOpaque(true);
        closeBtn.setBounds(760, 10, 30, 30);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(new Color(170, 50, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(new Color(200, 70, 70));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
            }
        });

        panel.add(closeBtn);
    }
}
