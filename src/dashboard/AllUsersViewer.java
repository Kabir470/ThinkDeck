package dashboard;

import com.mongodb.client.*;
import org.bson.Document;
import db.TestMongo;
import Utils.*;
import component.Toaster;

import javax.swing.*;
import java.awt.*;

public class AllUsersViewer extends JFrame {

    public AllUsersViewer() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(450, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        backgroundPanel.setBounds(0, 0, 450, 500);
        backgroundPanel.setLayout(null);
        backgroundPanel.setOpaque(false);
        add(backgroundPanel);

        JLabel title = new JLabel("All Registered Users", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 20, 450, 40);
        backgroundPanel.add(title);

        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(new Color(0, 0, 0, 0));

        MongoDatabase db = TestMongo.connect();
        MongoCollection<Document> users = db.getCollection("users");
        FindIterable<Document> docs = users.find();

        for (Document user : docs) {
            String username = user.getString("username");
            String email = user.getString("email");
            String location = user.getString("location");

            JPanel userCard = new JPanel();
            userCard.setLayout(new BorderLayout());
            userCard.setBackground(new Color(58, 64, 77));
            userCard.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            JLabel nameLabel = new JLabel("User: " + username);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel emailLabel = new JLabel("Email: " + email);
            emailLabel.setForeground(new Color(180, 180, 180));
            emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JLabel locationlLabel = new JLabel("location: " + location);
            emailLabel.setForeground(new Color(180, 180, 180));
            emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            textPanel.add(nameLabel);
            textPanel.add(emailLabel);
            textPanel.add(locationlLabel);

            userCard.add(textPanel, BorderLayout.CENTER);
            userCard.setMaximumSize(new Dimension(380, 100));

            userListPanel.add(userCard);
            userListPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setBounds(25, 70, 400, 390);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        backgroundPanel.add(scrollPane);

        setVisible(true);
    }
}
