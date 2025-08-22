package dashboard;

import Utils.UIUtils;
import db.FlashcardManager;
import db.UserManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class UserProfilePage extends JFrame {
    private final String userId;
    private final JFrame parentDashboard;

    private JTextField usernameField;
    private JTextField emailField;
    private JTextField locationField;
    private JLabel flashcardCountLabel;

    public UserProfilePage(String userId, JFrame parentDashboard) {
        this.userId = userId;
        this.parentDashboard = parentDashboard;

        setTitle("User Profile");
        setSize(800, 500);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Your Profile", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 28f));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Profile details form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = createTextField();
        formPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = createTextField();
        formPanel.add(emailField, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = createTextField();
        formPanel.add(locationField, gbc);

        // Flashcard Stats
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("Total Flashcards:"), gbc);
        gbc.gridx = 1;
        flashcardCountLabel = createLabel("");
        formPanel.add(flashcardCountLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JButton saveButton = new JButton("Save Changes");
        UIUtils.styleButton(saveButton, new Color(122, 201, 160), Color.BLACK);
        saveButton.addActionListener(e -> saveUserData());

        JButton backButton = new JButton("Back");
        UIUtils.styleButton(backButton, UIUtils.COLOR_INTERACTIVE, Color.BLACK);
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserData();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(UIUtils.FONT_GENERAL_UI);
        textField.setBackground(UIUtils.COLOR_BACKGROUND.brighter());
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_INTERACTIVE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return textField;
    }

    private void loadUserData() {
        ObjectId userObjectId = new ObjectId(userId);
        Document userDoc = UserManager.getUserById(userObjectId);

        if (userDoc != null) {
            usernameField.setText(userDoc.getString("username"));
            emailField.setText(userDoc.getString("email"));
            locationField.setText(userDoc.getString("location"));
        }

        int count = FlashcardManager.getFlashcards(userObjectId).size();
        flashcardCountLabel.setText(String.valueOf(count));
    }

    private void saveUserData() {
        String newUsername = usernameField.getText();
        String newEmail = emailField.getText();
        String newLocation = locationField.getText();

        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Email cannot be empty.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ObjectId userObjectId = new ObjectId(userId);
        UserManager.updateUser(userObjectId, newUsername, newEmail, newLocation);

        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        parentDashboard.dispose(); // Close old dashboard
        new Dashboard(userId).setVisible(true); // Open new one with updated name
        this.dispose();
    }
}
