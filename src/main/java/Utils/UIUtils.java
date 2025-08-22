package Utils;

import java.awt.*;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.BorderFactory;

public class UIUtils {

    public static final Font FONT_GENERAL_UI = new Font("Segoe UI", Font.PLAIN, 20);
    public static final Font FONT_FORGOT_PASSWORD = new Font("Segoe UI", Font.PLAIN, 12);

    public static final Color COLOR_OUTLINE = new Color(103, 112, 120);
    public static final Color COLOR_BACKGROUND = new Color(37, 51, 61);
    public static final Color COLOR_INTERACTIVE = new Color(108, 216, 158);
    public static final Color COLOR_INTERACTIVE_DARKER = new Color(87, 171, 127);
    // public static final Color OFFWHITE = new Color(229, 229, 229);
    public static final Color OFFWHITE = new Color(240, 240, 240);

    public static final String BUTTON_TEXT_LOGIN = "Login";
    public static final String BUTTON_TEXT_FORGOT_PASS = "Forgot your password?";
    public static final String BUTTON_TEXT_REGISTER = "Register";

    public static final String PLACEHOLDER_TEXT_USERNAME = "Username/email";

    public static final int ROUNDNESS = 8;

    public static Graphics2D get2dGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.addRenderingHints(new HashMap<RenderingHints.Key, Object>() {
            {
                put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            }
        });
        return g2;
    }

    public static void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(FONT_GENERAL_UI);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.darker());
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (button.getBounds().contains(evt.getPoint())) {
                    button.setBackground(originalColor.brighter());
                } else {
                    button.setBackground(originalColor);
                }
            }
        });
    }
}