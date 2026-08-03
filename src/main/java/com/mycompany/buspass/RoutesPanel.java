package com.mycompany.buspass; import javax.swing.*;
import java.awt.*;

public class RoutesPanel extends JPanel {
    private static final String[][] ROUTES = {
            {"Route 01 \u2014 Tilaghor Line", "Tilaghor \u2192 Shahi Eidgah \u2192 Campus", "DH-MET-TIL-11-4410", "8.9 km \u2022 20 min"},
            {"Route 02 \u2014 MU Express", "Khojarkhola Square \u2192 Rikabi Bazar \u2192 Campus", "DH-MET-KHO-11-6220", "17.9 km \u2022 37 min"},
            {"Route 03 \u2014 Rikabi Bazar Line", "Rikabi Bazar \u2192  Shahi Eidgah \u2192 Campus", "DH-MET-RIK-11-7788", "14.4 km \u2022 35 min"},
            {"Route 04 \u2014 South Surma Express", "Humayun Rashid Chattar \u2192  Shibganj \u2192 Campus", "DH-MET-SRM-11-1288", "16.6 km \u2022 31 min"},
    };

    public RoutesPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(new HeaderBar("Bus Routes", "All available Metropolitan University bus routes"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        for (String[] r : ROUTES) {
            RoundedPanel card = new RoundedPanel(Color.WHITE, 14);
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(14, 18, 14, 18)));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(2000, 90));

            JPanel left = new JPanel();
            left.setOpaque(false);
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            JLabel title = new JLabel("\uD83D\uDDFA " + r[0]);
            title.setFont(UITheme.FONT_HEADER);
            JLabel stops = new JLabel(r[1]);
            stops.setFont(UITheme.FONT_BODY);
            stops.setForeground(UITheme.TEXT_MUTED);
            left.add(title);
            left.add(Box.createVerticalStrut(4));
            left.add(stops);

            JPanel right = new JPanel();
            right.setOpaque(false);
            right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
            JLabel bus = new JLabel("\uD83D\uDE8C " + r[2]);
            bus.setFont(UITheme.FONT_BOLD_BODY);
            JLabel eta = new JLabel("\u23F1 " + r[3]);
            eta.setFont(UITheme.FONT_BODY);
            eta.setForeground(UITheme.TEXT_MUTED);
            right.add(bus);
            right.add(eta);

            card.add(left, BorderLayout.WEST);
            card.add(right, BorderLayout.EAST);
            body.add(card);
            body.add(Box.createVerticalStrut(14));
        }

        add(body, BorderLayout.CENTER);
    }
}
