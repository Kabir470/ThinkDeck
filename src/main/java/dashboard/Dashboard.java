package dashboard;

import component.Toaster;
import Utils.UIUtils;
import db.FlashcardManager;
import org.bson.types.ObjectId;
import db.UserManager;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Dashboard extends JFrame {
    private final Toaster toaster;
    private static final Color PRIMARY_COLOR = new Color(40, 44, 52);
    private final String userId;
    private String userName;
    private JPanel subjectCardsPanel;

    public Dashboard(String userId) {
        this.userId = userId;
        fetchUserName();

        initializeFrame();
        toaster = new Toaster(this.getContentPane());

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(PRIMARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(mainPanel);

        // Create and add panels
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterScrollPane(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        // Handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    private void fetchUserName() {
        try {
            Document userDoc = UserManager.getUserById(new ObjectId(userId));
            this.userName = (userDoc != null && userDoc.containsKey("username")) ? userDoc.getString("username")
                    : userId;
        } catch (Exception e) {
            this.userName = userId; // Fallback
        }
    }

    private void initializeFrame() {
        setTitle("Dashboard - ThinkDeck");
        setMinimumSize(new Dimension(800, 700));
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PRIMARY_COLOR);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // User Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.1;
        JLabel userLabel = new JLabel("User: " + userName);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, gbc);

        // Checkboxes
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        headerPanel.add(createHideMeCheckbox(), gbc);

        gbc.gridy = 1;
        headerPanel.add(createReceiveMessagesCheckbox(), gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(createQuizHistoryButton());
        buttonsPanel.add(createUserProfileButton());
        buttonsPanel.add(createAllUsersButton());

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.EAST;
        headerPanel.add(buttonsPanel, gbc);

        return headerPanel;
    }

    private JScrollPane createCenterScrollPane() {
        subjectCardsPanel = new JPanel(new GridBagLayout());
        subjectCardsPanel.setOpaque(false);

        addSubjectCards();

        JScrollPane scrollPane = new JScrollPane(subjectCardsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(20, 0));
        footerPanel.setOpaque(false);

        footerPanel.add(createSettingsButton(), BorderLayout.WEST);
        footerPanel.add(createAddFlashcardButton(), BorderLayout.CENTER);
        footerPanel.add(createLogoutButton(), BorderLayout.EAST);

        return footerPanel;
    }

    private JCheckBox createHideMeCheckbox() {
        JCheckBox hideMeCheckbox = new JCheckBox("Hide me from other users");
        styleCheckbox(hideMeCheckbox);
        try {
            Document userDoc = UserManager.getUserById(new ObjectId(userId));
            if (userDoc != null) {
                hideMeCheckbox.setSelected(userDoc.getBoolean("isHidden", false));
            }
        } catch (Exception e) {
            /* ignore */ }
        hideMeCheckbox.addActionListener(e -> {
            UserManager.setUserHiddenStatus(new ObjectId(userId), hideMeCheckbox.isSelected());
            toaster.success("Your visibility has been updated.");
        });
        return hideMeCheckbox;
    }

    private JCheckBox createReceiveMessagesCheckbox() {
        JCheckBox receiveMessagesCheckbox = new JCheckBox("Allow users to message me");
        styleCheckbox(receiveMessagesCheckbox);
        try {
            Document userDoc = UserManager.getUserById(new ObjectId(userId));
            if (userDoc != null) {
                receiveMessagesCheckbox.setSelected(userDoc.getBoolean("canReceiveMessages", true));
            }
        } catch (Exception e) {
            /* ignore */ }
        receiveMessagesCheckbox.addActionListener(e -> {
            UserManager.setUserMessagePreference(new ObjectId(userId), receiveMessagesCheckbox.isSelected());
            toaster.success("Your messaging preference has been updated.");
        });
        return receiveMessagesCheckbox;
    }

    private void styleCheckbox(JCheckBox checkbox) {
        checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        checkbox.setForeground(Color.WHITE);
        checkbox.setOpaque(false);
    }

    private JButton createQuizHistoryButton() {
        JButton historyButton = new JButton("Quiz History");
        UIUtils.styleButton(historyButton, UIUtils.COLOR_INTERACTIVE, Color.BLACK);
        historyButton.addActionListener(e -> new QuizHistoryPage(userId).setVisible(true));
        return historyButton;
    }

    private JButton createUserProfileButton() {
        JButton profileButton = new JButton("My Profile");
        UIUtils.styleButton(profileButton, UIUtils.COLOR_INTERACTIVE, Color.BLACK);
        profileButton.addActionListener(e -> new UserProfilePage(userId, this).setVisible(true));
        return profileButton;
    }

    private JButton createAllUsersButton() {
        JButton allUsersBtn = new JButton("Show All Users");
        UIUtils.styleButton(allUsersBtn, UIUtils.COLOR_INTERACTIVE, Color.BLACK);
        allUsersBtn.addActionListener(e -> new AllUsersViewer(userId));
        return allUsersBtn;
    }

    private void addSubjectCards() {
        subjectCardsPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        List<Subject> subjects = Arrays.asList(
                new Subject("Machine Learning", new Color(52, 89, 95)),
                new Subject("C++", new Color(166, 72, 49)),
                new Subject("English", new Color(39, 76, 66)),
                new Subject("OOP", new Color(88, 62, 117)),
                new Subject("Algorithms", new Color(120, 80, 60)),
                new Subject("Database", new Color(60, 80, 120)));

        int col = 0;
        int row = 0;
        int maxCols = 3;

        for (Subject subject : subjects) {
            gbc.gridx = col;
            gbc.gridy = row;
            subjectCardsPanel.add(createSubjectCard(subject), gbc);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
        subjectCardsPanel.revalidate();
        subjectCardsPanel.repaint();
    }

    private JPanel createSubjectCard(Subject subject) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Main content panel for the card
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(subject.color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS * 2, UIUtils.ROUNDNESS * 2);
            }
        };
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(subject.name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(nameLabel, BorderLayout.CENTER);

        card.add(contentPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(createQuizButton(subject));
        buttonsPanel.add(createAiQuizButton(subject));
        card.add(buttonsPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if the click was on the buttons, if so, do nothing
                if (e.getSource() instanceof JButton)
                    return;
                toaster.info("Opening " + subject.name + " Flashcards...");
                new create_flashcard.FlashcardPage(subject.name, userId);
                dispose();
            }
        });

        return card;
    }

    private JButton createQuizButton(Subject subject) {
        JButton quizButton = new JButton("QUIZ");
        UIUtils.styleButton(quizButton, subject.color.darker(), Color.BLACK);
        quizButton.addActionListener(e -> {
            ObjectId userObjectId = new ObjectId(userId);
            int cardCount = FlashcardManager.getFlashcards(userObjectId, subject.name).size();
            if (cardCount < 4) {
                toaster.warn("Need at least 4 flashcards to start quiz");
                return;
            }
            toaster.info("Launching " + subject.name + " Quiz...");
            new create_quiz.Test(userId, subject.name);
            dispose();
        });
        return quizButton;
    }

    private JButton createAiQuizButton(Subject subject) {
        JButton aiQuizButton = new JButton("AI QUIZ");
        UIUtils.styleButton(aiQuizButton, new Color(70, 130, 180), Color.BLACK);
        aiQuizButton.addActionListener(e -> {
            toaster.info("Generating AI Quiz for " + subject.name + "...");
            new create_quiz.AiQuizPage(userId, subject.name).setVisible(true);
            dispose();
        });
        return aiQuizButton;
    }

    private JButton createAddFlashcardButton() {
        JButton addFlashcardBtn = new JButton("Add Flashcards");
        addFlashcardBtn.setFont(new Font("Segoe UI", Font.BOLD, 36));
        UIUtils.styleButton(addFlashcardBtn, new Color(122, 201, 160), Color.BLACK);
        addFlashcardBtn.setToolTipText("Add new flashcard");
        addFlashcardBtn.addActionListener(e -> {
            Set<String> subjects = FlashcardManager.getAllSubjects(new ObjectId(userId));
            if (subjects.isEmpty()) {
                toaster.error("No subjects available. Create a subject first!");
                return;
            }
            String[] subjectArray = subjects.toArray(new String[0]);
            String selectedSubject = (String) JOptionPane.showInputDialog(
                    this, "Select subject for the new flashcard:", "Select Subject",
                    JOptionPane.QUESTION_MESSAGE, null, subjectArray, subjectArray[0]);
            if (selectedSubject != null) {
                new create_flashcard.FlashcardPage(selectedSubject, userId);
                dispose();
            }
        });
        return addFlashcardBtn;
    }

    private JButton createSettingsButton() {
        JButton settingsBtn = new JButton("Settings");
        UIUtils.styleButton(settingsBtn, new Color(100, 100, 100), Color.BLACK);
        settingsBtn.setToolTipText("Settings");
        settingsBtn.addActionListener(e -> new SettingsPage(userId, this).setVisible(true));
        return settingsBtn;
    }

    private JButton createLogoutButton() {
        JButton logoutBtn = new JButton("Logout");
        UIUtils.styleButton(logoutBtn, new Color(200, 70, 70), Color.BLACK);
        logoutBtn.setToolTipText("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new login.LoginUI();
        });
        return logoutBtn;
    }

    private static class Subject {
        String name;
        Color color;

        Subject(String name, Color color) {
            this.name = name;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Dashboard("DemoUser");
        });
    }
}
