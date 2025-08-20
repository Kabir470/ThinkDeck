package login;
//inherits from JTextField and JPasswordField respectively

//feature rounded corners

//Have custom background, font, border, and caret colours 

//Support dynamic border color changes on focus

import javax.swing.*;

import Utils.UIUtils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;


public class TextFieldPassword extends JPasswordField {
    private Shape shape;
    private Color borderColor = UIUtils.COLOR_OUTLINE;

    public TextFieldPassword() {
        setOpaque(false);    // background color
        setBackground(UIUtils.COLOR_BACKGROUND); 
        setForeground(Color.white);    // text colour
        setCaretColor(Color.white);    // cursor color 
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)); // proper text cursor 

        setMargin(new Insets(2, 10, 2, 2));  // padding inside the field 
        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(UIUtils.FONT_GENERAL_UI);
    }

    protected void paintComponent(Graphics g) {        // rounded background drawing 
        Graphics2D g2 = UIUtils.get2dGraphics(g);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
        super.paintComponent(g2);
    }

    protected void paintBorder(Graphics g) {            //rounded border drawing 
        Graphics2D g2 = UIUtils.get2dGraphics(g);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
        }
        return shape.contains(x, y);
    }

    public void setBorderColor(Color color) {
        borderColor = color;
        repaint();
    }
}