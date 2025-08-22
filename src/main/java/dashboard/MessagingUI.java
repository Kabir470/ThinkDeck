package dashboard;

import Utils.UIUtils;
import db.Message;
import db.MessageManager;
import db.UserManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessagingUI extends JFrame {
    private final ObjectId senderId;
    private final ObjectId receiverId;
    private final String receiverName;

    private JTextPane conversationPane;
    private JTextField messageField;
    private JButton sendButton;
    private JButton refreshButton;

    public MessagingUI(String senderIdStr, String receiverIdStr) {
        this.senderId = new ObjectId(senderIdStr);
        this.receiverId = new ObjectId(receiverIdStr);

        Document receiverDoc = UserManager.getUserById(this.receiverId);
        this.receiverName = receiverDoc != null ? receiverDoc.getString("username") : "Unknown User";

        setTitle("Chat with " + receiverName);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);

        initComponents();
        loadConversation();
    }

    private void initComponents() {
        // Header
        JLabel titleLabel = new JLabel("Chat with " + receiverName, SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Conversation Area
        conversationPane = new JTextPane();
        conversationPane.setEditable(false);
        conversationPane.setBackground(UIUtils.COLOR_BACKGROUND.darker());
        conversationPane.setForeground(Color.WHITE);
        conversationPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(conversationPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_OUTLINE));
        add(scrollPane, BorderLayout.CENTER);

        // Input Area
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();
        messageField.setFont(UIUtils.FONT_GENERAL_UI);
        messageField.addActionListener(e -> sendMessage());
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(UIUtils.FONT_GENERAL_UI);
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(UIUtils.FONT_GENERAL_UI);
        refreshButton.addActionListener(e -> loadConversation());
        inputPanel.add(refreshButton, BorderLayout.WEST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadConversation() {
        conversationPane.setText("");
        List<Message> messages = MessageManager.getConversation(senderId, receiverId);
        StyledDocument doc = conversationPane.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.WHITE);

        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, new Color(173, 216, 230)); // Light blue for sender

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");

        for (Message msg : messages) {
            try {
                boolean isSender = msg.getSenderId().equals(senderId);
                SimpleAttributeSet alignment = isSender ? right : left;
                String senderName = isSender ? "You" : receiverName;
                String formattedMsg = senderName + " (" + sdf.format(msg.getTimestamp()) + "):\n" + msg.getContent()
                        + "\n\n";

                doc.insertString(doc.getLength(), formattedMsg, alignment);
                doc.setParagraphAttributes(doc.getLength() - formattedMsg.length(), formattedMsg.length(), alignment,
                        false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            MessageManager.sendMessage(senderId, receiverId, content);
            messageField.setText("");
            loadConversation(); // Refresh conversation
        }
    }
}
