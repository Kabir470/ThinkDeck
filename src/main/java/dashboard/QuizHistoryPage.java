package dashboard;

import Utils.UIUtils;
import db.QuizManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class QuizHistoryPage extends JFrame {
    private final String userId;

    public QuizHistoryPage(String userId) {
        this.userId = userId;

        setTitle("Quiz History");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Your Quiz History", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 28f));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // History Table
        JTable historyTable = new JTable();
        historyTable.setFont(UIUtils.FONT_GENERAL_UI);
        historyTable.setRowHeight(30);
        historyTable.getTableHeader().setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD));
        historyTable.getTableHeader().setBackground(UIUtils.COLOR_INTERACTIVE);
        historyTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(UIUtils.COLOR_BACKGROUND.brighter());
        add(scrollPane, BorderLayout.CENTER);

        loadQuizHistory(historyTable);
    }

    private void loadQuizHistory(JTable table) {
        String[] columnNames = { "Date", "Subject", "Score", "Quiz Type" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Document> history = QuizManager.getQuizHistory(new ObjectId(userId));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Document doc : history) {
            String date = sdf.format(doc.getDate("date"));
            String subject = doc.getString("subject");
            String score = doc.getInteger("score") + " / " + doc.getInteger("totalQuestions");
            String quizType = doc.getString("quizType");
            model.addRow(new Object[] { date, subject, score, quizType });
        }

        table.setModel(model);
    }
}
