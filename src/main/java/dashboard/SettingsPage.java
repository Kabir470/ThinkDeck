package dashboard;

import Utils.UIUtils;
import db.FlashcardManager;
import db.MessageManager;
import db.UserManager;
import login.LoginUI;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class SettingsPage extends JFrame {
    private final String userId;
    private final JFrame parentDashboard;

    public SettingsPage(String userId, JFrame parentDashboard) {
        this.userId = userId;
        this.parentDashboard = parentDashboard;

        setTitle("Settings");
        setSize(400, 300);
        setLocationRelativeTo(parentDashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Account Settings", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        add(contentPanel, BorderLayout.CENTER);

        // Delete Account Button
        JButton deleteButton = new JButton("Delete My Account");
        deleteButton.setFont(UIUtils.FONT_GENERAL_UI);
        deleteButton.setBackground(new Color(200, 70, 70));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(e -> deleteAccount());
        contentPanel.add(deleteButton);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        JButton backButton = new JButton("Back");
        backButton.setFont(UIUtils.FONT_GENERAL_UI);
        backButton.addActionListener(e -> dispose());
        footerPanel.add(backButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void deleteAccount() {
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete your account?\nAll your flashcards and messages will be lost forever.",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            ObjectId userObjectId = new ObjectId(userId);
            // Perform deletion
            UserManager.deleteUser(userObjectId);
            FlashcardManager.deleteAllFlashcardsForUser(userObjectId);
            MessageManager.deleteAllMessagesForUser(userObjectId);

            // Show confirmation and close application
            JOptionPane.showMessageDialog(this, "Your account has been successfully deleted.", "Account Deleted",
                    JOptionPane.INFORMATION_MESSAGE);

            // Close settings and dashboard, then open login
            this.dispose();
            parentDashboard.dispose();
            new LoginUI().setVisible(true);
        }
    }
}
