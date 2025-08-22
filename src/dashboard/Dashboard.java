package dashboard;

import component.Toaster;
import Utils.UIUtils;
import db.FlashcardManager;
import org.bson.types.ObjectId;
import db.UserManager;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Dashboard extends JFrame {
    private final Toaster toaster;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    private static final Color PRIMARY_COLOR = new Color(40, 44, 52);
    private static final Color SECONDARY_COLOR = new Color(58, 64, 77);
    private final String userId;
    private final String userName;

    public Dashboard(String userId) {
        this.userId = userId;
        // Fetch username from DB
        String fetchedName = userId;
        try {
            Document userDoc = UserManager.getUserById(new ObjectId(userId));
            if (userDoc != null && userDoc.containsKey("username")) {
                fetchedName = userDoc.getString("username");
            }
        } catch (Exception e) {
            // fallback to userId
        }
        this.userName = fetchedName;
        initializeFrame();
        JPanel mainPanel = createMainPanel();
        toaster = new Toaster(mainPanel);

        addUserNameLabel(mainPanel);
        addHeader(mainPanel);
        addSubjectCards(mainPanel);
        addActionButtons(mainPanel);
        addAllUsersButton(mainPanel);

        setVisible(true);
    }

    private void addUserNameLabel(JPanel panel) {
        JLabel userLabel = new JLabel("User: " + userName);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(20, 20, 300, 25);
        panel.add(userLabel);
    }

    private void initializeFrame() {
        setTitle("Dashboard - Cardify");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY_COLOR);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(PRIMARY_COLOR);
        panel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        add(panel);
        return panel;
    }

    private void addHeader(JPanel panel) {
        JLabel title = new JLabel("Flashcards", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 30, WINDOW_WIDTH, 50);
        panel.add(title);

        JLabel subtitle = new JLabel("Select a subject to begin", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setBounds(0, 80, WINDOW_WIDTH, 30);
        panel.add(subtitle);
    }

    private void addSubjectCards(JPanel panel) {
        List<Subject> subjects = Arrays.asList(
                new Subject("Machine Learning", new Color(52, 89, 95), "ml_icon.png"),
                new Subject("C++", new Color(166, 72, 49), "cpp_icon.png"),
                new Subject("English", new Color(39, 76, 66), "english_icon.png"),
                new Subject("OOP", new Color(88, 62, 117), "oop_icon.png"),
                new Subject("Algorithms", new Color(120, 80, 60), "algo_icon.png"),
                new Subject("Database", new Color(60, 80, 120), "db_icon.png"));

        int cardWidth = 220;
        int cardHeight = 150;
        int horizontalGap = 30;
        int verticalGap = 30;
        int startX = (WINDOW_WIDTH - (3 * cardWidth + 2 * horizontalGap)) / 2;
        int startY = 150;

        for (int i = 0; i < subjects.size(); i++) {
            Subject subject = subjects.get(i);
            int row = i / 3;
            int col = i % 3;

            JPanel card = createSubjectCard(subject, cardWidth, cardHeight);
            card.setBounds(
                    startX + col * (cardWidth + horizontalGap),
                    startY + row * (cardHeight + verticalGap),
                    cardWidth,
                    cardHeight);
            panel.add(card);

            addQuizButton(panel, subject,
                    startX + col * (cardWidth + horizontalGap) + (cardWidth - 80) / 2,
                    startY + row * (cardHeight + verticalGap) + cardHeight + 10);
        }
    }

    private JPanel createSubjectCard(Subject subject, int width, int height) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = UIUtils.get2dGraphics(g);

                g2.setColor(subject.color.darker().darker());
                g2.fillRoundRect(2, 2, width - 4, height - 4, UIUtils.ROUNDNESS * 2, UIUtils.ROUNDNESS * 2);
                g2.setColor(subject.color);
                g2.fillRoundRect(0, 0, width - 4, height - 4, UIUtils.ROUNDNESS * 2, UIUtils.ROUNDNESS * 2);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                g2.setColor(Color.WHITE);

                FontMetrics metrics = g2.getFontMetrics();
                int textWidth = metrics.stringWidth(subject.name);
                int textX = (width - textWidth) / 2;
                int textY = height - 30;

                g2.drawString(subject.name, textX, textY);
            }
        };

        card.setLayout(null);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toaster.info("Opening " + subject.name + " Flashcards...");
                new create_flashcard.FlashcardPage(subject.name, userId);
                dispose();
            }
        });

        return card;
    }

    private void addQuizButton(JPanel parentPanel, Subject subject, int x, int y) {
        boolean[] hovered = new boolean[] { false };

        JLabel quizButton = new JLabel("QUIZ", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(hovered[0] ? subject.color.brighter() : subject.color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 12));
                FontMetrics metrics = g2.getFontMetrics();
                int textX = (getWidth() - metrics.stringWidth(getText())) / 2;
                int textY = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(getText(), textX, textY);
            }
        };

        quizButton.setBounds(x, y, 80, 30);
        quizButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        quizButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ObjectId userObjectId = new ObjectId(userId); // userId is ObjectId string
                int cardCount = FlashcardManager.getFlashcards(userObjectId, subject.name).size();
                if (cardCount < 4) {
                    toaster.warn("Need at least 4 flashcards to start quiz");
                    return;
                }
                toaster.info("Launching " + subject.name + " Quiz...");
                new create_quiz.Test(userId, subject.name); // Pass userId and subject
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hovered[0] = true;
                ((JLabel) e.getSource()).repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered[0] = false;
                ((JLabel) e.getSource()).repaint();
            }
        });

        parentPanel.add(quizButton);
    }

    private void addActionButtons(JPanel panel) {
        JButton addFlashcardBtn = createRoundButton("+", new Color(122, 201, 160),
                WINDOW_WIDTH / 2 - 30, WINDOW_HEIGHT - 110, 60, 60);
        addFlashcardBtn.setToolTipText("Add new flashcard");

        addFlashcardBtn.addActionListener(e -> {
            Set<String> subjects = FlashcardManager.getAllSubjects(new ObjectId(userId));

            if (subjects.isEmpty()) {
                toaster.error("No subjects available. Create a subject first!");
                return;
            }

            String[] subjectArray = subjects.toArray(new String[0]);
            String selectedSubject = (String) JOptionPane.showInputDialog(
                    this,
                    "Select subject for the new flashcard:",
                    "Select Subject",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    subjectArray,
                    subjectArray[0]);

            if (selectedSubject != null) {
                new create_flashcard.FlashcardPage(selectedSubject, userId);
                dispose();
            }
        });

        panel.add(addFlashcardBtn);

        JButton settingsBtn = createRoundButton("", new Color(100, 100, 100),
                30, WINDOW_HEIGHT - 90, 40, 40);
        ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/images/settings.png"));
        settingsBtn.setIcon(settingsIcon);

        settingsBtn.setToolTipText("Settings");
        settingsBtn.addActionListener(e -> {
            toaster.info("Opening settings...");
        });
        panel.add(settingsBtn);

        // Add logout button to bottom right
        JButton logoutBtn = createRoundButton("Logout", new Color(200, 70, 70),
                WINDOW_WIDTH - 160, WINDOW_HEIGHT - 90, 120, 44);
        logoutBtn.setToolTipText("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoutBtn.addActionListener(e -> {
            dispose();
            new login.LoginUI();
        });
        panel.add(logoutBtn);
    }

    private void addAllUsersButton(JPanel panel) {
        final Color[] userButtonColors = { UIUtils.COLOR_INTERACTIVE, Color.WHITE };

        JLabel allUsersButton = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                super.paintComponent(g2);

                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                g2.setColor(userButtonColors[0]);
                g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                FontMetrics metrics = g2.getFontMetrics(UIUtils.FONT_GENERAL_UI);
                int x = (getWidth() - metrics.stringWidth("Show All Users")) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                g2.setColor(userButtonColors[1]);
                g2.drawString("Show All Users", x, y);
            }
        };

        allUsersButton.setBounds(WINDOW_WIDTH - 180, 30, 140, 44);
        allUsersButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        allUsersButton.setBackground(UIUtils.COLOR_BACKGROUND);

        allUsersButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new AllUsersViewer();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                userButtonColors[0] = UIUtils.COLOR_INTERACTIVE_DARKER;
                userButtonColors[1] = UIUtils.OFFWHITE;
                allUsersButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                userButtonColors[0] = UIUtils.COLOR_INTERACTIVE;
                userButtonColors[1] = Color.WHITE;
                allUsersButton.repaint();
            }
        });

        panel.add(allUsersButton);
    }

    private JButton createRoundButton(String text, Color bgColor, int x, int y, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.darker());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics metrics = g2.getFontMetrics();
                int textX = (getWidth() - metrics.stringWidth(text)) / 2;
                int textY = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(text, textX, textY);
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBounds(x, y, width, height);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static class Subject {
        String name;
        Color color;
        String iconPath;

        Subject(String name, Color color, String iconPath) {
            this.name = name;
            this.color = color;
            this.iconPath = iconPath;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // For demo/testing, pass a sample user name
            new Dashboard("DemoUser");
        });
    }
}
