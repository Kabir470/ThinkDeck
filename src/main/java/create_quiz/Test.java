package create_quiz;

import db.FlashcardManager;
import db.QuizManager;
import org.bson.types.ObjectId;
import Utils.*;
import component.Toaster;
import db.FlashcardManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Test extends JFrame {
    private final JPanel panel;
    private final Toaster toaster;
    private final String userId;

    public Test(String userId) {
        this(userId, null);
    }

    public Test(String userId, String subject) {
        this.userId = userId;
        setTitle("Take a Quiz");
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
        addTitle(subject);

        if (subject != null) {
            startQuiz(subject); // Auto-start if subject provided
        } else {
            addSubjectSelection();
        }

        addBackButton();
    }

    private void addWindowControls() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false);
        controlPanel.setBounds(0, 0, 800, 40);

        JButton minimizeButton = createControlButton("—", new Color(100, 100, 100));
        minimizeButton.addActionListener(unused -> setState(Frame.ICONIFIED));

        JButton closeButton = createControlButton("×", new Color(200, 70, 70));
        closeButton.addActionListener(unused -> dispose());

        controlPanel.add(minimizeButton);
        controlPanel.add(closeButton);
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

    private void addTitle(String subject) {
        JLabel title = new JLabel(
                subject == null ? "Select a Subject for Quiz" : "Starting Quiz: " + subject,
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 50, 800, 40);
        panel.add(title);
    }

    private void addSubjectSelection() {
        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new GridLayout(0, 2, 20, 20));
        subjectPanel.setBounds(150, 120, 500, 250);
        subjectPanel.setOpaque(false);

        Set<String> subjects = FlashcardManager.getAllSubjects(new org.bson.types.ObjectId(userId));

        if (subjects == null || subjects.isEmpty()) {
            JLabel noSubjects = new JLabel("No subjects available. Please create some flashcards first.",
                    SwingConstants.CENTER);
            noSubjects.setForeground(Color.WHITE);
            noSubjects.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            noSubjects.setBounds(0, 200, 800, 30);
            panel.add(noSubjects);
            return;
        }

        for (String subject : subjects) {
            JButton subjectButton = createSubjectButton(subject);
            subjectPanel.add(subjectButton);
        }

        JScrollPane scrollPane = new JScrollPane(subjectPanel);
        scrollPane.setBounds(150, 120, 500, 250);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
    }

    private JButton createSubjectButton(String subject) {
        JButton button = new JButton(subject) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_INTERACTIVE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(subject)) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(subject, x, y);
            }
        };

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(e -> startQuiz(subject));

        return button;
    }

    private void startQuiz(String subject) {
        if (!FlashcardManager.hasEnough(new ObjectId(userId), subject, 4)) {
            toaster.warn("Not enough flashcards (min 4 required)");
            return;
        }

        dispose();
        new QuizPage(subject, userId).setVisible(true);
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back to Dashboard") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(new Color(70, 130, 180)); // Steel blue
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth("Back to Dashboard")) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString("Back to Dashboard", x, y);
            }
        };

        backButton.setBounds(300, 400, 200, 44);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new dashboard.Dashboard(userId).setVisible(true);
        });

        panel.add(backButton);
    }

    public static void main(String[] args) {
        // Replace "guest" with actual username retrieval logic if needed
        SwingUtilities.invokeLater(() -> new Test("66c7111605366b45448133b3").setVisible(true));
    }
}