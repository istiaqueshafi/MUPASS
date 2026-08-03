package com.mycompany.buspass;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class HeaderBar extends JPanel {
    public HeaderBar(String title, String subtitle) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_TITLE);
        t.setForeground(UITheme.TEXT_DARK);
        JLabel s = new JLabel(subtitle);
        s.setFont(UITheme.FONT_BODY);
        s.setForeground(UITheme.TEXT_MUTED);
        left.add(t);
        left.add(s);
        add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        JLabel bell = new JLabel("\uD83D\uDD14");
        bell.setFont(new Font("SansSerif", Font.PLAIN, 20));
        right.add(bell);

        Student cur = DataStore.getCurrentStudent();
        String initial = (cur != null && cur.getFullName().length() > 0)
                ? cur.getFullName().substring(0, 1).toUpperCase() : "?";
        right.add(new Avatar(initial));
        add(right, BorderLayout.EAST);
    }

    static class Avatar extends JComponent {
        private final String initial;
        Avatar(String initial) {
            this.initial = initial;
            setPreferredSize(new Dimension(36, 36));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UITheme.PRIMARY);
            g2.fill(new Ellipse2D.Float(0, 0, 36, 36));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(initial);
            g2.drawString(initial, (36 - tw) / 2f, 24);
            g2.dispose();
        }
    }
}
