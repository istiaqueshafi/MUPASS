package com.mycompany.buspass;import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private Map<String, JButton> buttons = new LinkedHashMap<>();
    private String active;
    private Consumer<String> onNavigate;

    private static final String[][] ITEMS = {
        {"dashboard", "\uD83C\uDFE0  Dashboard"},
        {"apply", "\uD83D\uDE8C  Apply Pass"},
        {"routes", "\uD83D\uDCCD  Routes"},
        {"tracking", "\uD83D\uDE8C  Live Tracking"},
        {"status", "\uD83C\uDFAB  View Status"},
        {"mypass", "\uD83C\uDFAB  My Pass"},
        {"profile", "\uD83D\uDC64  Profile"},
    };

    public Sidebar(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        setLayout(new BorderLayout());
        setBackground(UITheme.SIDEBAR_BG);
        setPreferredSize(new Dimension(210, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));

        JLabel logo = new JLabel("\uD83C\uDF93 MuPass");
        logo.setFont(new Font("SansSerif", Font.BOLD, 19));
        logo.setForeground(UITheme.PRIMARY);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(logo);
        top.add(Box.createVerticalStrut(24));

        for (String[] item : ITEMS) {
            JButton b = makeNavButton(item[1]);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.addActionListener(e -> setActive(item[0]));
            buttons.put(item[0], b);
            top.add(b);
            top.add(Box.createVerticalStrut(6));
        }

        add(top, BorderLayout.NORTH);

  JButton logout = makeNavButton("\uD83D\uDEAA  Logout");
logout.setBackground(UITheme.RED);               
logout.setForeground(Color.WHITE);                 
logout.setHorizontalAlignment(SwingConstants.CENTER); 
logout.addActionListener(e -> onNavigate.accept("logout"));
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 16, 18, 16));
        bottom.add(logout, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
    }

    private JButton makeNavButton(String text) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(UITheme.FONT_BOLD_BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setBackground(UITheme.SIDEBAR_BG);
        b.setForeground(UITheme.TEXT_DARK);
        b.setMaximumSize(new Dimension(180, 38));
        b.setPreferredSize(new Dimension(180, 38));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return b;
    }

    public void setActive(String key) {
        setActiveQuiet(key);
        onNavigate.accept(key);
    }

    
    public void setActiveQuiet(String key) {
        active = key;
        for (Map.Entry<String, JButton> e : buttons.entrySet()) {
            boolean isActive = e.getKey().equals(key);
            JButton b = e.getValue();
            b.setBackground(isActive ? UITheme.PRIMARY : UITheme.SIDEBAR_BG);
            b.setForeground(isActive ? Color.WHITE : UITheme.TEXT_DARK);
        }
    }
}
